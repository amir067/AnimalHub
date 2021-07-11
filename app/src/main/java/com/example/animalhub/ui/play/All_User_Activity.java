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
import com.example.animalhub.adapter.All_User_View_Adapter;
import com.example.animalhub.model.Register_ModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class All_User_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ArrayList<Register_ModelClass> userList=new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__user_);

        recyclerView= findViewById(R.id.record);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Processing...");

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_ry_venue));
        recyclerView.addItemDecoration(dividerItemDecoration);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList=new ArrayList<>();
                for (DataSnapshot us:snapshot.getChildren()){
                        Register_ModelClass ad=new Register_ModelClass();
                        ad = us.getValue(Register_ModelClass.class);
                        if (!ad.isDelete()) {
                            userList.add(ad);
                        }

                }
                try {
                    All_User_View_Adapter adapter = new All_User_View_Adapter(
                            userList);
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