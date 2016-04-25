package in.nemi.ncontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
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

        //check for superuser
        Boolean a = databaseHelper.checkS();
        if (!a) {
            Dialog d = new Dialog(Main.this);
            d.setContentView(R.layout.createsuper);
            d.setTitle("Super doesn't exist!");
            d.show();
        }
    }
}