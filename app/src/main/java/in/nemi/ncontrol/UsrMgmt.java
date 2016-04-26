package in.nemi.ncontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Developer on 21-04-2016.
 */
public class UsrMgmt extends Activity {

    TextView name, role;
    Button Edit, Delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_row);

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