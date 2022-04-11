package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class qrscanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    DatabaseReference dbref;
    public static String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        //dbref= FirebaseDatabase.getInstance().getReference("qrdata");
        dbref= FirebaseDatabase.getInstance().getReference().child("bus").child("b01");

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                scannerView.startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void handleResult(Result rawResult) {
        data = rawResult.getText().toString();
        Toast.makeText(qrscanner.this, data, Toast.LENGTH_SHORT).show();
        /*dbref.push().setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        MainActivity.tv.setText("Data Inserted Successfully");
                        onBackPressed();
                    }
                });*/
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String str = dataSnapshot.child(data).getValue().toString();
                // str is either true or false

                if(str.equalsIgnoreCase("true")){
                    //Toast.makeText(qrscanner.this, "true", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), PaymentActivity.class));

                }
                else if(str.equalsIgnoreCase("false")){
                    //Toast.makeText(qrscanner.this, "false", Toast.LENGTH_SHORT).show();
                    //MainActivity.tv.setText("Seat already booked");
                    AlertDialog.Builder builder = new AlertDialog.Builder(qrscanner.this);
                    builder.setTitle("Sorry!!");
                    builder.setMessage("This Seat is already reserved.");
                    builder.setCancelable(false);
                    builder.setNeutralButton("Okay",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(qrscanner.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(qrscanner.this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}