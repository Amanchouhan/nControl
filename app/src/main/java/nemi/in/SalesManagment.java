package nemi.in;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import in.nemi.ncontrol.R;

/**
 * Created by Aman on 6/3/2016.
 */
public class SalesManagment extends Fragment {
    ndbHelper databaseHelper;
    SalesManagmentAdapter salesManagmentAdapter;
    ListView bill_list, bill_details;
    public SalesManagment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.salesmgmt, container, false);
        databaseHelper = new ndbHelper(getActivity(), null, null, 1);

        salesManagmentAdapter = new SalesManagmentAdapter(getActivity(), databaseHelper.getBill());
        bill_list = (ListView) rootView.findViewById(R.id.userlistview);
        bill_list.setAdapter(salesManagmentAdapter);

        return rootView;
    }

    public class SalesManagmentAdapter extends CursorAdapter {

        public SalesManagmentAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.bill_listview_adap, parent, false);
            return view;
        }
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView bill_number_tv = (TextView) view.findViewById(R.id.tv_bill_number_fecth_id);
            TextView bill_date_tv = (TextView) view.findViewById(R.id.tv_bill_date_fetch_id);
            TextView bill_amount_tv = (TextView) view.findViewById(R.id.tv_bill_amount_fetch_id);
            Button delete_item_btn = (Button) view.findViewById(R.id.del_item);
            Button view_item_id = (Button) view.findViewById(R.id.view_item);

            bill_number_tv.setText(cursor.getString(0));
            bill_date_tv.setText(cursor.getString(1));
            bill_amount_tv.setText(cursor.getString(2));

            final String bill_number = bill_number_tv.getText().toString();

            view_item_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cursor c = databaseHelper.getSale(Integer.parseInt(bill_number));
                }
            });

            delete_item_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHelper.deleteBill(Integer.parseInt(bill_number));
                    Cursor c = databaseHelper.getBill();
                    salesManagmentAdapter.changeCursor(c);
                }
            });
        }
    }
}
