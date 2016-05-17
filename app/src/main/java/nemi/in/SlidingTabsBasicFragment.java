package nemi.in;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
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
//            Cursor c = databaseHelper.getCategories();
//            c.moveToFirst();
//            for (int i = 0; i < c.getCount(); i++) {
//                Log.e("hello", c.getString(0));
//                String s = c.getString(0);
//                c.moveToNext();
//                return s+position;
//            }
//            c.close();
//            return null;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item, container, false);
            container.addView(view);
            ListView items_list = (ListView) view.findViewById(R.id.items_list_id);
//            title.setText(String.valueOf(position + 1));
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }
    }
    public class MyCursorAdapter extends CursorAdapter
    {

        public MyCursorAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }
}
