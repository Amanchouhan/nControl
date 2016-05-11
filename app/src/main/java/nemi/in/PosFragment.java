package nemi.in;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

public class PosFragment extends Fragment implements TabHost.OnTabChangeListener,ViewPager.OnPageChangeListener {
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
        View rootView = inflater.inflate(R.layout.main_activity_for_swipe, container, false);
        tHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        viewPager = (ViewPager)rootView.findViewById(R.id.view_pager);
         horizontalScrollView = (HorizontalScrollView)rootView.findViewById(R.id.h_scroll_view);
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
        List<Fragment>fragmentList=new ArrayList<Fragment>();
        fragmentList.add(new Windows());
        fragmentList.add(new Ios());
        fragmentList.add(new Android());
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }
    private void initializeTabHost() {
        tHost.setup();

            String[] tabName = {"Windows","Ios","Android"};
            for (int i=0;i<tabName.length;i++)
            {
                TabHost.TabSpec tabSpec;
                tabSpec = tHost.newTabSpec(tabName[i]);
                tabSpec.setIndicator(tabName[i]);
                tabSpec.setContent(new FakeContent(getActivity()));
                tHost.addTab(tabSpec);
            }


        tHost.setOnTabChangedListener(this);

    }
    class FakeContent implements TabHost.TabContentFactory{

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
    public class Windows extends Fragment {
        ItemsAdapter itemsAdapter;
        ndbHelper databaseHelper;
        ListView itemview;
        public Windows() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.itemslistview, container, false);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
            itemsAdapter = new ItemsAdapter(getActivity(), databaseHelper.getItems());
            itemview = (ListView) rootView.findViewById(R.id.itemlistview);
            itemview.setAdapter(itemsAdapter);
            return rootView;
        }

        public class ItemsAdapter extends CursorAdapter {
            public ItemsAdapter(Context context, Cursor cursor) {
                super(context, cursor);
            }



            @Override
            public void bindView(View view, Context context, final Cursor cursor) {


                final TextView tv_column = (TextView) view.findViewById(R.id.fetch);
                final TextView tv_item = (TextView) view.findViewById(R.id.item_fetch);
                final TextView tv_category = (TextView) view.findViewById(R.id.category_fetch);
                final TextView tv_price = (TextView) view.findViewById(R.id.price_fetch);

                Button click = (Button) view.findViewById(R.id.click_here);

                tv_column.setText(cursor.getString(0));
                tv_item.setText(cursor.getString(1));
                tv_category.setText(cursor.getString(2));
                tv_price.setText(cursor.getString(3));

                click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"Click is going on...", Toast.LENGTH_LONG).show();
                        new ListVi();
                    }
                });
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_list_adap_image, parent, false);
                return view;
            }
        }
    }

    public class ListVi extends Fragment
    {
        ItemsAdapter itemsAdapter;
        ndbHelper databaseHelper;
        ListView itemlist;
        public ListVi() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.main_activity_for_swipe,container,false);
            itemlist = (ListView) view.findViewById(R.id.userlist);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
            itemsAdapter = new ItemsAdapter(getActivity(), databaseHelper.getItems());
            itemlist.setAdapter(itemsAdapter);

            return view;

        }
        public class ItemsAdapter extends CursorAdapter {
            public ItemsAdapter(Context context, Cursor c) {
                super(context, c);
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_view_on_front, parent, false);
                return view;
            }
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tv_item_fe = (TextView) view.findViewById(R.id.item_fe);
                TextView tv_category_fe = (TextView) view.findViewById(R.id.category_fe);
                TextView tv_price_fe = (TextView) view.findViewById(R.id.price_fe);

                tv_item_fe.setText(cursor.getString(0));
                tv_category_fe.setText(cursor.getString(1));
                tv_price_fe.setText(cursor.getString(2));
            }

        }
    }

    /*-------------------------------------------------------Android Fragment-----------------------------------------------------------------*/
    public class Android extends Fragment {
        ItemsAdapter itemsAdapter;
        ndbHelper databaseHelper;
        ListView itemview;

        public Android() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.itemslistview, container, false);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
            itemsAdapter = new ItemsAdapter(getActivity(), databaseHelper.getItems());
            itemview = (ListView) rootView.findViewById(R.id.itemlistview);
            itemview.setAdapter(itemsAdapter);
            return rootView;
        }

        public class ItemsAdapter extends CursorAdapter {
            public ItemsAdapter(Context context, Cursor cursor) {
                super(context, cursor);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tv_column = (TextView) view.findViewById(R.id.item_fetch);
                TextView tv_item = (TextView) view.findViewById(R.id.category_fetch);
                TextView tv_category = (TextView) view.findViewById(R.id.price_fetch);
//            TextView tv_price = (TextView) view.findViewById(R.id.tv_price_id);

                tv_column.setText(cursor.getString(0));
                tv_item.setText(cursor.getString(1));
                tv_category.setText(cursor.getString(2));
//            tv_price.setText(cursor.getString(3));
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_list_adap_image, parent, false);
                return view;
            }
        }
    }

    /*-------------------------------------------------------Ios Fragment-----------------------------------------------------------------*/
    public class Ios extends Fragment {
        ItemsAdapter itemsAdapter;
        ndbHelper databaseHelper;
        ListView itemview;

        public Ios() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.itemslistview, container, false);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
            itemsAdapter = new ItemsAdapter(getActivity(), databaseHelper.getItems());
            itemview = (ListView) rootView.findViewById(R.id.itemlistview);
            itemview.setAdapter(itemsAdapter);
            return rootView;
        }

        public class ItemsAdapter extends CursorAdapter {
            public ItemsAdapter(Context context, Cursor cursor) {
                super(context, cursor);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tv_column = (TextView) view.findViewById(R.id.item_fetch);
                TextView tv_item = (TextView) view.findViewById(R.id.category_fetch);
                TextView tv_category = (TextView) view.findViewById(R.id.price_fetch);
//            TextView tv_price = (TextView) view.findViewById(R.id.tv_price_id);

                tv_column.setText(cursor.getString(0));
                tv_item.setText(cursor.getString(1));
                tv_category.setText(cursor.getString(2));
//            tv_price.setText(cursor.getString(3));
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_list_adap_image, parent, false);
                return view;
            }
        }
    }

}