package com.example.versa.category;

import android.app.Activity;
import android.content.Context;
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

import com.example.versa.R;
import com.example.versa.bottomSheet.GiveAccessBottomSheet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class CategoryListAdapter extends ArrayAdapter<CategoryData> {
    private FragmentActivity activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String uid = FirebaseAuth.getInstance().getUid();
    private String roomId;

    public CategoryListAdapter(@NonNull FragmentActivity activity, ArrayList<CategoryData> dataArrayList, String roomId) {
        super(activity, R.layout.list_item, dataArrayList);
        this.activity = activity;
        this.roomId = roomId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        CategoryData listData = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }


        ImageView more = view.findViewById(R.id.moreIv);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(parent.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.category_list_item_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.option1) {


                            db.collection("Users").document(uid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if (documentSnapshot.exists()){
                                                    if (documentSnapshot.get("jobtitle").equals("Admin")){

                                                        Log.d("TAG", "onMenuItemClick: "+position);

                                                        DocumentReference userRef = db.collection("Rooms").document(roomId);
                                                        userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                                                            if (documentSnapshot1.exists()) {
                                                                List<String> categories = (List<String>) documentSnapshot1.get("categories");
                                                                if (categories != null && categories.size() > 0) {
                                                                    categories.remove(position); // Удаляем элемент по индексу, например, нулевой
                                                                    userRef.update("categories", categories)
                                                                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Element removed by index"))
                                                                            .addOnFailureListener(e -> Log.e("Firestore", "Error updating categories", e));
                                                                }
                                                            }
                                                        }).addOnFailureListener(e -> Log.e("Firestore", "Error getting document", e));

                                                        db.collection("Users")
                                                                .whereArrayContains("categories", String.valueOf(position)) // Проверяем, содержит ли массив "categories" значение "2"
                                                                .get()
                                                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                                                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                                                                        Log.d("Firestore", "User ID: " + document.getId());
                                                                        db.collection("Users").document(document.getId())
                                                                                .update("categories", FieldValue.arrayRemove(String.valueOf(position)));
                                                                    }
                                                                })
                                                                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching users", e));
                                                    } else {
                                                        Toast.makeText(getContext(), "you are not an admin", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {

                                                }
                                            } else {
                                            }
                                        }
                                    });


                            return true;
                        } else if (id == R.id.option2) {


                            db.collection("Users").document(uid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if (documentSnapshot.exists()){
                                                    if (documentSnapshot.get("jobtitle").equals("Admin")){
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("position", String.valueOf(position));
                                                        GiveAccessBottomSheet bottomSheet = new GiveAccessBottomSheet();
                                                        bottomSheet.setArguments(bundle);
                                                        bottomSheet.show(activity.getSupportFragmentManager(), "GiveAccessBottomSheet");
                                                    } else {
                                                        Toast.makeText(getContext(), "you are not an admin", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {

                                                }
                                            } else {
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


        TextView roomName = view.findViewById(R.id.listName);
        TextView roomId = view.findViewById(R.id.listId);

        roomName.setText(listData.name);
        

        return view;
    }
}