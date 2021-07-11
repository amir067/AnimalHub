package com.example.animalhub.OldData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.animalhub.R;
import com.example.animalhub.ui.Admin.Admin_Activity;
import com.example.animalhub.ui.auth.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityOld extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ImageButton imageButton;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);
        imageButton = findViewById(R.id.imageButton);
        firebaseAuth = FirebaseAuth.getInstance();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser()!= null){
                    if (firebaseAuth.getCurrentUser().getEmail().equalsIgnoreCase("admin@animalhub.com")){

                        Intent i = new Intent(MainActivityOld.this, Admin_Activity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        databaseReference = FirebaseDatabase.getInstance().getReference("User");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                   String id = firebaseAuth.getCurrentUser().getUid();
                                    Log.e(TAG, "onDataChange: user id is:"+id );

                                    if(snapshot.child(id).child("delete").exists()){
                                        boolean delete = snapshot.child(id).child("delete").getValue(boolean.class);

                                        if (delete==true){
                                            firebaseAuth.signOut();
                                            Toast.makeText(MainActivityOld.this, "You are Deleted by admin", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(MainActivityOld.this, LogInActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else{
                                            Intent i = new Intent(getApplicationContext(), HomeActivityOld.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }else{
                                        Toast.makeText(MainActivityOld.this,"User not found!",Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                    }


                                }else{
                                    firebaseAuth.signOut();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }


                }
                else{

                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}