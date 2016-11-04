package karman.com.gcmproject.util;

/**
 * Created by Admin on 21-12-2015.
 */
public class Constants {

    public static final String REGISTRATION_ID = "registration_id";
    public static final String APP_VERSION = "app_version";
    public static final String USER_MOBILE = "user_mobile";
    public static final String USER_NAME = "user_name";


    public static String name = "";
    public static String mobile = "";
    public static String regid = "";
    public static String app_version = "0";

    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

//    public static String base_url = "http://10.0.3.2/gcm_project/v1/";
    public static String base_url = "http://actiknow-demo.com/karman/gcm_project/v1/";
 //   public static String base_url = "http://callsikandar.com/callsikandar1/gcm_demo/";

    public final static String register_url = base_url + "user/login";


    public interface ACTION {
        public static String HAPPY = "happy";
        public static String NEUTRAL = "neutral";
        public static String SAD = "sad";
        public static String CANCEL_NOTIFICATION = "cancel_notification";
        public static String START_NOTIFICATION = "start_notification";

        public static String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

}