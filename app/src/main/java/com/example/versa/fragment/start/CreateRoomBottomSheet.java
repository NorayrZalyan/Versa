package com.example.versa.fragment.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.databinding.RoomBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateRoomBottomSheet extends BottomSheetDialogFragment {

    private RoomBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = RoomBottomSheetBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }
}
