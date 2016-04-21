package in.nemi.ncontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Developer on 21-04-2016.
 */
public class ndbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ncontrol.db";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public ndbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_USERS + "(" +
                COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_ROLE + " text," +
                COLUMN_USERNAME + " text," +
                COLUMN_PASSWORD + " text" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_USERS);
        onCreate(db);
    }

    //Add a user to the db
    public void addUser() {

    }

    //remove a user from the db
    public void deleteUser() {

    }
}