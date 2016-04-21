package in.nemi.ncontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by Developer on 21-04-2016.
 */
public class ncontrolDatabaseHelper extends SQLiteOpenHelper{

    public static SQLiteDatabase database;
    private static final String DATABASE_NAME="ncontrol.db";
    private static final int DATABASE_VERSION=1;

  public ncontrolDatabaseHelper (Context context){

      super(context,DATABASE_NAME,null,DATABASE_VERSION);

  }

    @Override
    public void onCreate(SQLiteDatabase database) {

        ncontrolUsrMngmntTable.onCreate(database);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldversion, int newversion) {

         ncontrolUsrMngmntTable.onUpgrade(database,oldversion,newversion);
    }
}
