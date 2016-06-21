/**
 * 
 */
package nemi.in;

import android.app.Activity;
import android.bluetooth.BluetoothClass.Device.Major;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;

import in.nemi.ncontrol.R;

/**
 * @author sangeeta
 * 
 */
public class BluetoothListDataBinder extends BaseAdapter {

	static final String KEY_NAME = "name";
	static final String KEY_ADDRESS = "address";
	static final String KEY_CLASS = "calss";

	LayoutInflater inflater;
	ImageView thumb_image;
	List<HashMap<String, String>> DeviceCollection;
	ViewHolder holder;

	public BluetoothListDataBinder(Activity act,
			List<HashMap<String, String>> map) {

		this.DeviceCollection = map;

		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return DeviceCollection.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if (convertView == null) {

			vi = inflater.inflate(R.layout.list_row, null);
			holder = new ViewHolder();

			holder.tvInfo = (TextView) vi.findViewById(R.id.tvInfo); // city
																		// name
			holder.tvDescription = (TextView) vi
					.findViewById(R.id.tvDescription); // city weather overview

			holder.tvInfoImage = (ImageView) vi.findViewById(R.id.list_image); // thumb
																				// image

			vi.setTag(holder);
		} else {

			holder = (ViewHolder) vi.getTag();
		}

		// Setting all values in listview

		holder.tvInfo.setText(DeviceCollection.get(position).get(KEY_NAME));
		holder.tvDescription.setText(DeviceCollection.get(position).get(
				KEY_ADDRESS));

		// Setting an image
		if (DeviceCollection.get(position).containsKey(KEY_CLASS)) {
			int classcode = Integer.valueOf(DeviceCollection.get(position).get(
					KEY_CLASS));
			switch (classcode) {
			case Major.PHONE:
				holder.tvInfoImage.setImageResource(R.drawable.mobile_basic_blue);
				break;
			case Major.MISC:
				holder.tvInfoImage.setImageResource(R.drawable.laptop_blue);
				break;
			case Major.COMPUTER:
				holder.tvInfoImage.setImageResource(R.drawable.computer);
				break;
			case Major.IMAGING:
			case Major.AUDIO_VIDEO:
				holder.tvInfoImage.setImageResource(R.drawable.audvid);
				break;
			case Major.NETWORKING:
				holder.tvInfoImage.setImageResource(R.drawable.networking);
				break;
			case Major.WEARABLE:
				holder.tvInfoImage.setImageResource(R.drawable.headphone_blue);
				break;
			case Major.PERIPHERAL:
				holder.tvInfoImage.setImageResource(R.drawable.peripherals);
				break;
			case Major.HEALTH:
				holder.tvInfoImage.setImageResource(R.drawable.plus);
				break;
			default:
				holder.tvInfoImage.setImageResource(R.drawable.bluetoothn);
				break;
			}
		} else {
			holder.tvInfoImage.setImageResource(R.drawable.bluetoothn);
		}

		return vi;
	}

	/*
	 * 
	 * */
	static class ViewHolder {

		TextView tvInfo;

		TextView tvDescription;
		ImageView tvInfoImage;
	}

}
