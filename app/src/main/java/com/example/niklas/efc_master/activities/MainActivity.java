package com.example.niklas.efc_master.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;
import com.example.niklas.efc_master.fragments.DashboardFragment;
import com.example.niklas.efc_master.fragments.StatsTabDetailsFragment;
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
import java.util.Calendar;

import static com.example.niklas.efc_master.profiles.NordicProfile.CHARACTERISTIC_RX;
import static com.example.niklas.efc_master.profiles.NordicProfile.CHARACTERISTIC_TX;
import static com.example.niklas.efc_master.profiles.NordicProfile.DESCRIPTOR_CONFIG;
import static com.example.niklas.efc_master.profiles.NordicProfile.SERVICE_UUID;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

	private SensorManager sensorManager;
	Sensor accelerometer;

	private static final String TAG = MainActivity.class.getSimpleName();
	private static String device_address;
	public static final String EXTRA_DEVICE_ADDRESS = "mAddress";
	public static final int STRING_MAX_SPEED = 8100;
	public int startingCreepRPM = 8100;
	public String bumpStringImg;

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;
	//Variables used to interpret data coming in and out
	public Igndata live_data = new Igndata();
	private boolean engine_running = false;
	private boolean dashboard_fragment_loaded = false;
	public boolean start_fragment_loaded = false;
	private boolean start_high_temp_fragment_loaded = false;
	private boolean lite_trim_on = false;
	private boolean hide_running_features = false;
	private boolean hide_starting_features = false;
	private boolean start_rpm_creep = false;
	public static boolean start_bump_notif = false;
	public boolean did_we_clear_bump = true;
	public boolean detected_accelerometer_bump = false;
	public boolean did_we_recieve_last_run_date = false;

	private BottomNavigationView navigation;
	private StartFragment startFragment = new StartFragment();
	private StartHighTempFragment startHighTempFragment = new StartHighTempFragment();
	private StatsTabsFragment statsTabsFragment = new StatsTabsFragment();
	private DashboardFragment dashboardFragment = new DashboardFragment();

	private ShakeListener mShaker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "MainActivity onCreate: " + device_address);
		//Try to connect to Gatt Server
		device_address = getIntent().getStringExtra(EXTRA_DEVICE_ADDRESS);
		connectToGattServer();

		//Setup interface views
		setContentView(R.layout.activity_main);

		navigation = findViewById(R.id.navigation_main);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		setConditionalStartingFragment();
		setStartFragment();
		hideRunningFeatures();
		start_fragment_loaded = true;
		dashboard_fragment_loaded = false;

		bumpStringImg = "string";  // default
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i)
	{

	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent)
	{
		float myX = sensorEvent.values[1];
		float myY = sensorEvent.values[1];
		float myZ = sensorEvent.values[2];
		DecimalFormat df = new DecimalFormat("#0.000");
		if (myX > -20 && myX < -3 && myY > -20 && myY < -3 && myZ > 12) {
			detected_accelerometer_bump = true;
			Log.i(TAG, "BUMP: X:" + df.format(myX) + "   Y:" + df.format(myY) + "   Z:" + df.format(myZ));
		}
		Log.i(TAG, "AXIS POS: X:" + df.format(myX) + "   Y:" + df.format(myY) + "   Z:" + df.format(myZ));
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
					setConditionalStartingFragment();
					hideRunningFeatures();
					return true;

				case R.id.navigation_stats:
					loadFragment(statsTabsFragment);
					hideRunningFeatures();
					return true;

				case R.id.navigation_light_trim:
					hideStartingFeatures();
					lite_trim_on = !lite_trim_on; //change state of lite trim mode
					if (lite_trim_on) {
						writeToIgnitionModule(protocol.BTN_TRIM_MODE, protocol.LITE_TRIM);
						setCheckable(navigation, true);
					}
					else
					{
						writeToIgnitionModule(protocol.BTN_TRIM_MODE, protocol.NORMAL_TRIM);
						setCheckable(navigation, false);
					}

					return true;

				case R.id.navigation_kill:
					hideStartingFeatures();
					writeToIgnitionModule(protocol.BTN_STOP, protocol.STOP_ON);
					setCheckable(navigation, true);
					return true;

				case R.id.navigation_tool:
					final Intent intent = new Intent(getApplicationContext(), ToolSelectionActivity.class);
					startActivityForResult(intent, 1);
			}
			return false;
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == Activity.RESULT_OK)
		{
			String toolCode = data.getStringExtra(ToolSelectionActivity.RESULT_TOOL);
			Integer myTool = Integer.valueOf(toolCode);

			if (myTool == 0) {
				writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_BLADE);
				if (dashboard_fragment_loaded)
					dashboardFragment.updateToolView(myTool);
				if (!did_we_clear_bump)
					dashboardFragment.myBUMP.clearAnimation();
				Bundle bundle = new Bundle();
				bundle.putInt("TOOL", myTool);
				dashboardFragment.setArguments(bundle);
				Toast.makeText(getApplicationContext(), "BLADE ATTACHMENT", Toast.LENGTH_SHORT).show();
				bumpStringImg = "not string";
			}

			if (myTool == 1) {
				writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_BLOWER);
				if (dashboard_fragment_loaded)
					dashboardFragment.updateToolView(myTool);
				if (!did_we_clear_bump)
					dashboardFragment.myBUMP.clearAnimation();
				Bundle bundle = new Bundle();
				bundle.putInt("TOOL", myTool);
				dashboardFragment.setArguments(bundle);
				Toast.makeText(getApplicationContext(), "BLOWER ATTACHMENT", Toast.LENGTH_SHORT).show();
				bumpStringImg = "not string";
			}

			if (myTool == 2) {
				writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_EDGER);
				if (dashboard_fragment_loaded)
					dashboardFragment.updateToolView(myTool);
				if (!did_we_clear_bump)
					dashboardFragment.myBUMP.clearAnimation();
				Bundle bundle = new Bundle();
				bundle.putInt("TOOL", myTool);
				dashboardFragment.setArguments(bundle);
				Toast.makeText(getApplicationContext(), "EDGER ATTACHMENT", Toast.LENGTH_SHORT).show();
				bumpStringImg = "not string";
			}

			if (myTool == 3) {
				writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_POLE_SAW);
				if (dashboard_fragment_loaded)
					dashboardFragment.updateToolView(myTool);
				if (!did_we_clear_bump)
					dashboardFragment.myBUMP.clearAnimation();
				Bundle bundle = new Bundle();
				bundle.putInt("TOOL", myTool);
				dashboardFragment.setArguments(bundle);
				Toast.makeText(getApplicationContext(), "POLE SAW ATTACHMENT", Toast.LENGTH_SHORT).show();
				bumpStringImg = "not string";
			}

			if (myTool == 4)
			{
				writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_TILLER);
				if (dashboard_fragment_loaded)
					dashboardFragment.updateToolView(myTool);
				if (!did_we_clear_bump)
					dashboardFragment.myBUMP.clearAnimation();
				Bundle bundle = new Bundle();
				bundle.putInt("TOOL", myTool);
				dashboardFragment.setArguments(bundle);
				Toast.makeText(getApplicationContext(), "TILLER ATTACHMENT", Toast.LENGTH_SHORT).show();
				bumpStringImg = "not string";
			}

			if (myTool == 5) {
				writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_STRING);
				if (dashboard_fragment_loaded)
					dashboardFragment.updateToolView(myTool);
				if (!did_we_clear_bump && dashboard_fragment_loaded) {
					dashboardFragment.flashBUMP();
					listenForBUMP();

				}
				Bundle bundle = new Bundle();
				bundle.putInt("TOOL", myTool);
				dashboardFragment.setArguments(bundle);
				Toast.makeText(getApplicationContext(), "STRING ATTACHMENT", Toast.LENGTH_SHORT).show();
				bumpStringImg = "string";

			}
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
			if (status == BluetoothGatt.GATT_FAILURE) {
				Log.i(TAG, "GATT_FAILURE, Disconnected from GATT client");
				disconnectGattServer();
				return;
			} else if (status != BluetoothGatt.GATT_SUCCESS) {
				Log.i(TAG, "!GATT_SUCCESS, Disconnected from GATT client");
				disconnectGattServer();
				// connectToGattServer();
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
							Log.w(TAG, "Trying to subscribe to notifications: " + connected);
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
				final byte[] data = characteristic.getValue();
				//if engine not running
				if (data[0] == live_data.ENGINE_NOT_RUNNING && data.length == data[1])
				{
					engine_running = false;
					stopAccelerometer();
					//navigation.findViewById(R.id.navigation_stats).setEnabled(true);
					live_data.setTemperature(data[2]);
					live_data.setAttachment_nbr_status(data[3]);
					live_data.setTps_status(data[4]);
					live_data.setError_code(data[5]); // flash if ==1
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

					if (live_data.getTps_status() != 1)
					{
						start_fragment_loaded = false;
						navigation.findViewById(R.id.navigation_stats).setEnabled(false);
					}

					if (live_data.getTps_status() == 1)
						navigation.findViewById(R.id.navigation_stats).setEnabled(true);

					updateStartingScreen();

					//Log.i(TAG, "ENGINE_NOT_RUNNING!: " + live_data.getTemperature() + "," + live_data.getAttachment_nbr_status() + "," + live_data.getTps_status());
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
					updateRunningScreen();
					//Log.i(TAG, "ENGINE_RUNNING!: " + live_data.getRpm() + " - " + live_data.getRun_time()+ "," + live_data.getTps_status());
				}
				else
				{
					Log.i(TAG, "Garbage Data!");
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
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		//Now setup radio to communicate with WBLE using address found from scanning and passed as parameter to this class
		GattClientCallback mGattCallback = new GattClientCallback();
		BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(device_address);
		mBluetoothGatt = bluetoothDevice.connectGatt(this, false, mGattCallback,2);
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

	public void hideStartingFeatures()
	{
		//navigation.invalidate();
		new Handler(getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				navigation.findViewById(R.id.navigation_start_instructions).setVisibility(View.GONE);
				navigation.findViewById(R.id.navigation_stats).setVisibility(View.GONE);
				navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.VISIBLE);
				navigation.findViewById(R.id.navigation_kill).setVisibility(View.VISIBLE);
				hide_running_features = false;
				hide_starting_features = true;
				//navigation.findViewById(R.id.navigation_tool).setVisibility(View.VISIBLE);
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
		if (live_data.getTemperature() >= 50)
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

	public void listenForBUMP()
	{
		mShaker = new ShakeListener(getApplicationContext());
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
			public void onShake()
			{
				if (live_data.getTps_status() == 2 && bumpStringImg.equals("string") && detected_accelerometer_bump) {
					start_bump_notif = false;
					did_we_clear_bump = true;
					Toast.makeText(getApplicationContext(), "BUMPED STRING", Toast.LENGTH_SHORT).show();
					Log.i(TAG, String.valueOf(live_data.getTps_status()));
					stopAccelerometer();
					detected_accelerometer_bump = false;
					startingCreepRPM = 8100;
					dashboardFragment.myBUMP.clearAnimation();
					dashboardFragment.updateSpeedometer(live_data.getRpm());
				}
			}
		});
	}

	public void getLastRunDateTime()
	{
		String date;
		String runtime;
		String RunTimeAndDate;
		if (live_data.getRun_time() >= 5 && !engine_running && !did_we_recieve_last_run_date)
		{
			did_we_recieve_last_run_date = true;
			DateFormat df = new SimpleDateFormat("MMM d, yyyy HH:mm:ss z");
			date = df.format(Calendar.getInstance().getTime());
			runtime = String.valueOf(live_data.getRun_time());
			RunTimeAndDate = runtime + "-" + date;
			Bundle bundle = new Bundle();
			bundle.putString("LAST_RUN_DATE", RunTimeAndDate);
			statsTabsFragment.setArguments(bundle);
		}
	}

	public void startAccelerometer()
	{
		sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void stopAccelerometer()
	{
		sensorManager.unregisterListener(this);
	}

	public void updateStartingScreen()
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//if selected nav item is Start:
				if (live_data.getTemperature() < 50 && !start_high_temp_fragment_loaded)
				{
					startFragment.updatePrimerBulb(live_data.getTemperature());
					if (live_data.getError_code() == 1)
					{
						startFragment.flashSqueezeThrottle();
						writeToIgnitionModule(protocol.BTN_CLEAR_CODE, protocol.RESET_CODE);
					}
				}
			}
		});
	}

	public void updateRunningScreen()
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//if selected nav item is Dash:
				if (!start_rpm_creep)
					dashboardFragment.updateSpeedometer(live_data.getRpm());
				dashboardFragment.updateRunTimer(live_data.getRun_time());
				if (engine_running && live_data.getTps_status() ==2)
				{
					navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.GONE);
					navigation.findViewById(R.id.navigation_tool).setVisibility(View.GONE);

					if (live_data.getRpm() > STRING_MAX_SPEED && live_data.getAttachment_nbr_status() == 0) //string
					{
						start_rpm_creep = true;
						startingCreepRPM += 13;
						dashboardFragment.updateSpeedometer(startingCreepRPM);

						if (startingCreepRPM >= 9000 && !start_bump_notif)
						{
							start_bump_notif = true;
							did_we_clear_bump = false;
							startAccelerometer();
							dashboardFragment.flashBUMP();
							listenForBUMP();
						}
						else if (startingCreepRPM >= 9500)
						{
							dashboardFragment.updateSpeedometer(9500);
						}
					}
					else
					{
						start_rpm_creep = false;
						startingCreepRPM = 8100;
					}
				}
				else
				{
					start_rpm_creep = false;
					navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.VISIBLE);
					navigation.findViewById(R.id.navigation_tool).setVisibility(View.VISIBLE);

					if (start_bump_notif && !bumpStringImg.equals("string"))
					{
						start_bump_notif = false;
						dashboardFragment.myBUMP.clearAnimation();
					}
				}
			}
		});
	}

}