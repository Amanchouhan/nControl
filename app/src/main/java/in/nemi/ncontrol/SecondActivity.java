package in.nemi.ncontrol;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by Developer on 25-04-2016.
 */
public class SecondActivity extends Activity {

    ListView user_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usrmgmt);

        user_listview = (ListView)findViewById(R.id.listView);
    }
}
