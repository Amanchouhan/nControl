package nemi.in;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Developer on 21-04-2016.
 */
public class ndbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ncontrol.db";
    public static final String COLUMN_ID = "_id";

    //users table vars
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_LOGINSTATUS = "loginstatus";

    //items table vars
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM = "item";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PRICE = "price";

    //sales table vars
    public static final String TABLE_SALES = "sales";
    public static final String COLUMN_BILLNUMBER = "billnumber";
    public static final String COLUMN_QUANTITY = "quantity";


    //bill table vars
    public static final String TABLE_BILL = "bill";
    public static final String COLUMN_C_NAME = "c_name";
    public static final String COLUMN_C_CONTACT = "c_contact";
    public static final String COLUMN_BILL_DATE_TIME = "c_billdatetime";
    public static final String COLUMN_BILLAMOUNT = "billamount";

    public ndbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String usersquery = "create table " + TABLE_USERS + "(" +
                COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_ROLE + " text," +
                COLUMN_USERNAME + " text," +
                COLUMN_PASSWORD + " text," +
                COLUMN_LOGINSTATUS + " text DEFAULT 'false'" +
                ");";
        db.execSQL(usersquery);

        String itemsquery = "create table " + TABLE_ITEMS + "(" +
                COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_ITEM + " text not null," +
                COLUMN_CATEGORY + " text not null," +
                COLUMN_PRICE + " integer not null" +
                ");";
        db.execSQL(itemsquery);

        String salesquery = "create table " + TABLE_SALES + "(" +
                COLUMN_ID + " INTEGER primary key autoincrement," +
                COLUMN_BILLNUMBER + " integer not null," +
                COLUMN_ITEM + " text not null," +
                COLUMN_QUANTITY + " text not null," +
                COLUMN_PRICE + " integer not null" +
                ");";
        db.execSQL(salesquery);

        String billquery = "create table " + TABLE_BILL + "(" +
                COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_C_NAME + " text not null," +
                COLUMN_C_CONTACT + " text not null," +
                COLUMN_BILL_DATE_TIME + " text NOT NULL," +
                COLUMN_BILLAMOUNT + " INTEGER NOT NULL" +
                ");";
        db.execSQL(billquery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_BILL);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_SALES);

        onCreate(db);
    }

    //Check for superuser
    public boolean checkS() {
        Boolean exists = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"role"}, "role = ?", new String[]{"SUPER"}, null, null, null);
        if (cursor.getCount() == 0) {
            exists = false;
        } else {
            exists = true;
        }
        cursor.close();
        db.close();
        return exists;
    }

    //user name check
     public boolean checkuser(String user){
         SQLiteDatabase db = getReadableDatabase();
         Cursor mCursor= db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE username='"+user+"'",null);

         if (!(mCursor.moveToFirst()) || mCursor.getCount() ==0)
         {
             return false;
    /* record exist */
         }
         else
         {
             return true;
    /* record not exist */
         }



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

    //Change login status
    public void loginStatus(String loginstatus, String user) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOGINSTATUS, loginstatus);
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_USERS, cv, COLUMN_USERNAME + " = ?",
                new String[]{user});
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

    public void addItem(String item, String category, int price) {
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

    public void deleteBill(int billnumber) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BILL + " WHERE " + COLUMN_ID + "=\"" + billnumber + "\";");
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
        return db.rawQuery("select distinct category from " + TABLE_ITEMS + "", null);
        //category :- fruit,food,seafood;
    }

    public Cursor getPOSItems(String a) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from items where " + COLUMN_CATEGORY + "=\"" + a + "\";", null);
    }

/*-------------------------------------------------------pos bill-----------------------------------------------------------------*/

    public int checkLastBillNumber() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT billnumber FROM sales ORDER BY billnumber DESC LIMIT 1;", null);
        int billnumber = 0;
        if (cursor.moveToFirst()) {
            billnumber = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return billnumber;
    }

    public void sales(int billnumber, String item, int quantity, int price) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BILLNUMBER, billnumber);
        cv.put(COLUMN_ITEM, item);
        cv.put(COLUMN_QUANTITY, quantity);
        cv.put(COLUMN_PRICE, price);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_SALES, null, cv);
        db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void bill(String c_name, String c_number, int billamount) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_C_NAME, c_name);
        cv.put(COLUMN_C_CONTACT, c_number);
        cv.put(COLUMN_BILLAMOUNT, billamount);
        cv.put(COLUMN_BILL_DATE_TIME, getDateTime());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_BILL, null, cv);
        db.close();
    }

    /*----------------------------------------------------------get bill magmt---------------------------------------------------------*/
    public Cursor getBill() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT _id, c_billdatetime, billamount FROM bill ORDER BY _id DESC", null);
    }

    public Cursor getSale(int billnumber) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT _id, item, quantity, price FROM sales WHERE billnumber = " + billnumber, null);
    }

    public Cursor getBillInfo(int billnumber) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT _id, c_billdatetime, billamount, c_name, c_contact FROM bill WHERE _id = " + billnumber, null);
    }

    public String getLoggedInUser() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT username FROM users WHERE loginstatus = 'true'", null);
        c.moveToFirst();
        return c.getString(0);
    }

    public String getLoggedInRole() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT role FROM users WHERE loginstatus = 'true'", null);
        c.moveToFirst();
        return c.getString(0);
    }
    /*=========================================================Search query here=================================================================*/
    public Cursor searchByBillNumber(int billnumber) {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "SELECT _id, c_billdatetime, billamount FROM bill WHERE _id = " + billnumber + ";";
        return db.rawQuery(qry, null);
    }

    public Cursor searchByDate(String fdate, String tdate) {
//    public Cursor searchByDate() {
        SQLiteDatabase db = getReadableDatabase();
//        String qry = "SELECT _id, c_billdatetime, billamount FROM bill WHERE CAST(c_billdatetime AS DATE) BETWEEN" +
//                "CAST(" + fdate + " AS DATE) AND CAST(" + tdate + " AS DATE) ORDER BY _id DESC;" ;
//        String qry = "SELECT _id, c_billdatetime, billamount FROM bill " +
//                "WHERE CAST(c_billdatetime AS DATETIME) BETWEEN CAST(" + fdate + " AS DATETIME) AND CAST(" +
//                tdate + " AS DATETIME) ORDER BY _id DESC;" ;

//        String qry = "SELECT _id, c_billdatetime, billamount FROM bill " +
//                "WHERE CAST(c_billdatetime AS DATETIME) BETWEEN CAST " + fdate + " AND CAST " +
//                tdate + " ORDER BY _id DESC;" ;
//        return db.rawQuery(qry, null);

        String qry = "select _id, c_billdatetime, billamount from bill where date(c_billdatetime) BETWEEN " +
                "date('" + fdate + "') AND date('" + tdate +"') ORDER BY _id DESC" ;
        return db.rawQuery(qry, null);
    }

    public Cursor searchByAmount(int billamount) {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "SELECT _id, c_billdatetime, billamount FROM bill WHERE billamount = " + billamount;
        return db.rawQuery(qry, null);
    }

    public Cursor searchByCustomerName(String customername) {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "SELECT _id, c_billdatetime, billamount FROM bill WHERE c_name LIKE '" + customername + "'";
        return db.rawQuery(qry, null);
    }

    public Cursor searchByCustomerContact(String contact) {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "SELECT _id, c_billdatetime, billamount FROM bill WHERE c_contact LIKE '" + contact + "'";
        return db.rawQuery(qry, null);
    }

    /*================================================================================================================================*/


}