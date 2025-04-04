package com.example.versa;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
import com.example.versa.clients.Client;
import com.example.versa.databinding.ActivitySelectCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectCategoryActivity extends AppCompatActivity {
    private ActivitySelectCategoryBinding binding;
    private ArrayList<CategoryData> dataArrayList = new ArrayList<>();
    private String roomId;
    private String roomName;
    private String categoryName;
    private int clientPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        roomName = intent.getStringExtra("roomName");
        clientPosition = intent.getIntExtra("clientPosition", 0);
        categoryName = intent.getStringExtra("categoryName");





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


                            ArrayList<String> nameList = new ArrayList<>();
                            for (int i = 0; i < roomCategories.size(); i++) {
                                nameList.add((String)roomCategories.get(i).get("name"));
                            }
                            for (int i = 0; i < nameList.size(); i++) {
                                CategoryData categoryData = new CategoryData(nameList.get(i));
                                dataArrayList.add(categoryData);
                            }
                            CategoryListAdapter categoryListAdapter = new CategoryListAdapter(SelectCategoryActivity.this, dataArrayList, roomId, roomName, nameList);
                            binding.listview.setAdapter(categoryListAdapter);
                            binding.listview.setClickable(true);
                            binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    List<Map<String, Object>> categories = (List<Map<String, Object>>) document.get("categories");
                                    for (int i = 0; i < categories.size(); i++) {
                                        if (categories.get(i).get("name").equals(categoryName)){
                                            Map<String, Object> categoryMap = categories.get(i);
                                            List<Map<String, String>> clients = (List<Map<String, String>>) categoryMap.get("clients");

                                            Client client = new Client(
                                                    clients.get(clientPosition).get("name"),
                                                    clients.get(clientPosition).get("phone"),
                                                    clients.get(clientPosition).get("email"),
                                                    clients.get(clientPosition).get("description")
                                            );

                                            clients.remove(clientPosition);
                                            categoryMap.put("clients", clients);
                                            Map<String, Object> updateMap = new HashMap<>();
                                            updateMap.put("categories", categories);
                                            docRef.update(updateMap);

                                            Map<String, Object> categoryMap1 = categories.get(position);
                                            List<Client> clients1 = (List<Client>) categoryMap1.get("clients");
                                            clients1.add(client);
                                            categoryMap1.put("clients", clients1);
                                            Map<String, Object> updateMap1 = new HashMap<>();
                                            updateMap1.put("categories", categories);
                                            docRef.update(updateMap1);
                                            Toast.makeText(SelectCategoryActivity.this, client.getName()+" moved to "+categoryMap1.get("name"), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(SelectCategoryActivity.this, DetailedActivity.class);
                                            intent.putExtra("roomName", roomName);
                                            intent.putExtra("roomId", roomId);
                                            startActivity(intent);

                                            break;
                                        }
                                    }

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

    @Override
    public void onBackPressed() {
    }
}