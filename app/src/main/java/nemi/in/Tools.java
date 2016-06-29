package nemi.in;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;

public class Tools {
    public final static String LOG_TAG = "gtalksms";
    public final static String APP_NAME = "GTalkSMS";
    public final static String LineSep = System.getProperty("line.separator");
    
    
    
    public static String getVersionName(Context context, Class<?> cls) {

        try {
            ComponentName comp = new ComponentName(context, cls);
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);

            return "v" + pinfo.versionName + " @ Yakoo";
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return "";
        }
    }
    
    public static String getVersion(Context context, Class<?> cls) {

        try {
            ComponentName comp = new ComponentName(context, cls);
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);

            return pinfo.versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return "";
        }
    }
    
    public static String getVersionCode(Context context, Class<?> cls) {

        try {
            ComponentName comp = new ComponentName(context, cls);
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);

            return "" + pinfo.versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return "";
        }
    }
    
    public static <T> List<T> getLastElements(ArrayList<T> list, int nbElems) {
        return list.subList(Math.max(list.size() - nbElems, 0), list.size());
    }
    
    public static Long getLong(Cursor c, String col) {
        return c.getLong(c.getColumnIndex(col));
    }
    
    public static int getInt(Cursor c, String col) {
        return c.getInt(c.getColumnIndex(col));
    }

    public static String getString(Cursor c, String col) {
        return c.getString(c.getColumnIndex(col));
    }

    public static boolean getBoolean(Cursor c, String col) {
        return getInt(c, col) == 1;
    }

    public static Date getDateSeconds(Cursor c, String col) {
        return new Date(Long.parseLong(Tools.getString(c, col)) * 1000);
    }

    public static Date getDateMilliSeconds(Cursor c, String col) {
        return new Date(Long.parseLong(Tools.getString(c, col)));
    }
    
    
    public static Integer parseInt(String value) {
        Integer res = null;
        try { 
            res = Integer.parseInt(value); 
        } catch(Exception e) {}
        
        return res;
    }
    
    public static int getMinNonNeg(int... x) {
        int min = Integer.MAX_VALUE;
        for(int i : x) {
            if(i >= 0 && i < min)
                min = i;
        }
        return min;
    }
}
