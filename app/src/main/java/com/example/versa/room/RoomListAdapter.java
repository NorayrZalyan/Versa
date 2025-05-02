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
import com.example.versa.staff.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RoomListAdapter extends ArrayAdapter<RoomData> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private String uid = FirebaseAuth.getInstance().getUid();
    private String[] idList;
    public RoomListAdapter(@NonNull Context context, ArrayList<RoomData> dataArrayList, String[] idList) {
        super(context, R.layout.list_item, dataArrayList);
        this.context = context;
        this.idList = idList;
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
                                    .setCancelable(true)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            LoadingDialog loadingDialog = new LoadingDialog((Activity) context);



//                                            deleting a room from users
                                            db.collection("Users")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    List<Map<String, Object>> rooms = (List<Map<String, Object>>) document.get("rooms");
                                                                    List<Map<String, String>> categories = (List<Map<String, String>>) document.get("categories");
                                                                    for (int i = 0; i < rooms.size(); i++) {
                                                                        if (rooms.get(i).get("roomId").equals(idList[position])){
                                                                            Log.d("Test", "onComplete: "+rooms.get(i).get("roomId")+" "+i);
                                                                            rooms.remove(i);
                                                                        }
                                                                        for (int j = 0; j < categories.size(); j++) {
                                                                            for (String key :categories.get(j).keySet()) {
                                                                                if (key.equals(idList[position])){
                                                                                    categories.remove(j);
                                                                                }
                                                                            }
                                                                        }
                                                                        Map<String, Object> updateMap = new HashMap<>();
                                                                        updateMap.put("rooms",rooms);
                                                                        updateMap.put("categories",categories);
                                                                        db.collection("Users").document(document.getId()).update(updateMap);
                                                                        ((Activity) context).recreate();
                                                                        break;
                                                                    }
                                                                }
                                                            } else {
                                                                Log.w("Firestore", "Error getting documents.", task.getException());
                                                            }
                                                        }
                                                    });
//                                            deleting a room
                                            db.collection("Rooms").document(idList[position]).delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                ((Activity) context).recreate();
                                                                Toast.makeText(getContext(),"company deleted",Toast.LENGTH_LONG).show();
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
                popup.show();
            }
        });



        TextView roomName = view.findViewById(R.id.listName);


        roomName.setText(listData.name);




        return view;
    }
}