package com.gadiness.kimaru.ussdhelper.other;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.gadiness.kimaru.ussdhelper.activity.MainActivity;
import com.gadiness.kimaru.ussdhelper.data.UssdDbHelper;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.body.JSONObjectBody;

/**
 * Created by kimaru on 10/13/17.
 */

public class ApiSyncHelper {
    /**
     * helper class to open a URL and read the contents of the
     *
     */

    WriteToLog Log = new MainActivity().myLog;
    static String stream = null;
    public static String TAG = " APISYNCHELPER ";
    Context context;

    public ApiSyncHelper(Context context){this.context = context;}

    public void schedulePhoneQueueTask(){
        final Handler handler = new Handler(Looper.getMainLooper());
        Timer timer = new Timer();
        TimerTask getPhoneTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppPreferences appPreferences = new AppPreferences(context);
                        StringBuilder url = new StringBuilder();
                        url.append(appPreferences.getApiServer());
                        android.util.Log.d(TAG, appPreferences.getApiServer());
                        android.util.Log.d(TAG, "=========================");
                        url.append("/api/");
                        url.append(appPreferences.getApiVersion());
                        url.append("/");
                        url.append(appPreferences.getPhoneQueueEndpoint());
                        Log.log(url.toString());
                        android.util.Log.d(TAG, url.toString());
                        new getPhoneQueueFromApi().execute(url.toString());
                    }
                });
            }
        };
        timer.schedule(getPhoneTask, 0, 60*1000 * 3); //every 3 minutes
    }

    public void scheduleUssdUploadTask(){
        final Handler handler = new Handler(Looper.getMainLooper());
        Timer timer = new Timer();
        TimerTask uploadTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = new AppPreferences(context).getUssdEndpoint() ;
                        new postUssdMessagesToApi().execute(url);
                    }
                });
            }
        };
        timer.schedule(uploadTask, 0, 60*1000 * 10); //every 10 minutes
    }

    public String getUrlData (String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == 200){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();
                urlConnection.disconnect();
            }else{
                Log.log(TAG+" STATUS IS NOT 200 ");
            }
        } catch (MalformedURLException e){
            Log.log(TAG+" MALFORMED URL ");
            Log.log(TAG+e.getMessage());
        }catch(IOException e){
            Log.log(TAG+" IO EXCEPTION");
            Log.log(TAG+e.getMessage());
        }
        return stream;
    }

    /**
     *
     * @param json: Json object to send to server, this is the body of the POST message
     * @param JsonRoot: The root string of the JSON
     * @param url: The endpoint for the API, the full URL including the http(s):// part
     * @return: String Response from the server
     * @throws Exception: Exception if error occurs
     */
    private String postJsonToUrl(JSONObject json, String JsonRoot, String url) throws Exception {
        //  get the server URL
        AsyncHttpPost p = new AsyncHttpPost(url);
        p.setBody(new JSONObjectBody(json));
        JSONObject ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get();
        return ret.getString(JsonRoot);
    }

    private class getPhoneQueueFromApi extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            stream = getUrlData(urlString);
            if (stream != null){
                try{
                    JSONObject reader = new JSONObject(stream);
                    Log.log(TAG + " getPhoneQueueFromApi() - Creating a phone queue record");
                    android.util.Log.d(TAG, " getPhoneQueueFromApi() - Creating a phone queue record");
                    JSONArray recs = reader.getJSONArray(new AppPreferences(context).getPhoneJsonRoot());
                    for (int x = 0; x < recs.length(); x++){
                        Log.log(TAG + " getPhoneQueueFromApi() - Creating a phone queue record");
                        android.util.Log.d(TAG, " getPhoneQueueFromApi() - Creating a phone queue record");
                        new UssdDbHelper(context).phoneFromJson(recs.getJSONObject(x));
                    }
                } catch (JSONException e){
                    Log.log(TAG + " ERROR getPhoneQueueFromApi() - "+ e.getMessage());
                    android.util.Log.d(TAG,  " ERROR getPhoneQueueFromApi() - "+ e.getMessage());
                }
            }else{
                Log.log(TAG + " ERROR getPhoneQueueFromApi() - No Stream data received");
                android.util.Log.d(TAG,  " ERROR getPhoneQueueFromApi() - No Stream data received");
            }
            return  stream;
        }
    }

    private class postUssdMessagesToApi extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String postResults;
            try {
                postResults = postJsonToUrl(
                        new UssdDbHelper(context).getMessagesToSyncJson(),
                        UssdDbHelper.USSD_JSON_ROOT,
                        new AppPreferences(context).getUssdEndpoint()
                );
            } catch (Exception e){
                postResults = null;
            }
            return postResults;
        }
    }
}
