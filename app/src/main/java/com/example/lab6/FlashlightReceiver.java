package com.example.lab6;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.widget.RemoteViews;

public class FlashlightReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        SharedPreferences sharedPreferences = context.getSharedPreferences("FlashlightPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            String cameraId = cameraManager.getCameraIdList()[0];

            boolean isFlashlightOn = sharedPreferences.getBoolean("flashlight_state", false);

            if (isFlashlightOn) {
                cameraManager.setTorchMode(cameraId, false);
                editor.putBoolean("flashlight_state", false);
            } else {
                cameraManager.setTorchMode(cameraId, true);
                editor.putBoolean("flashlight_state", true);
            }

            editor.apply();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetProvider .class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setTextViewText(R.id.flashlight_button, isFlashlightOn ? "On" : "Off");
            appWidgetManager.updateAppWidget(thisWidget, views);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}

