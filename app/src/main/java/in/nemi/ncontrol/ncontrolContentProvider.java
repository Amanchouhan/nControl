package in.nemi.ncontrol;

/**
 * Created by Developer on 21-04-2016.
 */
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import in.nemi.ncontrol.ncontrolDatabaseHelper;

/**
 * Created by Developer on 21-04-2016.
 */
public class ncontrolContentProvider extends ContentProvider{

    //database
    private ncontrolDatabaseHelper database;


    private static final int DATAS=10;
    private static final int DATA_ID=20;

    private static final String AUTHORITY="in.nemi.ncontrol.provider";
    private static final String BASE_PATH="todos";
    public static final Uri CONTENT_URI=Uri.parse("content")



    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}

