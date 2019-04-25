package com.home.bilalhussain.myhomecontroller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Sms extends AppCompatActivity {
Button lt_on,lt_off,fan_on,fan_off,sw_on,swoff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        lt_on=findViewById(R.id.bt_light1);
        lt_off=findViewById(R.id.bt_light2);
        fan_on=findViewById(R.id.bt_fan1);
        fan_off=findViewById(R.id.bt_fan2);
        sw_on=findViewById(R.id.bt_switch1);
        swoff=findViewById(R.id.bt_switch2);



        lt_on.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","03104617147");
                smsIntent.putExtra("sms_body","ledon");
                startActivity(smsIntent);
            }

        });
        lt_off.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","03104617147");
                smsIntent.putExtra("sms_body","ledoff");
                startActivity(smsIntent);
            }

        });
        fan_on.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","03104617147");
                smsIntent.putExtra("sms_body","fanon");
                startActivity(smsIntent);
            }

        });
        fan_off.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","03104617147");
                smsIntent.putExtra("sms_body","fanoff");
                startActivity(smsIntent);
            }

        });
        sw_on.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","03104617147");
                smsIntent.putExtra("sms_body","son");
                startActivity(smsIntent);
            }

        });
        swoff.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","03104617147");
                smsIntent.putExtra("sms_body","soff");
                startActivity(smsIntent);
            }

        });
    }
}
