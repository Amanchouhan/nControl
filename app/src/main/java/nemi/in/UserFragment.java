package nemi.in;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        final View rootView = inflater.inflate(R.layout.usrmgmt, container, false);
        databaseHelper = new ndbHelper(getActivity(), null, null, 1);
        usersAdapter = new UsersAdapter(getActivity(), databaseHelper.getUsers());
        usersview = (ListView) rootView.findViewById(R.id.userlistview);
        usersview.setAdapter(usersAdapter);
        role = (EditText) rootView.findViewById(R.id.rolefield);
        role.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        username = (EditText) rootView.findViewById(R.id.usernamefield);
        password = (EditText) rootView.findViewById(R.id.passwordfield);
        re_enter_password = (EditText) rootView.findViewById(R.id.co_passwordfield);
        final String LoggedInRole = databaseHelper.getLoggedInRole();

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
                } else if (databaseHelper.checkuser(u)) {
                    username.setError("user name already exists");
                } else if (p.compareTo(re) != 0) {
                    re_enter_password.setError("Re-enter password");
                } else if (r.equals("")) {
                    role.setError("Role");
                } else if (!LoggedInRole.equals("SUPER") && !r.equalsIgnoreCase("ADMIN") && (!r.equalsIgnoreCase("USER"))) {
                    role.setError("User role can only be either USER or ADMIN");
                } else {
                    databaseHelper.addUser(r, u, p);
                    CursorAdapter adapter = (CursorAdapter) usersview.getAdapter();
                    Cursor cursor = databaseHelper.getUsers();
                    usersAdapter.changeCursor(cursor);
                    databaseHelper.close();

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
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.userslistviewitem, parent, false);
            return view;
        }

        @Override
        public void bindView(final View view, Context context, Cursor cursor) {
            TextView a1 = (TextView) view.findViewById(R.id.column_id);
            TextView a2 = (TextView) view.findViewById(R.id.column_role);
            TextView a3 = (TextView) view.findViewById(R.id.column_username);
            ImageButton delete = (ImageButton) view.findViewById(R.id.dele_id);
            final ImageButton update = (ImageButton) view.findViewById(R.id.update_id);

            a1.setText(cursor.getString(0));
            a2.setText(cursor.getString(1));
            a3.setText(cursor.getString(2));


            final String val1 = a1.getText().toString();
            final String val2 = a2.getText().toString();
            final String val3 = a3.getText().toString();
            final String LoggedInRole = databaseHelper.getLoggedInRole();

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LoggedInRole.equals("SUPER")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Please select an action!");
                        alertDialogBuilder.setIcon(R.drawable.question_mark);
                        alertDialogBuilder.setMessage("Are you sure you want to delete this user or admin ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        databaseHelper.deleteUser(val1);
                                        Cursor cursor = databaseHelper.getUsers();
                                        usersAdapter.changeCursor(cursor);
                                        databaseHelper.close();
                                    }
                                }).setCancelable(false).setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        Toast.makeText(getActivity(), "You need to be logged in as SUPER to perform this action!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LoggedInRole.equals("SUPER")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Please select an action!");
                        alertDialogBuilder.setIcon(R.drawable.question_mark);
                        alertDialogBuilder.setMessage("Are you sure you want to update this?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        role.setText(val2);
                                        username.setText(val3);
                                        password.setText("");
                                        databaseHelper.deleteUser(val1);
                                        Cursor cursor = databaseHelper.getUsers();
                                        usersAdapter.changeCursor(cursor);
                                        databaseHelper.close();
                                    }
                                }).setCancelable(false).setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        Toast.makeText(getActivity(), "You need to be logged in as SUPER to perform this action!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}
