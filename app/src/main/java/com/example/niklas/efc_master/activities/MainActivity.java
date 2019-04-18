package com.example.niklas.efc_master.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.niklas.efc_master.fragments.DashboardFragment;
//import com.example.niklas.efc_master.fragments.StatsTabDetailsFragment;
//import com.example.niklas.efc_master.fragments.StatsTabRPMBinsFragment;
import com.example.niklas.efc_master.fragments.StatsTabRecentRunsFragment;
import com.example.niklas.efc_master.profiles.Igndata;
import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.listeners.ShakeListener;
import com.example.niklas.efc_master.fragments.StartFragment;
import com.example.niklas.efc_master.fragments.StatsTabsFragment;
import com.example.niklas.efc_master.fragments.StartHighTempFragment;
import com.example.niklas.efc_master.profiles.protocol;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;

import static com.example.niklas.efc_master.profiles.NordicProfile.CHARACTERISTIC_RX;
import static com.example.niklas.efc_master.profiles.NordicProfile.CHARACTERISTIC_TX;
import static com.example.niklas.efc_master.profiles.NordicProfile.DESCRIPTOR_CONFIG;
import static com.example.niklas.efc_master.profiles.NordicProfile.SERVICE_UUID;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

	private SensorManager sensorManager;
	Sensor mSensorGyroscope;
	Sensor mSensorAccelerometer;
	SharedPreferences sharedPreferences;

	private static final String TAG = MainActivity.class.getSimpleName();
	private static String device_address;
	public static final String EXTRA_DEVICE_ADDRESS = "mAddress";
	public static final int STRING_MAX_SPEED = 9300;
	public static final int STRING_DEBUMP_SPEED = 9100;
	public String bumpStringImg;
	public static int[] array_last_run = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static int[] array_total_run = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static int[] array_start_temp = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static int[] array_usage = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static final String MY_PREFERENCES = "my_preferences";
	public static final String LAST_RUN_TIME_AND_DATE = "lastRunKey";
	public String RunTimeAndDate = "";
	public static String runtime;

	float oldX = -50;
	float oldY = -50;
	float oldZ = -50;

	public int lastRun2500 = 0;

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	public BluetoothGatt mBluetoothGatt;
	//Variables used to interpret data coming in and out
	public Igndata live_data = new Igndata();
	private boolean engine_running = false;
	private boolean dashboard_fragment_loaded = false;
	public boolean start_fragment_loaded = false;
	public boolean stats_fragment_loaded = false;
	private boolean start_high_temp_fragment_loaded = false;
	private boolean lite_trim_on = false;
	private boolean hide_running_features = false;
	private boolean hide_starting_features = false;
	private boolean start_rpm_creep = false;
	public static boolean start_bump_notif = false;
	public boolean start_change_notif = false;
	public boolean did_we_clear_bump = true;
	public boolean did_we_clear_change = true;
	public boolean detected_accelerometer_bump = false;
	public boolean did_we_recieve_last_run_date = false;
	public static boolean epoch_lastRun_sent = false;
	public static boolean first_connection = false;
	private boolean start_gyroscope = false;

	private BottomNavigationView navigation;
	private StartFragment startFragment = new StartFragment();
	private StartHighTempFragment startHighTempFragment = new StartHighTempFragment();
	private StatsTabsFragment statsTabsFragment = new StatsTabsFragment();
	private DashboardFragment dashboardFragment = new DashboardFragment();
	private StatsTabRecentRunsFragment statsTabRecentRunsFragment = new StatsTabRecentRunsFragment();
	private ShakeListener mShaker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "MainActivity_onCreate: " + device_address);
		//Try to connect to Gatt Server
		device_address = getIntent().getStringExtra(EXTRA_DEVICE_ADDRESS);
		connectToGattServer();

		//Get stored strings from phone storage, for example last run data
		sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
		if (sharedPreferences.contains(LAST_RUN_TIME_AND_DATE))
		{
			RunTimeAndDate = sharedPreferences.getString(LAST_RUN_TIME_AND_DATE, "");
		}

		//Setup interface views
		setContentView(R.layout.activity_main);

		navigation = findViewById(R.id.navigation_main);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		setConditionalStartingFragment();
		//setStartFragment();
		hideRunningFeatures();
		start_fragment_loaded = true;
		dashboard_fragment_loaded = false;

		//writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_STRING);

		bumpStringImg = "string";  // default
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (null != sensorManager) {
			mSensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//startAccelerometer();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i)
	{

	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent)
	{
        DecimalFormat df = new DecimalFormat("#0.000");
        // The sensor type (as defined in the Sensor class).
        int sensorType = sensorEvent.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                float roll = sensorEvent.values[1];
                float pitch = sensorEvent.values[2];
                float azimuth = sensorEvent.values[0];

                // Fill in the string placeholders and set the textview text.
                //Log.i(TAG, "Roll: " + df.format(roll) + "   PITCH: " + df.format(pitch) + "   AZIMUTH: " + df.format(azimuth));
/*                if (roll < 0 && azimuth < 0)
                {
                    Log.i(TAG, "DETECTED BUMP!!! --- Roll: " + df.format(roll) + "   PITCH: " + df.format(pitch) + "   AZIMUTH: " + df.format(azimuth));
                    detected_accelerometer_bump = true;
                }*/

                //dashboardFragment.updateBubbleLevel(roll, pitch);
                break;
            case Sensor.TYPE_GYROSCOPE:
                float myX = sensorEvent.values[0];
                float myY = sensorEvent.values[1];
                float myZ = sensorEvent.values[2];
                if (live_data.getTps_status() != protocol.TPS_IDLE && live_data.getRpm() > 4500) {
/*                    if (myX < -0.4 || myX > 0.4 || myY < -0.4 || myY > 0.4 || myZ < -2 || myZ > 2) {
                        if ((myX < -0.7 || myX > 0.7 || myY < -0.6 || myY > 0.6) && (myZ < -0.4 || myZ > 0.4)) {*/
                    if ((myX < -0.8 || myX > 1) && (myY < -0.7 || myY > 0.7) && (myZ < -2.2 || myZ > 2)) {
                            Log.i(TAG, "CAREFUL!: X:" + df.format(myX) + "   Y:" + df.format(myY) + "   Z:" + df.format(myZ));
                            writeToIgnitionModule(protocol.BTN_SAFETY, protocol.SAFETY_ON);
                        //}
                    }
                }
                break;
        }
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
                    Log.i(TAG, "MainActivity_nav_start_instructions " + "CLICKED!");
					setConditionalStartingFragment();
					hideRunningFeatures();
					stats_fragment_loaded = false;
					return true;

				case R.id.navigation_stats:
                    Log.i(TAG, "MainActivity_nav_stats " + "CLICKED!");
					writeToIgnitionModule(protocol.BTN_STATS_REQUEST,protocol.REQUEST_STATS); //Ask for last run_time stats from module
					try {//We need a delay here so the BLE module has time to send the stats pages needed for last_run in stats tab
						Thread.sleep(250);
					}
					catch (InterruptedException ignored)
					{}
					did_we_recieve_last_run_date = false;
					getLastRunDateTime();
					loadFragment(statsTabsFragment);
					stats_fragment_loaded = true;
					hideRunningFeatures();
					return true;

				case R.id.navigation_light_trim:
                    Log.i(TAG, "MainActivity_nav_start_light_trim " + "CLICKED!");
					hideStartingFeatures();
					stats_fragment_loaded = false;
					lite_trim_on = !lite_trim_on; //change state of lite trim mode
					if (lite_trim_on) {
                        Log.i(TAG, "MainActivity_nav_start_instructions " + "LITE TRIM ON");
						writeToIgnitionModule(protocol.BTN_TRIM_MODE, protocol.LITE_TRIM);
						setCheckable(navigation, true);
					}
					else
					{
                        Log.i(TAG, "MainActivity_nav_start_instructions " + "LITE TIRM OFF");
						writeToIgnitionModule(protocol.BTN_TRIM_MODE, protocol.NORMAL_TRIM);
						setCheckable(navigation, false);
					}
					return true;

				case R.id.navigation_kill:
                    Log.i(TAG, "MainActivity_nav_kill " + "CLICKED!");
					stats_fragment_loaded = false;
					hideStartingFeatures();
					writeToIgnitionModule(protocol.BTN_STOP, protocol.STOP_ON);
					setCheckable(navigation, true);
					return true;

				case R.id.navigation_tool:
					if (!lite_trim_on) {
                        Log.i(TAG, "MainActivity_nav_tool " + "CLICKED!");
						stats_fragment_loaded = false;
						final Intent intent = new Intent(getApplicationContext(), ToolSelectionActivity.class);
						startActivityForResult(intent, 1);
					}
			}
			return false;
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{//TOOL Selection from menu, also sends that to BLE module
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == Activity.RESULT_OK)
		{
			int toolCode = Integer.parseInt(data.getStringExtra(ToolSelectionActivity.RESULT_TOOL));
			byte myTool  = (byte)(toolCode);
			writeToIgnitionModule(protocol.BTN_TOOL_SELECT, myTool);
			Toast.makeText(getApplicationContext(),protocol.TOOL_STR[myTool], Toast.LENGTH_SHORT).show();
			if (dashboard_fragment_loaded)
				dashboardFragment.updateToolView(myTool);
/*			if (!did_we_clear_bump && dashboard_fragment_loaded)
				dashboardFragment.flashBUMP();*/
			Bundle bundle = new Bundle();
			bundle.putInt("TOOL", myTool);
			dashboardFragment.setArguments(bundle);
			bumpStringImg = "string";
            Log.i(TAG, "MainActivity_OnActivityResult " + "TOOL: " + myTool);
		}
	}

	@Override
	public void onResume(){
		super.onResume();
	}

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
			Log.i(TAG, "MainActivity_GattClientCallBack STATE CHANGE! GATT: " + gatt + " STATUS: " + status + " NEW STATE: " + newState);
			if (status == BluetoothGatt.GATT_FAILURE) {
				Log.i(TAG, "MainActivity_GattClientCallBack GATT_FAILURE, Disconnected from GATT client");
				disconnectGattServer();
				return;
			} else if (status != BluetoothGatt.GATT_SUCCESS) {
				Log.i(TAG, "MainActivity_GattClientCallBack !GATT_SUCCESS, Disconnected from GATT client");
					disconnectGattServer();
				return;
			}
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				Log.i(TAG, "MainActivity_GattClientCallBack STATE_CONNECTED, discover services");
				gatt.discoverServices();
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				Log.i(TAG, "MainActivity_GattClientCallBack STATE_DISCONNECTED, Disconnected from GATT client");
				disconnectGattServer();
			}
		}

		//Discovering Gatt Services callback
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status)
		{
			if (status == BluetoothGatt.GATT_SUCCESS)
			{
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
							boolean connected = gatt.writeDescriptor(descriptor);
							Log.w(TAG, "MainActivity_OnServicesDiscovered Trying to subscribe to notifications: " + connected);
						}
					}
				}
			}
			else
			{
				Log.w(TAG, "MainActivity_onServicesDiscovered NO GATT_SUCCESS: " + status);
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
				first_connection = true;
				final byte[] data = characteristic.getValue();
				//if engine not running
				if (data[0] == live_data.ENGINE_NOT_RUNNING && data.length == data[1])
				{
					if (start_gyroscope) {
						stopGyroscope();
						start_gyroscope = false;
					}
					engine_running = false;
					live_data.setTemperature(data[2]);
					live_data.setAttachment_nbr_status(data[3]);
					//live_data.setTps_status(data[4]);
					live_data.setTps_status(1);  //Removed sensing of tps by BLE module, interfering with ignition module when programming and startup
					live_data.setError_code(data[5]); // flash if ==1
					live_data.setOil_life_cntr(data[6]); //how about now?
					if (!epoch_lastRun_sent) {
						send_EPOCH_to_BLE_module(protocol.EPOCH_ID_LAST_RUN);
                        Log.i(TAG, "MainActivity_readCounterCharacteristic " + "EPOCH LAST RUN SENT!");
					}
					Log.i(TAG, "ENGINE_NOT_RUNNING!: " + "TEMP: " + live_data.getTemperature() + ", TOOL: " + live_data.getAttachment_nbr_status() + ", TPS: " + live_data.getTps_status() +
					 														", OIL: " + live_data.getOil_life_cntr() +", RUNTIME: " + live_data.getRun_time());
					getLastRunDateTime();
					//hide navigational features:
					if (!hide_running_features)
						hideRunningFeatures();

					if (!start_fragment_loaded)
					{
						setConditionalStartingFragment();
						setStartFragment();
						hideRunningFeatures();
						start_fragment_loaded = true;
						dashboard_fragment_loaded = false;
					}

					if (live_data.getTps_status() != protocol.TPS_IDLE)
					{
						start_fragment_loaded = false;
						navigation.findViewById(R.id.navigation_stats).setEnabled(false);
					}
					else
					    navigation.findViewById(R.id.navigation_stats).setEnabled(true);

/*					if (live_data.getTps_status() == protocol.TPS_IDLE)
						navigation.findViewById(R.id.navigation_stats).setEnabled(true);*/

					if (!stats_fragment_loaded)
						updateStartingScreen();
					if (stats_fragment_loaded)
					{
						if (live_data.getError_code() == 1)
						{
							start_fragment_loaded = false;
							stats_fragment_loaded = false;
							writeToIgnitionModule(protocol.BTN_CLEAR_CODE, protocol.RESET_CODE);
						}
					}
					//Log.i(TAG, "MainActivity_readCounterCharacteristic ENGINE_NOT_RUNNING!: " + "TEMP: " + live_data.getTemperature() + ", TPS: " + live_data.getTps_status());
				}

				//if engine running
				else if (data[0] == live_data.ENGINE_RUNNING && data.length == data[1])
				{
					engine_running = true;
					live_data.setRpm((data[3]<< 8)&0x0000ff00|(data[2]&0x000000ff));
					live_data.setRun_time((data[5]<< 8)&0x0000ff00|(data[4]&0x000000ff));
					live_data.setTemperature(data[6]);
					live_data.setAttachment_nbr_status(data[7]);
					live_data.setTrim_mode_status(data[8]);
					live_data.setStop_status(data[9]);
					live_data.setTps_status(data[10]);
					live_data.setTotal_run_time((data[12]<< 8)&0x0000ff00|(data[11]&0x000000ff));
					live_data.setOil_life_cntr(data[13]);

					//hide navigational features:
					if (!hide_starting_features)
						hideStartingFeatures();

					if (!dashboard_fragment_loaded) {
						loadFragment(dashboardFragment);
						navigation.findViewById(R.id.navigation_kill).setBackgroundColor(getColor(R.color.colorStopButton));
						dashboard_fragment_loaded = true;
						start_fragment_loaded = false;
						start_high_temp_fragment_loaded = false;
						did_we_recieve_last_run_date = false;
					}
					//Check if we need to cancel lite trim mode, user can by touching throttle
					if (lite_trim_on && live_data.getTps_status() != protocol.TPS_IDLE)
						disable_lite_trim();
					//When first entering, check what attachment the ignition module have and set that as our selection
					updateRunningScreen();
					epoch_lastRun_sent = false;
					Log.i(TAG, "MainActivity_readCounterCharacteristic ENGINE_RUNNING!: " + "TEMP: " + live_data.getTemperature() + ", TPS: " + live_data.getTps_status());
				}
				else if (data[0] == live_data.DETAILS_STATS_PAGE && data.length == data[1])
				{
					live_data.setTotal_run_time((data[2]&0xff) + ((data[3]&0xff)*256));
					live_data.setRun_time((data[4]&0xff) + ((data[5]&0xff)*256));
					live_data.setTotal_run_date((data[6]&0xff) + ((data[7]&0xff)*256) + ((data[8]&0xff)*256*256) + ((data[9]&0xff)*256*256*256));
                    live_data.setOil_life_date((data[10]&0xff) + ((data[11]&0xff)*256) + ((data[12]&0xff)*256*256) + ((data[13]&0xff)*256*256*256));

					Log.i(TAG, "MainActivity_readCounterCharacteristic DETAILS_STATE_PAGE: " + live_data.getTotal_run_date());
				}
				else if (data[0] == live_data.LAST_RUN_STATS_PAGE_1 && data.length == data[1])
				{
					for (int i=0, j=2; i<9; i++, j +=2) {
						array_last_run[i] = (data[j]&0xff) + ((data[j+1]&0xff)*256); //Make sure it is interpreted as positive number, looking for times
					}
				}
				else if (data[0] == live_data.LAST_RUN_STATS_PAGE_2 && data.length == data[1])
				{
					for (int i=9, j=2; i<18; i++, j +=2) {
						array_last_run[i] = (data[j]&0xff) + ((data[j+1]&0xff)*256);
					}
				}
				else if (data[0] == live_data.TOTAL_RUN_STATS_PAGE_1 && data.length == data[1])
				{
					for (int i=0, j=2; i<9; i++, j +=2) {
						array_total_run[i] = (data[j]&0xff) + ((data[j+1]&0xff)*256);
					}
				}
				else if (data[0] == live_data.TOTAL_RUN_STATS_PAGE_2 && data.length == data[1])
				{
					for (int i=9, j=2; i<18; i++, j +=2) {
						array_total_run[i] = (data[j]&0xff) + ((data[j+1]&0xff)*256);
					}
				}
				else if (data[0] == live_data.START_TEMP_STATS_PAGE && data.length == data[1])
				{
					for (int i=0; i<18; i++) {
						array_start_temp[i] = data[i+2];  //Need to interpret int correctly for minus temps, only one byte per bin!
					}
				}
				else if (data[0] == live_data.USAGE_STATS_PAGE && data.length == data[1])
				{
					for (int i=0; i<18; i++) {
						array_usage[i] = (data[i+2]&0xff); //Need only positive numbers and one byte per bin
					}
				}
				else
				{
					Log.i(TAG, "MainActivity_Garbage Data!");
				}
			}
		}
	}

	public void writeToIgnitionModule(byte btn_id, byte btn_state)
	{
		BluetoothGattCharacteristic interactor = mBluetoothGatt
				.getService(SERVICE_UUID)
				.getCharacteristic(CHARACTERISTIC_RX);
		//Prepare protocol packet
		byte[] sendBytes = new byte[4];
		sendBytes[0] = protocol.PHONE_PROTOCOL_NBR;
		sendBytes[1] = protocol.PHONE_PROTOCOL_BYTES;
		sendBytes[2] = btn_id;
		sendBytes[3] = btn_state;

		interactor.setValue(sendBytes); //packet must go trough this xxx.setValue before being sent out
		mBluetoothGatt.writeCharacteristic(interactor);
	}

	public void connectToGattServer()
	{
		//Get bluetooth radio, need to do it again in this class
		mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		if (mBluetoothManager != null) {
			mBluetoothAdapter = mBluetoothManager.getAdapter();
		}
		//Now setup radio to communicate with WBLE using address found from scanning and passed as parameter to this class
		GattClientCallback mGattCallback = new GattClientCallback();
		BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(device_address);

		//Log.i(TAG, "UUIDS: " + bluetoothDevice.getUuids().length);
		mBluetoothGatt = bluetoothDevice.connectGatt(this, false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
	}

	public void disconnectGattServer()
	{
		Log.i(TAG, "Closing Gatt connection");
		if (mBluetoothGatt != null)
		{
			mBluetoothGatt.disconnect();
			mBluetoothGatt.close();
			Intent intent = new Intent(this, ScanActivity.class);
			startActivity(intent);
			//this.finish();
		}
	}

	public void hideRunningFeatures()
	{
		new Handler(getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				navigation.findViewById(R.id.navigation_start_instructions).setVisibility(View.VISIBLE);
				navigation.findViewById(R.id.navigation_stats).setVisibility(View.VISIBLE);
				navigation.findViewById(R.id.navigation_tool).setVisibility(View.VISIBLE);
				navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.GONE);
				navigation.findViewById(R.id.navigation_kill).setVisibility(View.GONE);
				hide_running_features = true;
				hide_starting_features = false;
				//navigation.findViewById(R.id.navigation_tool).setVisibility(View.GONE);
			}
		});
	}

	public void onDestroy()
	{
		super.onDestroy();
		disconnectGattServer();
		first_connection = false;
		Log.i(TAG, "Closing Gatt connection -- onDestroy");
	}

	public void hideStartingFeatures()
	{
		//navigation.invalidate();
		new Handler(getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				navigation.findViewById(R.id.navigation_start_instructions).setVisibility(View.GONE);
				navigation.findViewById(R.id.navigation_stats).setVisibility(View.GONE);
				if (live_data.getAttachment_nbr_status() == protocol.TOOL_STRING)
					navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.VISIBLE);
				navigation.findViewById(R.id.navigation_kill).setVisibility(View.VISIBLE);
				hide_running_features = false;
				hide_starting_features = true;
			}
		});
	}

	public void setStartFragment()
	{
		new Handler(getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				navigation.setSelectedItemId(R.id.navigation_start_instructions);
			}
		});
	}

	public void setConditionalStartingFragment()
	{
		if (live_data.getTemperature() > 27)
		{
			loadFragment(startHighTempFragment);
			start_high_temp_fragment_loaded = true;
		}
		else
		{
			loadFragment(startFragment);
			start_high_temp_fragment_loaded = false;
		}
	}

	public static void setCheckable(BottomNavigationView view, boolean checkable) {
		final Menu menu = view.getMenu();
		for(int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setCheckable(checkable);
		}
	}

	@Override
	public void onBackPressed() {
		// Simply Do noting!
	}

	public void getLastRunDateTime()
	{
		String date;
		if (live_data.getRun_time() >= 2 && !engine_running && !did_we_recieve_last_run_date)
		{
			did_we_recieve_last_run_date = true;
			DateFormat df = new SimpleDateFormat("MMM d, yyyy HH:mm:ss z");
			date = df.format(Calendar.getInstance().getTime());
			float f = live_data.getRun_time()*1024/1000;   //1.024 seconds per tick
			runtime = String.valueOf((int)(f));
			RunTimeAndDate = runtime + "-" + date;
			Bundle bundle = new Bundle();
			bundle.putString("LAST_RUN_DATE", RunTimeAndDate);
			bundle.putFloat("TOTAL_RUN_TIME", (float)(live_data.getTotal_run_time())*167/10); //scale into seconds, 16.7 seconds per tick
			statsTabsFragment.setArguments(bundle);
			//Store last run in phone storage
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(LAST_RUN_TIME_AND_DATE,RunTimeAndDate);
			editor.apply();
		}
	}

	public void startAccelerometer()
	{
		sensorManager.registerListener(MainActivity.this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void stopAccelerometer()
	{
		sensorManager.unregisterListener(this);
	}

    public void startGyroscope()
    {
        sensorManager.registerListener(MainActivity.this, mSensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);//gyro
    }

    public void stopGyroscope()
    {
        sensorManager.unregisterListener(this, mSensorGyroscope);
    }

	public void updateStartingScreen()
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//if selected nav item is Start:
				if (live_data.getTemperature() < 27 && !start_high_temp_fragment_loaded)
				{
					startFragment.updatePrimerBulb(live_data.getTemperature());
					if (live_data.getError_code() == 1)
					{
						startFragment.flashSqueezeThrottle();
						writeToIgnitionModule(protocol.BTN_CLEAR_CODE, protocol.RESET_CODE);
					}
				}
				else if (live_data.getTemperature() < 27 && start_high_temp_fragment_loaded)
				{
					setConditionalStartingFragment();
					setStartFragment();
				}

				else if (live_data.getTemperature() > 27 && !start_high_temp_fragment_loaded)
				{
					setConditionalStartingFragment();
					setStartFragment();
				}
			}
		});
	}

	public void updateRunningScreen()
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (live_data.getAttachment_nbr_status() == protocol.TOOL_BLADE)
					dashboardFragment.updateToolView(0);
				if (live_data.getAttachment_nbr_status() == protocol.TOOL_BLOWER)
					dashboardFragment.updateToolView(1);
				if (live_data.getAttachment_nbr_status() == protocol.TOOL_EDGER)
					dashboardFragment.updateToolView(2);
				if (live_data.getAttachment_nbr_status() == protocol.TOOL_POLE_SAW)
					dashboardFragment.updateToolView(3);
				if (live_data.getAttachment_nbr_status() == protocol.TOOL_TILLER)
					dashboardFragment.updateToolView(4);
				if (live_data.getAttachment_nbr_status() == protocol.TOOL_STRING)
					dashboardFragment.updateToolView(5);

				//if selected nav item is Dash:
				dashboardFragment.updateSpeedometer(live_data.getRpm());
				dashboardFragment.updateRunTimer(live_data.getRun_time());
				dashboardFragment.updateOilLife(live_data.getOil_life_cntr());
				dashboardFragment.updateThermometer(live_data.getTemperature());

				if (engine_running && live_data.getTps_status() != protocol.TPS_IDLE)
				{
					if (!start_gyroscope) {
						startGyroscope();
						start_gyroscope = true;
					}
					navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.GONE);
					navigation.findViewById(R.id.navigation_tool).setVisibility(View.GONE);

					if (live_data.getRpm() > STRING_MAX_SPEED && live_data.getAttachment_nbr_status() == protocol.TOOL_STRING && !start_bump_notif) //string
					{
							start_bump_notif = true;
							did_we_clear_bump = false;
							dashboardFragment.flashBUMP();
					}
					else if (live_data.getRpm() <= STRING_DEBUMP_SPEED && live_data.getAttachment_nbr_status() == protocol.TOOL_STRING && start_bump_notif) //string
					{
						start_bump_notif = false;
						did_we_clear_bump = true;
						Toast.makeText(getApplicationContext(), "BUMPED STRING", Toast.LENGTH_SHORT).show();
						dashboardFragment.myBUMP.clearAnimation();
					}
				}
				else
				{
					if (start_gyroscope) {
						stopGyroscope();
						start_gyroscope = false;
					}
					if (live_data.getAttachment_nbr_status() == protocol.TOOL_STRING) //live_data.getAttachment_nbr_status() == protocol.TOOL_STRING)
						navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.VISIBLE);
					else
						navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.GONE);
					navigation.findViewById(R.id.navigation_tool).setVisibility(View.VISIBLE);

					if (start_bump_notif && !did_we_clear_bump && live_data.getAttachment_nbr_status() != protocol.TOOL_STRING)
					{
						start_bump_notif = false;
						did_we_clear_bump = true;
						dashboardFragment.myBUMP.clearAnimation();
					}
