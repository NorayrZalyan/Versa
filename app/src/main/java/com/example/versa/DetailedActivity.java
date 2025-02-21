package com.example.versa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.versa.bottomSheet.CreateCategoryBottomSheet;
import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
import com.example.versa.databinding.ActivityDetailedBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailedActivity extends AppCompatActivity {

    private ActivityDetailedBinding binding;
    private ArrayList<CategoryData> dataArrayList = new ArrayList<>();
    FirebaseAuth mAuth;
    CategoryListAdapter categoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        binding.roomNameTv.setText(name);
        binding.roomIdTV.setText(id);






        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = mAuth.getCurrentUser();
        String Uid = fUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("1", "DocumentSnapshot data: " + document.getData());

                        if (document.getString("jobtitle").equals("Owner")){
                            binding.createCategoryBt.setVisibility(View.VISIBLE);
                        } else if(document.getString("jobtitle").equals("Other")) {
                            binding.createCategoryBt.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });





        binding.createCategoryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("roomid", id);
                CreateCategoryBottomSheet bottomSheet = new CreateCategoryBottomSheet();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            }
        });



        DocumentReference docRef = db.collection("Rooms").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                        // Получаем список категорий
                        List<Map<String, Object>> categories = (List<Map<String, Object>>) document.get("categories");

                        if (categories != null && !categories.isEmpty()) {
                            String name = (String) categories.get(0).get("name");
                            Log.d("TAG", "First category name: " + name);
                            String[] nameList = new String[categories.size()];
                            for (int i = 0; i < categories.size(); i++) {
                                nameList[i] = (String) categories.get(i).get("name");
                            }
                            for (int i = 0; i < nameList.length; i++) {
                                CategoryData categoryData = new CategoryData(nameList[i]);
                                dataArrayList.add(categoryData);
                            }
                            categoryListAdapter = new CategoryListAdapter(DetailedActivity.this, dataArrayList);
                            binding.listview.setAdapter(categoryListAdapter);
                            binding.listview.setClickable(true);



                            binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long longid) {
                                    Log.d("TAG", "onItemClick: "+position);
                                    Intent intent = new Intent(DetailedActivity.this, CategoryActivity.class);
                                    intent.putExtra("name", nameList[position]);
                                    intent.putExtra("id",id);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                            });


                        } else {
                            Log.d("TAG", "Categories list is empty or null");
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });



    }
}