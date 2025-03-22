package com.example.versa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.versa.bottomSheet.AddClientBottomSheet;
import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
import com.example.versa.clients.ClientListAdapter;
import com.example.versa.clients.Client;
import com.example.versa.clients.ClientData;
import com.example.versa.databinding.ActivityCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private FirebaseFirestore db;
    private ArrayList<ClientData> clientData = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String roomName = intent.getStringExtra("roomName");
        String id = intent.getStringExtra("id");
        int position = intent.getIntExtra("position", 0);
        binding.categoryNameTv.setText(name);


        binding.addClientBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("roomId", id);
                bundle.putInt("category", position);
                AddClientBottomSheet bottomSheet = new AddClientBottomSheet();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            }
        });


        db.collection("Rooms").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Map<String, Object>> categoriesList = (List<Map<String, Object>>) document.get("categories");
                                Map<String, Object> categoryMap = categoriesList.get(position);
                                List<Map<String, String>> clientsList = (List<Map<String, String>>) categoryMap.get("clients");

                                String[] nameList = new String[clientsList.size()];
                                for (int i = 0; i < clientsList.size(); i++) {
                                    nameList[i] = clientsList.get(i).get("name");
                                }
                                for (int i = 0; i < nameList.length; i++) {
                                    ClientData clientData1 = new ClientData(nameList[i]);
                                    clientData.add(clientData1);
                                }
                                ClientListAdapter clientListAdapter = new ClientListAdapter( CategoryActivity.this, clientData, id, position);
                                binding.listview.setAdapter(clientListAdapter);
                                binding.listview.setClickable(true);
                                binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                        List<Map<String, Object>> categoriesList = (List<Map<String, Object>>) document.get("categories");

                                        for (int i = 0; i < categoriesList.size(); i++) {
                                            Map<String, Object> categoryMap = categoriesList.get(i);

                                            List<Map<String, String>> clientsList = (List<Map<String, String>>) categoryMap.get("clients");
                                            for (int j = 0; j < clientsList.size(); j++) {

                                                if (clientsList.get(position).get("name").equals(nameList[position])){
                                                    Log.d("TAG", "onItemClick: "+clientsList.get(position).get("name")+ nameList[position]);

                                                    String clientName = clientsList.get(position).get("name");
                                                    String clientPhone = clientsList.get(position).get("phone");
                                                    String clientEmail = clientsList.get(position).get("email");
                                                    String clientdescription = clientsList.get(position).get("description");

                                                    ClientDataDialog clientData1 = new ClientDataDialog(CategoryActivity.this, clientName, clientPhone, clientEmail, clientdescription);
                                                    clientData1.startLoading();

                                                }

                                            }


                                        }
                                        List<Map<String, String>> clientsList = (List<Map<String, String>>) categoryMap.get("clients");

                                    }
                                });

                            } else {

                            }


                        } else {
                            Log.e("TAG", "" + task.getException());
                        }
                    }
                });

    }
}