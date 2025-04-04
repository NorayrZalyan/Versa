package com.example.versa.room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.Dialog.LoadingDialog;
import com.example.versa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoomListAdapter extends ArrayAdapter<RoomData> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private String uid = FirebaseAuth.getInstance().getUid();
    public RoomListAdapter(@NonNull Context context, ArrayList<RoomData> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        RoomData listData = getItem(position);

        if (view == null){
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


                            new AlertDialog.Builder(context)
                                    .setMessage("Are you sure you want to delete this client?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            LoadingDialog loadingDialog = new LoadingDialog((Activity) context);
                                            loadingDialog.startLoading();

                                            db.collection("Users")
                                                    .get()
                                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                                        for (DocumentSnapshot userDoc : queryDocumentSnapshots) {
                                                            ArrayList<Map<String, String>> rooms = (ArrayList<Map<String, String>>) userDoc.get("rooms");
                                                            if (rooms != null) {
                                                                ArrayList<Map<String, String>> updatedRooms = new ArrayList<>();
                                                                for (Map<String, String> room : rooms) {
                                                                    if (!(room.containsKey(listData.id) && room.get(listData.id).equals(listData.name))) {
                                                                        updatedRooms.add(room);
                                                                    }
                                                                }
                                                                if (updatedRooms.size() != rooms.size()) {
                                                                    db.collection("Users").document(userDoc.getId())
                                                                            .update("rooms", updatedRooms)
                                                                            .addOnSuccessListener(aVoid -> {
                                                                                Log.d("Update", "Categories updated successfully");
                                                                                db.collection("Rooms").document(listData.id).delete();


                                                                                db.collection("Users")
                                                                                        .get()
                                                                                        .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                                                            for (DocumentSnapshot userDoc1 : queryDocumentSnapshots1) {
                                                                                                ArrayList<Map<String, String>> categories = (ArrayList<Map<String, String>>) userDoc1.get("categories");
                                                                                                if (categories != null) {
                                                                                                    ArrayList<Map<String, String>> updatedCategories = new ArrayList<>();
                                                                                                    for (Map<String, String> category : categories) {
                                                                                                        if (!(category.containsKey(listData.id) )) {
                                                                                                            updatedCategories.add(category);
                                                                                                        }
                                                                                                    }
                                                                                                    if (updatedCategories.size() != categories.size()) {
                                                                                                        db.collection("Users").document(userDoc1.getId())
                                                                                                                .update("categories", updatedCategories)
                                                                                                                .addOnSuccessListener(aVoid1 -> {
                                                                                                                    Log.d("Update", "Categories updated successfully");
                                                                                                                })
                                                                                                                .addOnFailureListener(e -> {
                                                                                                                    loadingDialog.dismisDialog();
                                                                                                                    Log.w("Update", "Error updating categories", e);
                                                                                                                });
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(e -> {
                                                                                            loadingDialog.dismisDialog();
                                                                                            Log.w("Error", "Error getting documents", e);
                                                                                        });


                                                                                loadingDialog.dismisDialog();
                                                                                ((Activity) context).recreate();
                                                                            })
                                                                            .addOnFailureListener(e -> {
                                                                                loadingDialog.dismisDialog();
                                                                                Log.w("Update", "Error updating categories", e);
                                                                            });
                                                                }
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        loadingDialog.dismisDialog();
                                                        Log.w("Error", "Error getting documents", e);
                                                    });





                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();






                            return true;
                        }
                        return false;
                    }
                });
                db.collection("Users").document(uid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){
                                        if (documentSnapshot.get("jobtitle").equals("Admin")){
                                            popup.show();
                                        }
                                    } else {
                                    }
                                } else {
                                }
                            }
                        });

            }
        });



        TextView roomName = view.findViewById(R.id.listName);
        TextView roomId = view.findViewById(R.id.listId);

        roomName.setText(listData.name);
        roomId.setText(listData.id);



        return view;
    }
}