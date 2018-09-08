package com.example.niklas.efc_master;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.EditText;

import static android.content.Context.BLUETOOTH_SERVICE;
import static com.example.niklas.efc_master.NordicProfile.CHARACTERISTIC_RX;
import static com.example.niklas.efc_master.NordicProfile.CHARACTERISTIC_TX;
import static com.example.niklas.efc_master.NordicProfile.DESCRIPTOR_CONFIG;
import static com.example.niklas.efc_master.NordicProfile.SERVICE_UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BLEService
{
	private static final String TAG = BLEService.class.getSimpleName();

	public interface OnCounterReadListener
	{
		void onCounterRead(byte[] value);

		void onConnected(boolean success);
	}

	private Context mContext;
	private OnCounterReadListener mListener;
	private String mDeviceAddress;

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;
	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback()

	{
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
		{
			if (newState == BluetoothProfile.STATE_CONNECTED)
			{
				Log.i(TAG, "Connected to GATT client. Attempting to start service discovery");
				gatt.discoverServices();
			}
			else if (newState == BluetoothProfile.STATE_DISCONNECTED)
			{
				Log.i(TAG, "Disconnected from GATT client");
				mListener.onConnected(false);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status)
		{
			if (status == BluetoothGatt.GATT_SUCCESS)
			{
				boolean connected = false;

				BluetoothGattService service = gatt.getService(SERVICE_UUID);
				if (service != null)
				{
					BluetoothGattCharacteristic characteristic = service.getCharacteristic(CHARACTERISTIC_TX);
					if (characteristic != null)
					{
						gatt.setCharacteristicNotification(characteristic, true);

						BluetoothGattDescriptor descriptor = characteristic.getDescriptor(DESCRIPTOR_CONFIG);
						if (descriptor != null)
						{
							descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
							connected = gatt.writeDescriptor(descriptor);
						}
					}
				}
				mListener.onConnected(connected);
			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
		{
			readCounterCharacteristic(characteristic);
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
		{
			readCounterCharacteristic(characteristic);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
		{
			if (DESCRIPTOR_CONFIG.equals(descriptor.getUuid()))
			{
				BluetoothGattCharacteristic characteristic = gatt.getService(SERVICE_UUID).getCharacteristic(CHARACTERISTIC_TX);
				gatt.readCharacteristic(characteristic);
			}
		}

		private void readCounterCharacteristic(BluetoothGattCharacteristic characteristic)
		{
			if (CHARACTERISTIC_TX.equals(characteristic.getUuid()))
			{
				byte[] data = characteristic.getValue();
					mListener.onCounterRead(data);
			}
		}
	};

	private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);

			switch (state)
			{
				case BluetoothAdapter.STATE_ON:
					startClient();
					break;
				case BluetoothAdapter.STATE_OFF:
					stopClient();
					break;
				default:
					// Do nothing
					break;
			}
		}
	};

	public void onCreate(Context context, String deviceAddress, OnCounterReadListener listener) throws RuntimeException
	{
		mContext = context;
		mListener = listener;
		mDeviceAddress = deviceAddress;

		mBluetoothManager = (BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (!checkBluetoothSupport(mBluetoothAdapter))
		{
			throw new RuntimeException("GATT client requires Bluetooth support");
		}

		// Register for system Bluetooth events
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		mContext.registerReceiver(mBluetoothReceiver, filter);
		if (!mBluetoothAdapter.isEnabled())
		{
			Log.w(TAG, "Bluetooth is currently disabled... enabling");
			mBluetoothAdapter.enable();
		} else {
			Log.i(TAG, "Bluetooth enabled... starting client");
			startClient();
		}
	}

	public void onDestroy()
	{
		mListener = null;

		BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
		if (bluetoothAdapter.isEnabled())
		{
			stopClient();
		}

		mContext.unregisterReceiver(mBluetoothReceiver);
	}

	public void writeInteractor()
	{
		//EditText myText = (EditText)this.activityInteract.findViewById(R.id.textbox);
		//String[] splitMyText = myText.getText().toString().trim().split("\\s+");

		BluetoothGattCharacteristic interactor = mBluetoothGatt
				.getService(SERVICE_UUID)
				.getCharacteristic(CHARACTERISTIC_RX);

/*			byte[] myBytes = new byte[4];
			for (int i=0; i<splitMyText.length; i++)
			{
				Integer myInt = (Integer.parseInt(splitMyText[i], 16));
				byte b = (byte)(myInt & 0xFF);
				myBytes[i] = b;
			}*/
			//interactor.setValue(myBytes);
		mBluetoothGatt.writeCharacteristic((interactor));
	}

	private boolean checkBluetoothSupport(BluetoothAdapter bluetoothAdapter)
	{
		if (bluetoothAdapter == null)
		{
			Log.w(TAG, "Bluetooth is not supported");
			return false;
		}

		if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
		{
			Log.w(TAG, "Bluetooth LE is not supported");
			return false;
		}

		return true;
	}

	private void startClient()
	{
		BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
		mBluetoothGatt = bluetoothDevice.connectGatt(mContext, false, mGattCallback);

		if (mBluetoothGatt == null)
		{
			Log.w(TAG, "Unable to create GATT client");
			return;
		}
	}

	private void stopClient()
	{
		if (mBluetoothGatt != null)
		{
			mBluetoothGatt.close();
			mBluetoothGatt = null;
		}

		if (mBluetoothAdapter != null)
		{
			mBluetoothAdapter = null;
		}
	}
}
