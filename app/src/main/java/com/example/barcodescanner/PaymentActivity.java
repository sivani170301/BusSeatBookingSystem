package com.example.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Button pay_btn;
    TextView tv2;
    DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbref= FirebaseDatabase.getInstance().getReference().child("bus").child("b01");

        pay_btn = findViewById(R.id.pay_btn);
        tv2 = findViewById(R.id.tv2);
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amt = 500*100;

                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_nEeFtwvXOh9Ow8");
                //checkout.setImage();

                JSONObject object = new JSONObject();
                try{
                    object.put("name","BusSeatBooking");
                    object.put("description", "Payment for your current bus seat.");
                    object.put("theme.color","");
                    object.put("amount", amt);
                    object.put("prefill.contact", "9988776655");
                    object.put("prefill.email", "abc@gmail.com");
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
        String busSeatNum = qrscanner.data;
        tv2.setText("Payment Success\n Transaction Num: "+s);
        dbref.child(busSeatNum).setValue("false");
        MainActivity.tv.setText("Payment Success\n Transaction Num: "+s);
        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {
        tv2.setText("Payment Failed");
        MainActivity.tv.setText("Payment Failed");
        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}