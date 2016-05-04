package in.nemi.ncontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aman on 5/4/2016.
 */
public class LogoutFragment extends android.app.Fragment {
    public LogoutFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main, container, false);
        return rootView;
    }
}
