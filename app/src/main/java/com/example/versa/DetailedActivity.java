package com.example.versa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.versa.bottomSheet.CreateCategoryBottomSheet;
import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
import com.example.versa.databinding.ActivityDetailedBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailedActivity extends AppCompatActivity {

    private ActivityDetailedBinding binding;
    private ArrayList<CategoryData> dataArrayList = new ArrayList<>();
    FirebaseAuth mAuth;
    CategoryListAdapter categoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String roomName = intent.getStringExtra("roomName");
        String roomId = intent.getStringExtra("roomId");
        binding.roomNameTv.setText(roomName);
        binding.roomIdTV.setText(roomId);


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
                            binding.createCategoryBt.setVisibility(View.VISIBLE);
                        } else if(document.getString("jobtitle").equals("Other")) {
                            binding.createCategoryBt.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });





        binding.createCategoryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("roomid", roomId);
                CreateCategoryBottomSheet bottomSheet = new CreateCategoryBottomSheet();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            }
        });




        String uid = FirebaseAuth.getInstance().getUid();
        db.collection("Users").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){

                                List<Map<String, String>> userCategories = (List<Map<String, String>>) documentSnapshot.get("categories");
                                Log.d("TAG", "onComplete: "+userCategories);
                                ArrayList<String> nameList = new ArrayList<>();
                                for (int i = 0; i < userCategories.size(); i++) {
                                    for (String key : userCategories.get(i).keySet()) {
                                        Log.d("TAG", "onComplete: "+key);
                                        if (key.equals(roomId)){
                                            nameList.add(userCategories.get(i).get(key));
                                        }
                                    }
                                }
                                for (int i = 0; i < nameList.size(); i++) {
                                    CategoryData categoryData = new CategoryData(nameList.get(i));
                                    dataArrayList.add(categoryData);
                                }
                                categoryListAdapter = new CategoryListAdapter(DetailedActivity.this, dataArrayList, roomId, roomName, nameList);
                                binding.listview.setAdapter(categoryListAdapter);
                                binding.listview.setClickable(true);
                                binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Intent intent = new Intent(DetailedActivity.this, CategoryActivity.class);
                                        intent.putExtra("roomId", roomId);
                                        intent.putExtra("roomName", roomName);
                                        intent.putExtra("categoryName", nameList.get(position));
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
}