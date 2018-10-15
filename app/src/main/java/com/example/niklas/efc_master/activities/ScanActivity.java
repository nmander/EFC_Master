package com.example.niklas.efc_master.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.niklas.efc_master.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

import static com.example.niklas.efc_master.profiles.NordicProfile.SERVICE_UUID;

public class ScanActivity extends AppCompatActivity
{
    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final long SCAN_TIMEOUT_MS = 15_000;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSION_LOCATION = 1;
    public static final String EXTRA_DEVICE_ADDRESS = "mAddress";
    private boolean mScanning;
    private ProgressBar scanning_wheel;
    private Button btnScan;
    private Button btnConnect;
    //private int temp = -999, rssi, index;

    private final BluetoothLeScannerCompat mScanner = BluetoothLeScannerCompat.getScanner();
    private final Handler mStopScanHandler = new Handler();

    ArrayList<String> WBLE_Names = new ArrayList<String>();
    ArrayList<BluetoothDevice> WBLE_Addresses = new ArrayList<BluetoothDevice>();
    ListView myDevices;
    int selectedDevice;
    AlertDialog dialog;
    ArrayAdapter<String> adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Enable Bluetooth", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission accepted");
        } else {
            Toast.makeText(getApplicationContext(), "Enable Location", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WBLE_Names.clear();
        WBLE_Addresses.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanning_wheel = findViewById(R.id.scanning_wheel);
        //scanning_wheel.setVisibility(View.INVISIBLE);

        btnScan = findViewById(R.id.btn_scan);
        btnScan.setVisibility(View.INVISIBLE);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnScan.setVisibility(View.INVISIBLE);
                Log.w(TAG, "setOnClickListener");
                prepareForScan();

                Toast.makeText(getApplicationContext(), "Scanning for WBLE Modules...", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(), "Scanning for WBLE Modules...", Toast.LENGTH_SHORT).show();
    }

    public void show_found_devices()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View row = getLayoutInflater().inflate(R.layout.dialog_wble_devices, null);
        myDevices = (ListView)row.findViewById(R.id.list_wble_devices);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, WBLE_Names);
        myDevices.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        alertDialog.setView(row);
        dialog = alertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded);
        dialog.show();
        btnConnect = (Button)row.findViewById(R.id.btn_connect) ;
        btnConnect.setVisibility(View.INVISIBLE);

        myDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            for (int i = 0; i < parent.getCount() ; i ++)
	            {
		            View v = parent.getChildAt(i);
		            CheckedTextView checkedTextView = ((CheckedTextView)v);
		            checkedTextView.setChecked(false);

	            }
                CheckedTextView checkedTextView = ((CheckedTextView)view);
	            checkedTextView.setChecked(true);



                //change radio button state
                //CheckedTextView checkedTextView = ((CheckedTextView)view);
                //checkedTextView.setChecked(!checkedTextView.isChecked());

                if (checkedTextView.isChecked())
                {
                    selectedDevice = position;
                    btnConnect.setVisibility(View.VISIBLE);
                }
                else
                    btnConnect.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void Connect(View v)
    {
        Log.i(TAG, "YOU CLICKED CONNECT");
        Toast.makeText(getApplicationContext(), "Connected: " + WBLE_Names.get(selectedDevice), Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        startMainActivity(WBLE_Addresses.get(selectedDevice));
//        WBLE_Names.clear();
//        WBLE_Addresses.clear();
    }

    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            // We scan with report delay > 0. This will never be called.
            Log.i(TAG, "onScanResult: " + result.getDevice().getAddress());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.i(TAG, "onBatchScanResults: " + results.toString());

            if (!results.isEmpty()) {
                for (int i = 0; i < results.size(); i++) {
                    ScanResult result = results.get(i);
                    Log.i(TAG, "found BLE: " + result.getDevice().getName());
                    if (result.getDevice().getName().contains("WEFC")) {
                        WBLE_Names.add(result.getDevice().getName());
                        WBLE_Addresses.add(result.getDevice());
                    }
                }
                stopLeScan();
                if (WBLE_Names.size() > 1)
                    show_found_devices();
                else if (WBLE_Names.size() == 1) {
                    ScanResult result = results.get(0);
                    Log.i(TAG, "found BLE: " + results.toString());
                    btnScan.setVisibility(View.INVISIBLE);
                    try {
                        if (result.getDevice().getName().contains("WEFC")) {
                            Toast.makeText(getApplicationContext(), "Connected: " + result.getDevice().getName(), Toast.LENGTH_SHORT).show();
                            startMainActivity(result.getDevice());
                        }
                    } catch (Exception e) {
                        Log.i(TAG, "Can't find device: " + result.getDevice().getName());
                        prepareForScan();
                    }
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.w(TAG, "Scan failed: " + errorCode);
            stopLeScan();
        }
    };

    private final Runnable mStopScanRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "No compatible WBLE modules found", Toast.LENGTH_SHORT).show();
            btnScan.setVisibility(View.VISIBLE);
            stopLeScan();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume");
        btnScan.setVisibility(View.INVISIBLE);
        prepareForScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLeScan();
    }

    private void stopLeScan() {
        if (mScanning) {
            Log.w(TAG, "Stopping Scan");
            mScanning = false;
            scanning_wheel.setVisibility(View.INVISIBLE);
            btnScan.setVisibility(View.VISIBLE);
            mScanner.stopScan(mScanCallback);
            mStopScanHandler.removeCallbacks(mStopScanRunnable);
            invalidateOptionsMenu();
        }
    }

    private void startLeScan() {
        WBLE_Names.clear();
        WBLE_Addresses.clear();
        Log.w(TAG, "Starting Scan");
        mScanning = true;
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(1000)
                .build();
        List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(SERVICE_UUID)).build());

        mScanner.startScan(filters, settings, mScanCallback);

        // Stops scanning after a pre-defined scan period.
        mStopScanHandler.postDelayed(mStopScanRunnable, SCAN_TIMEOUT_MS);
        invalidateOptionsMenu();
    }

    private boolean isBleSupported() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    private void prepareForScan() {
        if (isBleSupported())
        {
            scanning_wheel.setVisibility(View.VISIBLE);
            // Ensures Bluetooth is enabled on the device
            BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter btAdapter = btManager.getAdapter();
            if (btAdapter.isEnabled()) {
                // Prompt for runtime permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startLeScan();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
                }
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        } else {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void startMainActivity(BluetoothDevice device)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_DEVICE_ADDRESS, device.getAddress());

        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Simply Do noting!
    }
}
