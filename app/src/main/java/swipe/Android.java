package swipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.nemi.ncontrol.R;

/**
 * Created by Aman on 5/4/2016.
 */
public class Android extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.swipe_fragment, container, false);
        return android;
    }
}
