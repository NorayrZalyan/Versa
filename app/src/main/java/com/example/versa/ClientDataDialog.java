package com.example.versa;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TextView;

public class ClientDataDialog {

    Activity activity;
    AlertDialog dialog;
    String name;
    String phone;
    String email;
    String description;

    public ClientDataDialog(Activity activity, String name, String phone, String email, String description){
        this.activity = activity;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.description = description;
    }

    public void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.clientdatadialog, null));
        builder.setCancelable(true);
        dialog = builder.create();

        TextView clientName = inflater.inflate(R.layout.clientdatadialog, null).findViewById(R.id.client_nameTv);
        TextView clientPhone = inflater.inflate(R.layout.clientdatadialog, null).findViewById(R.id.client_phoneTv);
        TextView clientEmail = inflater.inflate(R.layout.clientdatadialog, null).findViewById(R.id.client_phoneTv);
        TextView clientDescription = inflater.inflate(R.layout.clientdatadialog, null).findViewById(R.id.client_descriptionTv);

        clientName.setText(name);
        clientPhone.setText(phone);
        clientEmail.setText(email);
        clientDescription.setText(description);


        dialog.show();
    }

    public void dismisDialog(){
        dialog.dismiss();
    }

}