/*					else if (start_bump_notif && !did_we_clear_bump && live_data.getAttachment_nbr_status() == protocol.TOOL_STRING)
					{
						start_bump_notif = true;
						did_we_clear_bump = false;
						dashboardFragment.flashBUMP();
					}*/
					else if (start_bump_notif && live_data.getRpm() <= STRING_DEBUMP_SPEED && live_data.getAttachment_nbr_status() == protocol.TOOL_STRING)
					{
						start_bump_notif = false;
						did_we_clear_bump = true;
						Toast.makeText(getApplicationContext(), "BUMPED STRING", Toast.LENGTH_SHORT).show();
						dashboardFragment.myBUMP.clearAnimation();
					}
				}
			}
		});
	}

	public void send_EPOCH_to_BLE_module(byte id)
	{//Date
		long epoch_time = Calendar.getInstance().getTimeInMillis()/1000;//1000;
		BluetoothGattCharacteristic interactor = mBluetoothGatt
				.getService(SERVICE_UUID)
				.getCharacteristic(CHARACTERISTIC_RX);
		//Prepare protocol packet
		byte[] sendBytes = new byte[7];
		sendBytes[0] = protocol.PHONE_EPOCH_PROTOCOL_NBR;
		sendBytes[1] = protocol.PHONE_EPOCH_PROTOCOL_BYTES;
		if (id == protocol.EPOCH_ID_LAST_RUN)
		{
			sendBytes[2] = protocol.EPOCH_ID_LAST_RUN;
		}
		else
		{
			sendBytes[2] = protocol.EPOCH_ID_OIL_CHANGE;
		}
		sendBytes[3] = (byte)(epoch_time);
		sendBytes[4] = (byte)(epoch_time>>8);
		sendBytes[5] = (byte)(epoch_time>>16);
		sendBytes[6] = (byte)(epoch_time>>24);
		interactor.setValue(sendBytes); //packet must go trough this xxx.setValue before being sent out
		mBluetoothGatt.writeCharacteristic(interactor);
		epoch_lastRun_sent = true;
	}

	public void disable_lite_trim()
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				navigation.findViewById(R.id.navigation_light_trim).performClick();
			}});
	}
}