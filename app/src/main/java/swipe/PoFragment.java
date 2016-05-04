package swipe;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.nemi.ncontrol.R;

public class PoFragment extends android.app.Fragment implements ActionBar.TabListener {
    private ViewPager viewPager;
    private TabPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = {"Grocery", "Drink", "Vegetable"};

    public PoFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_activity_for_swipe, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //This my problem
        mAdapter = new TabPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(PoFragment.this));
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        return rootView;
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }


}