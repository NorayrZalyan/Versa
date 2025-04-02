package com.example.versa.bottomSheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.LoadingDialog;
import com.example.versa.databinding.GieAccessBottomSheetBinding;
import com.example.versa.databinding.RoomBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
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
                Log.d("TAG", "onClick: "+binding.userEmailEt.getText().toString());
                CollectionReference usersRef = db.collection("Users");
                usersRef.whereEqualTo("email", binding.userEmailEt.getText().toString())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String userId = document.getId(); // Получаем UID документа
                                    Log.d("Firestore", "Документ найден: " + userId);

                                    Map<String, String> room = new HashMap<>();
                                    room.put(roomId, roomName);
                                    db.collection("Users").document(userId)
                                            .update("rooms", FieldValue.arrayUnion(room));
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
                                Log.d("Firestore", "Документ с таким email не найден.");
                            }
                        });
            }
        });





        return binding.getRoot();
    }


}



