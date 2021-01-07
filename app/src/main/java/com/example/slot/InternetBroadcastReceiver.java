package com.example.slot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class InternetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if (noConnectivity) {
                Toast.makeText(context, "החיבור לרשת אבד! לא ניתן לקבוע פגישות או לקבל מידע.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
