package com.example.easyexchangeapp.NotificationManagerFiles;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.easyexchangeapp.Activity.MainActivity;
import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static com.example.easyexchangeapp.NotificationManagerFiles.AppClass.Channel_ID;

public class NotificationServiceClass extends Service {
    private DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Chat-Rooms");
        Intent notificationIntent=new Intent(this, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification=new NotificationCompat.Builder(
                this,
                Channel_ID)
                .setContentTitle("Easy Exchange is Running...")
                .setSmallIcon(R.drawable.logo_transparent)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(101,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean compare(String a,String b){
        if(a.length()!=b.length()){
            return false;
        }
        for(int i=0;i<a.length();i++) {
            if (a.charAt(i) != b.charAt(i))
                return false;
        }
        return true;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1=new Intent(getBaseContext(),NotificationReceiver.class);
            String userId=new SharedPrefManager(getBaseContext()).getValue(Constants.USER_ID);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String val=dataSnapshot.child("sender").getValue(String.class);
                        if(val==null||compare(val,userId)){
                            continue;
                        }
                        if(dataSnapshot.getKey().contains(userId)&&dataSnapshot.hasChild("sender")){
                            if(!compare(val,userId)){
                                intent1.putExtra("username",val);
                                sendBroadcast(intent1);
                                databaseReference.child(dataSnapshot.getKey()).child("sender").setValue(null);
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