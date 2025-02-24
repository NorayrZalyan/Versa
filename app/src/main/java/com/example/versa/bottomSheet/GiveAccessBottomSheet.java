package com.example.versa.bottomSheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.databinding.GieAccessBottomSheetBinding;
import com.example.versa.databinding.RoomBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class GiveAccessBottomSheet extends BottomSheetDialogFragment {
    private GieAccessBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = GieAccessBottomSheetBinding.inflate(inflater, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String category = getArguments().getString("position");

        binding.giveAccessBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("TAG", "onClick: "+binding.userEmailEt.getText().toString());
                CollectionReference usersRef = db.collection("Users");
                usersRef.whereEqualTo("email", binding.userEmailEt.getText().toString())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String userId = document.getId(); // Получаем UID документа
                                    Log.d("Firestore", "Документ найден: " + userId);
                                    db.collection("Users").document(userId)
                                            .update("categories", FieldValue.arrayUnion(category))
                                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Категория добавлена"))
                                            .addOnFailureListener(e -> Log.w("Firestore", "Ошибка добавления", e));
                                }
                            } else {
                                Log.d("Firestore", "Документ с таким email не найден.");
                            }
                        });
            }
        });





        return binding.getRoot();
    }


}



