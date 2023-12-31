package com.example.myapplication.feature.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, NetworkService.class);
            context.startService(serviceIntent);
        }
    }
}