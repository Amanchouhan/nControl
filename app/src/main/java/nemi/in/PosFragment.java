package nemi.in;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import common.view.SlidingTabLayout;
import in.nemi.ncontrol.R;

import android.support.annotation.Nullable;

import java.util.ArrayList;

public class PosFragment extends Fragment {
    ListView lv, items_list;
    Button pay_button, clear_button;
    TextView total_amo;
    ArrayAdapter<BillItems> billAdap;
    ArrayList<BillItems> alist;
    Button set_qty_btn, delete_bill_btn;
    EditText qty_et, c_name_et, c_contact_et;
    ndbHelper databaseHelper;
    BillItems billItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pos, container, false);
        alist = new ArrayList<BillItems>();
        lv = (ListView) view.findViewById(R.id.userlist);
        total_amo = (TextView) view.findViewById(R.id.total_amo);
        databaseHelper = new ndbHelper(getActivity(), null, null, 1);
        pay_button = (Button) view.findViewById(R.id.pay);
        clear_button = (Button) view.findViewById(R.id.clear);
        c_name_et = (EditText) view.findViewById(R.id.c_name_id);
        c_contact_et = (EditText) view.findViewById(R.id.c_number_id);

        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!alist.isEmpty()) {
                    String c_name = c_name_et.getText().toString();
                    String c_contact = c_contact_et.getText().toString();
                    {

                    int a = Integer.parseInt(total_amo.getText().toString());
                    databaseHelper.bill(c_name, c_contact, a);
                    c_name_et.setText("");
                    c_contact_et.setText("");
                    int billnumber = databaseHelper.checkLastBillNumber();
                    billnumber++;
                    for (int i = 0; i < alist.size(); i++) {
                        databaseHelper.sales(billnumber, alist.get(i).getItem(), alist.get(i).getQty(), alist.get(i).getPrice());
                    }

                    lv.setAdapter(billAdap);   // set value
                    billAdap.notifyDataSetChanged();

                    int total = 0;
                    for (int j = 0; j < alist.size(); j++) {
                        total += alist.get(j).getPrice() * alist.get(j).getQty();
                        total_amo.setText("" + total);
                    }

                    billAdap.clear();
                    total_amo.setText("0");

                }
            }}
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billAdap.clear();
                total_amo.setText("0");
            }
        });
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
            // BillAdapre set here
            billAdap = new BillAdapter(getActivity(), alist);
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
            String col;

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
            public Object instantiateItem(final ViewGroup container, final int position) {
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
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        TextView itemidfetch = (TextView) view.findViewById(R.id.fetch);
                        TextView fetchitem = (TextView) view.findViewById(R.id.item_fetch);
                        TextView pricefetch = (TextView) view.findViewById(R.id.price_fetch);


                        String itemidfetchvar = itemidfetch.getText().toString();
                        String fetchitemvar = fetchitem.getText().toString();
                        int pricefetchvar = Integer.parseInt(pricefetch.getText().toString());

                        int qty = 1;

                        if (alist.isEmpty()) {
                            alist.add(new BillItems(itemidfetchvar, fetchitemvar, 1, pricefetchvar));
                            billAdap.notifyDataSetChanged();
                            lv.setAdapter(billAdap);
                        } else {
                            int flag = 0;
                            for (int i = 0; i < alist.size(); i++) {

                                flag = 0;
                                //                                //match _id
                                if (itemidfetchvar.equalsIgnoreCase(alist.get(i).getId())) {
                                    alist.set(i, new BillItems(itemidfetchvar, fetchitemvar, alist.get(i).getQty() + 1,
                                            pricefetchvar));

                                    //* alist.get(i).getQty() + pricefetchvar   increment by items
                                    lv.setAdapter(billAdap);
                                    break;
                                } else {
                                    flag = 1;
                                }
                            }
                            if (flag == 1) {
                                alist.add(new BillItems(itemidfetchvar, fetchitemvar, 1, pricefetchvar));
                                lv.setAdapter(billAdap);
                            }
                        }

                        int total = 0;

                        for (int j = 0; j < alist.size(); j++) {
                            total += alist.get(j).getPrice() * alist.get(j).getQty();
                            total_amo.setText("" + total);
                        }

                    }
                });
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        }

        public class BillAdapter extends ArrayAdapter<BillItems> {
            public BillAdapter(Context context, ArrayList<BillItems> alist) {
                super(context, 0, alist);
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                // Get the data item for this position
                billItems = getItem(position);
//                Log.e("ibillItems", billItems.toString());
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_view_on_front, parent, false);
                }
                // Lookup view for data population
                final TextView fetch_col = (TextView) convertView.findViewById(R.id.fetch_fe);
                final TextView fetch_item = (TextView) convertView.findViewById(R.id.item_fe);
                final TextView fetch_qty = (TextView) convertView.findViewById(R.id.category_fe);
                TextView fetch_price = (TextView) convertView.findViewById(R.id.price_fe);

                delete_bill_btn = (Button) convertView.findViewById(R.id.minus_item);
                set_qty_btn = (Button) convertView.findViewById(R.id.qty_item);

                set_qty_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Dialog d = new Dialog(getActivity());
                        d.setTitle("Update Quantity !");
                        d.setContentView(R.layout.dialog);
                        Button b1 = (Button) d.findViewById(R.id.button1);
                        Button b2 = (Button) d.findViewById(R.id.button2);
                        qty_et = (EditText) d.findViewById(R.id.numberPicker1);
                        qty_et.setText(String.valueOf(billItems.getQty()));
                        String sTextFromET = qty_et.getText().toString();
                        final int qty = new Integer(sTextFromET).intValue();


                        b1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                    int quantity = Integer.parseInt(qty_et.getText().toString());
                                    if(quantity <=0){
                                        qty_et.setError("Quantity must be greater than 0");
                                    }
                                    else{
                                alist.set(position, new BillItems(alist.get(position).getId(), alist.get(position).getItem(),
                                        quantity, alist.get(position).getPrice()));
                                lv.setAdapter(billAdap);   // set value
                                billAdap.notifyDataSetChanged();

                                int total = 0;
                                for (int j = 0; j < alist.size(); j++) {
                                    total += alist.get(j).getPrice() * alist.get(j).getQty();
                                    total_amo.setText("" + total);
                                }
                                d.dismiss();
                            }}}
                        );
                        b2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });
                        d.show();

                    }
                });

                delete_bill_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Please select an action!");
                        alertDialogBuilder.setMessage("Are you sure you want to delete this item?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        alist.remove(position);
                                        lv.setAdapter(billAdap);   // set value
                                        billAdap.notifyDataSetChanged();

                                        //Total calculation
                                        int total = 0;
                                        for (int j = 0; j < alist.size(); j++) {
                                            total += alist.get(j).getPrice() * alist.get(j).getQty();
                                            total_amo.setText("" + total);
                                        }
                                    }
                                }).setCancelable(false).setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });


                // Populate the data into the template view using the data object
                fetch_col.setText(billItems.getId());
                fetch_item.setText(billItems.getItem());
                fetch_qty.setText(String.valueOf(billItems.getQty()));
                fetch_price.setText(String.valueOf(billItems.getPrice()));

                // Return the completed view to render on screen
                return convertView;
            }


        }

    }

}