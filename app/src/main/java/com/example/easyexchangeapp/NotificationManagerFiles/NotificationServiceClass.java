package com.example.easyexchangeapp.NotificationManagerFiles;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.easyexchangeapp.Activity.SplashScreen;
import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.easyexchangeapp.NotificationManagerFiles.AppClass.Channel_ID;

public class NotificationServiceClass extends Service {

    private DatabaseReference databaseReference;
    private String userId;
    @Override
    public void onCreate() {
        SharedPrefManager manager=new SharedPrefManager(getApplicationContext());
        userId=manager.getValue(Constants.USER_ID);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Chat-Rooms");
        Intent notificationIntent=new Intent(this, SplashScreen.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification=new NotificationCompat.Builder(
                this,
                Channel_ID)
                .setContentTitle("Easy Exchange is Running...")
                .setSmallIcon(R.drawable.logo_transparent)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Query query=databaseReference.orderByChild("search").startAt("/"+userId+"/");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    System.out.println("ROOMS: "+dataSnapshot.getKey());
                    if(dataSnapshot.hasChild("sender")){
                        String val=dataSnapshot.child("sender").getValue(String.class);
                        System.out.println("DEBUG: "+val);
                        if(val!=""){
                            if(val!=userId){
                                Intent intent1=new Intent(getApplicationContext(),NotificationReceiver.class);
                                intent1.putExtra("username",val);
                                sendBroadcast(intent1);
                                databaseReference.child(dataSnapshot.getKey()).child("sender").setValue("");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}