package com.kartik.callprompt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by kartik on Sun, 4/3/18 in call-prompt.
 */

public class OutgoingCallHandler extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(PromptContacts.showDialog) {
            String phoneNumber = getResultData();
            if (phoneNumber == null) {
                phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            }
            ContactInfo temp = new ContactInfo();
            Log.i("Listener", "listened");
            temp.setContactNumber(phoneNumber);
            Log.i("Phone number", phoneNumber);
            if (PromptContacts.promptContactList.contains(temp)) {
                Log.i("Compare", "Matched!");
                setResultData(null);
                Intent dialog = new Intent(context, DialogActivity.class);
                dialog.putExtra("phone", phoneNumber);
                context.startActivity(dialog);
            }
        } else {
            PromptContacts.showDialog = true;
        }
    }
}
