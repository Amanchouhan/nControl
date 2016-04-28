package in.nemi.ncontrol;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Developer on 25-04-2016.
 */
public class UsrMgmt extends Activity {

    ndbHelper db = new ndbHelper(this, null, null, 1);

    EditText username, password;
    Button adduser;
    static List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usrmgmt);


        //Drop down for user type
        final Spinner spinner = (Spinner) findViewById(R.id.dropdown_list);

        ArrayAdapter<String> adapter;

        list = new ArrayList<>();
        list.add("User");
        list.add("Admin");
        adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        populateListView();

        username = (EditText) findViewById(R.id.ed_create_user);
        password = (EditText) findViewById(R.id.ed_create_password);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (username.hasFocus()) {
                    username.setHint("");
                }
                if (!username.hasFocus()) {
                    username.setHint("User name");
                }

            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (password.hasFocus()) {
                    password.setHint("");
                }
                if (!password.hasFocus()) {
                    password.setHint("Password");
                }
            }
        });

        adduser = (Button) findViewById(R.id.button2);

        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String role = spinner.getSelectedItem().toString();


                if (user.equals("")) {
                    username.setError("User Name");
                } else if (pass.equals("")) {
                    password.setError("Password");

                } else if (user.equals("") && pass.equals("")) {
                    username.setError("User Name");
                    password.setError("Password");
                } else {
                    addUser(role, user, pass);
                    username.setText("");
                    password.setText("");
                    populateListView();
                }
            }
        });
    }

    //Populating data into list view
    public void populateListView() {
        Cursor cursor = db.getAllRows();
        String[] fields = new String[]{ndbHelper.COLUMN_ROLE, ndbHelper.COLUMN_USERNAME, ndbHelper.COLUMN_ID};
        int[] toViews = new int[]{R.id.role_list, R.id.username_list};
        SimpleCursorAdapter cursorAdapter;
        cursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.userlistview, cursor, fields, toViews, 0);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(cursorAdapter);
    }

    public void addUser(String role, String user, String pass) {
        ContentValues cv = new ContentValues();
        cv.put(ndbHelper.COLUMN_ID, user);
        cv.put(ndbHelper.COLUMN_USERNAME, user);
        cv.put(ndbHelper.COLUMN_ROLE, role);
        cv.put(ndbHelper.COLUMN_PASSWORD, pass);
        SQLiteDatabase dbs = db.getWritableDatabase();
        dbs.insert(ndbHelper.TABLE_USERS, null, cv);
        dbs.close();
    }

}

