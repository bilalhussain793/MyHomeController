package com.home.bilalhussain.myhomecontroller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashBoard extends AppCompatActivity {

    Button light,fan,swt,door;
    TextView tv_light,tv_fan,tv_door,tv_switch,tv_name;

    int lt,fn,sw,dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        tv_name=findViewById(R.id.tv);

        String u= getIntent().getStringExtra("user_id");
        if (u.equals("")||u.length()==0){
            startActivity(new Intent(DashBoard.this,LoginActivity.class));
        }else {
            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference myRef2 = database1.getReference("ID/" + u);
// Read from the database
            myRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.child("Name").getValue(String.class);
                    // Log.d(TAG, "Value is: " + value);
                    tv_name.setText("Welcome Mr. " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    // Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


        }




        light=findViewById(R.id.bt_light);
        fan=findViewById(R.id.bt_fan);
        swt=findViewById(R.id.bt_sw);
        door=findViewById(R.id.bt_door);

        tv_light=findViewById(R.id.tv_light);
        tv_door=findViewById(R.id.tv_door);
        tv_switch=findViewById(R.id.tv_sw);
        tv_fan=findViewById(R.id.tv_fan);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Appliances");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                lt = dataSnapshot.child("light").getValue(Integer.class);
                fn = dataSnapshot.child("fan").getValue(Integer.class);
                sw = dataSnapshot.child("switch").getValue(Integer.class);
                dr = dataSnapshot.child("dr").getValue(Integer.class);
               // Log.d(TAG, "Value is: " + value);

                    tv_light.setText(""+lt);

                    tv_fan.setText(""+fn);

                    tv_switch.setText(""+sw);

                    tv_door.setText(""+dr);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lt==1){
                    myRef.child("light").setValue(0);
                }else {
                    myRef.child("light").setValue(1);
                }

            }
        });
        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fn==1){
                    myRef.child("fan").setValue(0);
                }else {
                    myRef.child("fan").setValue(1);
                }

            }
        });
        swt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw==1){
                    myRef.child("switch").setValue(0);
                }else {
                    myRef.child("switch").setValue(1);
                }

            }
        });
        door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dr==1){
                    myRef.child("dr").setValue(0);
                }else {
                    myRef.child("dr").setValue(1);
                }

            }
        });



    }
}
