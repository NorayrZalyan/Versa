package com.example.versa.fragment.start;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.versa.databinding.ForchiefFragmentBinding;
import com.example.versa.recyclerview.RoomAdapter;
import com.example.versa.recyclerview.RoomRecView;

import java.util.ArrayList;


public class ForChiefFragment extends Fragment {
    private ForchiefFragmentBinding binding;
    private RecyclerView recView;
    private ArrayList<RoomRecView> rooms = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ForchiefFragmentBinding.inflate(inflater, container, false);
        recView = binding.recView;


        initializeData();
        recView = binding.recView;
        RoomAdapter roomAdapter = new RoomAdapter(this, rooms);

        recView.setAdapter(roomAdapter);




//        binding.createRoomBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CreateRoomBottomSheet roomBottomSheet =  new CreateRoomBottomSheet();
//                roomBottomSheet.show(getActivity().getSupportFragmentManager(), "room bottom sheet");
//            }
//        });

        return binding.getRoot();

    }

    private void initializeData(){
        rooms.add(new RoomRecView("Room 1", 1));
        rooms.add(new RoomRecView("Room 2", 2));
        rooms.add(new RoomRecView("Room 3", 3));
    }

}