package com.example.versa.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.versa.HomeActivity;
import com.example.versa.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.versa.User;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        binding.signupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = binding.usernameEt.getText().toString().trim();
                final String email = binding.emailEt.getText().toString().trim();
                final String pass = binding.passwordEt.getText().toString().trim();
                final String jobtitle = binding.JobTitleEt.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || jobtitle.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser fUser = mAuth.getCurrentUser();
                                    if (fUser != null){
                                        String uid = fUser.getUid();
                                        User newUser = new User(uid, name, email, jobtitle);
                                        db.collection("Users").document(uid).set(newUser)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task2) {
                                                        if (task2.isSuccessful()){
                                                            Toast.makeText(RegisterActivity.this, "Registration is completed", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                                        } else {
                                                            Toast.makeText(RegisterActivity.this, "Error saving profile" + task2.getException() , Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }





}