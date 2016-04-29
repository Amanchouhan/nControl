package in.nemi.ncontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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

/**
 * Created by Developer on 25-04-2016.
 */
public class UsrMgmt extends Activity {

    ndbHelper databaseHelper;
    EditText role, username, password;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usrmgmt);

        databaseHelper = new ndbHelper(this, null, null, 1);

        final ListView usersview = (ListView) findViewById(R.id.userlistview);
        final UsersAdapter usersAdapter = new UsersAdapter(this, databaseHelper.getUsers());
        usersview.setAdapter(usersAdapter);
//        for heading for the listview
//        usersview.addHeaderView(usersview);

        role = (EditText) findViewById(R.id.rolefield);
        username = (EditText) findViewById(R.id.usernamefield);
        password = (EditText) findViewById(R.id.passwordfield);
        add = (Button) findViewById(R.id.addbutton);

        //Add User to db
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = username.getText().toString();
                String p = password.getText().toString();
                String r = role.getText().toString();
                if (u.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter the user name", Toast.LENGTH_LONG).show();
                } else if (p.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter the password", Toast.LENGTH_LONG).show();
                } else if (r.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter the role", Toast.LENGTH_LONG).show();
                } else {
                    databaseHelper.addUser(r, u, p);
                    CursorAdapter adapter = (CursorAdapter) usersview.getAdapter();
                    Cursor cursor = databaseHelper.getUsers();
                    usersAdapter.changeCursor(cursor);

                    username.setText("");
                    password.setText("");
                    role.setText("");
                }
            }
        });

        usersview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog d = new Dialog(UsrMgmt.this);
                final TextView a, b, c;
                a = (TextView) view.findViewById(R.id.column_id);
                b = (TextView) view.findViewById(R.id.column_role);
                c = (TextView) view.findViewById(R.id.column_username);
                final String val1 = a.getText().toString();
                final String val2 = b.getText().toString();
                final String val3 = c.getText().toString();
                d.setContentView(R.layout.useraction);
                d.setTitle("Please select an action!");
                d.setCancelable(true);
                Button delete = (Button) d.findViewById(R.id.deleteuser);
                Button update = (Button) d.findViewById(R.id.updateuser);


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseHelper.deleteUser(val1);
                        Cursor cursor = databaseHelper.getUsers();
                        usersAdapter.changeCursor(cursor);
                        d.dismiss();
                    }
                });
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        role = (EditText) findViewById(R.id.rolefield);
                        username = (EditText) findViewById(R.id.usernamefield);
                        password = (EditText) findViewById(R.id.passwordfield);
                        role.setText(val2);
                        username.setText(val3);
                        password.setText("");
                        databaseHelper.deleteUser(val1);
                        Cursor cursor = databaseHelper.getUsers();
                        usersAdapter.changeCursor(cursor);
                        d.dismiss();
                    }
                });
                d.show();
            }
        });
    }

    public class UsersAdapter extends CursorAdapter {

        public UsersAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView a1 = (TextView) view.findViewById(R.id.column_id);
            a1.setText(cursor.getString(0));
            TextView a2 = (TextView) view.findViewById(R.id.column_role);
            a2.setText(cursor.getString(1));
            TextView a3 = (TextView) view.findViewById(R.id.column_username);
            a3.setText(cursor.getString(2));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.userslistviewitem, parent, false);
            return view;
        }
    }
}