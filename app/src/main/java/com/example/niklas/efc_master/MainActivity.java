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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String device_address;
    public static final String EXTRA_DEVICE_ADDRESS = "mAddress";
    boolean foundGattService = false;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private boolean mna = false;
 //   private BluetoothGattCallback mGattCallback = new BluetoothGattCallback()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device_address = getIntent().getStringExtra(EXTRA_DEVICE_ADDRESS);
        Log.i(TAG, "MainActivity onCreate: " + device_address);
//        //Get bluetooth radio, need to do it again init this class
//        mBluetoothManager = (BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE);
//        mBluetoothAdapter = mBluetoothManager.getAdapter();
//        //Now setup radio to communicate with WBLE using address found from scanning and passed as parameter to this class
//        BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(device_address);
//        mBluetoothGatt = bluetoothDevice.connectGatt(mContext, false, mGattCallback,2);

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

}
