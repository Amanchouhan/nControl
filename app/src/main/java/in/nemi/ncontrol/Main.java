package in.nemi.ncontrol;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by shouryas on 4/21/2016.
 */
public class Main extends Activity {

    ndbHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        databaseHelper = new ndbHelper(this, null, null, 1);
    }
}