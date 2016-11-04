package karman.com.gcmproject.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Admin on 21-12-2015.
 */
public class Utils {

    public static int getAppVersion (Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager ().getPackageInfo (context.getPackageName (), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException ("Could not get package name: " + e);
        }
    }


}
