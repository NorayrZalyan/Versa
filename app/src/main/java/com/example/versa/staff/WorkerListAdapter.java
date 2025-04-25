package com.example.versa.staff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.DetailedActivity;
import com.example.versa.R;
import com.example.versa.SelectCategoryActivity;
import com.example.versa.clients.ClientData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkerListAdapter extends ArrayAdapter<WorkerData> {

    private Context context;
    private String activity;
    private String roomId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public WorkerListAdapter(@NonNull Context context, ArrayList<WorkerData> dataArrayList, String roomId, String activity) {
        super(context, R.layout.list_item, dataArrayList);
        this.context = context;
        this.roomId = roomId;
        this.activity = activity;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        WorkerData listData = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }


        ImageView more = view.findViewById(R.id.moreIv);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(parent.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.room_list_item_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.option1) {


                            db.collection("Users")
                                    .whereEqualTo("email", listData.name)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    if (document.get("jobtitle").equals("Admin")){
                                                        Toast.makeText(getContext(), "you can't remove the admin", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }

                                                    List<Map<String, String>> categories = (List<Map<String, String>>) document.get("categories");
                                                    for (int i = 0; i < categories.size(); i++) {
                                                        for (String key : categories.get(i).keySet()) {
                                                            if (key.equals(roomId)){
                                                                categories.remove(i);
                                                            }
                                                        }
                                                    }
                                                    Map<String, Object> updateMap = new HashMap<>();
                                                    updateMap.put("categories", categories);
                                                    db.collection("Users").document(document.getId())
                                                            .update(updateMap)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(getContext(), "user has been deleted", Toast.LENGTH_SHORT).show();
                                                                    ((Activity) context).recreate();
                                                                }
                                                            });
                                                    if (activity.equals("DetailedActivity")){
                                                        List<Map<String, String>> rooms = (List<Map<String, String>>) document.get("rooms");
                                                        for (int j = 0; j < rooms.size(); j++) {
                                                            for (String key : rooms.get(j).keySet()) {
                                                                if (key.equals(roomId)){
                                                                    rooms.remove(j);
                                                                }
                                                            }
                                                        }
                                                        Map<String, Object> updateMap1 = new HashMap<>();
                                                        updateMap1.put("rooms", rooms);
                                                        db.collection("Users").document(document.getId())
                                                                .update(updateMap1)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(getContext(), "user has been deleted", Toast.LENGTH_SHORT).show();
                                                                        ((Activity) context).recreate();
                                                                    }
                                                                });
                                                    }



                                                }
                                            } else {
                                                Log.d("TEST", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });


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
        TextView listid = view.findViewById(R.id.listId);
        listid.setText(listData.jobTitle);

        return view;
    }
}
