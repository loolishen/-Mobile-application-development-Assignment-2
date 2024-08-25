package com.fit2081.assignment_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
    private static IMessage messageListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        Log.d("Assignment_1_TAG","OnRecieve: ");

        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String senderNum = currentMessage.getDisplayOriginatingAddress();
            String message = currentMessage.getDisplayMessageBody();
            Log.d("Assignment_1_TAG","OnRecieve: "+message);

            if (messageListener != null) {
                messageListener.messageReceived(message);
            }
        }
    }

    public static void bindListener(IMessage listener){
        messageListener=listener;
    }
}
