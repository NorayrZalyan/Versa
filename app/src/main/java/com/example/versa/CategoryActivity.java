package com.example.versa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.versa.bottomSheet.AddClientBottomSheet;
import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
//import com.example.versa.clients.ClientListAdapter;
import com.example.versa.clients.Client;
import com.example.versa.clients.ClientData;
import com.example.versa.clients.ClientListAdapter;
import com.example.versa.databinding.ActivityCategoryBinding;
import com.google.android.gms.common.util.ClientLibraryUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private FirebaseFirestore db;
    private ArrayList<ClientData> clientDataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");
        String roomName = intent.getStringExtra("roomName");
        String roomId = intent.getStringExtra("roomId");
        binding.categoryNameTv.setText(categoryName);


        binding.addClientBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("roomId", roomId);
                bundle.putString("category", categoryName);
                AddClientBottomSheet bottomSheet = new AddClientBottomSheet();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            }
        });





        db.collection("Rooms").document(roomId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){

                                List<Map<String, Object>> categories = (List<Map<String, Object>>) documentSnapshot.get("categories");
                                for (Map<String, Object> category : categories) {
                                    if (category.get("name").equals(categoryName)){
                                        List<Map<String, Object>> clients = (List<Map<String, Object>>) category.get("clients");
                                        String[] nameList = new String[clients.size()];
                                        for (int i = 0; i < clients.size(); i++) {
                                            nameList[i] = (String) clients.get(i).get("name");
                                        }
                                        for (int i = 0; i < nameList.length; i++) {
                                            ClientData clientData = new ClientData(nameList[i]);
                                            clientDataArrayList.add(clientData);
                                        }
                                        ClientListAdapter clientListAdapter = new ClientListAdapter(CategoryActivity.this, clientDataArrayList, roomId, roomName, categoryName);
                                        binding.listview.setAdapter(clientListAdapter);
                                    }
                                }

                            } else {
                            }
                        } else {
                        }
                    }
                });




    }
}