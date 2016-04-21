package in.nemi.ncontrol;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Developer on 21-04-2016.
 */
public class ncontrolUsrMngmntTable {

    //DATABASE TABLE
    public static final String TABLE_NAME= "UsrMngmntTable";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_ROLE="role";
    public static final String COLUMN_USERNM="username";
    public static final String COLUMN_PASSWRD="password";


    private static final String DATABASE_CREATE = "create table"
            +TABLE_NAME
            +"("
            +COLUMN_ID+"INTEGER PRIMARY KEY AUTOINCREMENT"
            +COLUMN_ROLE+"VARCHAR"
            +COLUMN_USERNM+"VARCHAR"
            +COLUMN_PASSWRD+"VARCHAR"
            +");";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

    }


    public static void onUpgrade(SQLiteDatabase database, int oldversion, int newversion) {

        database.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(database);

    }



}
