package com.gadiness.kimaru.ussdhelper.manenos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by kimaru on 12/5/17.
 */

public class IncomingSms extends BroadcastReceiver{
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                }
            } // bunlde is nulll
        } catch (Exception e) {
            Log.d("LG - ERROR ", e.getMessage());
        }
    }

}
