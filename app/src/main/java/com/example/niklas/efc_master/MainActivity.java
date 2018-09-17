package com.example.niklas.efc_master;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.List;

import static com.example.niklas.efc_master.NordicProfile.CHARACTERISTIC_RX;
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
    private boolean dashboard_fragment_loaded = false;
    private boolean start_fragment_loaded = false;
    private boolean start_high_temp_fragment_loaded = false;
    private boolean lite_trim_on = false;
    //
	private Menu menu;
	private BottomNavigationView navigation;
    private StartFragment startFragment = new StartFragment();
    private StartHighTempFragment startHighTempFragment = new StartHighTempFragment();
    private StatsTabsFragment statsTabsFragment = new StatsTabsFragment();
	private DashboardFragment dashboardFragment = new DashboardFragment();

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

      //  loadFragment(startFragment);
        hideRunningFeatures();

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

                    else {
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String countryCode = data.getStringExtra(ToolSelectionActivity.RESULT_TOOL);
            Toast.makeText(this, "You selected code:" + countryCode, Toast.LENGTH_LONG).show();
        }
    }


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
                writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_BLADE);
				return true;

	        case R.id.action_menu2:
                Toast.makeText(getApplicationContext(), "BLOWER ATTACHMENT", Toast.LENGTH_LONG).show();
		        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_blower_white));
                writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_BLOWER);
		        return true;

            case R.id.action_menu3:
                Toast.makeText(getApplicationContext(), "EDGER ATTACHMENT", Toast.LENGTH_LONG).show();
	            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_edger_white));
                writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_EDGER);
	            return true;

            case R.id.action_menu4:
                Toast.makeText(getApplicationContext(), "POLE SAW ATTACHMENT", Toast.LENGTH_LONG).show();
	            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_pole_saw_white));
                writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_POLE_SAW);
	            return true;

            case R.id.action_menu5:
                Toast.makeText(getApplicationContext(), "TILLER ATTACHMENT", Toast.LENGTH_LONG).show();
	            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_tiller_white));
                writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_TILLER);
	            return true;

            case R.id.action_menu6:
                Toast.makeText(getApplicationContext(), "STRING ATTACHMENT", Toast.LENGTH_LONG).show();
	            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_string_white));
                writeToIgnitionModule(protocol.BTN_TOOL_SELECT, protocol.TOOL_STRING);
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
                final byte[] data = characteristic.getValue();
                //if engine not running
                if (data[0] == live_data.ENGINE_NOT_RUNNING && data.length == data[1])
                {
                    engine_running = false;
                    //hide navigational features:
                    hideRunningFeatures();

                    live_data.setTemperature(data[2]);
                    live_data.setAttachment_nbr_status(data[3]);
                    if (!start_fragment_loaded)
                    {
                        //live_data.setTemperature(50);
                        setConditionalStartingFragment();
                        setStartFragment();
                        start_fragment_loaded = true;
                        dashboard_fragment_loaded = false;
                    }
	                runOnUiThread(new Runnable() {
		                @Override
		                public void run() {
		                    //if selected nav item is Start:
                            if (live_data.getTemperature() < 50 && !start_high_temp_fragment_loaded)
                            {
                                startFragment.updatePrimerBulb(live_data.getTemperature());
                            }
			                //else: statstab graph logic:
		                }
	                });

                    Log.i(TAG, "ENGINE_NOT_RUNNING!: " + live_data.getTemperature() + "," + live_data.getAttachment_nbr_status());
                }

                //if engine running
                else if (data[0] == live_data.ENGINE_RUNNING && data.length == data[1])
                {
                    engine_running = true;
                    if (!dashboard_fragment_loaded) {
	                    loadFragment(dashboardFragment);
                        dashboard_fragment_loaded = true;
	                    start_fragment_loaded = false;
	                    start_high_temp_fragment_loaded = false;
                    }
                    //hide navigational features:
	                hideStartingFeatures();

                    live_data.setRpm((data[3]<< 8)&0x0000ff00|(data[2]&0x000000ff));
                    live_data.setRun_time((data[5]<< 8)&0x0000ff00|(data[4]&0x000000ff));
                    live_data.setTemperature(data[6]);
                    live_data.setAttachment_nbr_status(data[7]);
                    live_data.setTrim_mode_status(data[8]);
                    live_data.setStop_status(data[9]);
                    live_data.setAt_idle_status(data[10]);
                    runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                        //if selected nav item is Dash:
		                    dashboardFragment.updateSpeedometer(live_data.getRpm());
		                    dashboardFragment.updateRunTimer(live_data.getRun_time());
                            if (engine_running && live_data.getAt_idle_status()==0)
                            {
                                menu.findItem(R.id.miAttachments).setVisible(false);
                                menu.findItem(R.id.miAttachments).setEnabled(false);
                                navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.GONE);
                                navigation.findViewById(R.id.navigation_tool).setVisibility(View.GONE);

                            }
                            else
                            {
                                menu.findItem(R.id.miAttachments).setVisible(true);
                                menu.findItem(R.id.miAttachments).setEnabled(true);
                                navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.VISIBLE);
                                navigation.findViewById(R.id.navigation_tool).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    Log.i(TAG, "ENGINE_RUNNING!: " + live_data.getRpm() + " - " + live_data.getRun_time());
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
                navigation.findViewById(R.id.navigation_light_trim).setVisibility(View.GONE);
                navigation.findViewById(R.id.navigation_kill).setVisibility(View.GONE);
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

}