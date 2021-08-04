package com.example.easyexchangeapp.NotificationManagerFiles;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AppClass extends Application {
    public static final String Channel_ID="notificationServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel serviceChannel=new NotificationChannel(
                    Channel_ID,
                    "Easy Exchange App",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
