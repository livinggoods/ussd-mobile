package com.gadiness.kimaru.ussdhelper.manenos;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.gadiness.kimaru.ussdhelper.activity.MainActivity;
import com.gadiness.kimaru.ussdhelper.data.UssdDbHelper;
import com.gadiness.kimaru.ussdhelper.mzigos.PhoneQueue;
import com.gadiness.kimaru.ussdhelper.mzigos.UssdMessage;
import com.gadiness.kimaru.ussdhelper.other.WriteToLog;

import java.util.List;

/**
 * Created by kimaru on 10/17/17.
 */


public class UssdService extends AccessibilityService {
    public static String TAG = "LG USSD";
    String nextPhoneNumber;
    PhoneQueue phoneQueue;
    WriteToLog writeToLog = new MainActivity().myLog;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){
        Log.d(TAG, "onAccessibilityEvent");
        String text = event.getText().toString();
        if (event.getClassName().equals("android.app.AlertDialog")){
            if (text.contains("balance for")){
                //save the message
                saveUssdMessage(text, phoneQueue);
                writeToLog.log(text);
                phoneQueue.setStatus(1);
                new UssdDbHelper(this).updatePhoneQueue(phoneQueue);
                //update the phone number so that we do not pick it again.
            }
            Log.d(TAG, text);
        }
        Log.d(TAG, "Setting the Input");
        AccessibilityNodeInfo source = event.getSource();
        if (source != null) {
            try{
                //capture the EditText simply by using FOCUS_INPUT (since the EditText has the focus), you can probably find it with the viewId input_field
                AccessibilityNodeInfo inputNode = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
                if (inputNode != null) { //prepare you text then fill it using ACTION_SET_TEXT
                    Bundle arguments = new Bundle();
                    // set the NextNumber
                    phoneQueue = new UssdDbHelper(this).getNextPhone();
                    if (phoneQueue.getPhoneNumber() != null) {
                        nextPhoneNumber = phoneQueue.getPhoneNumber();
                        Log.d(TAG, "-------------------------------------------------");
                        Log.d(TAG, nextPhoneNumber);
                        writeToLog.log("Checking balance (Running USSD) for " + nextPhoneNumber);
                        Log.d(TAG, "-------------------------------------------------");
                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, nextPhoneNumber);
                        inputNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    }
                }
                //"Click" the Send button
                List<AccessibilityNodeInfo> list = source.findAccessibilityNodeInfosByText("Send");
                for (AccessibilityNodeInfo node : list) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }catch (Exception e){}
        }

    }

    @Override
    public void onInterrupt(){}

    @Override
    protected void onServiceConnected(){
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    public void saveUssdMessage(String ussdMessage, PhoneQueue phoneQueue){
        String bundleBalance;
        // split by space
        UssdMessage message = new UssdMessage();
        message.setSynced(false);
        message.setDeleted(false);
        message.setActive(true);
        message.setMessage(ussdMessage);
        message.setPhoneNumber(phoneQueue.getPhoneNumber());
        message.setQueueId(phoneQueue.getQueueId());
        message.setPhoneId(phoneQueue.getPhoneId());
        Log.d("PHONEQUEUE", "===============/////////////////////============");
        Log.d("PHONEQUEUE", String.valueOf(phoneQueue.getQueueId())+" "+ phoneQueue.getPhoneNumber());
        Log.d("PHONEQUEUE", "===============/////////////////////============");
        UssdDbHelper ussdDbHelper = new UssdDbHelper(this);
        ussdDbHelper.addUssdMessage(message);
        ussdDbHelper.deletePhoneQueue(phoneQueue);

    }

}
