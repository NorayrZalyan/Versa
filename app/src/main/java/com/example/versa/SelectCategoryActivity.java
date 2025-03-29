package com.example.versa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
import com.example.versa.clients.Client;
import com.example.versa.databinding.ActivityDetailedBinding;
import com.example.versa.databinding.ActivitySelectCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.versa.databinding.ActivitySelectCategoryBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectCategoryActivity extends AppCompatActivity {
    private ActivitySelectCategoryBinding binding;
    private ArrayList<CategoryData> dataArrayList = new ArrayList<>();
    private String roomId;
    private int categoryPosition;
    private int category;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        categoryPosition = intent.getIntExtra("position", -1);
        category = intent.getIntExtra("category", -1);


        FirebaseFirestore db = FirebaseFirestore.getInstance();


        binding = ActivitySelectCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DocumentReference docRef = db.collection("Rooms").document(roomId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        List<Map<String, Object>> roomCategories = (List<Map<String, Object>>) document.get("categories");
                        if (roomCategories != null && !roomCategories.isEmpty()) {
                            String name = (String) roomCategories.get(0).get("name");
                            Log.d("TAG", "First category name: " + name);

                            String[] nameList = new String[roomCategories.size()];

                            for (int i = 0; i < roomCategories.size(); i++) {
                                nameList[i] = (String) roomCategories.get(i).get("name");
                            }
                            for (int i = 0; i < nameList.length; i++) {
                                CategoryData categoryData = new CategoryData(nameList[i]);
                                dataArrayList.add(categoryData);
                            }
                            CategoryListAdapter categoryListAdapter = new CategoryListAdapter(SelectCategoryActivity.this, dataArrayList, roomId, nameList);
                            binding.listview.setAdapter(categoryListAdapter);
                            binding.listview.setClickable(true);
                            binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long longid) {



                                }
                            });


                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            }
        });













    }
}