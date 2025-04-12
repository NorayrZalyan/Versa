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







    }
    @Override
    public void onBackPressed() {
    }
}