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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.versa.CategoryFragment;
import com.example.versa.Dialog.LoadingDialog;
import com.example.versa.R;
import com.example.versa.bottomSheet.GiveAccessBottomSheet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private ArrayList<CategoryData> mData;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private FragmentManager fragmentManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String roomId;
    private String roomName;
    private ArrayList<String> nameList = new ArrayList<>();

    // data is passed into the constructor
    public CategoryListAdapter(Context context, ArrayList<CategoryData> data, ArrayList<String> nameList, String roomId, FragmentManager fragmentManager, String roomName) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.roomId = roomId;
        this.fragmentManager = fragmentManager;
        this.roomName = roomName;
        this.nameList = nameList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryData animal = mData.get(position);
        holder.myTextView.setText(animal.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.listName);
            itemView.setOnClickListener(this);


            ImageView moreIv = itemView.findViewById(R.id.moreIv);
            moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, v);
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
                                                                        if (!(category.containsKey(roomId) && category.get(roomId).equals(nameList.get(getAdapterPosition())))) {
                                                                            updatedCategories.add(category);
                                                                        }
                                                                    }
                                                                    if (updatedCategories.size() != categories.size()) {
                                                                        db.collection("Users").document(userDoc.getId())
                                                                                .update("categories", updatedCategories)
                                                                                .addOnSuccessListener(aVoid -> {
                                                                                    Log.d("Update", "Categories updated successfully");
                                                                                    db.collection("Rooms").document(roomId)
                                                                                            .get()
                                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                    if (task.isSuccessful()){
                                                                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                                                                        if (documentSnapshot.exists()){
                                                                                                            List<Map<String, String>> categories = (List<Map<String, String>>) documentSnapshot.get("categories");
                                                                                                            categories.remove(getAdapterPosition());
                                                                                                            Map<String, Object> updateMap = new HashMap<>();
                                                                                                            updateMap.put("categories",categories);
                                                                                                            db.collection("Rooms").document(roomId).update(updateMap);
                                                                                                        }
                                                                                                    }

                                                                                                }
                                                                                            });



                                                                                    FragmentManager fm = fragmentManager;
                                                                                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                                                                        fm.popBackStack();
                                                                                    }                                                                                    loadingDialog.dismisDialog();
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
                                bundle.putString("categoryName", getItem(getAdapterPosition()));
                                bundle.putString("roomId", roomId);
                                bundle.putString("roomName", roomName);
                                GiveAccessBottomSheet bottomSheet = new GiveAccessBottomSheet();
                                bottomSheet.setArguments(bundle);
                                bottomSheet.show(fragmentManager, "GiveAccessBottomSheet");


                                return true;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        }





        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("name", getItem(getAdapterPosition()));
            bundle.putString("roomId", roomId);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, CategoryFragment.class, bundle)
                    .addToBackStack(null).commit();
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).getName();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
