package nemi.in;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.nemi.ncontrol.R;

/**
 * Created by Aman on 6/20/2016.
 */
public class SettingFragment extends Fragment {
    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_main,container,false);
        return view;
    }
}
