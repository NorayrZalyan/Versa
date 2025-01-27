package com.example.versa.fragment.start;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.versa.databinding.StartFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class StartFragment extends Fragment {
    private StartFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StartFragmentBinding.inflate(inflater, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });


        FirebaseUser fUser = mAuth.getCurrentUser();
        String uid = fUser.getUid();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Получаем документ "Уникальный IT-пользователь" из коллекции "Users"
        db.collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Получаем значение поля "email"
                            String jobtitle = document.getString("jobtitle");


                            Toast.makeText(getContext(), "Jobtitle: " + jobtitle, Toast.LENGTH_SHORT).show();

                            if (jobtitle.equals("Owner")) {
                                binding.createRoomBt.setVisibility(View.VISIBLE);
                                binding.joinRoomBt.setVisibility(View.GONE);
                            } else {
                                binding.createRoomBt.setVisibility(View.GONE);
                                binding.joinRoomBt.setVisibility(View.VISIBLE);
                            }



                            Log.d("Firestore", "Email: " + jobtitle);
                        } else {
                            Log.d("Firestore", "Документ не найден!");
                        }
                    } else {
                        Log.w("Firestore", "Ошибка при чтении документа.", task.getException());
                    }
                });



        binding.createRoomBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateRoomBottomSheet createRoomBottomSheet = new CreateRoomBottomSheet();
                createRoomBottomSheet.show(getChildFragmentManager(), "teg");

            }
        });

        binding.joinRoomBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JoinRoomBottomSheet joinRoomBottomSheet = new JoinRoomBottomSheet();
                joinRoomBottomSheet.show(getChildFragmentManager(), "teg");

            }
        });


        return binding.getRoot();

    }



}