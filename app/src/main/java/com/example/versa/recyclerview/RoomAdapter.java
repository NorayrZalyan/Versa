package com.example.versa.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.versa.R;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<RoomRecView> Rooms;

    public RoomAdapter(Context context, List<RoomRecView> Rooms){
        this.Rooms = Rooms;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.roomitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomRecView roomRecView = Rooms.get(position);
        holder.roomname.setText(roomRecView.getRoomName());
        holder.Id.setText(roomRecView.getRoomId());
    }

    @Override
    public int getItemCount() {
        return Rooms.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView roomname, Id;

        public ViewHolder(View view) {
            super(view);

            roomname = view.findViewById(R.id.RoomName);
            Id = view.findViewById(R.id.RoomId);

        }
    }


}
