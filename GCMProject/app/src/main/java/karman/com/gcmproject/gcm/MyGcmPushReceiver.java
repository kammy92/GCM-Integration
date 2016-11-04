package karman.com.gcmproject.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import karman.com.gcmproject.activity.MainActivity;


public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName ();

    private NotificationUtils notificationUtils;

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */
/*

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        if (intent.getAction ().equals (Constants.ACTION.START_NOTIFICATION)) {
            showNotification ();
        } else if (intent.getAction ().equals (Constants.ACTION.HAPPY)) {
            Toast.makeText (this, "Thank you for submitting your response", Toast.LENGTH_SHORT).show ();
            Log.i (LOG_TAG, "Happy");
            new SubmitResponse (this).execute ("1", "1", "");
            stopForeground (true);
            stopSelf ();
        } else if (intent.getAction ().equals (Constants.ACTION.NEUTRAL)) {
            Toast.makeText (this, "Thank you for submitting your response", Toast.LENGTH_SHORT).show ();
            Log.i (LOG_TAG, "Neutral");
            new SubmitResponse (this).execute ("1", "2", "");
            stopForeground (true);
            stopSelf ();
        } else if (intent.getAction ().equals (Constants.ACTION.SAD)) {
            Toast.makeText (this, "Thank you for submitting your response", Toast.LENGTH_SHORT).show ();
            Log.i (LOG_TAG, "Sad");
            new SubmitResponse (this).execute ("1", "3", "");
            stopForeground (true);
            stopSelf ();
        } else if (intent.getAction ().equals (
                Constants.ACTION.CANCEL_NOTIFICATION)) {
            Log.i (LOG_TAG, "Received Stop Foreground Intent");
//            Toast.makeText (this, "Service Stopped", Toast.LENGTH_SHORT).show ();
            stopForeground (true);
            stopSelf ();
        }
        return START_STICKY;
    }

*/
    @Override
    public void onMessageReceived (String from, Bundle bundle) {
        Log.e (TAG, "Bundle: " + bundle);
        String title = bundle.getString ("title");
        String message = bundle.getString ("message");
        String image = bundle.getString ("image");
        String timestamp = bundle.getString ("created_at");
        Log.e (TAG, "From: " + from);
        Log.e (TAG, "Title: " + title);
        Log.e (TAG, "message: " + message);
        Log.e (TAG, "image: " + image);
        Log.e (TAG, "timestamp: " + timestamp);

        if (! NotificationUtils.isAppIsInBackground (getApplicationContext ())) {

            Intent resultIntent = new Intent (getApplicationContext (), MainActivity.class);
            resultIntent.putExtra ("message", message);

            if (TextUtils.isEmpty (image)) {
                showNotificationMessage (getApplicationContext (), title, message, timestamp, resultIntent);
            } else {
                showNotificationMessageWithBigImage (getApplicationContext (), title, message, timestamp, resultIntent, image);
            }
/*
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent (Config.PUSH_NOTIFICATION);
            pushNotification.putExtra ("message", message);
            LocalBroadcastManager.getInstance (this).sendBroadcast (pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils ();
            notificationUtils.playNotificationSound ();

  */
        } else {

            Intent resultIntent = new Intent (getApplicationContext (), MainActivity.class);
            resultIntent.putExtra ("message", message);

            if (TextUtils.isEmpty (image)) {
                showNotificationMessage (getApplicationContext (), title, message, timestamp, resultIntent);
            } else {
                showNotificationMessageWithBigImage (getApplicationContext (), title, message, timestamp, resultIntent, image);
            }
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage (Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils (context);
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage (title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage (Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils (context);
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage (title, message, timeStamp, intent, imageUrl);
    }
}