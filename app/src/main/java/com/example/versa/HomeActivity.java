package com.example.versa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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
                        if (document.getString("roomId") != null){
                            binding.createroomBottomsheetBt.setVisibility(View.INVISIBLE);
                            binding.joinRoomBt.setVisibility(View.INVISIBLE);
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
//                recreate();

            }
        });










        db.collection("Users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("2", "DocumentSnapshot data: " + document.getData());
                        String userRoom = document.getString("roomId");

                        if(document.getString("roomId") != null){

                            db.collection("Rooms").document(userRoom).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document1 = task1.getResult();
                                        if (document.exists()) {
                                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                                            String userRoomName = document1.getString("roomName");
                                            long userRoomId = document1.getLong("roomId");

                                            String[] nameList = new String[]{userRoomName};
                                            String[] idList = new String[]{String.valueOf(userRoomId)};
                                            for (int i = 0; i < nameList.length; i++){
                                                roomData = new RoomData(nameList[i], idList[i]);
                                                dataArrayList.add(roomData);
                                            }


                                            listAdapter = new RoomListAdapter(HomeActivity.this, dataArrayList);
                                            binding.listview.setAdapter(listAdapter);

                                            binding.listview.setClickable(true);
                                            binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                    Intent intent = new Intent(HomeActivity.this, DetailedActivity.class);
                                                    intent.putExtra("name", nameList[i]);
                                                    intent.putExtra("id", idList[i]);
                                                    startActivity(intent);

                                                }
                                            });




                                        } else {
                                            Log.d("TAG", "No such document");
                                        }
                                    } else {
                                        Log.d("TAG", "get failed with ", task.getException());
                                    }
                                }
                            });


                        }


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