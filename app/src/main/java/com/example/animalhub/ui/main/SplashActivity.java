package com.example.animalhub.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.animalhub.R;
import com.example.animalhub.ui.MainActivity;
import com.example.animalhub.ui.auth.LogInActivity;
import com.example.animalhub.OldData.HomeActivityOld;
import com.example.animalhub.utils.Tools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    FirebaseUser user;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        final TextView txt1=findViewById(R.id.textView10);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.7f);
        alphaAnimation.setDuration(2000);
        //txt1.startAnimation(alphaAnimation);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //go to next activity;
            }
        }, 3000); // for 3 second
    }

    @Override
    public void onStart() {
        Log.e(TAG, "onStart: " );
        super.onStart();
        user = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //go to next activity
                updateUI(user);
            }
        }, 3000);

    }
    private void updateUI(FirebaseUser user) {
        Log.e(TAG, "updateUI: " );
        if (user != null) {
            Log.e(TAG, "updateUI: user != null" );
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else
        {
            Log.e(TAG, "updateUI: user != null else" );
            SharedPreferences user_type = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            String user_typ = user_type.getString("user_type", "");
            if(user_typ.equals("old")){
                Toasty.info(this, "login to continue", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this, LogInActivity.class));
                finish();
            }else{
                //startActivity(new Intent(LoginActivity.this, WalkThrowActivity.class));

                Toasty.info(this, "login to continue", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this, LogInActivity.class));
                finish();
            }
        }
    }

}
