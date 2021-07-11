package com.example.animalhub.ui.play;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.animalhub.R;
import com.example.animalhub.adapter.All_AdView_Adapter;
import com.example.animalhub.model.ModelAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class All_Ads_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ArrayList<ModelAd> adsList=new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__ads_);
        recyclerView= findViewById(R.id.record);

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

                            adsList.add(ad);


                    }
                }
                try {
                    All_AdView_Adapter adapter = new All_AdView_Adapter(
                            adsList);
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}