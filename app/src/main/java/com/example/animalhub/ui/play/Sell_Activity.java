package com.example.animalhub.ui.play;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animalhub.R;
import com.example.animalhub.model.ModelAd;
import com.example.animalhub.OldData.HomeActivityOld;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class Sell_Activity extends AppCompatActivity {
    private static final String TAG = "Sell_Activity";

    Button publish,farm,desert,birds,reptile,sea,jungle;
    EditText title,description,price,phone,u_location,address;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    ImageView imgad;
    private int REQUEST_CODE=111;
    private String DownloadImageUrl;
    Uri imgUri;
    private String ad_title;
    private String ad_description;
    private String ad_price;
    private String ad_phone;
    private String location;
    private String ad_address;
    ProgressDialog progressDialog;
    String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_);
        publish = findViewById(R.id.publish);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        phone = findViewById(R.id.phone);
        u_location = findViewById(R.id.location);
        address = findViewById(R.id.address);
        imgad = findViewById(R.id.imgad);
        farm = findViewById(R.id.farm);
        desert = findViewById(R.id.desert);
        birds = findViewById(R.id.birds);
        reptile = findViewById(R.id.reptile);
        sea = findViewById(R.id.sea);
        jungle = findViewById(R.id.jungle);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Processing...");

        farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farm.setBackground(getResources().getDrawable(R.drawable.bg_cetag));
                desert.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                birds.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                reptile.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                sea.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                jungle.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                type="Farm";
            }
        });

        desert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farm.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                desert.setBackground(getResources().getDrawable(R.drawable.bg_cetag));
                birds.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                reptile.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                sea.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                jungle.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                type="Desert";
            }
        });

        birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farm.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                desert.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                birds.setBackground(getResources().getDrawable(R.drawable.bg_cetag));
                reptile.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                sea.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                jungle.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                type="Birds";
            }
        });

        reptile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farm.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                desert.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                birds.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                reptile.setBackground(getResources().getDrawable(R.drawable.bg_cetag));
                sea.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                jungle.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                type="Reptile";
            }
        });

        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farm.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                desert.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                birds.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                reptile.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                sea.setBackground(getResources().getDrawable(R.drawable.bg_cetag));
                jungle.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                type="Sea";
            }
        });

        jungle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farm.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                desert.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                birds.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                reptile.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                sea.setBackground(getResources().getDrawable(R.drawable.bg_cell));
                jungle.setBackground(getResources().getDrawable(R.drawable.bg_cetag));
                type="Jungle";
            }
        });
        storage = FirebaseStorage.getInstance();
        imgad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_title = title.getText().toString().toUpperCase();
                ad_description = description.getText().toString();
                ad_price = price.getText().toString();
                ad_phone = phone.getText().toString();
                location = u_location.getText().toString().toUpperCase();
                ad_address = address.getText().toString().toUpperCase();
                //ad_url = url.getText().toString().toUpperCase();

                if (ad_title.isEmpty()
                        || ad_description.isEmpty()
                        || ad_price.isEmpty() ||
                        ad_phone.isEmpty() ||
                        location.isEmpty() ||
                        ad_address.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Fill all Fields first", Toast.LENGTH_SHORT).show();


                } else if (phone.length() != 11) {
                    phone.setError("Please Enter Complete Number ");
                    return;
                } else {
                    progressDialog.show();
                    uploadImage(imgUri);


                }


            }

        });

    }

    private void updateUI(FirebaseUser user) {
    }
    public void ShowImage(Uri uri){
        Bitmap image=null;
        try {
            image=getBitmapFromUri(uri);
            imgad.setImageBitmap(image);
        } catch (IOException e) {
            // Toast.makeText(this, "Exception While Reading Image : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==REQUEST_CODE && resultCode== RESULT_OK && data!=null){
            imgUri = data.getData();
            ShowImage(imgUri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void  uploadImage(Uri uri)
    {
        StorageReference reference = storage.getReference();
        StorageReference imageFolder = reference.child("Ads Images");
        StorageReference img = imageFolder.child(UUID.randomUUID().toString());
        img.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Uri downloadUrl;
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                downloadUrl = urlTask.getResult();
                DownloadImageUrl = downloadUrl.toString();
                firebaseAuth = FirebaseAuth.getInstance();

                if(firebaseAuth.getCurrentUser()!=null) {
                    Calendar calendar = Calendar.getInstance();
                    String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

                    databaseReference = FirebaseDatabase.getInstance().getReference("Ads").child(type);
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                    String Id = user.getUid();
                    ModelAd modelAd = new ModelAd();
                    modelAd.setId(Id);
                    modelAd.setTitle(ad_title);
                    modelAd.setDescription(ad_description);
                    modelAd.setPrice(ad_price);
                    modelAd.setLocation(location);
                    modelAd.setPhone(ad_phone);
                    modelAd.setAddress(ad_address);
                    modelAd.setDate(currentDate);
                    modelAd.setImage(DownloadImageUrl);
                    modelAd.setApproved(false);
                    modelAd.setCategory(type);
//                    modelAd.setCategory(category.getSelectedItem().toString());
//                    modelAd.setSubCategory(subcategory.getSelectedItem().toString());
                    databaseReference.child(modelAd.getAdId()).setValue(modelAd);

                    Log.e(TAG, "onSuccess: ");
                    Toasty.success(getApplicationContext(), "Your Ad is Successfully Posted for approval", Toast.LENGTH_SHORT).show();
                    /*Intent i = new Intent(getApplicationContext(), HomeActivityOld.class);
                    startActivity(i);*/
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(), "Error" , Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}