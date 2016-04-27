package in.nemi.ncontrol;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Developer on 25-04-2016.
 */
public class UsrMgmt extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usrmgmt);

        ndbHelper databaseHelper;
        databaseHelper = new ndbHelper(this, null, null, 1);
        Cursor cursor = databaseHelper.getUsers();
        String[] users = new String[]{
                databaseHelper.COLUMN_USERNAME
        };
        int[] id = new int[]{};

        ListAdapter userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        ListView userListView = (ListView) findViewById(R.id.userListView);
        userListView.setAdapter(userAdapter);

    }
}