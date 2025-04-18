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
import com.example.versa.room.Room;
import com.example.versa.databinding.RoomBottomSheetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class  CreateRoomBottomSheet extends BottomSheetDialogFragment {

    private RoomBottomSheetBinding binding;
    private LoadingDialog loadingDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RoomBottomSheetBinding.inflate(inflater, container, false);

        loadingDialog = new LoadingDialog(getActivity());


        binding.createroomBottomsheetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoading();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String roomName = binding.RoomNameEt.getText().toString();

                db.collection("Rooms")
                        .orderBy("roomId", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot lastDocument = queryDocumentSnapshots.getDocuments().get(0);
                                int lastRoomID = lastDocument.getLong("roomId").intValue();
                                Room room = new Room(roomName, lastRoomID+1);
                                int finalLastRoomID = lastRoomID;
                                db.collection("Rooms").document(String.valueOf(++lastRoomID))
                                        .set(room)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                                    loadingDialog.dismisDialog();
                                                    dismiss();
                                                    Activity activity = getActivity();
                                                    if (activity != null) {
                                                        activity.recreate();
                                                    }
                                                } else {
                                                    loadingDialog.dismisDialog();
                                                    Log.w("TAG", "Error writing document", task.getException());
                                                }
                                            }
                                        });
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                int finalLastRoomID1 = lastRoomID;
                                db.collection("Rooms").document(String.valueOf(lastRoomID))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    if (documentSnapshot.exists()){
                                                        Map<String, String> room1 = new HashMap<>();
                                                        room1.put(String.valueOf(finalLastRoomID1), (String) documentSnapshot.get("roomName"));
                                                        Log.d("TEST", "onComplete: "+room1);
                                                        db.collection("Users").document(uid).update("rooms", FieldValue.arrayUnion(room1));
                                                    } else {
                                                        Toast.makeText(getContext(), "document not found", Toast.LENGTH_LONG).show();
                                                        Log.w("MESSAGE", "onComplete: document not found");
                                                    }
                                                } else {
                                                    Log.e("ERROr", "onComplete: ", task.getException());
                                                }
                                            }
                                        });



                            } else {
                                Log.d("Firestore", "Документы отсутствуют.");

                                Room room1 = new Room(roomName, 1);
                                db.collection("Rooms").document("1")
                                        .set(room1)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    loadingDialog.dismisDialog();
                                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                                    dismiss();
                                                    Activity activity = getActivity();
                                                    if (activity != null) {
                                                        activity.recreate();  // Пересоздаём активность
                                                    }
                                                } else {
                                                    loadingDialog.dismisDialog();
                                                    Log.w("TAG", "Error writing document", task.getException());
                                                }
                                            }
                                        });
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                db.collection("Rooms").document("1")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    if (documentSnapshot.exists()){
                                                        Map<String, String> room1 = new HashMap<>();
                                                        room1.put("1", (String) documentSnapshot.get("roomName"));
                                                        Log.d("TAG", "onComplete: "+room1);
                                                        db.collection("Users").document(uid).update("rooms", FieldValue.arrayUnion(room1));
                                                    } else {
                                                    }
                                                }else {
                                                }
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(e -> {
                            loadingDialog.dismisDialog();
                            Log.e("Firestore", "Ошибка получения данных", e);
                        });









            }
        });





        return binding.getRoot();
    }
}

