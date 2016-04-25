package in.nemi.ncontrol;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by shouryas on 4/21/2016.
 */
public class Main extends Activity {

    ndbHelper databaseHelper;
    EditText user_name, password;
    Button login;
    TextView create_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        databaseHelper = new ndbHelper(this, null, null, 1);

        user_name = (EditText)findViewById(R.id.ed_username_1);
        password = (EditText)findViewById(R.id.ed_password_1);
        create_user = (TextView)findViewById(R.id.tv_create_user) ;
        create_user.setClickable(true);

        login = (Button)findViewById(R.id.button_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SecondActivity.class);
                startActivity(i);
                databaseHelper.addUser();
            }
        });


    }
}