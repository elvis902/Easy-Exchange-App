package gdscnits.easyexchange.app.NotificationManagerFiles;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import gdscnits.easyexchange.app.Activity.MainActivity;
import com.example.easyexchangeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManagerCompat;
    private DatabaseReference reference;
    @Override
    public void onReceive(Context context, Intent intent) {
        String user=intent.getStringExtra("username");
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(user);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot tempSnap: snapshot.getChildren()){
                    if(tempSnap.getKey().equals("userName")){
                        String temp=tempSnap.getValue(String.class);
                        Intent intent1=new Intent(context, MainActivity.class);
                        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent1,0);
                        notificationManagerCompat=NotificationManagerCompat.from(context);
                        Notification notification=new NotificationCompat.Builder(context,
                                AppClass.Channel_ID)
                                .setSmallIcon(R.drawable.logo_transparent)
                                .setContentTitle("You have a new message from "+temp)
                                .setContentIntent(pendingIntent)
                                .build();
                        notificationManagerCompat.notify(2,notification);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}