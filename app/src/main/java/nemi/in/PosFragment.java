package nemi.in;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import common.view.SlidingTabLayout;
import in.nemi.ncontrol.R;

import android.support.annotation.Nullable;

import java.util.List;

public class PosFragment extends Fragment {
    ListView lv, items_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_for_swipe, container, false);
        lv = (ListView) view.findViewById(R.id.userlist);


        if (savedInstanceState == null) {
            android.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        return view;


    }

    public class SlidingTabsBasicFragment extends Fragment {
        ndbHelper databaseHelper;
        static final String LOG_TAG = "SlidingTabsBasicFragment";

        private SlidingTabLayout mSlidingTabLayout;
        private ViewPager mViewPager;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_sample, container, false);
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
            return view;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
            mViewPager.setAdapter(new SamplePagerAdapter());
            mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
            mSlidingTabLayout.setViewPager(mViewPager);

        }

        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< PagerAdapter >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        public class POSCursorAdapter extends CursorAdapter {

            String pos_col_items_id,pos_item_items_id,pos_price_items_id;
            public POSCursorAdapter(Context context, Cursor c) {
                super(context, c);
            }

            @Override
            public View newView(Context context, final Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_list_adap_image, parent, false);
                return view;
            }

            @Override

            public void bindView(View view, final Context context, final Cursor cursor) {
                TextView tv_column = (TextView) view.findViewById(R.id.fetch);
                TextView tv_item = (TextView) view.findViewById(R.id.item_fetch);
                TextView tv_price = (TextView) view.findViewById(R.id.price_fetch);

                tv_column.setText(cursor.getString(0));
                tv_item.setText(cursor.getString(1));
                tv_price.setText(cursor.getString(3));
            }

        }
        class SamplePagerAdapter extends PagerAdapter {
            @Override
            public int getCount() {
                Cursor c = databaseHelper.getCategories();
                return c.getCount();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return o == view;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                Toast.makeText(getContext(), "Its on: " + position, Toast.LENGTH_SHORT).show();
                return "Item " + (position + 1);
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                final View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item, container, false);

                container.addView(view);
                String ourTabName;
                POSCursorAdapter posCursorAdapter;
                databaseHelper = new ndbHelper(getActivity(), null, null, 1);
                final Cursor c = databaseHelper.getCategories();

                String[] TabbyName = new String[c.getCount()];
                c.moveToFirst();

                for (int i = 0; i < c.getCount(); i++) {
                    ourTabName = c.getString(0);
                    TabbyName[i] = ourTabName;
                    c.moveToNext();
                }

                final String a = TabbyName[position];
                posCursorAdapter = new POSCursorAdapter(getActivity(), databaseHelper.getPOSItems(a));
                items_list = (ListView) view.findViewById(R.id.items_list_id);
                items_list.setAdapter(posCursorAdapter);
                items_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView itemfetch = (TextView) view.findViewById(R.id.fetch);
                        TextView fetchitem = (TextView) view.findViewById(R.id.item_fetch);
                        TextView pricefetch = (TextView) view.findViewById(R.id.price_fetch);
                        String itemfetchvar = itemfetch.getText().toString();
                        String fetchitemvar = fetchitem.getText().toString();
                        String pricefetchvar = pricefetch.getText().toString();
                        String[] BillArray = {itemfetchvar,fetchitemvar,pricefetchvar};
//
                    }
                });

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

        }

//      <<<<<<<<<<<<<<<<<<<<

    }

}