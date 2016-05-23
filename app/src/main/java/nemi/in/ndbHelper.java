package nemi.in;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Developer on 21-04-2016.
 */
public class ndbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ncontrol.db";

    //users table vars
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    //items table vars
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM = "item";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PRICE = "price";


    public ndbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String usersquery = "create table " + TABLE_USERS + "(" +
                COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_ROLE + " text," +
                COLUMN_USERNAME + " text," +
                COLUMN_PASSWORD + " text" +
                ");";
        db.execSQL(usersquery);

        String itemsquery = "create table " + TABLE_ITEMS + "(" +
                COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_ITEM + " text not null," +
                COLUMN_CATEGORY + " text not null," +
                COLUMN_PRICE + " integer not null" +
                ");";
        db.execSQL(itemsquery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ITEMS);

        onCreate(db);
    }

    //Check for superuser
    public boolean checkS() {
        Boolean exists = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"role"}, "role = ?", new String[]{"super"}, null, null, null);
        if (cursor.getCount() == 1) {
            exists = true;
        } else {
            exists = false;
        }
        cursor.close();
        db.close();
        return exists;
    }

    //Add a user to the db
    public void addUser(String role, String user, String pass) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user);
        cv.put(COLUMN_ROLE, role);
        cv.put(COLUMN_PASSWORD, pass);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USERS, null, cv);
        db.close();
    }
    public Cursor getUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "select * from " + TABLE_USERS,
                null
        );
    }
     /*------------------remove a user from the db (please remember you'll get the username by touching it in its list view---------------------------------------*/

    public void deleteUser(String username) {
        int col_id = Integer.parseInt(username);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + "=\"" + col_id + "\";");
        db.close();
    }

    /*------------------------------------Add a user to the db-----------------------------------------------------------------*/

    public void addItem(String item, String category, String price) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ITEM, item);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_PRICE, price);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ITEMS, null, cv);
        db.close();
    }
    public Cursor getItems() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "select * from " + TABLE_ITEMS,
                null
        );
    }
      /*------------//remove a item from the db (please remember you'll get the username by touching it in its listview)----------------------*/

    public void deleteItems(String itemname) {
        int col_id = Integer.parseInt(itemname);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ID + "=\"" + col_id + "\";");
        db.close();
    }



    /*-------------------------------------login-----------------------------------------------------------------*/

    public String loginUser(String u) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + COLUMN_USERNAME + "," + COLUMN_PASSWORD + " FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b = "NOT FOUND";
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                if (a.equals(u)) {
                    b = cursor.getString(1);
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return b;
    }




    /*-------------------------------------------------------Tab categories Fragment-----------------------------------------------------------------*/
    public Cursor getCategories() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select distinct category from "+ TABLE_ITEMS + "", null);
        //category :- fruit,food,seafood;
    }
//
//    public Cursor getCategoriesArray() {
//        SQLiteDatabase db = getReadableDatabase();
//        return db.rawQuery("select distinct category from items", null);
//    }
    public Cursor getPOSItems(String a) {
        SQLiteDatabase db = getReadableDatabase();
//        return db.rawQuery("select * from "+ TABLE_ITEMS + " where category='food'", null);
        return db.rawQuery("select * from items where " + COLUMN_CATEGORY + "=\"" + a + "\";", null);
//        return db.rawQuery("select * from items", null);
    }

}