package nemi.in;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import in.nemi.ncontrol.R;
public class PosFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_for_swipe, container, false);

        if (savedInstanceState == null) {
            android.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        return view;
    }
}