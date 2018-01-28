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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gadiness.kimaru.ussdhelper.activity.MainActivity;
import com.gadiness.kimaru.ussdhelper.data.UssdDbHelper;
import com.gadiness.kimaru.ussdhelper.mzigos.Queue;
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
                        android.util.Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        android.util.Log.d(TAG, url.toString());
                        android.util.Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        // since the ques can be more than one, let us retrieve them
                        List<Queue> selectedQueues = new UssdDbHelper(context).getSelectedQueues();
                        for (Queue q: selectedQueues){
                            String validUrl =url.toString()+ "/"+String.valueOf(q.getId());
                            android.util.Log.d("PHONES", "API URL IS - "+url.toString());
                            new getPhoneQueueFromApi().execute(validUrl);
                        }
                    }
                });
            }
        };
        timer.schedule(getPhoneTask, 0, 60*1000 * 3); //every 3 minutes
    }

    public void scheduleUssdUploadTask(){
        Log.log("scheduleUssdUploadTask()  - scheduleUssdUploadTask - Starting Uploading Messages to the Cloud");
        final Handler handler = new Handler(Looper.getMainLooper());
        Timer timer = new Timer();
        TimerTask uploadTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = new AppPreferences(context).getUssdEndpoint() ;
                        android.util.Log.d("USSD", "Task ");
                        new postUssdMessagesToApi().execute(url);
                    }
                });
            }
        };
        timer.schedule(uploadTask, 0, 60*1000 * 3); //every 3 minutes
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
                Log.log(TAG+stream);
                android.util.Log.d("getUrlData", stream);
                urlConnection.disconnect();
            }else{
                android.util.Log.d("getUrlData", "Status not 200");
                Log.log(TAG+" STATUS IS NOT 200 ");
            }
        } catch (MalformedURLException e){
            android.util.Log.d("getUrlData", "Malformed URL");
            Log.log(TAG+" MALFORMED URL ");
            Log.log(TAG+e.getMessage());
        }catch(IOException e){
            android.util.Log.d("getUrlData", "Exception");
            Log.log(TAG+" IO EXCEPTION");
            Log.log(TAG+e.getMessage());
        }
        return stream;
    }

    private class UpdateCloudSelectedQueue extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings){
            String postResults = null;
            try {
                StringBuilder urlString = new StringBuilder();
                AppPreferences appPreferences = new AppPreferences(context);
                urlString.append(appPreferences.getApiServer());
                urlString.append("/api/");
                urlString.append(appPreferences.getApiVersion());
                urlString.append("/");
                urlString.append("selectedqueues");
                UssdDbHelper ussdDbHelper = new UssdDbHelper(context);
                postResults = postJsonToUrl(
                        ussdDbHelper.getSelectedQueuesAsJson(),
                        UssdDbHelper.UPSTREAM_QUEUE_JSON_ROOT,
                        urlString.toString()
                );
                Log.log(TAG +"  -  "+ postResults);
            } catch (MalformedURLException e){
                android.util.Log.d("USSD", "MALFORMED URL "+e.getMessage());
                Log.log("USSD"+" MALFORMED URL ");
                Log.log("USSD"+e.getMessage());
            }catch(IOException e){
                android.util.Log.d("USSD", " IO EXCEPTION "+e.getMessage());
                Log.log(TAG+" IO EXCEPTION");
                Log.log(TAG+e.getMessage());
            }catch (Exception e) {
                android.util.Log.d("USSD", "EXCEPTION "+e.getMessage());
                postResults = null;
                android.util.Log.d("LGUSD", e.getMessage());
            }
            return postResults;
        }
    }

    public void notifyCloudSelectedQueues(){
        Log.log("scheduleUssdUploadTask()  - scheduleUssdUploadTask - Starting Uploading Messages to the Cloud");
        StringBuilder urlString = new StringBuilder();
        AppPreferences appPreferences = new AppPreferences(context);
        urlString.append(appPreferences.getApiServer());
        urlString.append("/api/");
        urlString.append(appPreferences.getApiVersion());
        urlString.append("/");
        urlString.append("selectedqueues");
        new UpdateCloudSelectedQueue().execute(urlString.toString());
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
        JSONObject ret;
        ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get();
        return ret.getString(JsonRoot);
    }

    private class getPhoneQueueFromApi extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            JSONArray savedNumbers = new JSONArray();
            JSONObject apiData = new JSONObject();
            Long id;
            stream = getUrlData(urlString);
            if (stream != null){
                try{
                    JSONObject reader = new JSONObject(stream);
                    Log.log(TAG + " getPhoneQueueFromApi() - Starting to create a phone queue record");
                    android.util.Log.d(TAG, " getPhoneQueueFromApi() - Starting to create a phone queue record");
                    JSONArray recs = reader.getJSONArray(new AppPreferences(context).getPhoneJsonRoot());
                    for (int x = 0; x < recs.length(); x++){
                        Log.log(TAG + " getPhoneQueueFromApi() - Creating a phone queue record");
                        android.util.Log.d(TAG, " getPhoneQueueFromApi() - Creating a phone queue record");
                        id = new UssdDbHelper(context).phoneFromJson(recs.getJSONObject(x));
                        if(id != null){
                            JSONObject saved = new JSONObject();
                            saved.put("id", id);
                            savedNumbers.put(saved);
                        }
                    }
                    //now that I have the items, let me post them back
                    try{
                        apiData.put("phones", savedNumbers);
                        ///
                        AppPreferences appPreferences = new AppPreferences(context);
                        StringBuilder ackUrl = new StringBuilder();
                        ackUrl.append(appPreferences.getApiServer());
                        ackUrl.append("/api/");
                        ackUrl.append(appPreferences.getApiVersion());
                        ackUrl.append("/");
                        ackUrl.append("phone-queue/received");
                        postJsonToUrl(apiData, "status", ackUrl.toString());
                    }catch (Exception e){
                        //resend this later
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
        @Override
        protected String doInBackground(String... strings){
            android.util.Log.d("USSD", " Async Task");
            String postResults = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                android.util.Log.d("USSD", "--=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-==--=-==-=-=-=-=-=-=-=-=-");
                android.util.Log.d("USSD", new UssdDbHelper(context).getMessagesToSyncJson().toString());
                android.util.Log.d("USSD", "--=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-==--=-==-=-=-=-=-=-=-=-=-");



                StringBuilder urlString = new StringBuilder();
                AppPreferences appPreferences = new AppPreferences(context);


                urlString.append(appPreferences.getApiServer());
                urlString.append("/api/");
                urlString.append(appPreferences.getApiVersion());
                urlString.append("/");
                urlString.append(appPreferences.getUssdEndpoint());
                UssdDbHelper ussdDbHelper = new UssdDbHelper(context);
                Log.log ("--=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-==--=-==-=-=-=-=-=-=-=-=-");
                Log.log(ussdDbHelper.getMessagesToSyncJson().toString());
                Log.log("--=-=-=-=-=-=-=-=-=-==-=-=-=-=-=-=-==--=-==-=-=-=-=-=-=-=-=-");
                postResults = postJsonToUrl(
                        ussdDbHelper.getMessagesToSyncJson(),
                        UssdDbHelper.USSD_JSON_ROOT,
                        urlString.toString()
                );
                Log.log(TAG +"  -  "+ postResults);
                // process the messages
                if (postResults != null){
                    try{
                        JSONObject results = new JSONObject(postResults);
                        JSONArray recs = results.getJSONArray(new AppPreferences(context).getPhoneJsonRoot());
                        for (int x = 0; x < recs.length(); x++){
                            // since the message has been uploaded, let us delete it
                            ussdDbHelper.deleteUssdMessage(ussdDbHelper
                                    .ussdMessageFromJson(recs.getJSONObject(x)));
                        }
                    } catch (JSONException e){
                        Log.log(TAG + " ERROR getPhoneQueueFromApi() - "+ e.getMessage());
                        android.util.Log.d(TAG,  " ERROR getPhoneQueueFromApi() - "+ e.getMessage());
                    }
                }else{
                    Log.log(TAG + " ERROR getPhoneQueueFromApi() - No Stream data received");
                    android.util.Log.d(TAG,  " ERROR getPhoneQueueFromApi() - No Stream data received");
                }



            } catch (MalformedURLException e){
                android.util.Log.d("USSD", "MALFORMED URL "+e.getMessage());
                Log.log("USSD"+" MALFORMED URL ");
                Log.log("USSD"+e.getMessage());
            }catch(IOException e){
                android.util.Log.d("USSD", " IO EXCEPTION "+e.getMessage());
                Log.log(TAG+" IO EXCEPTION");
                Log.log(TAG+e.getMessage());
            }catch (Exception e) {
                android.util.Log.d("USSD", "EXCEPTION "+e.getMessage());
                postResults = null;
                android.util.Log.d("LGUSD", e.getMessage());
            }
            return postResults;
        }
    }

    // GET THE QUEUES
    public void getQueuesTask(){
        final Handler handler = new Handler(Looper.getMainLooper());
        Timer timer = new Timer();
        TimerTask getQueueTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppPreferences appPreferences = new AppPreferences(context);
                        StringBuilder url = new StringBuilder();
                        url.append(appPreferences.getApiServer());
                        url.append("/api/");
                        url.append(appPreferences.getApiVersion());
                        url.append("/");
                        url.append(appPreferences.getQueueEndPoint());
                        Log.log(url.toString());
                        new getQueuesFromApi().execute(url.toString());
                    }
                });
            }
        };
        timer.schedule(getQueueTask, 0, 60*1000 / 3); //every 20 seconds
    }

    private class getQueuesFromApi extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            android.util.Log.d("getQueuesFromApi", "pppppppppppppppppppppppppppppppppppppppp");
            android.util.Log.d("getQueuesFromApiUrl", urlString);
            android.util.Log.d("getQueuesFromApi", "pppppppppppppppppppppppppppppppppppppppp");
            stream = getUrlData(urlString);
            if (stream != null){
                android.util.Log.d("getQueuesFromApi", "trying to sync the data");
                android.util.Log.d("getQueuesFromApi", "**************************");
                try{
                    JSONObject reader = new JSONObject(stream);
                    Log.log(TAG + " getQueuesFromApi() - Starting to create a queues record");
                    JSONArray recs = reader.getJSONArray(new AppPreferences(context).getQueueJsonRoot());
                    for (int x = 0; x < recs.length(); x++){
                        android.util.Log.d(TAG, " getQueuesFromApi() - Creating a phone queue record");
                        new UssdDbHelper(context).queueFromJson(recs.getJSONObject(x));
                    }
                } catch (JSONException e){
                    Log.log("DATA" + " ERROR getQueuesFromApi() - "+ e.getMessage());
                    android.util.Log.d("DATAERR",  "==+++++++++++===========+++++++++++++====+++++++======+++==");
                    android.util.Log.d("DATAERR",  " ERROR getQueuesFromApi() - "+ e.getMessage());
                    android.util.Log.d("DATAERR",  "==+++++++++++===========+++++++++++++====+++++++======+++==");
                }
            }else{
                android.util.Log.d("getQueuesFromApi", "no stream data");
                android.util.Log.d("getQueuesFromApi", "**************************");
            }
            return  stream;
        }
    }
}
