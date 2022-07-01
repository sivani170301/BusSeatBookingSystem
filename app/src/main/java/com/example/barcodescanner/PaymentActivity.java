package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Button pay_btn;
    TextView tv2, bus_details;
    DatabaseReference dbref, userRef;
    String mob, email, name;
    private FirebaseUser user;
    private String userID;
    String busSeatNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // make dynamic text here
        bus_details = findViewById(R.id.bus_details);

        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        dbref= FirebaseDatabase.getInstance().getReference().child("bus").child("b01");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);
                if(userProfile!=null){
                    name = userProfile.getName();
                    mob = userProfile.getMobileNum();
                    email = userProfile.getEmail();
                    Log.d("SeeHere", "onDataChange: "+mob+" "+email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pay_btn = findViewById(R.id.pay_btn);
        tv2 = findViewById(R.id.tv2);
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amt = 120*100;

                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_nEeFtwvXOh9Ow8");
                //checkout.setImage();

                JSONObject object = new JSONObject();
                try{
                    object.put("name","Bus_Seat_Booking");
                    object.put("description", "Payment for your current bus seat.");
                    object.put("theme.color","");
                    object.put("amount", amt);
                    JSONObject prefill = new JSONObject();
                    prefill.put("contact", mob);
                    prefill.put("email", email);
                    checkout.open(PaymentActivity.this, object);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public void onPaymentSuccess(String s) {
        busSeatNum = qrscanner.qrcodeValue;
        pay_btn.setVisibility(View.GONE);
        bus_details.setVisibility(View.GONE);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("Date here", date);
        tv2.setText("Ticket Confirmation\n\nTransaction Num\n"+s+"\n\nBus Name: KVZ-NLR\nAmount: Rs.120/-"+
                "\nDate: "+date+"\nSeat number: "+
                        busSeatNum.substring(3)+ "\n\n**Have a Safe and Happy Journey**");

        //tv2.setText("Payment Success\n Transaction Num: "+s);
        dbref.child(busSeatNum).setValue("false");
        //MainActivity.tv.setText("Payment Success\n Transaction Num: "+s);
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle("Payment Success!");
        builder.setMessage("Transaction Num:"+s+"\n\nSeat num "+busSeatNum.substring(3)+
                " allotted to you successfully!");




        builder.setCancelable(false);
        builder.setNeutralButton("Okay",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                //screenshot here
                View view1 = getWindow().getDecorView().getRootView();
                view1.setDrawingCacheEnabled(true);

                Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());
                view1.setDrawingCacheEnabled(false);

                String filePath = Environment.getExternalStorageDirectory()+"/Download/"+ Calendar.getInstance().getTime().toString().replaceAll(":", ".")+".jpg";
                Log.d("SeeHere", "filepath here");
                File fileScreenshot = new File(filePath);

                FileOutputStream fileOutputStream = null;

                try{
                    fileOutputStream = new FileOutputStream(fileScreenshot);
                    Log.d("SeeHere", "in try");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }catch (Exception e){
                    Log.d("SeeHere", "in catch");
                    e.printStackTrace();
                }

                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);



            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
       /* Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
    }




        @Override
        public void onPaymentError ( int i, String s){
            tv2.setText("Payment Failed");
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }