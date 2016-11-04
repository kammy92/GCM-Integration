package karman.com.gcmproject.async;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import karman.com.gcmproject.activity.MainActivity;
import karman.com.gcmproject.helper.UserDetailsPrefData;
import karman.com.gcmproject.util.Constants;
import karman.com.gcmproject.util.ServiceHandler;

public class Register extends AsyncTask<String, Void, Void> {

    String result;
    boolean error;

    Activity mActivity;
    public Register (Activity activity) {
        mActivity = activity;
    }

    @Override
    protected void onPreExecute () {
        super.onPreExecute ();
    }

    @Override
    protected Void doInBackground (String... arg) {
        Constants.name = arg[0];
        Constants.mobile = arg[1];
        Log.d ("name", Constants.name);
        Log.d ("mobile", Constants.mobile);
        Log.d ("gcm_reg_id", Constants.regid);
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        params.add (new BasicNameValuePair ("name", Constants.name));
        params.add (new BasicNameValuePair ("mobile", Constants.mobile));
        params.add (new BasicNameValuePair ("gcm_reg_id", Constants.regid));
        ServiceHandler serviceClient = new ServiceHandler ();
        result = serviceClient.makeServiceCall (Constants.register_url, ServiceHandler.POST, params);
        Log.d ("URL", Constants.register_url);
        Log.d ("SERVER_RESPONSE", result);
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject (result);
                error = jsonObject.getBoolean ("error");
            } catch (Exception e) {
                e.printStackTrace ();
            }
        } else {
            Log.e ("SERVER_RESPONSE", "DIDNT_RECEIVE_ANY_DATA_FROM_SERVER");
        }
        return null;
    }

    @Override
    protected void onPostExecute (Void result2) {
        super.onPostExecute (result2);
        if (error==false) {
            UserDetailsPrefData userDetailsPrefData = UserDetailsPrefData.getInstance ();
            userDetailsPrefData.putStringPref (mActivity, Constants.USER_NAME, Constants.name);
            userDetailsPrefData.putStringPref (mActivity, Constants.USER_MOBILE, Constants.mobile);

//                storeUserDetails (context);
            Intent intent = new Intent (mActivity, MainActivity.class);
            intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mActivity.startActivity (intent);
        } else {
            Toast.makeText (mActivity, "Try Again" + result, Toast.LENGTH_LONG).show ();
        }
    }
}

