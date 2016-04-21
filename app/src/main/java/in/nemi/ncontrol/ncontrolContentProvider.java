package in.nemi.ncontrol;

/**
 * Created by Developer on 21-04-2016.
 */
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.HashSet;

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

    public static final Uri CONTENT_URI=Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/todos";
    public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/UsrMngmntTable";

    private static final UriMatcher sURIMatcher =new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sURIMatcher.addURI(AUTHORITY,BASE_PATH,DATAS);
        sURIMatcher.addURI(AUTHORITY,BASE_PATH,DATA_ID);

    }


    @Override
    public boolean onCreate() {
        database =new ncontrolDatabaseHelper(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        //to check the if the requested column is existting
        checkColumns(projection);

        //setting the UsrMngmntTable
        queryBuilder.setTables( ncontrolUsrMngmntTable.TABLE_NAME);
         int uriType = sURIMatcher.match(uri);
        switch (uriType){
            case DATAS:
                break;
            case DATA_ID:
                //adding to query
                queryBuilder.appendWhere(ncontrolUsrMngmntTable.COLUMN_ID + "=" +uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("unknown URI:"+uri);
        }
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    private void checkColumns(String[] projection) {
        String[] available ={ncontrolUsrMngmntTable.COLUMN_PASSWRD,
        ncontrolUsrMngmntTable.COLUMN_ROLE,ncontrolUsrMngmntTable.COLUMN_USERNM,ncontrolUsrMngmntTable.COLUMN_ID};
        if (projection !=null){
            HashSet<String> requestedcolumns = new HashSet<>(Arrays.asList(projection));
            HashSet<String> availablecolumns = new HashSet<>(Arrays.asList(available));

            if(!availablecolumns.containsAll(requestedcolumns)){
                throw new IllegalArgumentException("unknown columns");

            }

        }


    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType=sURIMatcher.match(uri);
        SQLiteDatabase sqldb = database.getWritableDatabase();
        long id=0;
        switch (uriType){
            case DATAS:
                id=sqldb.insert(ncontrolUsrMngmntTable.TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException("unknown URI:"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return uri.parse(BASE_PATH+"/"+id);
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

