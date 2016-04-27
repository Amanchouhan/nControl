package in.nemi.ncontrol;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Developer on 25-04-2016.
 */
public class UsrMgmt extends Activity  {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usrmgmt);
        ndbHelper db=new ndbHelper(this,null,null,1);


        Spinner spinner = (Spinner) findViewById(R.id.dropdown_list);
        ArrayAdapter<String> adapter;
        ListAdapter listAdapter;
        List<String> list;

        list = new ArrayList();
        list.add("User");
        list.add("Admin");


        adapter = new ArrayAdapter(getApplication(), android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




            //Retrieving data into list view
            String[] data = new String[db.getUsername().size()];
            data = db.getUsername().toArray(data);

            listAdapter = new ArrayAdapter<String>(this,R.layout.pos,R.id.username_list,data);
            ListView listView = (ListView)findViewById(R.id.listView);
            listView.setAdapter(listAdapter);

    }
}

