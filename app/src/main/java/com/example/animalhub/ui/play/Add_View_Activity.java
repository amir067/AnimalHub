package com.example.animalhub.ui.play;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.example.animalhub.Chat.ChatDetailsActivity;
import com.example.animalhub.Chat.Friend;
import com.example.animalhub.R;
import com.example.animalhub.adapter.FeedBack_Adapter;
import com.example.animalhub.model.ModelAd;
import com.example.animalhub.model.Model_FeedBack;
import com.example.animalhub.model.Register_ModelClass;
import com.example.animalhub.ui.auth.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Add_View_Activity extends AppCompatActivity {

    ModelAd ad;
    ImageView imgad;
    TextView name,price,detail,title,location,contact,submit;
    Button chat;
    String Name,Image,Id;
    DatabaseReference dref,databaseReference;
    EditText txtFeedBAck;
    FirebaseAuth firebaseAuth;
    String uNAme,AdId;
    String UId;
    RecyclerView recyclerView;
    ArrayList<Model_FeedBack> FeedBackList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__view_);
        chat = findViewById(R.id.chat);
        imgad = findViewById(R.id.imgad);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        detail = findViewById(R.id.detail);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        contact = findViewById(R.id.contact);
        txtFeedBAck = findViewById(R.id.txtFeedBack);
        submit = findViewById(R.id.submit);
        recyclerView = findViewById(R.id.record);
        ad=(ModelAd)getIntent().getSerializableExtra("ad");
        Picasso.get().load(ad.getImage()).into(imgad);
        title.setText(ad.getTitle());
        price.setText(ad.getPrice()+"/-");
        detail.setText(ad.getDescription());
        AdId = ad.getAdId();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        updateUI(user);
        UId = user.getUid();
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        dref = FirebaseDatabase.getInstance().getReference("FeedBack");
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FeedBackList=new ArrayList<>();
                for (DataSnapshot categ:snapshot.getChildren()){
                    for (DataSnapshot ads:categ.getChildren()){
                        Model_FeedBack ad=new Model_FeedBack();
                        ad = ads.getValue(Model_FeedBack.class);
                        if (ad.getAdId().equals(AdId)){
                            FeedBackList.add(ad);
                        }

                    }
                }
                try {


                        FeedBack_Adapter adapter = new FeedBack_Adapter(
                                FeedBackList);
                        recyclerView.setAdapter(adapter);



                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startChat(new View(getApplicationContext()));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot us:snapshot.getChildren()){
                    Register_ModelClass user = new Register_ModelClass();
                    user = us.getValue(Register_ModelClass.class);
                    if (user.getId().equals(UId)) {
                        uNAme = user.getName();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedBack = txtFeedBAck.getText().toString();
                if (feedBack.isEmpty()){
                    txtFeedBAck.setError("Please enter feed back");
                    return;
                }
                else if (UId.equals(Id)){
                    txtFeedBAck.setError("This ad is belong to you so you can't submit feed back");
                    txtFeedBAck.setText(null);
                    return;
                   }

                else {

                    dref = FirebaseDatabase.getInstance().getReference("FeedBack");

                    Model_FeedBack feed = new Model_FeedBack();

                    feed.setId(UId);
                    feed.setAdId(AdId);
                    feed.setUName(uNAme);
                    feed.setFeedBack(feedBack);

                    dref.child(AdId).child(UId).setValue(feed);
                    txtFeedBAck.setText(null);
                    Toast.makeText(Add_View_Activity.this, "Feed Back Submitted", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateUI(FirebaseUser user) {
    }

    public void showFullImage(View view) {
        Dialog dialog=new Dialog(Add_View_Activity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ImageView imageView=new ImageView(getApplicationContext());
        Picasso.get().load(ad.getImage()).into(imageView);
        dialog.setContentView(imageView);
        dialog.show();
    }
        public void startChat(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            /*Intent intent = new Intent(getApplicationContext(), ChatDetailsActivity.class);
            intent.putExtra("FRIEND", new Friend(Id,Name, Image));
            intent.putExtra("fromHome", true);
            startActivity(intent);*/

            Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
            intent.putExtra(ConversationUIService.USER_ID, Id);
            intent.putExtra(ConversationUIService.DISPLAY_NAME,  Name); //put it for displaying the title.
            intent.putExtra(ConversationUIService.TAKE_ORDER,true); //Skip chat list for showing on back press
            startActivity(intent);

        } else {
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        }
    }
}