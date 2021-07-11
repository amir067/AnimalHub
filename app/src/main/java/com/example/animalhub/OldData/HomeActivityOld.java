package com.example.animalhub.OldData;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.animalhub.Chat.Constants;

import com.example.animalhub.Chat.SettingsAPI;
import com.example.animalhub.R;
import com.example.animalhub.model.ModelAd;
import com.example.animalhub.ui.auth.LogInActivity;
import com.example.animalhub.ui.play.Buy_Activity;
import com.example.animalhub.ui.play.Food_Info_Activity;
import com.example.animalhub.ui.play.My_Ads_Activity;
import com.example.animalhub.ui.play.Sell_Activity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivityOld extends AppCompatActivity {
    DrawerLayout drawer;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    ToggleButton toggleButton;
    TextView name;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<ModelAd> adsList=new ArrayList<>();
    String nameDB;

    ImageView farm,desert,birds,reptile,sea,jungle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal__hub_);
        drawer = findViewById(R.id.drawer_ac);
        toggleButton = findViewById(R.id.toggleButton);
        farm = findViewById(R.id.farm);
        desert = findViewById(R.id.desert);
        birds = findViewById(R.id.birds);
        reptile = findViewById(R.id.reptile);
        sea = findViewById(R.id.sea);
        jungle = findViewById(R.id.jungle);


        farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Buy_Activity.class);
                intent.putExtra("name","Farm");
                startActivity(intent);
            }
        });
        desert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Buy_Activity.class);
                intent.putExtra("name","Desert");
                startActivity(intent);
            }
        });
        birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Buy_Activity.class);
                intent.putExtra("name","Birds");
                startActivity(intent);
            }
        });
        reptile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Buy_Activity.class);
                intent.putExtra("name","Reptile");
                startActivity(intent);
            }
        });
        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Buy_Activity.class);
                intent.putExtra("name","Sea");
                startActivity(intent);
            }
        });

        jungle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Buy_Activity.class);
                intent.putExtra("name","Jungle");
                startActivity(intent);
            }
        });
        nav = drawer.findViewById(R.id.navDrawer);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawer.openDrawer(drawer);
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        Query checkUser = databaseReference.orderByChild("id").equalTo(uid);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    nameDB = snapshot.child(uid).child("name").getValue(String.class);
                    //nameDB = nameDB.toUpperCase();
                    name.setText(nameDB);

                    SettingsAPI set;
                    set = new SettingsAPI(getApplicationContext());
                    set.addUpdateSettings(Constants.PREF_MY_ID, uid);
                    set.addUpdateSettings(Constants.PREF_MY_NAME, nameDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        nav = drawer.findViewById(R.id.navDrawer);
        View v =  nav.getHeaderView(0);
        //profile_image = v.findViewById(R.id.drlogo);
        name = v.findViewById(R.id.name_header);
        toggle = new ActionBarDrawerToggle(this,drawer,R.string.Open,R.string.Close);
        drawer.addDrawerListener(toggle);
        drawer.isDrawerOpen(Gravity.LEFT);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.myAds:
                        Intent ini = new Intent(getApplicationContext(), My_Ads_Activity.class);
                        drawer.closeDrawer(Gravity.LEFT);
                        startActivity(ini);
                        break;
                    case R.id.sell:
                        Intent in = new Intent(getApplicationContext(), Sell_Activity.class);
                        drawer.closeDrawer(Gravity.LEFT);
                        startActivity(in);
                        break;
                    case R.id.buy:
                        Intent inte = new Intent(getApplicationContext(), Buy_Activity.class);
                        drawer.closeDrawer(Gravity.LEFT);
                        inte.putExtra("name","Ads");
                        startActivity(inte);
                        break;
                    case R.id.chats:
                        if (firebaseAuth.getCurrentUser()!=null){
                            try {
                                SettingsAPI set;
                                set = new SettingsAPI(getApplicationContext());
                                set.addUpdateSettings(Constants.PREF_MY_ID, uid);
                                set.addUpdateSettings(Constants.PREF_MY_NAME, nameDB);
                                //set.addUpdateSettings(Constants.PREF_MY_DP, imageDB);
                               // startActivity(new Intent(getApplicationContext(), Main_ChatActivity.class));

                            }catch (Exception e){}

                        }else {
                            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                        }

                        break;
//                    case R.id.feedback:
//                        Intent intent = new Intent(getApplicationContext(),FeedBack_Activity.class);
//                        drawer.closeDrawer(Gravity.LEFT);
//                        startActivity(intent);
//                        break;
                    case R.id.food_info:
                        Intent iii = new Intent(getApplicationContext(), Food_Info_Activity.class);
                        drawer.closeDrawer(Gravity.LEFT);
                        startActivity(iii);
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent ii = new Intent(getApplicationContext(), LogInActivity.class);
                        drawer.closeDrawer(Gravity.LEFT);
                        startActivity(ii);
                        finish();
                        break;
                }
                return true;

            }
        });
    }
}