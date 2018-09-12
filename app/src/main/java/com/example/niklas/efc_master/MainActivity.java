package com.example.niklas.efc_master;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Button;
import android.widget.Toast;

import static com.example.niklas.efc_master.NordicProfile.CHARACTERISTIC_TX;
import static com.example.niklas.efc_master.NordicProfile.DESCRIPTOR_CONFIG;
import static com.example.niklas.efc_master.NordicProfile.SERVICE_UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String device_address;
    public static final String EXTRA_DEVICE_ADDRESS = "mAddress";
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    //Variables used to interpret data coming in and out
    Igndata live_data = new Igndata();
    private boolean engine_running = false;
    //
	private Menu menu;
	private BottomNavigationView navigation;
	private Fragment fragment;
    private StartFragment startFragment = new StartFragment();
    private DashboardTabFragment dbTabFragment = new DashboardTabFragment();
	private DashboardSpeedometerFragment dbSpeedometerFragment = new DashboardSpeedometerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MainActivity onCreate: " + device_address);
        //Try to connect to Gatt Server
        device_address = getIntent().getStringExtra(EXTRA_DEVICE_ADDRESS);
        connectToGattServer();

        //Setup interface views
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.app_name); // set the top title
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_walbro_w);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        navigation = findViewById(R.id.navigation_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(startFragment);
	    //btnPrimeBulb = (Button) StartFragment.getView().findViewById(R.id.instruction_prime_bulb);

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
                	fragment = new StartFragment();
                	loadFragment(fragment);
                    return true;

                case R.id.navigation_stats:
                    return true;

                case R.id.navigation_light_trim:
                    return true;

                case R.id.navigation_dash:
	                fragment = new DashboardTabFragment();
	                loadFragment(fragment);
                    return true;

                case R.id.navigation_kill:
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attachments, menu);
	    this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.action_menu1:
                Toast.makeText(getApplicationContext(), "BLADE ATTACHMENT", Toast.LENGTH_LONG).show();
				menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_blade_white));
                return true;

	        case R.id.action_menu2:
                Toast.makeText(getApplicationContext(), "BLOWER ATTACHMENT", Toast.LENGTH_LONG).show();
		        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_blower_white));
                return true;

            case R.id.action_menu3:
                Toast.makeText(getApplicationContext(), "EDGER ATTACHMENT", Toast.LENGTH_LONG).show();
	            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_edger_white));
                return true;

            case R.id.action_menu4:
                Toast.makeText(getApplicationContext(), "POLE SAW ATTACHMENT", Toast.LENGTH_LONG).show();
	            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_pole_saw_white));
                return true;

            case R.id.action_menu5:
                Toast.makeText(getApplicationContext(), "TILLER ATTACHMENT", Toast.LENGTH_LONG).show();
	            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_tiller_white));
                return true;

            case R.id.action_menu6:
                Toast.makeText(getApplicationContext(), "STRING ATTACHMENT", Toast.LENGTH_LONG).show();
	            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_string_white));
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
                if (data[0] == live_data.ENGINE_NOT_RUNNING && data.length == data[1])
                {

                    engine_running = false;
                    live_data.setTemperature(data[2]);
                    live_data.setAttachment_nbr_status(data[3]);
	                runOnUiThread(new Runnable() {
		                @Override
		                public void run() {
			                startFragment.updatePrimerBulb(live_data.getTemperature());
		                }
	                });

                    Log.i(TAG, "ENGINE_NOT_RUNNING!: " + live_data.getTemperature() + "," + live_data.getAttachment_nbr_status());
                }
                else if (data[0] == live_data.ENGINE_RUNNING && data.length == data[1])
                {
                    engine_running = true;
                    live_data.setRpm((data[3]<< 8)&0x0000ff00|(data[2]&0x000000ff));
                    live_data.setRun_time((data[5]<< 8)&0x0000ff00|(data[4]&0x000000ff));
                    live_data.setTemperature(data[6]);
                    live_data.setAttachment_nbr_status(data[7]);
                    live_data.setTrim_mode_status(data[8]);
                    live_data.setStop_status(data[9]);
                    loadFragment(dbSpeedometerFragment);


                    runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
		                    dbSpeedometerFragment.updateSpeedometer(live_data.getRpm());
		                    //navigation.setSelectedItemId(R.id.navigation_dash);
	                    }
                    });
                    Log.i(TAG, "ENGINE_RUNNING!: " + live_data.getRpm());
                }
                else
                {
                    Log.i(TAG, "Garbage Data!");
                }
            }
        }
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
}
