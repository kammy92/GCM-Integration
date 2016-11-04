package karman.com.gcmproject.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ServiceHandler {
    static InputStream is = null;
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;
    public final static int DELETE = 4;
    HttpResponse httpResponse = null;
    StatusLine responseCode;

    public ServiceHandler () {
    }

    public String makeServiceCall (String url, int method) {
        return this.makeServiceCall (url, method, null);
    }

    public String makeServiceCall (String url, int method, List<NameValuePair> params) {
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient ();
            HttpEntity httpEntity = null;
            if (method == POST) {
                HttpPost httpPost = new HttpPost (url);
                if (params != null) {
                    httpPost.setEntity (new UrlEncodedFormEntity (params));
                }
                httpResponse = httpClient.execute (httpPost);
            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils.format (params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet (url);
                httpResponse = httpClient.execute (httpGet);
            } else if (method == PUT) {
                HttpPut httpPut = new HttpPut (url);
                if (params != null) {
                    httpPut.setEntity (new UrlEncodedFormEntity (params));
                }
                httpResponse = httpClient.execute (httpPut);
            } else if (method == DELETE) {
                HttpDelete httpDelete = new HttpDelete (url);
                httpResponse = httpClient.execute (httpDelete);
            }
            httpEntity = httpResponse.getEntity ();
            responseCode = httpResponse.getStatusLine ();
            is = httpEntity.getContent ();
        } catch (UnsupportedEncodingException e) {
            Log.d ("Error Message", e.getMessage ());
            e.printStackTrace ();
        } catch (ClientProtocolException e) {
            Log.d ("Error Message", e.getMessage ());
            e.printStackTrace ();
        } catch (IOException e) {
            Log.d ("Error Message", e.getMessage ());
            e.printStackTrace ();
        }
        try {
            BufferedReader reader = new BufferedReader (new InputStreamReader (is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder ();
            String line = null;
            while ((line = reader.readLine ()) != null) {
                sb.append (line + "\n");
            }
            is.close ();
            response = sb.toString ();
        } catch (Exception e) {
            Log.e ("Buffer Error", "Error: " + e.toString ());
            Log.d ("Error Message", e.getMessage ());
        }
        return response;
    }
}