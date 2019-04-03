package com.home.bilalhussain.myhomecontroller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    Button signin;
    EditText p_id,p_password;
    ProgressDialog pd;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("ID");



        signin=findViewById(R.id.email_sign_in_button);
        p_id=findViewById(R.id.p_id);
        p_password=findViewById(R.id.p_pass);
        checkBox=findViewById(R.id.chk);
        signin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validlogin(p_id,p_password);

              //  startActivity(new Intent(LoginActivity.this,DashBoard.class));

            }
        });





    }
    public void validlogin(final EditText id, final EditText password){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ID/"+id.getText().toString());
        pd=new ProgressDialog(LoginActivity.this);
        pd.setTitle("Loading....");
        pd.show();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                final String phn = dataSnapshot.child("ID").getValue(String.class);
                final String pass = dataSnapshot.child("Password").getValue(String.class);



                    Toast.makeText(LoginActivity.this, "Login here", Toast.LENGTH_SHORT).show();
                if (id.getText().toString().equals(phn)) {

                    if (password.getText().toString().equals(pass)) {

                        if (checkBox.isEnabled()) {
                            SharedPreferences.Editor editor = getSharedPreferences("LOGIN", MODE_PRIVATE).edit();
                            editor.putInt("flg", 2);
                            editor.putString("p_id", id.getText().toString());
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, DashBoard.class));
                            pd.dismiss();
                        } else {
                            SharedPreferences.Editor editor = getSharedPreferences("LOGIN", MODE_PRIVATE).edit();
                            editor.putString("p_id", id.getText().toString());
                            editor.apply();

                            startActivity(new Intent(LoginActivity.this, DashBoard.class));
                            pd.dismiss();
                        }

                    } else {

                        password.setError("Wrong Password");
                    }

                } else {
                    id.setError("Invalid Contact");
                }


                }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
                id.setError("Invalid User");
            }
        });

    }
}

