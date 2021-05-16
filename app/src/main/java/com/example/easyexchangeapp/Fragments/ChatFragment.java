package com.example.easyexchangeapp.Fragments;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.easyexchangeapp.Activity.MainActivity;
import com.example.easyexchangeapp.Adapters.MyConversations;
import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.MyChatRecord;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView chatRecordRV;
    private DatabaseReference databaseReference;
    private String currentUid;
    private List<MyChatRecord> myChatRecordList=new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        SharedPrefManager manager=new SharedPrefManager(getContext());
        currentUid=manager.getValue(Constants.USER_ID);

        chatRecordRV=view.findViewById(R.id.chatRecordRV);
        MyConversations conversationsAdapter=new MyConversations(getContext());
        chatRecordRV.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecordRV.hasFixedSize();
        chatRecordRV.setAdapter(conversationsAdapter);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("chatRooms");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myChatRecordList.clear();
                for(DataSnapshot temp:snapshot.getChildren()){
                    String chatRoomID=temp.getKey();
                    MyChatRecord record=new MyChatRecord(getUserName(chatRoomID),"",chatRoomID);
                    myChatRecordList.add(record);
                }
                conversationsAdapter.setChatRecordList(myChatRecordList);
                conversationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private String getUserName(String chatRoomID){
        String[] splitString=chatRoomID.split("--");
        String user = "";
        for(String temp: splitString){
            if(!temp.equals(currentUid)){
                user=temp;
            }
        }
        return user;
    }
}