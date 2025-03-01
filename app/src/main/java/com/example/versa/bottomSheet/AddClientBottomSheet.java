package com.example.versa.bottomSheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.category.Category;
import com.example.versa.classes.Client;
import com.example.versa.databinding.AddclientBottomSheetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClientBottomSheet extends BottomSheetDialogFragment {

    private AddclientBottomSheetBinding binding;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddclientBottomSheetBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();


        String roomId = getArguments().getString("roomId");
        int category = getArguments().getInt("category");

        String clientName = binding.clientNameEt.getText().toString();
        String clientPhone = binding.clientPhoneEt.getText().toString();
        String clientEmail = binding.clientEmailEt.getText().toString();
        String description = binding.clientDescriptionEt.getText().toString();

        binding.addClientBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Rooms").document(roomId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()){


                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        DocumentReference docRef = db.collection("Rooms").document("1");
                                        docRef.get().addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                // Получаем список категорий
                                                List<Map<String, Object>> categoriesList = (List<Map<String, Object>>) documentSnapshot.get("categories");

                                                Map<String, Object> categoryMap = categoriesList.get(category);

                                                List<Client> clientsList = (List<Client>) categoryMap.get("clients");

                                                Client newClient = new Client("New Client Name","0","@","d");

                                                clientsList.add(newClient);

                                                categoryMap.put("clients", clientsList);

                                                Map<String, Object> updateMap = new HashMap<>();
                                                updateMap.put("categories", categoriesList);

                                                docRef.update(updateMap).addOnSuccessListener(aVoid -> {
                                                    Log.d("Firestore", "Client added successfully!");
                                                }).addOnFailureListener(e -> {
                                                    Log.e("Firestore", "Error adding client: ", e);
                                                });

                                            }
                                        });

//

                                    } else {
                                        Log.e("TAG", "No such document");
                                    }
                                } else {
                                    Log.e("TAG", ""+task.getException());
                                }
                            }
                        });

            }
        });



        return binding.getRoot();
    }
}
