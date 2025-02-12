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

import com.example.versa.category.Category;
import com.example.versa.databinding.CategoryBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateCategoryBottomSheet extends BottomSheetDialogFragment {

    private CategoryBottomSheetBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CategoryBottomSheetBinding.inflate(inflater, container, false);

        binding.createCategoryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomid = getArguments().getString("roomid");


                if (binding.categoryNameEd.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Category category = new Category(binding.categoryNameEd.getText().toString());

                db.collection("Rooms").document(roomid).update("categories", FieldValue.arrayUnion(category))
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Категория добавлена"))
                        .addOnFailureListener(e -> Log.w("Firestore", "Ошибка добавления", e));

                dismiss();
                Activity activity = getActivity();
                if (activity != null) {
                    activity.recreate();
                }

            }
        });





        return binding.getRoot();
    }
}

