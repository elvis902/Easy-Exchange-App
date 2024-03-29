package gdscnits.easyexchange.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gdscnits.easyexchange.app.Constants.Constants;
import gdscnits.easyexchange.app.Models.Product;
import com.example.easyexchangeapp.R;
import gdscnits.easyexchange.app.SharedPrefManager.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {

    private TextView prodName, prodDescription, prodPrice, prodAddress;
    private ImageView prodImage;
    private Button chatBuyButton;
    private DatabaseReference databaseReference;
    private String clientId,userId;
    private String sellerName;
    private String chatRoomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Loading...");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");


        prodName = findViewById(R.id.detail_itemName);
        prodDescription = findViewById(R.id.detail_itemDescription);
        prodPrice = findViewById(R.id.detail_itemPrice);
        prodAddress = findViewById(R.id.detail_itemAddress);
        prodImage = findViewById(R.id.detail_itemImage);
        chatBuyButton=findViewById(R.id.button);

        Product item = (Product) getIntent().getSerializableExtra("item_bundle");
        SharedPrefManager manager=new SharedPrefManager(getApplicationContext());
        userId=manager.getValue(Constants.USER_ID);
        if (item != null) {
            clientId=item.getSellerID();
        }
        if(clientId.equals(userId)){
            chatBuyButton.setVisibility(View.INVISIBLE);
        }
        sellerName=item.getSellerName();
        assert item != null;



        displayInfo(item);

        chatBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRoomAvailability();
            }
        });
    }
    private void checkRoomAvailability(){
        DatabaseReference chatReference=databaseReference.child(userId).child("chatRooms");
        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> chatRoomIds=new HashMap<>();
                for(DataSnapshot temp: snapshot.getChildren()){
                    chatRoomIds.put(temp.getKey(),temp.getKey());
                }
                if(!chatRoomIds.containsKey(userId+"--"+clientId)&&!chatRoomIds.containsKey(clientId+"--"+userId)){
                    chatRoomId=userId+"--"+clientId;
                    chatReference.child(chatRoomId).setValue(chatRoomId);
                    databaseReference.child(clientId).child("chatRooms").child(chatRoomId).setValue(chatRoomId);
                }else if (chatRoomIds.containsKey(userId+"--"+clientId)){
                    chatRoomId=userId+"--"+clientId;
                }else if(chatRoomIds.containsKey(clientId+"--"+userId)){
                    chatRoomId=clientId+"--"+userId;
                }
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("chat-room",chatRoomId);
                intent.putExtra("client",clientId);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error creating chat room!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayInfo(Product item){
        prodName.setText(item.getProdName());
        prodAddress.setText(item.getProdAddress());
        prodPrice.setText(item.getProdPrice());
        prodDescription.setText(item.getProdDescription());
        databaseReference.child(item.getSellerID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot tempSnap: snapshot.getChildren()){
                    if(tempSnap.getKey().equals("userName")){
                        setTitle("Uploaded by: "+tempSnap.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Picasso.get().
                load(item.getImageUrl()).
                centerInside().
                fit().
                into(prodImage);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}