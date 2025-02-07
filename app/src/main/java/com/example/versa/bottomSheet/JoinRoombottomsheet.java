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

import com.example.versa.databinding.JoinroomBottomSheetBinding;
import com.example.versa.databinding.RoomBottomSheetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinRoombottomsheet extends BottomSheetDialogFragment {

    private JoinroomBottomSheetBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = JoinroomBottomSheetBinding.inflate(inflater, container, false);

        binding.joinRoomBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String uid = FirebaseAuth.getInstance().getUid();
                String roomId = binding.roomIdEt.getText().toString();



                db.collection("Rooms").document(roomId).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                db.collection("Users").document(uid)
                                                        .update("roomId", roomId)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Log.d("TAG", "onComplete: join room");
                                                                    dismiss();
                                                                    Activity activity = getActivity();
                                                                    if (activity != null) {
                                                                        activity.recreate();  // Пересоздаём активность
                                                                    }
                                                                } else {
                                                                    Log.e("TAG", "onComplete: error", task.getException() );
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Log.d("TAG", "No such document");
                                                Toast.makeText(getContext(), "No such room", Toast.LENGTH_SHORT).show();
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
