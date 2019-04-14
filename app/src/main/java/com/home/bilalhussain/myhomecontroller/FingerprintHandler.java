package com.home.bilalhussain.myhomecontroller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.media.Image;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.update("An Auth Error. " + errString, false);

    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Auth Failed. ", false);
        LoginActivity.mFingerprintImage.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);
        LoginActivity.mFingerprintImage.setColorFilter(ContextCompat.getColor(context, R.color.grey));

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        this.update("Successfull ", true);
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference myRef = database.getReference("DoorStatus");
//        myRef.setValue(1);
       // LoginActivity.ulock.setEnabled(true);
        LoginActivity.mFingerprintImage.setColorFilter(ContextCompat.getColor(context, R.color.Green));


    }

    private void update(String s, boolean b) {

        TextView paraLabel = (TextView) ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = (ImageView) ((Activity)context).findViewById(R.id.fingerprintImage);

        LoginActivity.mParaLabel.setText(s);

        if(b == false){

            LoginActivity.mParaLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        } else {

            LoginActivity.mParaLabel.setTextColor(ContextCompat.getColor(context, R.color.Green));
            LoginActivity.mFingerprintImage.setImageResource(R.drawable.fp);
            LoginActivity.mFingerprintImage.setColorFilter(ContextCompat.getColor(context, R.color.Green));
            LoginActivity.d.dismiss();
            SharedPreferences prefs = context.getSharedPreferences("L1", MODE_PRIVATE);
            String u = prefs.getString("p_id", "");
            String p = prefs.getString("p_pass", "");
            if(u.length()==0||p.length()==0){

            }else {

            }
            context.startActivity(new Intent(context, DashBoard.class).putExtra("user_id",u));
            ((Activity) context).finish();

        }
    }
}
