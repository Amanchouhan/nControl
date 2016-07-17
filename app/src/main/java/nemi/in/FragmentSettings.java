package nemi.in;

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
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.nemi.ncontrol.R;

/**
 * Created by Aman on 7/15/2016.
 */
public class FragmentSettings extends Fragment implements View.OnClickListener{
    PrinterBluetoothHandler printerBluetoothHandler;
    EditText etAddress;
    Button buttonAdd;
    DatabaseHelper databaseHelper;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String BLUETOOTH_KEY = "Bluetooth_address";
    private IntentFilter intentFilter = null;
    SharedPreferences sharedpreferences;
    public FragmentSettings() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_view, container, false);
        databaseHelper = new DatabaseHelper(getActivity(), null, null, 1);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        buttonAdd = (Button) view.findViewById(R.id.btnAdd);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        buttonAdd.setOnClickListener(this);
        etAddress.setOnClickListener(this);

        /*b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = hnumber.getText().toString();
                databaseHelper.addHAddress(n);
                hnumber.setText("");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(address, n);

                editor.commit();
                Toast.makeText(getActivity(), "Connect to the printer....", Toast.LENGTH_LONG).show();
            }
        });
        hnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please select a Category");
                ListView dialogCatList = new ListView(getActivity());
                HardwareAddressAdapter hardwareAddressAdapter = new HardwareAddressAdapter(getActivity()
                        ,databaseHelper.getHAddress());
                dialogCatList.setAdapter(hardwareAddressAdapter);
                builder.setView(dialogCatList);
                final Dialog dialog_set_qty = builder.create();

                dialogCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                        TextView tv_haddress = (TextView) view.findViewById(R.id.tv_old_haddress_id);
                        String haddress = tv_haddress.getText().toString();
                        hnumber.setText(haddress);
                        hnumber.setEnabled(false);
                        dialog_set_qty.cancel();
                    }

                });
                dialog_set_qty.show();
            }
        });*/
        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.btnAdd:
                String address = etAddress.getText().toString();
                //lets check whether the mac address length is equals to 17 or not
                if(address.trim().length() == 17){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(BLUETOOTH_KEY, address);
                    editor.commit();
                }
                else{
                    //tell the user to check the mac address
                    Toast.makeText(getActivity(),"Your MAC address is not correct!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.etAddress:
               //not used for now
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
