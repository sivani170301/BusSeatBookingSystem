package com.example.barcodescanner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private String num[];
    private int icon[];
    private LayoutInflater mInflater;
    private List<String> available_seats;

    // data is passed into the constructor
    MyRecyclerViewAdapter(int[] icon, String[] num, List<String> available_seats, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.num = num;
        this.icon = icon;
        this.available_seats = available_seats;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_frame, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(available_seats.contains(num[position])){
            ImageViewCompat.setImageTintList(holder.seatIconImg, ColorStateList.valueOf(0xFF4CAF52));
            holder.seatNumTV.setTextColor(0xFF4CAF52);
        }
        holder.seatNumTV.setText(num[position]);
        holder.seatIconImg.setImageResource(icon[position]);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return num.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView seatNumTV;
        private ImageView seatIconImg;

        ViewHolder(View itemView) {
            super(itemView);
            seatNumTV = itemView.findViewById(R.id.seat_num);
            seatIconImg = itemView.findViewById(R.id.seat_icon);
            ImageViewCompat.setImageTintList(seatIconImg, ColorStateList.valueOf(0xFFFF0000));
            seatNumTV.setTextColor(0xFFFF0000);


        }

    }
}
