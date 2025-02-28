package com.example.versa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.versa.category.Category;
import com.example.versa.classes.Client;
import com.example.versa.databinding.ActivityCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        int position = intent.getIntExtra("position", 0);
        binding.categoryNameTv.setText(name);


        binding.addClientBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Rooms").document("1");

                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Получаем список категорий
                        List<Map<String, Object>> categoriesList = (List<Map<String, Object>>) documentSnapshot.get("categories");

                        if (categoriesList != null && !categoriesList.isEmpty()) {
                            // Получаем первую категорию (или можно выбрать другую, если необходимо)
                            Map<String, Object> categoryMap = categoriesList.get(0);

                            // Извлекаем список клиентов
                            List<Client> clientsList = (List<Client>) categoryMap.get("clients");

                            // Создаем нового клиента
                            Client newClient = new Client("Name", "0", "@", "d");

                            // Преобразуем клиента в Map, так как Firestore работает с Map
//


                            // Добавляем нового клиента в список
                            if (clientsList == null) {
                                clientsList = new ArrayList<>();
                            }
                            clientsList.add(newClient);

                            // Обновляем категорию с новым списком клиентов
                            categoryMap.put("clients", clientsList);

                            // Обновляем документ в Firestore
                            Map<String, Object> updateMap = new HashMap<>();
                            updateMap.put("categories", categoriesList);

                            docRef.update(updateMap).addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "Client added successfully!");
                            }).addOnFailureListener(e -> {
                                Log.e("Firestore", "Error adding client: ", e);
                            });
                        }
                    }
                });

            }
        });


    }
}