package com.example.versa.bottomSheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.Dialog.LoadingDialog;
import com.example.versa.databinding.GieAccessBottomSheetBinding;
import com.example.versa.staff.Worker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class GiveAccessBottomSheet extends BottomSheetDialogFragment {
    private GieAccessBottomSheetBinding binding;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = GieAccessBottomSheetBinding.inflate(inflater, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String categoryName = getArguments().getString("categoryName");
        String roomId = getArguments().getString("roomId");
        String roomName = getArguments().getString("roomName");
        loadingDialog = new LoadingDialog(getActivity());

        binding.giveAccessBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.startLoading();
                String userEmail = binding.userEmailEt.getText().toString();
                String userJobTitle = binding.userJobTitleEt.getText().toString();
                if (userEmail.isEmpty() || userJobTitle.isEmpty()){
                    Toast.makeText(getContext(), "fields cannot be empty.", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismisDialog();
                    return;
                }
                Log.d("TAG", "onClick: "+userEmail);
                CollectionReference usersRef = db.collection("Users");
                usersRef.whereEqualTo("email", userEmail)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String userId = document.getId(); // Получаем UID документа
                                    Log.d("Firestore", "Документ найден: " + userId);

                                    Worker worker = new Worker(roomId,roomName,userJobTitle);
                                    db.collection("Users").document(userId)
                                            .update("rooms", FieldValue.arrayUnion(worker));


                                    Map<String, String> category = new HashMap<>();
                                    category.put(roomId, categoryName);
                                    db.collection("Users").document(userId)
                                            .update("categories", FieldValue.arrayUnion(category))
                                            .addOnSuccessListener(aVoid -> {
                                                loadingDialog.dismisDialog();
                                                Log.d("Firestore", "Категория добавлена");
                                            })
                                            .addOnFailureListener(e -> {
                                                loadingDialog.dismisDialog();
                                                Log.w("Firestore", "Ошибка добавления", e);
                                            });
                                    dismiss();
                                }
                            } else {
                                loadingDialog.dismisDialog();
                                Toast.makeText(getContext(), "User not found", Toast.LENGTH_LONG).show();
                                Log.d("Firestore", "Документ с таким email не найден.");
                            }
                        });
            }
        });





        return binding.getRoot();
    }


}



