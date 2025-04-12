package com.example.versa;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.versa.Auth.MainActivity;
import com.example.versa.bottomSheet.CreateCategoryBottomSheet;
import com.example.versa.category.CategoryData;
import com.example.versa.category.CategoryListAdapter;
import com.example.versa.clients.ClientData;
import com.example.versa.clients.ClientListAdapter;
import com.example.versa.databinding.ActivityDetailedBinding;
import com.example.versa.staff.WorkerData;
import com.example.versa.staff.WorkerListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailedActivity extends AppCompatActivity {

    private ActivityDetailedBinding binding;
    public String roomId;
    private ArrayList<CategoryData> dataArrayList = new ArrayList<>();
    FirebaseAuth mAuth;
    CategoryListAdapter categoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String roomName = intent.getStringExtra("roomName");
        roomId = intent.getStringExtra("roomId");
        binding.roomNameTv.setText(roomName);
        binding.roomIdTV.setText(roomId);


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

                        if (document.getString("jobtitle").equals("Admin")){
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
                bundle.putString("roomid", roomId);
                CreateCategoryBottomSheet bottomSheet = new CreateCategoryBottomSheet();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            }
        });




        String uid = FirebaseAuth.getInstance().getUid();
        db.collection("Users").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){

                                List<Map<String, String>> userCategories = (List<Map<String, String>>) documentSnapshot.get("categories");
                                Log.d("TAG", "onComplete: "+userCategories);
                                ArrayList<String> nameList = new ArrayList<>();
                                for (int i = 0; i < userCategories.size(); i++) {
                                    for (String key : userCategories.get(i).keySet()) {
                                        Log.d("TAG", "onComplete: "+key);
                                        if (key.equals(roomId)){
                                            nameList.add(userCategories.get(i).get(key));
                                        }
                                    }
                                }
                                for (int i = 0; i < nameList.size(); i++) {
                                    CategoryData categoryData = new CategoryData(nameList.get(i));
                                    dataArrayList.add(categoryData);
                                }
                                categoryListAdapter = new CategoryListAdapter(DetailedActivity.this, dataArrayList, roomId, roomName, nameList);
                                binding.listview.setAdapter(categoryListAdapter);
                                binding.listview.setClickable(true);
                                binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Intent intent = new Intent(DetailedActivity.this, CategoryActivity.class);
                                        intent.putExtra("roomId", roomId);
                                        intent.putExtra("roomName", roomName);
                                        intent.putExtra("categoryName", nameList.get(position));
                                        startActivity(intent);

                                    }
                                });

                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

    binding.backIv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(DetailedActivity.this, HomeActivity.class));
        }
    });

        binding.menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(DetailedActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.option1) {

                            showDialog();

                            return true;
                        } else if (id == R.id.option2) {

                            Intent intent = new Intent(DetailedActivity.this, HistoryActivity.class);
                            intent.putExtra("roomId",roomId);
                            intent.putExtra("roomName",roomName);
                            startActivity(intent);

                            return true;
                        }
                        return false;
                    }
                });
                popup.show();

            }
        });


    }
    public void showDialog(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ConstraintLayout constraintLayout = findViewById(R.id.constraintlayout);
        View view = LayoutInflater.from(DetailedActivity.this).inflate(R.layout.staff_dialog, constraintLayout);
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedActivity.this);
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
                    List<Map<String,String>> rooms = (List<Map<String, String>>) document.get("rooms");
                    for (int i = 0; i < rooms.size(); i++) {
                        Map<String, String> map = rooms.get(i);
                        for (String key : map.keySet()) {
                            if (key.equals(roomId)){
                                WorkerData workerData = new WorkerData((String) document.get("name"));

                                dataArreyList.add(workerData);
                                WorkerListAdapter workerListAdapter = new WorkerListAdapter(DetailedActivity.this, dataArreyList);
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