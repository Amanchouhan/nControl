package nemi.in;

import android.app.Fragment;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.nemi.ncontrol.R;

/**
 * Created by Aman on 7/15/2016.
 */
public class SettingFragments extends Fragment {
    MHandler mHandler;
    EditText ed1;
    Button b1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String address = "Bluetooth_address";
    String data = "88:68:2E:00:31:4A";

    private IntentFilter intentFilter = null;
    SharedPreferences sharedpreferences;
    public SettingFragments() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment,container,false);
        ed1=(EditText)view.findViewById(R.id.editText);
        b1=(Button)view.findViewById(R.id.button);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(address, data);
                editor.commit();
                Toast.makeText(getActivity(),"Connect to the printer....",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
