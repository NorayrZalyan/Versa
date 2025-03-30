package com.example.versa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.versa.Auth.MainActivity;
import com.example.versa.bottomSheet.CreateRoomBottomSheet;
import com.example.versa.bottomSheet.JoinRoombottomsheet;
import com.example.versa.databinding.ActivityHomeBinding;
import com.example.versa.room.RoomData;
import com.example.versa.room.RoomListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    
    FirebaseAuth mAuth;
    RoomListAdapter listAdapter;
    ArrayList<RoomData> dataArrayList = new ArrayList<>();
    RoomData roomData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = mAuth.getCurrentUser();
        String Uid = fUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("1", "DocumentSnapshot data: " + document.getData());

                        if (document.getString("jobtitle").equals("Admin")){
                            binding.createroomBottomsheetBt.setVisibility(View.VISIBLE);
                            binding.joinRoomBt.setVisibility(View.INVISIBLE);
                        } else if(document.getString("jobtitle").equals("Other")) {
                            binding.createroomBottomsheetBt.setVisibility(View.INVISIBLE);
                            binding.joinRoomBt.setVisibility(View.VISIBLE);
                        }



                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });


        binding.createroomBottomsheetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRoomBottomSheet createRoomBottomSheet = new CreateRoomBottomSheet();
                createRoomBottomSheet.show(getSupportFragmentManager(), "createRoomBottomSheet");
            }
        });
        binding.joinRoomBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinRoombottomsheet joinRoombottomsheet = new JoinRoombottomsheet();
                joinRoombottomsheet.show(getSupportFragmentManager(), "joinRoombottomsheet");
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));

            }
        });










        db.collection("Users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, String>> userRooms = (List<Map<String, String>>) document.get("rooms");
                        Log.d("TAG", "onComplete: ");
                        Log.d("TAG", "onComplete: "+userRooms);
                        String[] nameList = new String[userRooms.size()];
                        String[] idList = new String[userRooms.size()];
                        for (int i = 0; i < userRooms.size(); i++) {
                            for (String value: userRooms.get(i).values()) {
                                nameList[i] = value;
                            }
                            for (String key: userRooms.get(i).keySet()) {
                                idList[i] = key;
                            }
                        }
                        for (int i = 0; i < nameList.length; i++) {
                            RoomData roomData = new RoomData(nameList[i], idList[i]);
                            dataArrayList.add(roomData);
                        }
                        RoomListAdapter roomListAdapter = new RoomListAdapter(HomeActivity.this, dataArrayList);
                        binding.listview.setAdapter(roomListAdapter);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });




    }
}