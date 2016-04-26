package in.nemi.ncontrol;

import android.app.Activity;
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

        TextView name, role;
        Button Edit, Delete;

        name = (TextView)findViewById(R.id.username_list);
        role = (TextView)findViewById(R.id.role_list);

        Edit = (Button)findViewById(R.id.edit_userdetail);
        Delete = (Button)findViewById(R.id.delete_userdetail);


        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
