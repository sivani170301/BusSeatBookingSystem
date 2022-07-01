package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BusSeats extends AppCompatActivity {
    private MyRecyclerViewAdapter adapter;
    ImageView redImg, greenImg;
    private int icon[] = {R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car,
            R.drawable.car
           };

    private DatabaseReference reference;
    private String num[] = {"01","02","03","04","05","06","07","08",
            "09","10","11","12","13","14","15","16","17","18","19","20"};

    List<String> available_seats = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_seats);

        greenImg = findViewById(R.id.greenImg);
        redImg = findViewById(R.id.redImg);
        ImageViewCompat.setImageTintList(greenImg, ColorStateList.valueOf(0xFF4CAF52));
        ImageViewCompat.setImageTintList(redImg, ColorStateList.valueOf(0xFFFF0000));

        reference = FirebaseDatabase.getInstance().getReference("bus");
        reference.child("b01").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String str = postSnapshot.getValue().toString();
                    if(str.equalsIgnoreCase("true")){
                        available_seats.add(key.substring(3));
                    }
                }

                RecyclerView recyclerView = findViewById(R.id.rv_seats);
                int numberOfColumns = 4;
                recyclerView.setLayoutManager(new GridLayoutManager(BusSeats.this, numberOfColumns));
                adapter = new MyRecyclerViewAdapter(icon, num, available_seats, BusSeats.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}