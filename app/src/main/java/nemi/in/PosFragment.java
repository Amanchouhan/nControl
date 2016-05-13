package nemi.in;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import in.nemi.ncontrol.R;

public class PosFragment extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    ndbHelper databaseHelper;
    private TabHost tHost;
    private ViewPager viewPager;
    HorizontalScrollView horizontalScrollView;
    View rootView;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    int i = 0;

    Button btn_pay, btn_clear;
    EditText ed_name, ed_contact;

    public PosFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new ndbHelper(getActivity(), null, null, 1);
        View rootView = inflater.inflate(R.layout.main_activity_for_swipe, container, false);
        tHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.h_scroll_view);
//        ed_name = (EditText) rootView.findViewById(R.id.name_id);
//        ed_contact = (EditText) rootView.findViewById(R.id.contact_id);
//        btn_pay = (Button) rootView.findViewById(R.id.pay);
//        btn_clear = (Button) rootView.findViewById(R.id.clear);
        i++;
        this.initializeTabHost();
        this.initializeViewPager();


        return rootView;
    }

    private void initializeViewPager() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new Grocery());
        fragmentList.add(new Food());
        fragmentList.add(new Fruit());
        fragmentList.add(new Seafood());
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    private void initializeTabHost() {
        tHost.setup();
        Cursor c = databaseHelper.getCategories();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            TabHost.TabSpec tabSpec;
            tabSpec = tHost.newTabSpec(c.getString(0));
            tabSpec.setIndicator(c.getString(0));
            tabSpec.setContent(new FakeContent(getActivity()));
            tHost.addTab(tabSpec);
            c.moveToNext();
        }
        c.close();
        tHost.setOnTabChangedListener(this);
    }

    class FakeContent implements TabHost.TabContentFactory {

        private final Context context;

        public FakeContent(Context mcontext) {
            context = mcontext;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(context);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }
    }

    // TabListener
    @Override
    public void onTabChanged(String tabId) {
        int selectItem = tHost.getCurrentTab();
        viewPager.setCurrentItem(selectItem);
    }

    //ViewPagerListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int selectItem) {
        tHost.setCurrentTab(selectItem);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
//


    /*-------------------------------------------------------Windows Fragment-----------------------------------------------------------------*/
    public class Grocery extends Fragment {
        ndbHelper databaseHelper;
        ListView item_view;
        GroceryAdapter groceryAdapter;

        public Grocery() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.itemslistview, container, false);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
//            groceryAdapter = new GroceryAdapter(getActivity(), databaseHelper.getGrocery());
            item_view = (ListView) rootView.findViewById(R.id.itemlistview);
            item_view.setAdapter(groceryAdapter);
            return rootView;
        }

        public class GroceryAdapter extends CursorAdapter {
            public GroceryAdapter(Context context, Cursor c) {
                super(context, c);
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_list_adap_image, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tv_item = (TextView) view.findViewById(R.id.item_fetch);
                TextView tv_category = (TextView) view.findViewById(R.id.category_fetch);
                TextView tv_price = (TextView) view.findViewById(R.id.price_fetch);
                tv_item.setText(cursor.getString(1));
                tv_category.setText(cursor.getString(2));
                tv_price.setText(cursor.getString(3));
            }
        }
    }

    /*-------------------------------------------------------Android Fragment-----------------------------------------------------------------*/
    public class Food extends Fragment {
        ndbHelper databaseHelper;
        ListView itemview;
        GroceryAdapter groceryAdapter;

        public Food() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.itemslistview, container, false);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
//            groceryAdapter = new GroceryAdapter(getActivity(), databaseHelper.getCategories());
            itemview = (ListView) rootView.findViewById(R.id.itemlistview);
            itemview.setAdapter(groceryAdapter);
            return rootView;
        }

        public class GroceryAdapter extends CursorAdapter {
            public GroceryAdapter(Context context, Cursor c) {
                super(context, c);
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_list_adap_image, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tv_item = (TextView) view.findViewById(R.id.item_fetch);
                TextView tv_category = (TextView) view.findViewById(R.id.category_fetch);
                TextView tv_price = (TextView) view.findViewById(R.id.price_fetch);
                tv_item.setText(cursor.getString(1));
                tv_category.setText(cursor.getString(2));
                tv_price.setText(cursor.getString(3));
            }
        }
    }

    /*-------------------------------------------------------Ios Fragment-----------------------------------------------------------------*/
    public class Fruit extends Fragment {
        ndbHelper databaseHelper;
        ListView itemview;
        GroceryAdapter groceryAdapter;

        public Fruit() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.itemslistview, container, false);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
//            groceryAdapter = new GroceryAdapter(getActivity(), databaseHelper.getFruit());
            itemview = (ListView) rootView.findViewById(R.id.itemlistview);
            itemview.setAdapter(groceryAdapter);
            return rootView;
        }

        public class GroceryAdapter extends CursorAdapter {
            public GroceryAdapter(Context context, Cursor c) {
                super(context, c);
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_list_adap_image, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tv_item = (TextView) view.findViewById(R.id.item_fetch);
                TextView tv_category = (TextView) view.findViewById(R.id.category_fetch);
                TextView tv_price = (TextView) view.findViewById(R.id.price_fetch);
                tv_item.setText(cursor.getString(1));
                tv_category.setText(cursor.getString(2));
                tv_price.setText(cursor.getString(3));
            }
        }
    }

    /*-------------------------------------------------------Seafood Fragment-----------------------------------------------------------------*/
    public class Seafood extends Fragment {
        ndbHelper databaseHelper;
        ListView itemview;
        GroceryAdapter groceryAdapter;

        public Seafood() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.itemslistview, container, false);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
//            groceryAdapter = new GroceryAdapter(getActivity(), databaseHelper.getSeafood());
            itemview = (ListView) rootView.findViewById(R.id.itemlistview);
            itemview.setAdapter(groceryAdapter);
            return rootView;
        }

        public class GroceryAdapter extends CursorAdapter {
            public GroceryAdapter(Context context, Cursor c) {
                super(context, c);
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_list_adap_image, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tv_item = (TextView) view.findViewById(R.id.item_fetch);
                TextView tv_category = (TextView) view.findViewById(R.id.category_fetch);
                TextView tv_price = (TextView) view.findViewById(R.id.price_fetch);
                tv_item.setText(cursor.getString(1));
                tv_category.setText(cursor.getString(2));
                tv_price.setText(cursor.getString(3));
            }
        }
    }
}