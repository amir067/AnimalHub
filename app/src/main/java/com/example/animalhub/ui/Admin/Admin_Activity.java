package com.example.animalhub.ui.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalhub.ui.play.All_Ads_Activity;
import com.example.animalhub.ui.play.All_User_Activity;
import com.example.animalhub.ui.auth.LogInActivity;
import com.example.animalhub.R;
import com.example.animalhub.adapter.All_AdView_Adapter;
import com.example.animalhub.model.ModelAd;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Activity extends AppCompatActivity {
    DrawerLayout drawer;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    ToggleButton toggleButton;
    TextView text;
    TextView name;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ArrayList<ModelAd> adsList=new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_);
        drawer = findViewById(R.id.drawer_ac);
        toggleButton = findViewById(R.id.toggleButton);

        recyclerView= findViewById(R.id.record);
        text= findViewById(R.id.text);

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

                    case R.id.allAds:
                        Intent ini = new Intent(getApplicationContext(), All_Ads_Activity.class);
                        drawer.closeDrawer(Gravity.LEFT);
                        startActivity(ini);
                        break;
                    case R.id.allUser:
                        Intent in = new Intent(getApplicationContext(), All_User_Activity.class);
                        drawer.closeDrawer(Gravity.LEFT);
                        startActivity(in);
                        break;
                    case R.id.logout:
                        firebaseAuth.signOut();
                        Intent intent = new Intent(Admin_Activity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();


                }
                return true;

            }
        });
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Processing...");

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Ads");
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_ry_venue));
        recyclerView.addItemDecoration(dividerItemDecoration);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adsList=new ArrayList<>();
                for (DataSnapshot categ:snapshot.getChildren()){

                    for (DataSnapshot ads:categ.getChildren()){
                        ModelAd ad=new ModelAd();
                        ad = ads.getValue(ModelAd.class);
                        if (!ad.getApproved()){
                            adsList.add(ad);
                        }

                    }
                }
                try {
                    if (adsList.size()<=0) {
                        progressDialog.dismiss();
                        text.setText("No Ad for Approval");
                        Toast.makeText(Admin_Activity.this, "No Ad Found for Approval", Toast.LENGTH_SHORT).show();
                        All_AdView_Adapter adapter = new All_AdView_Adapter(
                                adsList);
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                    }
                    else {
                        All_AdView_Adapter adapter = new All_AdView_Adapter(
                                adsList);
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), LogInActivity.class);


        startActivity(i);
        finish();
    }
}