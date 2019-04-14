package com.home.bilalhussain.myhomecontroller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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
import android.telephony.TelephonyManager;
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
import android.widget.ImageView;
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

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    Button signin;
    EditText p_id,p_password;
    ProgressDialog pd;
    CheckBox checkBox;
    String user, pass;

    private static String id;
    private static String phone;
    public static Button cancel,lok;
    public static TextView mTextField,state,ublock;
    //  public static TextToSpeech t1;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    private TextView mHeadingLabel;
    public static ImageView mFingerprintImage;
    public static TextView mParaLabel;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef2 = database.getReference("Appliances/dr");
    public static Dialog d;
    TelephonyManager tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        d=new Dialog(LoginActivity.this);
        d.setContentView(R.layout.fp_dialog);

        cancel=d.findViewById(R.id.bc);
        mTextField=d.findViewById(R.id.mt);
        mHeadingLabel = (TextView) d.findViewById(R.id.headingLabel);
        mFingerprintImage = (ImageView) d.findViewById(R.id.fingerprintImage);
        mParaLabel = (TextView) d.findViewById(R.id.paraLabel);


        ublock=findViewById(R.id.ublock);
       // lok=findViewById(R.id.dlock);
       // state=findViewById(R.id.state);

        SharedPreferences prefs = getSharedPreferences("L1", MODE_PRIVATE);
        int r = prefs.getInt("flg", 0);
        if (r == 2) {
            ublock.setText("Use FingerPrint ID");
        }else{
            ublock.setText("Enable FingerPrint ID!");
        }

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("ID");



        signin=findViewById(R.id.email_sign_in_button);
        p_id=findViewById(R.id.p_id);
        p_password=findViewById(R.id.p_pass);
        checkBox=findViewById(R.id.chk);
        TextView signup=findViewById(R.id.signup);

                validlogin(p_id,p_password,signin,signup);

              //  startActivity(new Intent(LoginActivity.this,DashBoard.class));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        ublock.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                if(ublock.getText().toString().equals("Enable FingerPrint ID!")){



                    user = p_id.getText().toString();
                    pass = p_password.getText().toString();

                    if(user.equals("")){
                        p_id.setError("can't be blank");
                    }
                    else if(pass.equals("")){
                        p_password.setError("can't be blank");
                    }
                    else{
                        String url = "https://smarthomecontroller-1371a.firebaseio.com/ID.json";
                        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                        pd.setMessage("Loading...");
                        pd.show();

                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                            @Override
                            public void onResponse(String s) {
                                if(s.equals("null")){
                                    Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    try {
                                        JSONObject obj = new JSONObject(s);

                                        if(!obj.has(user)){
                                            Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                        }
                                        else if(obj.getJSONObject(user).getString("Password").equals(pass)){
                                            UserDetails.username = user;
                                            UserDetails.password = pass;
                                        //    startActivity(new Intent(LoginActivity.this, DashBoard.class));
                                            SharedPreferences.Editor editor = getSharedPreferences("L1", MODE_PRIVATE).edit();
                                            editor.putInt("flg", 2);
                                            editor.putString("p_id",p_id.getText().toString());
                                            editor.putString("p_pass",p_password.getText().toString());
                                            editor.apply();
                                            Toast.makeText(LoginActivity.this, "Successfully Enabled\n" +
                                                    "Now you can logged in with your finger print id", Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
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

                        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                        rQueue.add(request);
                    }

                }
                else{
                d.show();
                d.setCancelable(false);
                runInIt();
                }
            }
        });





    }
    public void runInIt(){


        mFingerprintImage.setImageResource(R.drawable.fp);
        fingerlock();
    }
    public void validlogin(final EditText id, final EditText password,Button loginButton,TextView registerUser){


        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = id.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    id.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else{
                    String url = "https://smarthomecontroller-1371a.firebaseio.com/ID.json";
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                    }
                                    else if(obj.getJSONObject(user).getString("Password").equals(pass)){
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        startActivity(new Intent(LoginActivity.this, DashBoard.class));
                                        Intent myintent=new Intent(LoginActivity.this, DashBoard.class).putExtra("user_id", user);
                                        startActivity(myintent);
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
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

                    RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                    rQueue.add(request);
                }

            }
        });
    }

    public void  fingerlock(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if(!fingerprintManager.isHardwareDetected()){

                mParaLabel.setText("Fingerprint Scanner not detected in Device");

            }  else if (!keyguardManager.isKeyguardSecure()){

                mParaLabel.setText("Add Lock to your Phone in Settings");

            } else if (!fingerprintManager.hasEnrolledFingerprints()){

                mParaLabel.setText("You should add atleast 1 Fingerprint to use this Feature");

            } else {

                mParaLabel.setText("Place your Finger to enable unlock button.");

                generateKey();

                if (cipherInit()){

                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(LoginActivity.this);
                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);


                }
            }

        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {

        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {

            e.printStackTrace();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {

            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    }

    private void srt(){

    }

}

