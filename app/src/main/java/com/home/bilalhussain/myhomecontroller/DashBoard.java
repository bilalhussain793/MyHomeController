package com.home.bilalhussain.myhomecontroller;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class DashBoard extends AppCompatActivity {

    Button light,fan,swt,door,nott;
    TextView tv_light,tv_fan,tv_door,tv_switch,tv_name,bt_smss;

    int lt,fn,sw,dr;

    Dialog d,d_a,dct;
    EditText et_pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        tv_name=findViewById(R.id.tv);

        d=new Dialog(DashBoard.this);
        d.setContentView(R.layout.door_layout);
        et_pin=d.findViewById(R.id.et_pin);
        bt_smss=findViewById(R.id.bt_sms);
        nott=findViewById(R.id.notifi);

        d_a=new Dialog(DashBoard.this);
        d_a.setContentView(R.layout.alert_layout);
        final TextView tva=d_a.findViewById(R.id.tvh);
        final TextView tvs=d_a.findViewById(R.id.tvt);


        FirebaseDatabase databa = FirebaseDatabase.getInstance();
        final DatabaseReference myRefa1 = databa.getReference("notify");

        dct=new Dialog(DashBoard.this);
        dct.setContentView(R.layout.noti_layout);
        final EditText tee=dct.findViewById(R.id.et_ptx);
        final Button btt=dct.findViewById(R.id.et_bt);
       // bt_smss=findViewById(R.id.bt_sms);
        nott.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dct.show();
            }
        });

        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tee.getText().toString().length()==0){
                    Toast.makeText(DashBoard.this, "Empty", Toast.LENGTH_SHORT).show();
                    tee.setError("Empty!");
                }else{
             myRefa1.setValue(tee.getText().toString());
                    Toast.makeText(DashBoard.this, "msg sent", Toast.LENGTH_SHORT).show();
                    dct.dismiss();
                }
            }
        });
        FirebaseDatabase database1a = FirebaseDatabase.getInstance();
        DatabaseReference myRefaa = database1a.getReference("Appliances");
        myRefaa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int value = dataSnapshot.child("flame").getValue(Integer.class);
                int value1 = dataSnapshot.child("smoke").getValue(Integer.class);
                int value2 = dataSnapshot.child("human").getValue(Integer.class);

                if(value==1){

                    tva.setText("Alert!");
                    tvs.setText("There is fire be careful about it.");
                    d_a.show();
                }else if(value1==1){
                    d_a.show();
                    tva.setText("Alert!");
                    tvs.setText("Environment is not clean\nGas detected\nEmergency...");
                }else if(value2==1){
                    d_a.show();
                    tva.setText("Person Detected");
                    tvs.setText("There is someone in the house");
                }else{
                    d_a.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        bt_smss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this,Sms.class));
            }
        });

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
                int flame=dataSnapshot.child("flame").getValue(Integer.class);
               // Log.d(TAG, "Value is: " + value);

                    tv_light.setText(""+lt);

                    tv_fan.setText(""+fn);

                    tv_switch.setText(""+sw);

                    tv_door.setText(""+dr);
                    if(flame==1){
                        notification(flame);
                    }
                    else {
                        notification(0);
                    }
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

                d.show();

            }
        });
        et_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
              //  Toast.makeText(DashBoard.this, ""+editable.toString(), Toast.LENGTH_SHORT).show();
                if(editable.toString().length()<4){
                    et_pin.setTextColor(Color.parseColor("#838383"));
                }else {
                    et_pin.setTextColor(Color.parseColor("#0088ff"));
                    getPC(et_pin.getText().toString());
                }
            }
        });

        Button cam_btn=findViewById(R.id.cam);
        cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.macrovideo.v380");
                if (intent != null) {
                    // We found the activity now start the activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    // Bring user to the market or let them choose an app?
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("market://details?id=" + "com.macrovideo.v380"));
                    startActivity(intent);
                }
            }
        });


    }
    private void getPC(final String pass){
        String url = "https://smarthomecontroller-1371a.firebaseio.com/info.json";
        final ProgressDialog pd = new ProgressDialog(DashBoard.this);
        pd.setMessage("Loading...");
        pd.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(s.equals("null")){
                        Toast.makeText(DashBoard.this, "user not found", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        JSONObject obj = new JSONObject(s);

                  //      Toast.makeText(DashBoard.this, ""+obj.getString("pin"), Toast.LENGTH_SHORT).show();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference("Appliances");

                        if (obj.getString("pin").equals(pass)){


                            if(dr==1){
                                myRef.child("dr").setValue(0);
                                Toast.makeText(DashBoard.this, "Door is Locked", Toast.LENGTH_SHORT).show();

                            }else {
                                myRef.child("dr").setValue(1);
                                Toast.makeText(DashBoard.this, "Door has been Unlocked", Toast.LENGTH_SHORT).show();
                            }
                            et_pin.setText("");
                            d.dismiss();

                        }
                        else{
                            et_pin.setTextColor(Color.parseColor("#ff0000"));
                            Toast.makeText(DashBoard.this, "Wrong PIN", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pd.dismiss();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                pd.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(DashBoard.this);
        rQueue.add(request);
    }

        public void notification(int i){

            Intent intent = new Intent(this, DashBoard.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(DashBoard.this)
                            .setSmallIcon(R.drawable.sm2)
                            .setContentTitle("Notification Title")
                            .setContentText("Notification "+i)
                            .setContentIntent(pendingIntent );
            mBuilder.setPriority(1);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build());




        }

}
