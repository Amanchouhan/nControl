package in.nemi.ncontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;



/**
 * Created by Aman on 5/3/2016.
 */
public class UserFragment extends android.app.Fragment {
    ndbHelper databaseHelper;
    EditText role, username, password;
    Button add;
    UsersAdapter usersAdapter;

    public UserFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.usrmgmt, container, false);

        databaseHelper = new ndbHelper(getActivity(), null, null, 1);


        usersAdapter = new UsersAdapter(getActivity(), databaseHelper.getUsers());
        final ListView usersview = (ListView) rootView.findViewById(R.id.userlistview);
        usersview.setAdapter(usersAdapter);

        role = (EditText) rootView.findViewById(R.id.rolefield);
        username = (EditText) rootView.findViewById(R.id.usernamefield);
        password = (EditText) rootView.findViewById(R.id.passwordfield);
        add = (Button) rootView.findViewById(R.id.addbutton);
        //Add User to db
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = username.getText().toString();
                String p = password.getText().toString();
                String r = role.getText().toString();
                if (u.equals("")) {
                    username.setError("UserName");
                } else if (p.equals("")) {
                    password.setError("Password");
                } else if (r.equals("")) {
                    role.setError("Role");
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final TextView a, b, c;
                a = (TextView) view.findViewById(R.id.column_id);
                b = (TextView) view.findViewById(R.id.column_role);
                c = (TextView) view.findViewById(R.id.column_username);
                final String val1 = a.getText().toString();
                final String val2 = b.getText().toString();
                final String val3 = c.getText().toString();


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Please select an action!");
                alertDialogBuilder.setMessage("Click yes to exit!").setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                databaseHelper.deleteUser(val1);
                                Cursor cursor = databaseHelper.getUsers();
                                usersAdapter.changeCursor(cursor);

                            }
                        })
                        .setNegativeButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                role.setText(val2);
                                username.setText(val3);
                                password.setText("");
                                databaseHelper.deleteUser(val1);
                                Cursor cursor = databaseHelper.getUsers();
                                usersAdapter.changeCursor(cursor);

                            }
                        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
        return rootView;
    }

    public class UsersAdapter extends CursorAdapter {

        public UsersAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView a1 = (TextView) view.findViewById(R.id.column_id);
            TextView a2 = (TextView) view.findViewById(R.id.column_role);
            TextView a3 = (TextView) view.findViewById(R.id.column_username);
            a1.setText(cursor.getString(0));
            a2.setText(cursor.getString(1));
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
