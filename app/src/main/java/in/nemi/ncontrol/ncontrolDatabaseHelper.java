package in.nemi.ncontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by Developer on 21-04-2016.
 */
public class ncontrolDatabaseHelper extends SQLiteOpenHelper{

    private static final string DATABASE_NAME="ncontrol.db"
    private static final int DATABASE_VERSION="1"

  public ncontrolDatabaseHelper (Context context){

      super(context,DATABASE_NAME,null,DATABASE_VERSION);

  }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
