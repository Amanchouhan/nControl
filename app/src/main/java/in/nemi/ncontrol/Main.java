package in.nemi.ncontrol;

import android.app.Activity;
        import android.app.DownloadManager;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.view.View;

/**
 * Created by shouryas on 4/21/2016.
 */
public class Main extends Activity {

    //    private Context context;
    //    public SQLiteDatabase database = null;
    ndbHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        databaseHelper = new ndbHelper(this, null, null, 1);
    }
}

//        ncontrolDatabaseHelper db = new ncontrolDatabaseHelper(context);
//        db.onCreate(database);
    // public static boolean hyperlink(String TABLE_NAME,String COLUMN_ROLE, String admin) {
    //   SQLiteDatabase db = ncontrolDatabaseHelper.database;

      // String Query = "Select * from " + TABLE_NAME + " where " + COLUMN_ROLE + " = " + admin;
      //  Cursor cursor = db.rawQuery(Query, null);
      //  if(cursor.getCount() <= 0){
         //   cursor.close();
         //   return false;
       // }
       // cursor.close();
       // return true;
   // }
