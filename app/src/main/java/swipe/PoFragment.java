package swipe;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import in.nemi.ncontrol.Main;
import in.nemi.ncontrol.R;
import in.nemi.ncontrol.ndbHelper;

public class PoFragment extends Fragment {
    TabHost tHost;
    Button btn_pay, btn_clear;
    EditText ed_name, ed_contact;
    public PoFragment() {
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_activity_for_swipe, container, false);
        tHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        ed_name = (EditText) rootView.findViewById(R.id.name_id);
        ed_contact = (EditText) rootView.findViewById(R.id.contact_id);
        btn_pay = (Button) rootView.findViewById(R.id.pay);
        btn_clear = (Button) rootView.findViewById(R.id.clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), Main.class);
                startActivity(in);
            }
        });
        tHost.setup();
        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                android.support.v4.app.FragmentManager fm = getChildFragmentManager();
                Android androidFragment = (Android) fm.findFragmentByTag("android");
                Ios appleFragment = (Ios) fm.findFragmentByTag("apple");
                Windows windows = (Windows) fm.findFragmentByTag("windows");
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();


                if (androidFragment != null)
                    ft.detach(androidFragment);
                if (appleFragment != null)
                    ft.detach(appleFragment);
                if (windows != null)
                    ft.detach(windows);


                if (tabId.equalsIgnoreCase("android")) {
                    if (androidFragment == null) {
                        ft.add(R.id.realtabcontent, new Android(), "android");
                    } else {
                        ft.attach(androidFragment);
                    }
                } else if (tabId.equalsIgnoreCase("apple")) {    /** If current tab is apple */
                    if (appleFragment == null) {
                        /** Create AppleFragment and adding to fragmenttransaction */
                        ft.add(R.id.realtabcontent, new Ios(), "apple");
                    } else {
                        /** Bring to the front, if already exists in the fragmenttransaction */
                        ft.attach(appleFragment);
                    }
                } else if (tabId.equalsIgnoreCase("windows")) {    /** If current tab is apple */
                    if (windows == null) {
                        /** Create AppleFragment and adding to fragmenttransaction */
                        ft.add(R.id.realtabcontent, new Windows(), "windows");
                    } else {
                        /** Bring to the front, if already exists in the fragmenttransaction */
                        ft.attach(windows);
                    }
                }
                ft.commit();
            }

        };

        /** Setting tabchangelistener for the tab */
        tHost.setOnTabChangedListener(tabChangeListener);

        /** Defining tab builder for Andriod tab */
        TabHost.TabSpec tSpecAndroid = tHost.newTabSpec("android");
        tSpecAndroid.setIndicator("Android", getResources().getDrawable(R.drawable.android));
        tSpecAndroid.setContent(new DummyTabContent(getActivity()));
        tHost.addTab(tSpecAndroid);


        /** Defining tab builder for Apple tab */
        TabHost.TabSpec tSpecApple = tHost.newTabSpec("apple");
        tSpecApple.setIndicator("Apple", getResources().getDrawable(R.drawable.apple));
        tSpecApple.setContent(new DummyTabContent(getActivity()));
        tHost.addTab(tSpecApple);

        /** Defining tab builder for Apple tab */
        TabHost.TabSpec tSpecWindows = tHost.newTabSpec("windows");
        tSpecWindows.setIndicator("windows", getResources().getDrawable(R.drawable.windows));
        tSpecWindows.setContent(new DummyTabContent(getActivity()));
        tHost.addTab(tSpecWindows);
        return rootView;
    }
    /*-------------------------------------------------------Windows Fragment-----------------------------------------------------------------*/
    public class Windows extends Fragment {
        ItemsAdapter itemsAdapter;
        ndbHelper databaseHelper;
        ListView itemview;
        public Windows() {
        }

        @Nullable
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
                        Toast.makeText(getActivity(),"Click is going on...",Toast.LENGTH_LONG).show();
//                        new ListVi();
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

//    public class ListVi extends Fragment
//    {
//        ItemsAdapter itemsAdapter;
//        ndbHelper databaseHelper;
//        ListView itemlist;
//        public ListVi() {
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.main_activity_for_swipe,container,false);
//            itemlist = (ListView) view.findViewById(R.id.userlist);
//            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
//            itemsAdapter = new ItemsAdapter(getActivity(), databaseHelper.getItems());
//            itemlist.setAdapter(itemsAdapter);
//
//            return view;
//
//        }
//        public class ItemsAdapter extends CursorAdapter{
//            public ItemsAdapter(Context context, Cursor c) {
//                super(context, c);
//            }
//
//            @Override
//            public View newView(Context context, Cursor cursor, ViewGroup parent) {
//                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//                View view = inflater.inflate(R.layout.item_view_on_front, parent, false);
//                return view;
//            }
//            @Override
//            public void bindView(View view, Context context, Cursor cursor) {
//                TextView tv_item_fe = (TextView) view.findViewById(R.id.item_fe);
//                TextView tv_category_fe = (TextView) view.findViewById(R.id.category_fe);
//                TextView tv_price_fe = (TextView) view.findViewById(R.id.price_fe);
//
//                tv_item_fe.setText(cursor.getString(0));
//                tv_category_fe.setText(cursor.getString(1));
//                tv_price_fe.setText(cursor.getString(2));
//            }
//
//        }
//    }

    /*-------------------------------------------------------Android Fragment-----------------------------------------------------------------*/
    public class Android extends Fragment {
        ItemsAdapter itemsAdapter;
        ndbHelper databaseHelper;
        ListView itemview;

        public Android() {
        }

        @Nullable
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

        @Nullable
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