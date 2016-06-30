package nemi.in;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import in.nemi.ncontrol.R;

/**
 * Created by Aman on 5/3/2016.
 */
public class ItemFragment extends Fragment {
    ItemsAdapter itemsAdapter;
    ndbHelper databaseHelper;
    EditText et_item, et_category, et_price;
    Button additem, upload;
    ListView itemview;
    String item, category, price;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    ImageView imgView;

    public ItemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.itemsmgmt, container, false);
        databaseHelper = new ndbHelper(getActivity(), null, null, 1);
        itemsAdapter = new ItemsAdapter(getActivity(), databaseHelper.getItems());
        final ListView itemview = (ListView) rootView.findViewById(R.id.itemlistview);
        itemview.setAdapter(itemsAdapter);
        imgView = (ImageView) rootView.findViewById(R.id.imgView);
        et_item = (EditText) rootView.findViewById(R.id.item_id);
        et_category = (EditText) rootView.findViewById(R.id.category_id);
        et_price = (EditText) rootView.findViewById(R.id.price_id);
        upload = (Button) rootView.findViewById(R.id.buttonLoadPicture);

        additem = (Button) rootView.findViewById(R.id.additembutton);
        et_price.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        et_category.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        //Add Item to db
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = et_item.getText().toString().replace(' ', ' ').trim();
                category = et_category.getText().toString().trim();
                price = et_price.getText().toString().trim();
//                getText().toString().length()>0
                if (item.equals("")) {
                    et_item.setError("Item");
                } else if (category.equals("")) {
                    et_category.setError("Category");
                } else if (price.equals("")) {
                    //please look after this before doing anything
                    et_price.setError("Price");
                } else {
//                    Integer.parseInt(et_price.getText().toString())

                    databaseHelper.addItem(item, category, Integer.parseInt(et_price.getText().toString()));
//                    CursorAdapter adapter = (CursorAdapter) itemview.getAdapter();
                    Cursor cursor = databaseHelper.getItems();
                    itemsAdapter.changeCursor(cursor);

                    et_item.setText("");
                    et_category.setText("");
                    et_price.setText("");
                }
            }
        });
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    public class ItemsAdapter extends CursorAdapter {

        public ItemsAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_listview_adap, parent, false);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv_column = (TextView) view.findViewById(R.id.tv_item_column_id);
            TextView tv_item = (TextView) view.findViewById(R.id.tv_item_id);
            TextView tv_category = (TextView) view.findViewById(R.id.tv_category_id);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_price_id);

            tv_column.setText(cursor.getString(0));
            tv_item.setText(cursor.getString(1));
            tv_category.setText(cursor.getString(2));
            tv_price.setText(cursor.getString(3));

            final String item_columnid = tv_column.getText().toString();
            final String item = tv_item.getText().toString();
            final String category = tv_category.getText().toString();
            final String price = tv_price.getText().toString();

            ImageButton delete = (ImageButton) view.findViewById(R.id.dele_item_id);
            ImageButton update = (ImageButton) view.findViewById(R.id.update_item_id);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Please select an action!");
                    alertDialogBuilder.setMessage("Are you sure ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    databaseHelper.deleteItems(item_columnid);
                                    //Refresh cursor
                                    Cursor cursor = databaseHelper.getItems();
                                    itemsAdapter.changeCursor(cursor);
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
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Please select an action!");
                    alertDialogBuilder.setMessage("Are you sure ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    et_item.setText(item);
                                    et_category.setText(category);
                                    et_price.setText(price);
                                    databaseHelper.deleteItems(item_columnid);
                                    Cursor cursor = databaseHelper.getItems();
                                    itemsAdapter.changeCursor(cursor);
                                }
                            }).setCancelable(false).setNeutralButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
            });

        }


    }


}
