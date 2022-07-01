package com.example.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminPage extends AppCompatActivity {

    private Button logout, viewSeats;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        logout = findViewById(R.id.admin_logout_btn);
        viewSeats = findViewById(R.id.view_seats_button);
        img = findViewById(R.id.img);
        ImageViewCompat.setImageTintList(img, ColorStateList.valueOf(0xFF018294));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminPage.this, "Logged out.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminPage.this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
    });
        viewSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPage.this, BusSeats.class));
            }
        });
}
}