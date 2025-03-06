package com.example.versa.clients;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.versa.CategoryActivity;
import com.example.versa.DetailedActivity;
import com.example.versa.R;
import com.example.versa.SelectCategoryActivity;
import com.example.versa.bottomSheet.GiveAccessBottomSheet;
import com.example.versa.clients.ClientData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class ClientListAdapter extends ArrayAdapter<ClientData> {

    private String roomId;
    private Context context1;
    private int category;

    public ClientListAdapter(@NonNull Context context, ArrayList<ClientData> dataArrayList, String roomId,  int category) {
        super(context, R.layout.list_item, dataArrayList);
        this.context1 = context;
        this.roomId = roomId;
        this.category = category;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ClientData listData = getItem(position);
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }


        ImageView more = view.findViewById(R.id.moreIv);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(parent.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.client_list_item_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.option1) {

                            Intent intent = new Intent(context1 , SelectCategoryActivity.class);
                            intent.putExtra("roomId", roomId);
                            intent.putExtra("position", position);
                            intent.putExtra("category", category);
                            context1.startActivity(intent);

                            return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });



        TextView listName = view.findViewById(R.id.listName);
        listName.setText(listData.name);

        return view;
    }
}