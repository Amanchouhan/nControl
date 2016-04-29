package in.nemi.ncontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Developer on 21-04-2016.
 */
public class ndbHelper extends SQLiteOpenHelper{

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
                COLUMN_ROLE + " text not null," +
                COLUMN_USERNAME + " text not null," +
                COLUMN_PASSWORD + " text not null" +
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
        onCreate(db);
    }

    //Check for superuser
    public boolean checkS() {
        Boolean exists = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] {"role"}, "role = ?", new String[] {"super"}, null, null, null);
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

    //login
    public String loginUser(String u) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + COLUMN_USERNAME + "," + COLUMN_PASSWORD + " FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);
        String a,b;
        b = "NOT FOUND";
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                if (a.equals(u)) {
                    b=cursor.getString(1);
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return b;
    }

    //remove a user from the db (please remember you'll get the username by touching it in its listview)
    public void deleteUser(String username) {
        int col_id= Integer.parseInt(username);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + "=\"" + col_id + "\";");
        db.close();
    }

    public Cursor getUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "select * from " + TABLE_USERS,
                null
        );
    }
}