package gdscnits.easyexchange.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gdscnits.easyexchange.app.Adapters.FavItemAdapter;
import gdscnits.easyexchange.app.Constants.Constants;
import gdscnits.easyexchange.app.Models.Product;
import com.example.easyexchangeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAds extends AppCompatActivity implements FavItemAdapter.OnFavItemClickListener {
    private  String userMail;
    private TextView mail;
    private RecyclerView recyclerView;
    private FavItemAdapter adapter;
    private DatabaseReference reference;
    private List<Product> myAdsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        getSupportActionBar().setTitle("My Ads");
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        userMail = getIntent().getStringExtra("user_mail");
        mail = findViewById(R.id.myAds_userMail);
        mail.setText(userMail);
        recyclerView = findViewById(R.id.myAds_RV);

        reference = FirebaseDatabase.getInstance().getReference().child(Constants.STORAGE_LOCATION);

        //Fetch My Ads
        getMyAds();

        adapter = new FavItemAdapter(myAdsList, this,this,Constants.INDICATOR_MY_ADDS);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
    }

    private void getMyAds() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myAdsList.clear();
                for(DataSnapshot mySnap : snapshot.getChildren()){
                    Product tempProd = mySnap.getValue(Product.class);
                    if(tempProd.getSellerEmail().equals(userMail)){
                        myAdsList.add(tempProd);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        reference.addValueEventListener(eventListener);
    }

    @Override
    public void onFavClicked(int position) {
        Intent intent = new Intent(this,ProductDetails.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item_bundle",myAdsList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}