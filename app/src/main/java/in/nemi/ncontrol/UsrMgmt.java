package in.nemi.ncontrol;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Developer on 25-04-2016.
 */
public class UsrMgmt extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usrmgmt);

        ndbHelper databaseHelper = new ndbHelper(this, null, null, 1);
        final ListView usersview = (ListView) findViewById(R.id.userlistview);
        final UsersAdapter usersAdapter = new UsersAdapter(this, databaseHelper.getUsers());
        usersview.setAdapter(usersAdapter);
    }

    public class UsersAdapter extends CursorAdapter {

        public UsersAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView a1 = (TextView) view.findViewById(R.id.column_id);
            a1.setText(cursor.getString(1));
            TextView a2 = (TextView) view.findViewById(R.id.column_username);
            a2.setText(cursor.getString(2));
            TextView a3 = (TextView) view.findViewById(R.id.column_role);
            a3.setText(cursor.getString(3));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.userslistviewitem, parent, false);
            return view;
        }

    }
}