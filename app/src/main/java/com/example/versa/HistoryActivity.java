package com.example.versa;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.versa.Dialog.LoadingDialog;
import com.example.versa.bottomSheet.AddClientBottomSheet;
import com.example.versa.bottomSheet.GiveAccessBottomSheet;
import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
//import com.example.versa.clients.ClientListAdapter;
import com.example.versa.clients.Client;
import com.example.versa.clients.ClientData;
import com.example.versa.clients.ClientListAdapter;
import com.example.versa.databinding.ActivityCategoryBinding;
import com.example.versa.databinding.ActivityHistoryBinding;
import com.example.versa.staff.WorkerData;
import com.example.versa.staff.WorkerListAdapter;
import com.google.android.gms.common.util.ClientLibraryUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private FirebaseFirestore db;
    private ArrayList<ClientData> clientDataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    public String categoryName;
    public String roomId;
    public String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        roomName = intent.getStringExtra("roomName");
        binding.categoryNameTv.setText("History");
        binding.backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, DetailedActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }
        });





        db.collection("Rooms").document(roomId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){

                                List<Map<String, Object>> history = (List<Map<String, Object>>) documentSnapshot.get("history");
                                String[] nameList = new String[history.size()];

                                for (int i = 0; i < history.size(); i++) {
                                    nameList[i] = (String) history.get(i).get("name");

                                }
                                for (int i = 0; i < nameList.length; i++) {
                                    ClientData clientData = new ClientData(nameList[i]);
                                    clientDataArrayList.add(clientData);
                                }
                                ClientListAdapter clientListAdapter = new ClientListAdapter(HistoryActivity.this, clientDataArrayList, roomId, roomName, categoryName, "HistoryActivity");
                                binding.listview.setAdapter(clientListAdapter);
                                binding.listview.setClickable(true);
                                binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                        a(
                                                (String) history.get(position).get("name"),
                                                (String) history.get(position).get("phone"),
                                                (String) history.get(position).get("email"),
                                                (String) history.get(position).get("description"),
                                                position
                                        );

                                    }
                                });


                            } else {
                                Toast.makeText(HistoryActivity.this, "No such documnt", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("ERROR", "onComplete: ",task.getException() );
                        }
                    }
                });





    }

    public void a(String name, String phone, String email, String description, int position){

        ConstraintLayout constraintLayout = findViewById(R.id.constraintlayout);
        View view = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.clientdata_dialog, constraintLayout);
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
        builder.setView(view);
        builder.setCancelable(true);
        LoadingDialog loadingDialog = new LoadingDialog(HistoryActivity.this);
        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        EditText nameEt = view.findViewById(R.id.nameEt);
        nameEt.setText(name);
        EditText phoneEt = view.findViewById(R.id.phoneEt);
        phoneEt.setText(phone);
        EditText emailEt = view.findViewById(R.id.emailEt);
        emailEt.setText(email);
        EditText descriptionTv = view.findViewById(R.id.descriptionEt);
        descriptionTv.setText(description);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoading();
                EditText nameEt = view.findViewById(R.id.nameEt);
                EditText phoneEt = view.findViewById(R.id.phoneEt);
                EditText emailEt = view.findViewById(R.id.emailEt);
                EditText descriptionEt = view.findViewById(R.id.descriptionEt);


                db.collection("Rooms").document(roomId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){


                                        List<Map<String, Object>> clients = (List<Map<String, Object>>) documentSnapshot.get("history");
                                        clients.get(position).put("name",nameEt.getText().toString());
                                        clients.get(position).put("phone",phoneEt.getText().toString());
                                        clients.get(position).put("email",emailEt.getText().toString());
                                        clients.get(position).put("description",descriptionEt.getText().toString());
                                        Map<String, Object> update = new HashMap<>();
                                        update.put("history", clients);
                                        db.collection("Rooms").document(roomId).update(update);
                                        loadingDialog.dismisDialog();
                                        alertDialog.cancel();
                                        recreate();


                                    } else {
                                    }
                                } else {
                                }
                            }
                        });



            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}