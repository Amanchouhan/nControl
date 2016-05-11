package nemi.in;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import in.nemi.ncontrol.R;


/**
 * Created by Aman on 5/3/2016.
 */
public class UserFragment extends Fragment {
    ndbHelper databaseHelper;
    EditText role, username, password, re_enter_password;
    Button add;
    UsersAdapter usersAdapter;
    ListView usersview;
    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.usrmgmt, container, false);

        databaseHelper = new ndbHelper(getActivity(), null, null, 1);


        usersAdapter = new UsersAdapter(getActivity(), databaseHelper.getUsers());
        usersview = (ListView) rootView.findViewById(R.id.userlistview);
        usersview.setAdapter(usersAdapter);

        role = (EditText) rootView.findViewById(R.id.rolefield);
        username = (EditText) rootView.findViewById(R.id.usernamefield);
        password = (EditText) rootView.findViewById(R.id.passwordfield);
        re_enter_password = (EditText) rootView.findViewById(R.id.co_passwordfield);

        add = (Button) rootView.findViewById(R.id.addbutton);
        //Add User to db
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = username.getText().toString();
                String p = password.getText().toString();
                String r = role.getText().toString();
                String re = re_enter_password.getText().toString();

                if (u.equals("")) {
                    username.setError("UserName");
                } else if (p.equals("")) {
                    password.setError("Password");
                } else if (p.compareTo(re) != 0) {
                    re_enter_password.setError("Re-enter password");
                } else if (r.equals("")) {
                    role.setError("Role");
                } else {
                    databaseHelper.addUser(r, u, p);
                    CursorAdapter adapter = (CursorAdapter) usersview.getAdapter();
                    Cursor cursor = databaseHelper.getUsers();
                    usersAdapter.changeCursor(cursor);

                    username.setText("");
                    password.setText("");
                    re_enter_password.setText("");
                    role.setText("");
                }
            }
        });

        return rootView;
    }

    public class UsersAdapter extends CursorAdapter {

        public UsersAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            TextView a1 = (TextView) view.findViewById(R.id.column_id);
            TextView a2 = (TextView) view.findViewById(R.id.column_role);
            TextView a3 = (TextView) view.findViewById(R.id.column_username);
            Button delete = (Button) view.findViewById(R.id.dele_id);
            Button update = (Button) view.findViewById(R.id.update_id);

            a1.setText(cursor.getString(0));
            a2.setText(cursor.getString(1));
            a3.setText(cursor.getString(2));

            final String val1 = a1.getText().toString();
            final String val2 = a2.getText().toString();
            final String val3 = a3.getText().toString();

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Please select an action!");
                    alertDialogBuilder.setMessage("are you sure ?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            databaseHelper.deleteUser(val1);
                            Cursor cursor = databaseHelper.getUsers();
                            usersAdapter.changeCursor(cursor);
                        }
                    }).setCancelable(false).setNeutralButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Please select an action!");
                    alertDialogBuilder.setMessage("are you sure ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    role.setText(val2);
                                    username.setText(val3);
                                    password.setText("");
                                    databaseHelper.deleteUser(val1);
                                    Cursor cursor = databaseHelper.getUsers();
                                    usersAdapter.changeCursor(cursor);
                                }
                            }).setCancelable(false).setNeutralButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.userslistviewitem, parent, false);
            return view;
        }
    }
}
