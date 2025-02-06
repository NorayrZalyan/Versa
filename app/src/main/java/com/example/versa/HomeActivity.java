package com.example.versa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.versa.bottomSheet.CreateRoomBottomSheet;
import com.example.versa.bottomSheet.JoinRoombottomsheet;
import com.example.versa.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    FirebaseAuth mAuth;
    ListAdapter listAdapter;
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
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                        if (document.getString("jobtitle").equals("Owner")){
                            binding.createroomBottomsheetBt.setVisibility(View.VISIBLE);
                            binding.joinRoomBt.setVisibility(View.INVISIBLE);
                        } else {
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

            }
        });












//        list View
//        String[] nameList = new String[0];
//        String[] idList = new String[0];
//        for (int i = 0; i < nameList.length; i++){
//            roomData = new RoomData(nameList[i], idList[i]);
//            dataArrayList.add(roomData);
//        }
//        listAdapter = new ListAdapter(HomeActivity.this, dataArrayList);
//        binding.listview.setAdapter(listAdapter);
//

    }
}