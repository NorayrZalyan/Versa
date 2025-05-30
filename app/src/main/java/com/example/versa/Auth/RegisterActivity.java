package com.example.versa.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.versa.HomeActivity;
import com.example.versa.Dialog.LoadingDialog;
import com.example.versa.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        loadingDialog = new LoadingDialog(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


        binding.testUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signInWithEmailAndPassword("individualproject2025@gmail.com", "Samsung2025")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Login Test User", "signInWithEmail:success");
                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                } else {
                                    Log.w("Login Test User", "signInWithEmail:failure", task.getException());
                                }
                            }
                        });


            }
        });


        binding.signupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.startLoading();

                final String name = binding.usernameEt.getText().toString().trim();
                final String email = binding.emailEt.getText().toString().trim();
                final String pass = binding.passwordEt.getText().toString().trim();
                final String conf_pass = binding.confPasswordEt.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    loadingDialog.dismisDialog();
                    Toast.makeText(RegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pass.equals(conf_pass)) {
                    loadingDialog.dismisDialog();
                    Log.d("TEST", "onClick: "+pass + " " + conf_pass);
                    Toast.makeText(RegisterActivity.this, "passwords do not match", Toast.LENGTH_SHORT).show();
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
                                        mAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            startActivity(new Intent(RegisterActivity.this, ConfirmationEmailActivity.class)
                                                                    .putExtra("name",name)
                                                                    .putExtra("email",email)
                                                                    .putExtra("uid",uid));
                                                        } else {
                                                            Log.d("ERROR", "onComplete: ."+task.getException());
                                                        }
                                                    }
                                                });

                                    }

                                } else {
                                    loadingDialog.dismisDialog();
                                    Toast.makeText(RegisterActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });






    }
}