package com.example.myapplication.feature.connect;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.Nullable;

public class NetworkService extends Service {

    private static final long INTERVAL = 3000;

    private boolean isRunning = false;
    private Handler handler;
    private Context context;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Ошибка")
                        .setMessage("Интернет не доступен")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }

            handler.postDelayed(this, INTERVAL);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;
            handler = new Handler();
            handler.postDelayed(runnable, INTERVAL);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacks(runnable);
        }
    }
}