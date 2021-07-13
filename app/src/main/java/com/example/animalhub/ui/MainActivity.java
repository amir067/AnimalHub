package com.example.animalhub.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.listners.AlLogoutHandler;
import com.example.animalhub.R;
import com.example.animalhub.utils.PreferenceHelperDemo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";
    public Toolbar toolbar;

    public NavController navController;

    private String UserFirstName;
    private String UserLastName;
    private String UserDesignation;

    protected PreferenceHelperDemo preferenceHelperDemo;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        FirebaseApp.initializeApp(this);
        Applozic.init(MainActivity.this, getResources().getString(R.string.APPLOZIC_APP_ID));
        mAuth = FirebaseAuth.getInstance();
        preferenceHelperDemo = new PreferenceHelperDemo(this);
        getSupportActionBar().hide();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.navigation_home_fragment, R.id.navigation_chat_fragment,
                R.id.navigation_information_fragment,R.id.navigation_profile_fragment).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);



        bottomNavigationView.getMenu().getItem(1).setVisible(false);
    }

    private void CheackUserLogIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.i(TAG, "User is Logged in");
            //Toasty.success(this,"Welcome",Toasty.LENGTH_SHORT).show();
            Log.e(TAG, "user != null" );
        } else
        {
            Toast.makeText(this, "login to continue", Toast.LENGTH_SHORT).show();
            doLogoutOnApplozic();
            //navigate(R.id.LoginFragment);
            //Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.action_HomeFragment_to_loginFragment);
        }
    }

    private void doLogoutOnApplozic() {

        Applozic.logoutUser(MainActivity.this, new AlLogoutHandler() {
            @Override
            public void onSuccess(Context context) {
                Log.e(TAG, "onSuccess: doLogoutOnApplozic" );
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onFailure: doLogoutOnApplozic" );

            }
        });


    }
    private void navigate(int destination) {
        //Nav Controller Actions
        if(navController!=null){
            //findNavController(view).navigate(R.id.action_first_to_second);
            navController.navigate(destination);
        }
    }



}