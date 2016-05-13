package nemi.in;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.nemi.ncontrol.R;


/**
 * Created by shouryas on 4/21/2016.
 */
public class Main extends Activity {

    ndbHelper databaseHelper;
    EditText username_super, password_super, confirm_password_super,username, password;
    Button add,login;

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
            //Dialog with custom layout to add super
            final Dialog d = new Dialog(Main.this);
            d.setContentView(R.layout.createsuper);
            d.setTitle("Super doesn't exist!");
            d.setCancelable(false);
            d.show();
            username_super = (EditText) d.findViewById(R.id.editText);
            password_super = (EditText) d.findViewById(R.id.editText2);
            confirm_password_super = (EditText) d.findViewById(R.id.editText3);


            add = (Button) d.findViewById(R.id.addsuper);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String u = username_super.getText().toString();
                    String p = password_super.getText().toString();
                    String conf_pass = confirm_password_super.getText().toString();

                    String r = "super";
                    if(u.equals("")) {
                        username_super.setError("User name");
                    } else if (p.equals("")) {
                        password_super.setError("Password");

                    } else if(p.compareTo(conf_pass)!=0) {
                        confirm_password_super.setError("Password is not correct");
                    }else{
                        databaseHelper.addUser(r, u, p);
                        d.dismiss();
                    }
                }
            });
        }
    }

    //User Login
    public void login(View v) {
//        String user = username.getText().toString();
//        String pass = password.getText().toString();
//        if(user.equals("")) {
//            username.setError("Username");
//        } else if (pass.equals("")) {
//            password.setError("password");
//        } else if (pass.equals(databaseHelper.loginUser(user))) {
            Intent i = new Intent(Main.this, MainActivity.class);
            startActivity(i);
//        } else {
//            Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
//        }
    }
}