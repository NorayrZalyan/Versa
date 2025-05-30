package com.example.versa;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.versa.Dialog.LoadingDialog;
import com.example.versa.bottomSheet.AddClientBottomSheet;
import com.example.versa.bottomSheet.CreateCategoryBottomSheet;
import com.example.versa.clients.ClientData;
import com.example.versa.clients.ClientListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String categoryName;
    private ArrayList<ClientData> clientDataArrayList = new ArrayList<>();

    public CategoryFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View context = inflater.inflate(R.layout.fragment_category, container, false);
        String name = requireArguments().getString("name");
        String roomId = requireArguments().getString("roomId");


        categoryName = name;

        context.findViewById(R.id.add_clientBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("roomId", roomId);
                bundle.putString("category", categoryName);
                AddClientBottomSheet bottomSheet = new AddClientBottomSheet();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());


            }
        });



        db.collection("Rooms").document(roomId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {

                                List<Map<String, Object>> categories =(List<Map<String, Object>>) documentSnapshot.get("categories");
                                for (int i = 0; i < categories.size(); i++) {
                                    if (categories.get(i).get("name").equals(name)){
                                        List<Map<String, Object>> clients = (List<Map<String, Object>>) categories.get(i).get("clients");
                                        String[] nameList = new String[clients.size()];
                                        for (int j = 0; j < clients.size(); j++) {
                                            nameList[j] = (String) clients.get(j).get("name");
                                        }
                                        for (int e = 0; e < nameList.length; e++) {
                                            ClientData clientData = new ClientData(nameList[e]);
                                            clientDataArrayList.add(clientData);
                                        }
                                        ClientListAdapter clientListAdapter = new ClientListAdapter(getActivity(), clientDataArrayList, roomId, "", categoryName, "CategoryFragment");
                                        ListView listView = context.findViewById(R.id.listview);
                                        listView.setAdapter(clientListAdapter);
                                        listView.setClickable(true);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                a(
                                                        (String) clients.get(position).get("name"),
                                                        (String) clients.get(position).get("phone"),
                                                        (String) clients.get(position).get("email"),
                                                        (String) clients.get(position).get("description"),
                                                        position,
                                                        roomId,
                                                        context
                                                );
                                            }
                                        });
                                    }
                                }

                            } else {
                                Log.w("Error", "onComplete: no such document");
                            }
                        } else {
                            Log.e("ERROR", "onComplete: ",task.getException() );
                        }

                    }
                });




        return context;
    }

    public void a(String name, String phone, String email, String description, int position, String roomId, View context){

        ConstraintLayout constraintLayout = context.findViewById(R.id.constraintlayout);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.clientdata_dialog, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();


        LoadingDialog loadingDialog = new LoadingDialog(getActivity());


        EditText nameEt = view.findViewById(R.id.nameEt);
        nameEt.setText(name);
        EditText phoneEt = view.findViewById(R.id.phoneEt);
        phoneEt.setText(phone);
        EditText emailEt = view.findViewById(R.id.emailEt);
        emailEt.setText(email);
        EditText descriptionTv = view.findViewById(R.id.descriptionEt);
        descriptionTv.setText(description);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoading();
                EditText nameEt = view.findViewById(R.id.nameEt);
                EditText phoneEt = view.findViewById(R.id.phoneEt);
                EditText emailEt = view.findViewById(R.id.emailEt);
                EditText descriptionEt = view.findViewById(R.id.descriptionEt);


                db.collection("Rooms").document(roomId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){
                                        List<Map<String, Object>> categories = (List<Map<String, Object>>) documentSnapshot.get("categories");
                                        for (int i = 0; i < categories.size(); i++) {
                                            if (categories.get(i).get("name").equals(categoryName)){
                                                Map<String, Object> categoryMap = (Map<String, Object>) categories.get(i);
                                                List<Map<String, Object>> clients = (List<Map<String, Object>>) categoryMap.get("clients");
                                                clients.get(position).put("name",nameEt.getText().toString());
                                                clients.get(position).put("phone",phoneEt.getText().toString());
                                                clients.get(position).put("email",emailEt.getText().toString());
                                                clients.get(position).put("description",descriptionEt.getText().toString());
                                                categoryMap.put("clients", clients);
                                                Map<String, Object> update = new HashMap<>();
                                                update.put("categories", categories);
                                                db.collection("Rooms").document(roomId).update(update);
                                                loadingDialog.dismisDialog();
                                                alertDialog.cancel();
                                                getActivity().recreate();
                                                break;
                                            }
                                        }
                                    } else {
                                    }
                                } else {
                                }
                            }
                        });



            }
        });




    }

}