package com.example.versa;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.versa.databinding.ActivityHomeBinding;
import com.example.versa.fragment.start.ForChiefFragment;

public class HomeActivity extends AppCompatActivity {

    private FrameLayout fragmentContainer;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), new ForChiefFragment()).commit();

    }
}