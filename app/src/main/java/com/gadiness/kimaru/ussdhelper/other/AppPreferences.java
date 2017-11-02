package com.gadiness.kimaru.ussdhelper.other;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by kimaru on 10/11/17.
 */

public class AppPreferences {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "ussdApp";
    public static final String KEY_USSD_CODE = "ussdCode";
    public static final String API_SERVER = "apiServer";
    public static final String API_VERSION = "apiVersion";
    public static final String USSD_ENDPOINT = "ussdEndpoint";
    public static final String PHONE_QUEUE_ENDPOINT = "phoneQueueEndpoint";
    public static final String PHONE_JSON_ROOT = "phoneJsonRoot";


    //constructor
    public AppPreferences (Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveUssdCode(String ussd){
        editor.putString(KEY_USSD_CODE, ussd);
        editor.commit();
    }
    /**
     *
     * Get the stored ussdCode
     *
     * */


    public String getUssdCode (){
        return  pref.getString(KEY_USSD_CODE, "");
    }

    public void saveApiServer(String apiUrl){
        editor.putString(API_SERVER, apiUrl);
        editor.commit();
    }
    /**
     *
     * Get the stored api Server
     *
     * */
    public String getApiServer (){
        return pref.getString(API_SERVER, "");
    }

    public void saveUssdEndpoint(String ussdEndpoint){
        editor.putString(USSD_ENDPOINT, ussdEndpoint);
        editor.commit();
    }

    /**
     *
     * Get the Ussd Endpoint
     *
     * */
    public String getUssdEndpoint (){
        return pref.getString(USSD_ENDPOINT, "");
    }

    public void savePhoneQueueEndpoint(String phoneQueueEndpoint){
        editor.putString(PHONE_QUEUE_ENDPOINT, phoneQueueEndpoint);
        editor.commit();
    }
    public String getPhoneQueueEndpoint (){
        return pref.getString(PHONE_QUEUE_ENDPOINT, "");
    }


    public void savePhoneJsonRoot(String phoneJsonRoot){
        editor.putString(PHONE_JSON_ROOT, phoneJsonRoot);
        editor.commit();
    }
    public String getPhoneJsonRoot (){
        return pref.getString(PHONE_JSON_ROOT, "");
    }

    public void saveApiVersion(String apiVersion){
        editor.putString(API_VERSION, apiVersion);
        editor.commit();
    }

    public String getApiVersion (){
        return pref.getString(API_VERSION, "");
    }
}
