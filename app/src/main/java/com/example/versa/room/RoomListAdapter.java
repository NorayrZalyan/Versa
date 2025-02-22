package com.example.versa.room;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.versa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoomListAdapter extends ArrayAdapter<RoomData> {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    String uid = FirebaseAuth.getInstance().getUid();
    public RoomListAdapter(@NonNull Context context, ArrayList<RoomData> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
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

                            db.collection("Rooms").document(String.valueOf(listData.id))
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting document", e);
                                        }
                                    });
                            db.collection("Users").document(uid)
                                    .update("roomId", null)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("TAG", "deleted from user");
                                            ((Activity) context).recreate();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("TAG", "not deleted from the user");
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
        roomId.setText(listData.id);



        return view;
    }
}