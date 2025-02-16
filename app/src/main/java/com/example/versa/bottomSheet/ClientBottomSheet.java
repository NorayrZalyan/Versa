package com.example.versa.bottomSheet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.CategoryActivity;
import com.example.versa.DetailedActivity;
import com.example.versa.category.Category;
import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
import com.example.versa.databinding.CategoryBottomSheetBinding;
import com.example.versa.databinding.ClientButtomSheetBinding;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.versa.classes.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientBottomSheet extends BottomSheetDialogFragment {

    private ClientButtomSheetBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ClientButtomSheetBinding.inflate(inflater, container, false);

        String id = getArguments().getString("id");
        int position = getArguments().getInt("position");


        binding.addClientBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client newClient = new Client(
                        "name",
                        "phone",
                        "email",
                        "description"
                );

                DocumentReference docRef = db.collection("Rooms").document(id);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
//                                List<Map<String, Object>> categories = (List<Map<String, Object>>) document.get("categories");
                                ArrayList<Category> categories = (ArrayList<Category>) document.get("categories");
                                if (categories != null && !categories.isEmpty()) {

                                    Log.d("TAG", "onComplete: "+categories.get(0).getName());

                                } else {
                                    Log.d("TAG", "Categories list is empty or null");
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
        });



        return binding.getRoot();
    }
}

