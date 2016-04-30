//package in.nemi.ncontrol;
//
//import android.app.Activity;
//import android.content.Context;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CursorAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * Created by Aman on 4/29/2016.
// */
//public class ItemsMgmt extends Activity {
//
//    ndbHelper databaseHelper;
//    EditText et_item, et_category, et_price;
//    Button additem;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.itemsmgmt);
//
//        databaseHelper = new ndbHelper(this, null, null, 1);
//
//        final ListView usersview = (ListView) findViewById(R.id.userlistview);
//        final ItemsAdapter usersAdapter = new ItemsAdapter(this, databaseHelper.getUsers());
//        usersview.setAdapter(usersAdapter);
////        for heading for the listview
////        usersview.addHeaderView(usersview);
//
//        et_item = (EditText) findViewById(R.id.item_id);
//        et_category = (EditText) findViewById(R.id.category_id);
//        et_price = (EditText) findViewById(R.id.price_id);
//        additem = (Button) findViewById(R.id.additembutton);
//
//        //Add Item to db
//        additem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String item = et_item.getText().toString();
//                String category = et_category.getText().toString();
//                int price = Integer.parseInt(et_price.getText().toString());
//
//                if (item.equals("")) {
//                    Toast.makeText(getApplicationContext(), "Enter the user name", Toast.LENGTH_LONG).show();
//                } else if (category.equals("")) {
//                    Toast.makeText(getApplicationContext(), "Enter the password", Toast.LENGTH_LONG).show();
//                } else if (price<=0) {
//                    //please look after this before doing anything
//                    Toast.makeText(getApplicationContext(), "Enter the role", Toast.LENGTH_LONG).show();
//                } else {
//                    databaseHelper.addUser(item, category, price);
//                    CursorAdapter adapter = (CursorAdapter) usersview.getAdapter();
//                    Cursor cursor = databaseHelper.getUsers();
//                    usersAdapter.changeCursor(cursor);
//
//                    et_item.setText("");
//                    et_category.setText("");
//                    et_price.setText("");
//                }
//            }
//        });
//    }
//
//    public class ItemsAdapter extends CursorAdapter {
//
//        public ItemsAdapter(Context context, Cursor cursor) {
//            super(context, cursor);
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            TextView a1 = (TextView) view.findViewById(R.id.column_id);
//            a1.setText(cursor.getString(0));
//            TextView a2 = (TextView) view.findViewById(R.id.column_role);
//            a2.setText(cursor.getString(1));
//            TextView a3 = (TextView) view.findViewById(R.id.column_username);
//            a3.setText(cursor.getString(2));
//        }
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup parent) {
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            View view = inflater.inflate(R.layout.userslistviewitem, parent, false);
//            return view;
//        }
//    }
//}
