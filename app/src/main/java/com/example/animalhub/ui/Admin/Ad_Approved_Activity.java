package com.example.animalhub.ui.Admin;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animalhub.R;
import com.example.animalhub.model.ModelAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Ad_Approved_Activity extends AppCompatActivity {
    ModelAd ad;
    ImageView imgad;
    TextView name,price,detail,title,location,contact;
    Button ad_approved;
    String Name,Image,Id;
    DatabaseReference dref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad__approved_);
        ad_approved = findViewById(R.id.ad_approved);
        imgad = findViewById(R.id.imgad);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        detail = findViewById(R.id.detail);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        contact = findViewById(R.id.contact);
        ad=(ModelAd)getIntent().getSerializableExtra("ad");
        Picasso.get().load(ad.getImage()).into(imgad);
        title.setText(ad.getTitle());
        price.setText(ad.getPrice()+"/-");
        detail.setText(ad.getDescription());


        dref= FirebaseDatabase.getInstance().getReference();
        dref.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d:snapshot.getChildren()){
                    if (d.getKey().equals(ad.getId())){
                        Name=d.child("name").getValue(String.class);
                        Id=d.child("id").getValue(String.class);
                        String Location=d.child("location").getValue(String.class);
                        String phone=d.child("phone").getValue(String.class);
                        name.setText(Name);
                        location.setText(Location);
                        contact.setText(phone);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void showFullImage(View view) {
        Dialog dialog=new Dialog(Ad_Approved_Activity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ImageView imageView=new ImageView(getApplicationContext());
        Picasso.get().load(ad.getImage()).into(imageView);
        dialog.setContentView(imageView);
        dialog.show();
    }

    public void approved(View view) {
        ad.setApproved(true);
        dref.child("Ads").child(ad.getCategory()).child(ad.getAdId()).setValue(ad);
        Toast.makeText(this, "Ad Approved Successfully", Toast.LENGTH_SHORT).show();
    }
}