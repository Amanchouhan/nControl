package in.nemi.ncontrol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by shouryas on 4/21/2016.
 */
public class Main extends Activity {

    ndbHelper databaseHelper;
    EditText username_super, password_super,username,password;
    Button add,login;
    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        username = (EditText)findViewById(R.id.ed_username_1);
        password = (EditText)findViewById(R.id.ed_password_1);
        login = (Button) findViewById(R.id.button_login);
        databaseHelper = new ndbHelper(this, null, null, 1);

        //check for superuser
        Boolean a = databaseHelper.checkS();
        if (!a) {
            final Dialog d = new Dialog(Main.this);
            d.setContentView(R.layout.createsuper);
            d.setTitle("Super doesn't exist!");
            d.setCancelable(false);
            d.show();
            username_super = (EditText) d.findViewById(R.id.editText);
            password_super = (EditText) d.findViewById(R.id.editText2);
            add = (Button) d.findViewById(R.id.addsuper);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String u = username_super.getText().toString();
                    String p = password_super.getText().toString();
                    String r = "super";
                    if(u.equals("")) {
                        Toast.makeText(getApplicationContext(),"Enter the user name", Toast.LENGTH_LONG).show();
                    } else if (p.equals("")) {
                        Toast.makeText(getApplicationContext(),"Enter the password", Toast.LENGTH_LONG).show();
                    } else {
                        databaseHelper.addUser(r, u, p);
                        d.dismiss();
                    }
                }
            });
        }
    }

    //User Login
    public void login(View v) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if(user.equals("")) {
            Toast.makeText(getApplicationContext(),"Enter the username", Toast.LENGTH_LONG).show();
        } else if (pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the password", Toast.LENGTH_LONG).show();
        } else if (pass.equals(databaseHelper.loginUser(user))) {
            Intent i = new Intent(Main.this, UsrMgmt.class);
            i.putExtra("Username", user);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
        }
    }
}