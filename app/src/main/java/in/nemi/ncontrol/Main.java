package in.nemi.ncontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by shouryas on 4/21/2016.
 */
public class Main extends Activity {

    ndbHelper databaseHelper;
    EditText username, password;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        databaseHelper = new ndbHelper(this, null, null, 1);

        //check for superuser
        Boolean a = databaseHelper.checkS();
        if (!a) {
            final Dialog d = new Dialog(Main.this);
            d.setContentView(R.layout.createsuper);
            d.setTitle("Super doesn't exist!");
            d.setCancelable(false);
            d.show();
            username = (EditText) d.findViewById(R.id.editText);
            password = (EditText) d.findViewById(R.id.editText2);
            add = (Button) d.findViewById(R.id.addsuper);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String u = username.getText().toString();
                    String p = password.getText().toString();
                    String r = "super";
                    databaseHelper.addUser(r,u,p);
                    d.dismiss();
                }
            });
        }
    }


    public void add(View view) {

    }
}