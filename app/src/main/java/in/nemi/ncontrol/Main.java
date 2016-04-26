package in.nemi.ncontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by shouryas on 4/21/2016.
 */
public class Main extends Activity {

    ndbHelper databaseHelper;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        databaseHelper = new ndbHelper(this, null, null, 1);

        //check for superuser
        Boolean a = databaseHelper.checkS();
        if (!a) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Super doesn't exist!");
            builder.setMessage("Please set a username and password for super");
            builder.setCancelable(false);
            builder.setView(getLayoutInflater().inflate(R.layout.createsuper, null));

            username = (EditText) findViewById(R.id.editText);
            password = (EditText) findViewById(R.id.editText2);

            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which){
                    String u = username.getText().toString();
                    String p = password.getText().toString();
                    String r = "super";
                    databaseHelper.addUser(r,u,p);
                }
            });

            AlertDialog ad = builder.create();
            ad.show();

//            Dialog d = new Dialog(Main.this);
//            d.setContentView(R.layout.createsuper);
//            d.setTitle("Super doesn't exist!");
//            d.setCancelable(false);
//            d.show();
        }
    }
}