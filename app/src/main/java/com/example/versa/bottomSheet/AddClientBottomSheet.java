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
import com.example.versa.clients.Client;
import com.example.versa.databinding.AddclientBottomSheetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClientBottomSheet extends BottomSheetDialogFragment {

    private AddclientBottomSheetBinding binding;
    private FirebaseFirestore db;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddclientBottomSheetBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();


        String roomId = getArguments().getString("roomId");
        String category = getArguments().getString("category");

        loadingDialog = new LoadingDialog(getActivity());


        binding.addClientBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.startLoading();

                String clientName = binding.clientNameEt.getText().toString();
                if (clientName.isEmpty()){
                    Toast.makeText(getContext(), "enter client name", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismisDialog();
                    return;
                }
                String clientPhone = binding.clientPhoneEt.getText().toString();
                String clientEmail = binding.clientEmailEt.getText().toString();
                String description = binding.clientDescriptionEt.getText().toString();

                Client client = new Client(
                        clientName,
                        clientPhone,
                        clientEmail,
                        description
                );
                Log.d("TAG", "onClick: "+client);


                db.collection("Rooms").document(roomId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){

                                        List<Map<String, Object>> categories = (List<Map<String, Object>>) documentSnapshot.get("categories");
                                        Log.d("TAG", "onComplete: import categories");

                                        for (int i = 0; i < categories.size(); i++) {
                                            Map<String, Object> categoryMap = (Map<String, Object>) categories.get(i);
                                            Log.d("TAG", "onComplete: import category");
                                            if (categoryMap.get("name").equals(category)){
                                                Log.d("TAG", "onComplete: The names are the same");
                                                List<Client> clients = (List<Client>) categoryMap.get("clients");
                                                clients.add(client);
                                                Log.d("TAG", "onComplete: clietn add clients");
                                                categoryMap.put("clients", clients);
                                                Map<String, Object> updateMap = new HashMap<>();
                                                updateMap.put("categories", categories);
                                                db.collection("Rooms").document(roomId).update(updateMap);
                                                Log.d("TAG", "onComplete: client add");
                                                loadingDialog.dismisDialog();
                                                Activity activity = getActivity();
                                                activity.recreate();
                                                dismiss();
                                                break;
                                            }
                                        }

                                    } else {

                                    }
                                } else {

                                }
                            }
                        });





            }
        });



        return binding.getRoot();
    }
}
