package com.example.versa.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.versa.HomeActivity;
import com.example.versa.R;
import com.example.versa.databinding.ActivityConfirmationEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmationEmailActivity extends AppCompatActivity {

    private ActivityConfirmationEmailBinding binding;
    private Handler handler = new Handler();
    private int count = 0;
    private final int maxCount = 5;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmationEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();


        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String uid = intent.getStringExtra("uid");





        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count < maxCount) {


                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        user.reload().addOnCompleteListener(task -> {
                            if (user.isEmailVerified()) {
                                User newUser = new User(uid, name, email);
                                db.collection("Users").document(uid).set(newUser)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task2) {
                                                if (task2.isSuccessful()) {
                                                    Toast.makeText(ConfirmationEmailActivity.this, "Registration is completed", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(ConfirmationEmailActivity.this, HomeActivity.class));
                                                } else {
                                                    Toast.makeText(ConfirmationEmailActivity.this, "Error saving profile" + task2.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                Toast.makeText(ConfirmationEmailActivity.this, "Email Verified! Logging in...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ConfirmationEmailActivity.this, HomeActivity.class));
                            }
                        });
                    }

                    Log.d("Counter", "Iteration: " + count);
                    count++;
                    handler.postDelayed(this, 7000);
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    startActivity(new Intent(ConfirmationEmailActivity.this, RegisterActivity.class));
                                } else {
                                    Log.e("ERROR", " delete user ",task.getException());
                                }
                            }
                        });
                    Toast.makeText(ConfirmationEmailActivity.this, "The link has expired. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.d("Counter", "Counter stopped at: " + count);
                }
            }
        }, 7000);



    }
}