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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Context context;
    private String roomName;
    private String categoryName;


    public ClientListAdapter(@NonNull Context context, ArrayList<ClientData> dataArrayList, String roomId, String roomName, String categoryName) {
        super(context, R.layout.list_item, dataArrayList);
        this.context = context;
        this.roomId = roomId;
        this.roomName = roomName;
        this.categoryName = categoryName;
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

                            Intent intent = new Intent(context , SelectCategoryActivity.class);
                            intent.putExtra("roomId", roomId);
                            intent.putExtra("roomName", roomName);
                            intent.putExtra("clientPosition", position);
                            intent.putExtra("categoryName", categoryName);
                            context.startActivity(intent);

                            return true;
                        } else if (id == R.id.option2) {
//                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//                            DocumentReference docRef = db.collection("Rooms").document(roomId);
//
//                                    docRef.get()
//                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                            if (task.isSuccessful()) {
//                                                DocumentSnapshot document = task.getResult();
//                                                if (document.exists()) {
//                                                    List<Map<String, Object>> categoriesList = (List<Map<String, Object>>) document.get("categories");
//                                                    Map<String, Object> categoryMap = categoriesList.get(position);
//                                                    List<Map<String, String>> clientsList = (List<Map<String, String>>) categoryMap.get("clients");
//                                                    clientsList.remove(position);
//                                                    categoryMap.put("clients", clientsList);
//                                                    Map<String, Object> updateMap = new HashMap<>();
//                                                    updateMap.put("categories", categoriesList);
//                                                    docRef.update(updateMap);
//                                                    ((Activity) context).recreate();
//                                                }
//                                            }
//                                        }
//                                    });

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