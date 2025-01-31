package com.example.versa.fragment.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.databinding.JoinroomBottomSheetBinding;
import com.example.versa.databinding.RoomBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinRoomBottomSheet extends BottomSheetDialogFragment {

    private JoinroomBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        binding = JoinroomBottomSheetBinding.inflate(inflater, container, false);

        binding.joinRoomBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rID = binding.roomIdTv.getText().toString();


                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = fUser.getUid();
                db.collection("Users").document(uid)
                        .update("roomId", rID);

                dismiss();
            }
        });

        return binding.getRoot();
    }
}
