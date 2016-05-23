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
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import common.view.SlidingTabLayout;
import in.nemi.ncontrol.R;

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
            return "Item " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item, container, false);
            container.addView(view);
            String ourTabName;
            MyCursorAdapter myCursorAdapter;
            databaseHelper = new ndbHelper(getActivity(), null, null, 1);
            final Cursor c = databaseHelper.getCategories();

            String[] TabbyName = new String[c.getCount()];
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {
                ourTabName = c.getString(0);
                TabbyName[i] = ourTabName;
                c.moveToNext();
            }

            String a = TabbyName[position];
            myCursorAdapter = new MyCursorAdapter(getActivity(), databaseHelper.getPOSItems(a));
            ListView items_list = (ListView) view.findViewById(R.id.items_list_id);
            items_list.setAdapter(myCursorAdapter);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< CursorAdapter >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public class MyCursorAdapter extends CursorAdapter {

        public MyCursorAdapter(Context context, Cursor c) {
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
            TextView tv_column = (TextView) view.findViewById(R.id.fetch);
            TextView tv_item = (TextView) view.findViewById(R.id.item_fetch);
            TextView tv_category = (TextView) view.findViewById(R.id.category_fetch);
            TextView tv_price = (TextView) view.findViewById(R.id.price_fetch);

            tv_column.setText(cursor.getString(0));
            tv_item.setText(cursor.getString(1));
            tv_category.setText(cursor.getString(2));
            tv_price.setText(cursor.getString(3));


        }
    }
//    <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   End >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}




