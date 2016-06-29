/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nemi.in;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import in.nemi.ncontrol.R;

/**
 * This Activity appears as a dialog. It lists any paired devices and devices
 * detected in the area after discovery. When a device is chosen by the user,
 * the MAC address of the device is sent back to the parent Activity in the
 * result Intent.
 */
public class DeviceListActivity extends Activity {
	// Debugging
	private static final String TAG = "DeviceListActivity";
	private static final boolean D = true;

	// Return Intent extra
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	public static String Device = "";
	// Member fields
	private BluetoothAdapter mBtAdapter;
	private List<HashMap<String, String>> mPairedDevicesArrayAdapter;
	private List<HashMap<String, String>> mNewDevicesArrayAdapter;
	ListView newDevicesListView;
	ListView pairedListView;
	final int REQUEST_ENABLE_BT = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);

		// Set result CANCELED incase the user backs out
		setResult(Activity.RESULT_CANCELED);

		// Initialize the button to perform device discovery
		Button scanButton = (Button) findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);
			}
		});

		// Initialize array adapters. One for already paired devices and
		// one for newly discovered devices
		// mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
		// R.layout.device_name);
		// mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
		// R.layout.device_name);

		// Find and set up the ListView for paired devices
		pairedListView = (ListView) findViewById(R.id.paired_devices);

		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Find and set up the ListView for newly discovered devices
		newDevicesListView = (ListView) findViewById(R.id.new_devices);

		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		mNewDevicesArrayAdapter = new ArrayList<HashMap<String, String>>();
		mPairedDevicesArrayAdapter = new ArrayList<HashMap<String, String>>();

		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		// Get the local Bluetooth adapter
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBtAdapter == null) {
			// ShowMessage("Bluetooth is not available");
			return;
		}

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBtAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			StartShowingDevices();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_ENABLE_BT:
			StartShowingDevices();
			break;
		default:
			break;
		}
	}

	private void StartShowingDevices() {
		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			HashMap<String, String> map = null;
			for (BluetoothDevice device : pairedDevices) {

				map = new HashMap<String, String>();
				map.put(BluetoothListDataBinder.KEY_NAME, device.getName());
				map.put(BluetoothListDataBinder.KEY_ADDRESS,
						device.getAddress());

				mPairedDevicesArrayAdapter.add(map);
			}
		} else {
			String noDevices = getResources().getText(R.string.none_paired).toString();
			HashMap<String, String> map = null;
			map = new HashMap<String, String>();
			map.put(BluetoothListDataBinder.KEY_NAME, noDevices);
			map.put(BluetoothListDataBinder.KEY_ADDRESS, noDevices);
			mPairedDevicesArrayAdapter.add(map);
		}

		pairedListView.setAdapter(new BluetoothListDataBinder(this,mPairedDevicesArrayAdapter));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Make sure we're not doing discovery anymore
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}

		// Unregister broadcast listeners
		this.unregisterReceiver(mReceiver);

	}

	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private void doDiscovery() {
		if (D)
			Log.d(TAG, "doDiscovery()");

		// Indicate scanning in the title
		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);

		// Turn on sub-title for new devices
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		// If we're already discovering, stop it
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		// Request discover from BluetoothAdapter
		mBtAdapter.startDiscovery();
	}

	// The on-click listener for all devices in the ListViews
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int position,long arg3) {
			try {
				if (mBtAdapter.isDiscovering()) {
					// Cancel discovery because it's costly and we're about to
					// connect
					mBtAdapter.cancelDiscovery();
				}
			} catch (Exception e) {
			}

			String info = "";
			if (newDevicesListView == (ListView) v.getParent()) {
				info = mNewDevicesArrayAdapter.get(position).get(BluetoothListDataBinder.KEY_ADDRESS);

			} else {
				info = mPairedDevicesArrayAdapter.get(position).get(BluetoothListDataBinder.KEY_ADDRESS);

			}

			Device = info;
			Intent i = new Intent(getApplicationContext(),AccordionWidgetDemoActivity.class);
			i.putExtra("Device", "00:02:0A:02:E9:9E");
			setResult(RESULT_OK, i);
			finish();
		}
	};

	// The BroadcastReceiver that listens for discovered devices and
	// changes the title when discovery is finished
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

					HashMap<String, String> map = new HashMap<String, String>();
					map.put(BluetoothListDataBinder.KEY_NAME, device.getName());
					map.put(BluetoothListDataBinder.KEY_ADDRESS,device.getAddress());
					BluetoothClass _BluetoothClass = device.getBluetoothClass();
					if (_BluetoothClass != null) {
						map.put(BluetoothListDataBinder.KEY_CLASS, String.valueOf(_BluetoothClass.getMajorDeviceClass()));
					}
					if (!mNewDevicesArrayAdapter.contains(map)) {
						mNewDevicesArrayAdapter.add(map);
						newDevicesListView.setAdapter(new BluetoothListDataBinder(DeviceListActivity.this,
								mNewDevicesArrayAdapter));
					}
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_device);
				if (mNewDevicesArrayAdapter.size() == 0) {
					String noDevices = getResources().getText(
							R.string.none_found).toString();
					HashMap<String, String> map = null;
					map = new HashMap<String, String>();
					map.put(BluetoothListDataBinder.KEY_NAME, noDevices);
					map.put(BluetoothListDataBinder.KEY_ADDRESS, noDevices);

					mNewDevicesArrayAdapter.add(map);
					newDevicesListView.setAdapter(new BluetoothListDataBinder(
							DeviceListActivity.this, mNewDevicesArrayAdapter));
				}
			}
		}
	};

}
