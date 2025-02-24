package com.example.versa.category;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.versa.R;
import com.example.versa.bottomSheet.GiveAccessBottomSheet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class CategoryListAdapter extends ArrayAdapter<CategoryData> {
    private FragmentActivity activity;

    public CategoryListAdapter(@NonNull FragmentActivity activity, ArrayList<CategoryData> dataArrayList) {
        super(activity, R.layout.list_item, dataArrayList);
        this.activity = activity;
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

                            

                            return true;
                        } else if (id == R.id.option2) {

                            Bundle bundle = new Bundle();
                            bundle.putString("position", String.valueOf(position));
                            GiveAccessBottomSheet bottomSheet = new GiveAccessBottomSheet();
                            bottomSheet.setArguments(bundle);
                            bottomSheet.show(activity.getSupportFragmentManager(), "GiveAccessBottomSheet");

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