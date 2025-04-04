package com.example.versa.bottomSheet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.Dialog.LoadingDialog;
import com.example.versa.category.Category;
import com.example.versa.databinding.CategoryBottomSheetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCategoryBottomSheet extends BottomSheetDialogFragment {

    private CategoryBottomSheetBinding binding;
    private LoadingDialog loadingDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CategoryBottomSheetBinding.inflate(inflater, container, false);

        String uId = FirebaseAuth.getInstance().getUid();

        loadingDialog = new LoadingDialog(getActivity());


        binding.createCategoryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoading();


                String roomid = getArguments().getString("roomid");
                db.collection("Rooms").document(roomid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){
                                        List<Map<String, Object>> categories = (List<Map<String, Object>>) documentSnapshot.get("categories");
                                        Log.d("TEST", "onComplete: categories "+categories.size());
                                        if (categories.size()==0){
                                            Category category = new Category(binding.categoryNameEd.getText().toString());
                                            Map<String, String> userCategory = new HashMap<>();
                                            userCategory.put(roomid, binding.categoryNameEd.getText().toString());
                                            db.collection("Users").document(uId).update("categories", FieldValue.arrayUnion(userCategory))
                                                    .addOnSuccessListener(aVoid -> {
                                                        loadingDialog.dismisDialog();
                                                        Log.d("Firestore", "Категория добавлена");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        loadingDialog.dismisDialog();
                                                        Log.w("Firestore", "Ошибка добавления", e);
                                                    });
                                            db.collection("Rooms").document(roomid).update("categories", FieldValue.arrayUnion(category))
                                                    .addOnSuccessListener(aVoid -> {
                                                        loadingDialog.dismisDialog();
                                                        Log.d("Firestore", "Категория добавлена");
                                                        dismiss();
                                                        Activity activity = getActivity();
                                                        if (activity != null) {
                                                            activity.recreate();
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        loadingDialog.dismisDialog();
                                                        Log.w("Firestore", "Ошибка добавления", e);
                                                    });
                                        } else {


                                            for (int i = 0; i < categories.size(); i++) {


                                                Log.d("TEST", "onComplete: categories != null");

                                                if (categories.get(i).get("name").equals(binding.categoryNameEd.getText().toString())) {
                                                    Toast.makeText(getContext(), "This category already exists", Toast.LENGTH_LONG).show();
                                                    loadingDialog.dismisDialog();
                                                    return;
                                                } else {

                                                    Category category = new Category(binding.categoryNameEd.getText().toString());
                                                    Map<String, String> userCategory = new HashMap<>();
                                                    userCategory.put(roomid, binding.categoryNameEd.getText().toString());
                                                    db.collection("Users").document(uId).update("categories", FieldValue.arrayUnion(userCategory))
                                                            .addOnSuccessListener(aVoid -> {
                                                                loadingDialog.dismisDialog();
                                                                Log.d("Firestore", "Категория добавлена");
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                loadingDialog.dismisDialog();
                                                                Log.w("Firestore", "Ошибка добавления", e);
                                                            });
                                                    db.collection("Rooms").document(roomid).update("categories", FieldValue.arrayUnion(category))
                                                            .addOnSuccessListener(aVoid -> {
                                                                loadingDialog.dismisDialog();
                                                                Log.d("Firestore", "Категория добавлена");
                                                                dismiss();
                                                                Activity activity = getActivity();
                                                                if (activity != null) {
                                                                    activity.recreate();
                                                                }
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                loadingDialog.dismisDialog();
                                                                Log.w("Firestore", "Ошибка добавления", e);
                                                            });
                                                }





                                            }
                                        }
                                    } else {
                                        Log.d("TEST", "onComplete: er");
                                    }
                                } else {
                                    Log.d("TEST", "onComplete: err");
                                }
                            }
                        });





            }
        });





        return binding.getRoot();
    }
}

