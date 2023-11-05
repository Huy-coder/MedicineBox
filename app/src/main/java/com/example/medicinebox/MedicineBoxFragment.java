package com.example.medicinebox;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicineBoxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineBoxFragment extends Fragment {
    private static final String TAG = MedicineBoxFragment.class.getSimpleName();
    private static final int REQUEST_BLUETOOTH_SCAN_PERMISSION = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        BluetoothLeScanner bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        ScanCallback scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                // Handle scan results here.
                BluetoothDevice device = result.getDevice();
                Log.d(TAG, "onScanResult: " + device);
                // You can get device information and connect to it here.
            }
        };
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "onCreate: Don't have permission to scan");
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_SCAN}, REQUEST_BLUETOOTH_SCAN_PERMISSION);
        }
        bluetoothLeScanner.startScan(scanCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_SCAN_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. You can now proceed with Bluetooth scanning.
            } else {
                // Permission was denied. Handle this case appropriately, such as showing a message to the user or explaining why the permission is needed.
                Log.e(TAG, "Permission denied for Bluetooth scanning");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medicine_box, container, false);
    }
}