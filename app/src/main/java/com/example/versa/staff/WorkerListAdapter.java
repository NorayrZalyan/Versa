package com.example.versa.staff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

import com.example.versa.CategoryActivity;
import com.example.versa.DetailedActivity;
import com.example.versa.Dialog.LoadingDialog;
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
    private String categoryName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public WorkerListAdapter(@NonNull Context context, ArrayList<WorkerData> dataArrayList, String roomId, String activity, String categoryName) {
        super(context, R.layout.list_item, dataArrayList);
        this.context = context;

        this.roomId = roomId;
        this.activity = activity;
        this.categoryName = categoryName;
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


                String uid = FirebaseAuth.getInstance().getUid();
                db.collection("Users").document(uid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){

                                        List<Map<String, Object>> rooms = (List<Map<String, Object>>) documentSnapshot.get("rooms");
                                        for (int i = 0; i < rooms.size(); i++) {
                                            if (rooms.get(i).get("roomId").equals(roomId)){
                                                if (!rooms.get(i).get("jobTitle").equals("Admin")){
                                                    Toast.makeText(context, "Only an admin can manage employees", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }
                                        }

                                    } else {
                                        Log.d("TEST", "onComplete: no such document");
                                    }
                                } else {
                                    Log.e("ERROR", "onComplete: ", task.getException());
                                }
                            }
                        });


                PopupMenu popup = new PopupMenu(parent.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.room_list_item_menu, popup.getMenu());
                if (listData.jobTitle.equals("Admin")){
                    MenuItem item = popup.getMenu().getItem(0);
                    SpannableString spanString = new SpannableString(item.getTitle());
                    spanString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanString.length(), 0); // Красный цвет
                    item.setTitle(spanString);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.option1) {

                            if (listData.jobTitle.equals("Admin")){
                                Toast.makeText(context, "You cannot delete the administrator", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            new AlertDialog.Builder(context)
                                    .setMessage("Are you sure you want to delete this client?")
                                    .setCancelable(true)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            LoadingDialog loadingDialog = new LoadingDialog((Activity) context);
                                            loadingDialog.startLoading();
                                            db.collection("Users")
                                                    .whereEqualTo("email", listData.name)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                                    if (activity.equals("DetailedActivity")){
                                                                        List<Map<String, String>> categories = (List<Map<String, String>>) document.get("categories");
                                                                        List<Map<String, Object>> rooms = (List<Map<String, Object>>) document.get("rooms");
                                                                        for (int i = 0; i < categories.size(); i++){
                                                                            for (String key : categories.get(i).keySet()) {
                                                                                if (key.equals(roomId)){
                                                                                    categories.remove(i);
                                                                                }
                                                                            }
                                                                        }
                                                                        for (int i = 0; i < rooms.size(); i++) {
                                                                            if (rooms.get(i).get("roomId").equals(roomId)){
                                                                                rooms.remove(i);
                                                                            }
                                                                        }
                                                                        Map<String, Object> updateMAp = new HashMap<>();
                                                                        updateMAp.put("categories",categories);
                                                                        updateMAp.put("rooms",rooms);
                                                                        db.collection("Users").document(document.getId()).update(updateMAp);
                                                                        Toast.makeText(getContext(), "employee removed" ,Toast.LENGTH_LONG).show();
                                                                        ((Activity) context).recreate();
                                                                        loadingDialog.dismisDialog();

                                                                    } else if (activity.equals("CategoryActivity")) {
                                                                        List<Map<String, String>> categories = (List<Map<String, String>>) document.get("categories");
                                                                        for (int i = 0; i < categories.size(); i++){
                                                                            for (String key : categories.get(i).keySet()) {
                                                                                for (String value : categories.get(i).values()) {
                                                                                    if (key.equals(roomId) && value.equals(categoryName)) {
                                                                                        categories.remove(i);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        Map<String, Object> updateMAp1 = new HashMap<>();
                                                                        updateMAp1.put("categories",categories);
                                                                        db.collection("Users").document(document.getId()).update(updateMAp1);
                                                                        Toast.makeText(getContext(), "employee removed" ,Toast.LENGTH_LONG).show();
                                                                        ((Activity) context).recreate();
                                                                        loadingDialog.dismisDialog();
                                                                    }


                                                                }
                                                            } else {
                                                                Log.d("ERROR", "Error getting documents: ", task.getException());
                                                                loadingDialog.dismisDialog();
                                                            }
                                                        }
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
                                    List<Map<String, Object>> rooms = (List<Map<String, Object>>)documentSnapshot.get("rooms");
                                    for (int i = 0; i < rooms.size(); i++) {
                                        if (rooms.get(i).get("jobTitle").equals("Admin")){
                                            popup.show();
                                        } else {
                                            Toast.makeText(getContext(), "Only the administrator can interact with employees",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                } else {
                                    Log.d("ERROR", "onComplete: Document not found");
                                }
                            } else
                                Log.d("ERROR", "onComplete: "+ task.getException());
                        }
                    });



            }
        });


        TextView listName = view.findViewById(R.id.listName);
        listName.setText(listData.name);
        TextView listid = view.findViewById(R.id.listId);
        listid.setText(listData.jobTitle);

        return view;
    }
}
