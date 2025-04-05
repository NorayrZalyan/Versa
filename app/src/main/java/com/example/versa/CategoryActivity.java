package com.example.versa;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.versa.bottomSheet.AddClientBottomSheet;
import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
//import com.example.versa.clients.ClientListAdapter;
import com.example.versa.clients.Client;
import com.example.versa.clients.ClientData;
import com.example.versa.clients.ClientListAdapter;
import com.example.versa.databinding.ActivityCategoryBinding;
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

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private FirebaseFirestore db;
    private ArrayList<ClientData> clientDataArrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    public String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");
        String roomName = intent.getStringExtra("roomName");
        String roomId = intent.getStringExtra("roomId");
        binding.categoryNameTv.setText(categoryName);


        binding.addClientBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("roomId", roomId);
                bundle.putString("category", categoryName);
                AddClientBottomSheet bottomSheet = new AddClientBottomSheet();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

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

                                List<Map<String, Object>> categories = (List<Map<String, Object>>) documentSnapshot.get("categories");
                                for (Map<String, Object> category : categories) {
                                    if (category.get("name").equals(categoryName)){
                                        List<Map<String, Object>> clients = (List<Map<String, Object>>) category.get("clients");
                                        String[] nameList = new String[clients.size()];

                                        for (int i = 0; i < clients.size(); i++) {
                                            nameList[i] = (String) clients.get(i).get("name");

                                        }
                                        for (int i = 0; i < nameList.length; i++) {
                                            ClientData clientData = new ClientData(nameList[i]);
                                            clientDataArrayList.add(clientData);
                                        }
                                        ClientListAdapter clientListAdapter = new ClientListAdapter(CategoryActivity.this, clientDataArrayList, roomId, roomName, categoryName);
                                        binding.listview.setAdapter(clientListAdapter);
                                        binding.listview.setClickable(true);
                                        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                Log.d("TAG", "onItemClick: "+(String) clients.get(i).get("phone"));
                                                a(
                                                        (String) clients.get(i).get("name"),
                                                        (String) clients.get(i).get("phone"),
                                                        (String) clients.get(i).get("email"),
                                                        (String) clients.get(i).get("description")
                                                );

                                            }
                                        });
                                    }
                                }

                            } else {
                            }
                        } else {
                        }
                    }
                });

        binding.backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, DetailedActivity.class)
                        .putExtra("roomName",roomName)
                        .putExtra("roomId",roomId));
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });


    }


    public void a(String name, String phone, String email, String description){

        ConstraintLayout constraintLayout = findViewById(R.id.constraintlayout);
        View view = LayoutInflater.from(CategoryActivity.this).inflate(R.layout.clientdata_dialog, constraintLayout);
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
        builder.setView(view);
        builder.setCancelable(true);
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

    }



    public void showDialog(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ConstraintLayout constraintLayout = findViewById(R.id.constraintlayout);
        View view = LayoutInflater.from(CategoryActivity.this).inflate(R.layout.staff_dialog, constraintLayout);
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        ListView listView = view.findViewById(R.id.listview);
        ArrayList<WorkerData> dataArreyList = new ArrayList<>();
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("Users");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    List<Map<String,String>> rooms = (List<Map<String, String>>) document.get("categories");
                    for (int i = 0; i < rooms.size(); i++) {
                        Map<String, String> map = rooms.get(i);
                        for (String value : map.values()) {
                            if (value.equals(categoryName)){
                                WorkerData workerData = new WorkerData((String) document.get("name"));

                                dataArreyList.add(workerData);
                                WorkerListAdapter workerListAdapter = new WorkerListAdapter(CategoryActivity.this, dataArreyList);
                                listView.setAdapter(workerListAdapter);

                            }
                        }
                    }
                }
            } else {
                Log.e("Error", "Error getting documents: ", task.getException());
            }
        });
        alertDialog.show();
    }




    @Override
    public void onBackPressed() {
    }
}