package karman.com.gcmproject.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDetailsPrefData {

    private String USER_DETAILS = "USER_DETAILS";

    public static final String REGISTRATION_ID = "registration_id";
    public static final String APP_VERSION = "app_version";
    public static final String USER_MOBILE = "user_mobile";
    public static final String USER_NAME = "user_name";

    private String userDetails[] = {REGISTRATION_ID, APP_VERSION, USER_MOBILE, USER_NAME};
    private static UserDetailsPrefData userDetailsPrefData;

    public  static UserDetailsPrefData getInstance()
    {
        if(userDetailsPrefData == null)
            userDetailsPrefData = new UserDetailsPrefData ();
        return userDetailsPrefData;
    }

    private SharedPreferences getPref(Context context)
    {
        return context.getSharedPreferences(USER_DETAILS, Context.MODE_PRIVATE);
    }

    public String getStringPref(Context context, String key)
    {
        return getPref(context).getString(key, "");
    }

    public int getIntPref(Context context, String key)
    {
        return getPref(context).getInt(key, 0);
    }

    public void putStringPref(Context context, String key, String value)
    {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putString (key, value);
        editor.apply ();
    }

    public void putIntPref(Context context, String key, int value)
    {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putInt (key, value);
        editor.apply ();
    }

    public void putUserDetailsPref(Context context,int value[])
    {
        SharedPreferences.Editor editor = getPref(context).edit();
        for (int i=0; i< userDetails.length;i++)
            editor.putInt(userDetails[i], value[i]);
        editor.apply();
    }
}
