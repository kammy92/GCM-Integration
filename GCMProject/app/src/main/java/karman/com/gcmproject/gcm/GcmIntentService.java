package karman.com.gcmproject.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import karman.com.gcmproject.R;
import karman.com.gcmproject.app.Config;
import karman.com.gcmproject.helper.UserDetailsPrefData;
import karman.com.gcmproject.util.Constants;
import karman.com.gcmproject.util.Utils;


public class GcmIntentService extends IntentService {

    private static final String TAG = GcmIntentService.class.getSimpleName ();

    public GcmIntentService () {
        super (TAG);
    }

    public static final String KEY = "key";


    @Override
    protected void onHandleIntent (Intent intent) {
        String key = intent.getStringExtra (KEY);
        switch (key) {
            default:
                // if key is not specified, register with GCM
                registerGCM ();
        }



    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM () {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences (this);
        String token = null;
        int appVersion = Utils.getAppVersion (this);
        try {
            InstanceID instanceID = InstanceID.getInstance (this);
            token = instanceID.getToken (getString (R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e (TAG, "GCM Registration Token: " + token);

            // sending the registration id to our server
            //sendRegistrationToServer (token);
            Constants.regid = token;
            UserDetailsPrefData userDetailsPrefData = UserDetailsPrefData.getInstance ();
            userDetailsPrefData.putStringPref (this, Constants.REGISTRATION_ID, token);
            userDetailsPrefData.putStringPref (this, Constants.APP_VERSION, String.valueOf (appVersion));

            sharedPreferences.edit ().putBoolean (Config.SENT_TOKEN_TO_SERVER, true).apply ();
        } catch (Exception e) {
            Log.e (TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit ().putBoolean (Config.SENT_TOKEN_TO_SERVER, false).apply ();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent (Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra ("token", token);
        LocalBroadcastManager.getInstance (this).sendBroadcast (registrationComplete);
    }
 }