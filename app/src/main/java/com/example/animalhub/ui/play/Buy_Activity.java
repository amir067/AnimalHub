package com.example.animalhub.ui.play;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animalhub.R;
import com.example.animalhub.adapter.AdView_Adapter;
import com.example.animalhub.model.ModelAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Buy_Activity extends AppCompatActivity {
    DatabaseReference databaseReference;
    TextView textanimal,text;
    GridView gridView;
    FirebaseAuth firebaseAuth;
    ArrayList<ModelAd> adsList = new ArrayList<>();

    String type = "";
    ProgressDialog progressDialog;
    EditText search;

    AdView_Adapter adapter;

    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (adapter!=null){
                adapter.getFilter().filter(s);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_);
        textanimal = findViewById(R.id.textanimal);
        search = findViewById(R.id.search);
        text = findViewById(R.id.text);
        textanimal.setText(getIntent().getStringExtra("name"));
        type = getIntent().getStringExtra("name");

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Processing...");

        adapter = new AdView_Adapter(this,adsList);
        search.addTextChangedListener(textWatcher);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Ads");
       gridView = findViewById(R.id.record);
        progressDialog.show();
        if(!type.equals("Ads")) {
            databaseReference.child(type).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    adsList = new ArrayList<>();
                    //for (DataSnapshot categ:snapshot.getChildren()){
                    for (DataSnapshot ads : snapshot.getChildren()) {
                        ModelAd ad = new ModelAd();
                        ad = ads.getValue(ModelAd.class);
                        if (ad.getApproved() == true) {
                            adsList.add(ad);
                        }
                    }

                    //}
                    try {
                        if (adsList.size() == 0) {
                            progressDialog.dismiss();
                            text.setText("No Item");
                            Toast.makeText(Buy_Activity.this, "No Relevant Ad Found", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(Buy_Activity.this, ""+adsList.size(), Toast.LENGTH_SHORT).show();
                            adapter = new AdView_Adapter(Buy_Activity.this, adsList);
                            gridView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    adsList = new ArrayList<>();
                    for (DataSnapshot categ:snapshot.getChildren()){
                    for (DataSnapshot ads : categ.getChildren()) {
                        ModelAd ad = new ModelAd();
                        ad = ads.getValue(ModelAd.class);
                        if (ad.getApproved() == true) {
                            adsList.add(ad);
                        }
                    }

                    }
                    try {
                        if (adsList.size() == 0) {
                            progressDialog.dismiss();
                            text.setText("No Item");
                            Toast.makeText(Buy_Activity.this, "No Relevant Ad Found", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(Buy_Activity.this, ""+adsList.size(), Toast.LENGTH_SHORT).show();
                            adapter = new AdView_Adapter(Buy_Activity.this, adsList);
                            gridView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}