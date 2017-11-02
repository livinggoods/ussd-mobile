package com.gadiness.kimaru.ussdhelper.manenos;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.gadiness.kimaru.ussdhelper.activity.MainActivity;

import java.util.List;

/**
 * Created by kimaru on 10/17/17.
 */


public class UssdService extends AccessibilityService {
    public static String TAG = "LG USSD";
    String nextPhoneNumber;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){
        Log.d(TAG, "onAccessibilityEvent");
        String text = event.getText().toString();
        if (event.getClassName().equals("android.app.AlertDialog")){
            if (text.contains("bundle balance for")){
                //save the message
                new MainActivity().saveUssdMessage(text, nextPhoneNumber);
            }
            Log.d(TAG, text);
        }
        Log.d(TAG, "Setting the Input");
        AccessibilityNodeInfo source = event.getSource();
        if (source != null) {
            Log.d(TAG, "SOURCE IS FOUND");
            //capture the EditText simply by using FOCUS_INPUT (since the EditText has the focus), you can probably find it with the viewId input_field
            AccessibilityNodeInfo inputNode = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
            if (inputNode != null) { //prepare you text then fill it using ACTION_SET_TEXT
                Bundle arguments = new Bundle();
                // set the NextNumber
                nextPhoneNumber = new MainActivity().getNextNumber();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, nextPhoneNumber);
                inputNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            }
            //"Click" the Send button
            List<AccessibilityNodeInfo> list = source.findAccessibilityNodeInfosByText("Send");
            for (AccessibilityNodeInfo node : list) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
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

}
