package com.example.versa.fragment.start;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.Room;
import com.example.versa.databinding.RoomBottomSheetBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateRoomBottomSheet extends BottomSheetDialogFragment {

    private RoomBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = RoomBottomSheetBinding.inflate(inflater, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        binding.createroomBottomsheetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rName = binding.RoomNameEt.getText().toString();

                Room room = new Room(rName);
                String rId = String.valueOf(room.getId());

                db.collection("Rooms").document(rId)
                        .set(room)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });
                FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                String uid = User.getUid();

                db.collection("Users").document(uid)
                        .update("roomId", rId);

                dismiss();

            }
        });

        return binding.getRoot();
    }
}
