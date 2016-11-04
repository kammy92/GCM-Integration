package karman.com.gcmproject.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import karman.com.gcmproject.R;
import karman.com.gcmproject.app.Config;
import karman.com.gcmproject.gcm.GcmIntentService;
import karman.com.gcmproject.helper.UserDetailsPrefData;
import karman.com.gcmproject.util.Constants;
import karman.com.gcmproject.util.Utils;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName ();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String PACKAGE_NAME;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext ().getPackageName ();

        UserDetailsPrefData userDetailsPrefData = UserDetailsPrefData.getInstance ();
        Constants.name = userDetailsPrefData.getStringPref (this, UserDetailsPrefData.USER_NAME);
        Constants.mobile = userDetailsPrefData.getStringPref (this, UserDetailsPrefData.USER_MOBILE);
        Constants.regid = userDetailsPrefData.getStringPref (this, UserDetailsPrefData.REGISTRATION_ID);
        Constants.app_version = userDetailsPrefData.getStringPref (this, UserDetailsPrefData.APP_VERSION);

//        Toast.makeText (this, "name :" + Constants.name, Toast.LENGTH_SHORT).show ();
//        Toast.makeText (this, "mobile :" + Constants.mobile, Toast.LENGTH_SHORT).show ();
//        Toast.makeText (this, "regid :" + Constants.regid, Toast.LENGTH_SHORT).show ();
//        Toast.makeText (this, "app version :" + Constants.app_version, Toast.LENGTH_SHORT).show ();

        if (Constants.app_version.isEmpty ())
            Constants.app_version = "0";

        int currentVersion = Utils.getAppVersion (this);
        if (Integer.parseInt (Constants.app_version) != currentVersion) {
//            Constants.regid = "";
//            new RegisterToGCM (context, 1).execute ();
            Log.d ("TAG", "App version changed.");
        }

        if (Constants.name.isEmpty () || Constants.mobile.isEmpty ()) {
            Log.d ("TAG", "Registration not found.");
            Intent intent = new Intent (this, LoginActivity.class);
            intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity (intent);
        }



        mRegistrationBroadcastReceiver = new BroadcastReceiver () {
            @Override
            public void onReceive (Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction ().equals (Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra ("token");

//                    Toast.makeText (getApplicationContext (), "GCM registration token: " + token, Toast.LENGTH_LONG).show ();

                } else if (intent.getAction ().equals (Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL

//                    Toast.makeText (getApplicationContext (), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show ();

                } else if (intent.getAction ().equals (Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    Toast.makeText (getApplicationContext (), "Push notification is received!", Toast.LENGTH_LONG).show ();
                }
            }
        };

        if (checkPlayServices ()) {
            registerGCM ();
        }
    }

    // starting the service to register with GCM
    private void registerGCM () {
        Intent intent = new Intent (this, GcmIntentService.class);
        intent.putExtra ("key", "register");
        startService (intent);
    }

    private boolean checkPlayServices () {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance ();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable (this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError (resultCode)) {
                apiAvailability.getErrorDialog (this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show ();
            } else {
                Log.i (TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText (getApplicationContext (), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show ();
                finish ();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume () {
        super.onResume ();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance (this).registerReceiver (mRegistrationBroadcastReceiver,
                new IntentFilter (Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance (this).registerReceiver (mRegistrationBroadcastReceiver,
                new IntentFilter (Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause () {
        LocalBroadcastManager.getInstance (this).unregisterReceiver (mRegistrationBroadcastReceiver);
        super.onPause ();
    }
}