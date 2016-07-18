package nemi.in;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import in.nemi.ncontrol.R;

/**
 * Created by Aman on 7/15/2016.
 */
public class FragmentSettings extends Fragment implements View.OnClickListener {
    PrinterBluetoothHandler printerBluetoothHandler;
    EditText etAddress;
    Button buttonAdd;
    TextView current_printer_add_tv;
    DatabaseHelper databaseHelper;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String BLUETOOTH_KEY = "Bluetooth_address";
    private IntentFilter intentFilter = null;
    SharedPreferences sharedpreferences;
    String address;
    String value;
    public FragmentSettings() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_view, container, false);
        databaseHelper = new DatabaseHelper(getActivity(), null, null, 1);
        current_printer_add_tv = (TextView) view.findViewById(R.id.current_printer_add_id);

        // this is fetching the address from shared preference
        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        // Reading from SharedPreferences
        value = settings.getString(FragmentSettings.BLUETOOTH_KEY, null);

        current_printer_add_tv.setText("Current Printer : " + value);
        Toast.makeText(getActivity(),"This is current printer : " + value,Toast.LENGTH_SHORT).show();
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        buttonAdd = (Button) view.findViewById(R.id.btnAdd);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buttonAdd.setOnClickListener(this);
        etAddress.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:

                address = etAddress.getText().toString();
                if(address.trim().length()==17){
                    databaseHelper.addHAddress(address);
                    etAddress.setText("");
                }   else {
                    etAddress.setError("Warning : This is not correct.(Ex:- 00:02:0A:03:1D:F5)");
                }

                //lets check whether the mac address length is equals to 17 or not
                if (address.trim().length() == 17) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(BLUETOOTH_KEY, address);
                    editor.commit();
                } else {
                    //tell the user to check the mac address
                    Toast.makeText(getActivity(), "Your MAC address is not correct!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.etAddress:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please select a Category");
                builder.setIcon(R.drawable.question_mark);
                ListView dialogCatList = new ListView(getActivity());
                HardwareAddressAdapter hardwareAddressAdapter = new HardwareAddressAdapter(getActivity()
                        , databaseHelper.getHAddress());
                dialogCatList.setAdapter(hardwareAddressAdapter);
                builder.setView(dialogCatList);
                final Dialog dialog_set_qty = builder.create();

                dialogCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv_address = (TextView) view.findViewById(R.id.tv_old_haddress_id);
                        String address = tv_address.getText().toString();
                        etAddress.setText(address);

                        etAddress.setEnabled(false);
                        dialog_set_qty.cancel();
                    }

                });
                dialog_set_qty.show();
                break;
        }
    }

    public class HardwareAddressAdapter extends CursorAdapter {
        public HardwareAddressAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.hardware_number_adap, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv_column = (TextView) view.findViewById(R.id.tv_haddress_old_column_id);
            TextView tv_category = (TextView) view.findViewById(R.id.tv_old_haddress_id);
            tv_column.setText(cursor.getString(0));
            tv_category.setText(cursor.getString(1));
        }
    }
}
