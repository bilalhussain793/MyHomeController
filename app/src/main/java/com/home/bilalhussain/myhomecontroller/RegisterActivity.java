package com.home.bilalhussain.myhomecontroller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText userid, username, password, ph;
    Button registerButton;
    String user, pass,u_id,phn;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userid=findViewById(R.id.user_id);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);
        ph=findViewById(R.id.user_contact);

        Firebase.setAndroidContext(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                u_id=userid.getText().toString();
                phn=ph.getText().toString();

                if(u_id.equals("")){
                    userid.setError("can't be blank");
                }
                else if(user.equals("")){
                    username.setError("can't be blank");
                }

                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else if(phn.equals("")){
                    ph.setError("can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("only alphabet or number allowed");
                }
                else if(user.length()<1){
                    username.setError("at least 5 characters long");
                }
                else if(pass.length()<8){
                    password.setError("at least 5 characters long");
                }
                else {
                    register();
                        // function here


                }
            }
        });
    }

    private void register(){
        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://smarthomecontroller-1371a.firebaseio.com/ID.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://smarthomecontroller-1371a.firebaseio.com/ID");

                if(s.equals("null")) {
                    reference.child(user).child("password").setValue(pass);
                    Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(user)) {

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("ID");


                            myRef.child("Name").setValue(user);
                            myRef.child("Password").setValue(pass);
                            myRef.child("Phone").setValue(phn);
                            myRef.child("ID").setValue(u_id);
                            Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                        } else {
     //                       reference.child(user).child("password").setValue(pass);
                            Toast.makeText(RegisterActivity.this, "username already exists", Toast.LENGTH_LONG).show();
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
                System.out.println("" + volleyError );
                pd.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(request);
    }
}