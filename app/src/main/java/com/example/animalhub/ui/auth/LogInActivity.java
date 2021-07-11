package com.example.animalhub.ui.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animalhub.OldData.HomeActivityOld;
import com.example.animalhub.R;
import com.example.animalhub.ui.Admin.Admin_Activity;
import com.example.animalhub.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    TextView textregister,textforget;
    Button log_in;
    EditText email,password;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_);
        textregister = findViewById(R.id.textregister);
        log_in = findViewById(R.id.log_in);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        textforget = findViewById(R.id.textforget);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Processing...");

        firebaseAuth = FirebaseAuth.getInstance();

        textforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txt=new EditText(LogInActivity.this);

                new AlertDialog.Builder(LogInActivity.this)
                        .setMessage("Enter email")
                        .setView(txt)
                        .setTitle("Reset Password")
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!TextUtils.isEmpty(txt.getText().toString())){
                                    FirebaseAuth.getInstance().sendPasswordResetEmail(txt.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(), "Link sent Successfully!", Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(getApplicationContext(), "email not registed! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                String pas = password.getText().toString();

                if(em.isEmpty()){

                    email.setError("Please Enter Email First ");
                    return;

                }
                if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
                    email.setError("Invalid Email Address ");
                    return;
                }
                if(pas.isEmpty()){

                    password.setError("Please Enter Password First ");
                    return;
                }
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(em,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Log In Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();

                            /*if(email.getText().toString().equalsIgnoreCase("admin@animalhub.com")){
                                Toast.makeText(LogInActivity.this, "Log In Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LogInActivity.this, Admin_Activity.class);
                                startActivity(i);
                                finish();
                            }*/

                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


        textregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp_Activity.class);
                startActivity(i);

            }
        });
    }
}