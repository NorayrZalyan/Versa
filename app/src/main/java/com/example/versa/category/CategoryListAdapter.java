package com.example.versa.category;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.versa.Dialog.LoadingDialog;
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

import org.checkerframework.checker.units.qual.A;

public class CategoryListAdapter extends ArrayAdapter<CategoryData> {
    private FragmentActivity activity;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String uid = FirebaseAuth.getInstance().getUid();
    private String roomId;
    private ArrayList<String> nameList;
    private String roomName;

    public CategoryListAdapter(@NonNull FragmentActivity activity, ArrayList<CategoryData> dataArrayList, String roomId, String roomName, ArrayList<String> nameList) {
        super(activity, R.layout.list_item, dataArrayList);
        this.context = activity;
        this.activity = activity;
        this.roomId = roomId;
        this.nameList = nameList;
        this.roomName = roomName;
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
                                                            ArrayList<Map<String, String>> categories = (ArrayList<Map<String, String>>) userDoc.get("categories");
                                                            if (categories != null) {
                                                                ArrayList<Map<String, String>> updatedCategories = new ArrayList<>();
                                                                for (Map<String, String> category : categories) {
                                                                    if (!(category.containsKey(roomId) && category.get(roomId).equals(nameList.get(position)))) {
                                                                        updatedCategories.add(category);
                                                                    }
                                                                }
                                                                if (updatedCategories.size() != categories.size()) {
                                                                    db.collection("Users").document(userDoc.getId())
                                                                            .update("categories", updatedCategories)
                                                                            .addOnSuccessListener(aVoid -> {
                                                                                Log.d("Update", "Categories updated successfully");
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
                        } else if (id == R.id.option2) {

                            Bundle bundle = new Bundle();
                            bundle.putString("categoryName", nameList.get(position));
                            bundle.putString("roomId", roomId);
                            bundle.putString("roomName", roomName);
                            GiveAccessBottomSheet bottomSheet = new GiveAccessBottomSheet();
                            bottomSheet.setArguments(bundle);
                            bottomSheet.show(activity.getSupportFragmentManager(), "GiveAccessBottomSheet");

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

                                        } else {
                                            Toast.makeText(getContext(), "you are not an admin", Toast.LENGTH_LONG).show();
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
        

        return view;
    }
}