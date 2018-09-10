package com.example.niklas.efc_master;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;

import static com.example.niklas.efc_master.NordicProfile.CHARACTERISTIC_TX;
import static com.example.niklas.efc_master.NordicProfile.DESCRIPTOR_CONFIG;
import static com.example.niklas.efc_master.NordicProfile.SERVICE_UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String device_address;
    public static final String EXTRA_DEVICE_ADDRESS = "mAddress";
    boolean foundGattService = false;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    //Variables used to interpret data coming in and out
    private static final byte ENGINE_RUNNING = 1;
    private static final byte ENGINE_NOT_RUNNING = 2;
    private boolean engine_running = false;
    private int module_temperature = 20;
    private int rpm = 0;
    private int run_time = 0;
    private int attachment_nbr_status = 0; //[String, Blade, Edger, Tiller, Blower, Pole Saw]
    private int trim_mode_status = 0;   //0 = normal, lite = 1
    private int stop_status = 0; //0 = Stop Button NOT pressed, 1 = Stop Button Pressed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device_address = getIntent().getStringExtra(EXTRA_DEVICE_ADDRESS);
        Log.i(TAG, "MainActivity onCreate: " + device_address);
        //Get bluetooth radio, need to do it again in this class
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        //Now setup radio to communicate with WBLE using address found from scanning and passed as parameter to this class
        GattClientCallback mGattCallback = new GattClientCallback();
        BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(device_address);
        mBluetoothGatt = bluetoothDevice.connectGatt(this, false, mGattCallback,2);
        //Setup interface views
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.app_name); // set the top title
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_walbro_w);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        final BottomNavigationView navigation = findViewById(R.id.navigation_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.navigation_start_instructions:
                    return true;

                case R.id.navigation_stats:
                    return true;

                case R.id.navigation_light_trim:
                    return true;

                case R.id.navigation_dash:
                    return true;

                case R.id.navigation_kill:
                    return true;
            }
            return false;
        }
    };

    private boolean loadFragment(Fragment fragment)
    {
        //switching fragment
        if (fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    //Gatt service routines below
    private class GattClientCallback extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_FAILURE) {
                Log.i(TAG, "GATT_FAILURE, Disconnected from GATT client");
               disconnectGattServer();
                return;
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "!GATT_SUCCESS, Disconnected from GATT client");
                disconnectGattServer();
                return;
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "STATE_CONNECTED, discover services");
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "STATE_DISCONNECTED, Disconnected from GATT client");
                disconnectGattServer();
            }
        }
        //Discovering Gatt Services callback
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
                            Log.w(TAG, "Trying to subsribe to notifications: " + connected);
                        }
                    }
                }
             }
            else
            {
                Log.w(TAG, "onServicesDiscovered NO GATT_SUCCESS: " + status);
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
                if (data[0] == ENGINE_NOT_RUNNING && data.length == data[1])
                {
                    engine_running = false;
                    module_temperature = data[2];
                    attachment_nbr_status = data[3];
                    Log.i(TAG, "ENGINE_NOT_RUNNING!: " + module_temperature + "," + attachment_nbr_status);
                }
                else if (data[0] == ENGINE_RUNNING && data.length == data[1])
                {
                    engine_running = true;
                    rpm = (data[3]<< 8)&0x0000ff00|(data[2]&0x000000ff);
                    run_time = (data[5]<< 8)&0x0000ff00|(data[4]&0x000000ff);
                    module_temperature = data[6];
                    attachment_nbr_status = data[7];
                    trim_mode_status = data[8];
                    stop_status = data[9];
                    Log.i(TAG, "ENGINE_RUNNING!: " + rpm + "," + run_time + "," + attachment_nbr_status + "," + trim_mode_status + "," + stop_status);
                }
                else
                {
                    Log.i(TAG, "Garbage Data!");
                }
            }
        }
    }

    public void disconnectGattServer()
    {
        Log.i(TAG, "Closing Gatt connection");
        if (mBluetoothGatt != null)
        {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
        }
    }
}
