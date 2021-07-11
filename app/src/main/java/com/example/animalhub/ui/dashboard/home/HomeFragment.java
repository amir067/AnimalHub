package com.example.animalhub.ui.dashboard.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalhub.R;
import com.example.animalhub.ui.play.Buy_Activity;
import com.example.animalhub.ui.play.My_Ads_Activity;
import com.example.animalhub.ui.play.Sell_Activity;
import com.example.animalhub.utils.MyUtils;
import com.example.animalhub.utils.PreferenceHelperDemo;
import com.example.animalhub.utils.Tools;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class HomeFragment extends Fragment   {

    private static final String TAG = "HomeFragment";

    //Firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    PreferenceHelperDemo preferenceHelperDemo;
    NavController navController;

    @BindView(R.id.btn_bar_btn1)
    MaterialButton barButton1;

    @BindView(R.id.btn_bar_btn2)
    MaterialButton btn_bar_btn2;

    @BindView(R.id.btn_bar_btn3)
    MaterialButton btn_bar_btn3;


    @BindView(R.id.farm)
    ImageView farm;

    @BindView(R.id.desert)
    ImageView desert;

    @BindView(R.id.birds)
    ImageView birds;

    @BindView(R.id.reptile)
    ImageView reptile;

    @BindView(R.id.sea)
    ImageView sea ;

    @BindView(R.id.jungle)
    ImageView jungle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(requireActivity());
        View root = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this,root);
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        navController = Navigation.findNavController(view);
        MyUtils.hideKeyboard(requireActivity());
        preferenceHelperDemo = new PreferenceHelperDemo(requireContext());

        //Nav Controller Actions
        if(navController!=null){
            // navController.navigate();
            //navController.popBackStack();
        }


        barButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ini = new Intent(requireContext(), My_Ads_Activity.class);
                startActivity(ini);
            }
        });

        btn_bar_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ini = new Intent(requireContext(), Sell_Activity.class);
                startActivity(ini);

            }
        });

        btn_bar_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ini = new Intent(requireContext(), Buy_Activity.class);
                ini.putExtra("name","Ads");
                startActivity(ini);

            }
        });



        farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), Buy_Activity.class);
                intent.putExtra("name","Farm");
                startActivity(intent);
            }
        });
        desert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Buy_Activity.class);
                intent.putExtra("name","Desert");
                startActivity(intent);
            }
        });
        birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Buy_Activity.class);
                intent.putExtra("name","Birds");
                startActivity(intent);
            }
        });
        reptile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Buy_Activity.class);
                intent.putExtra("name","Reptile");
                startActivity(intent);
            }
        });
        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Buy_Activity.class);
                intent.putExtra("name","Sea");
                startActivity(intent);
            }
        });

        jungle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),Buy_Activity.class);
                intent.putExtra("name","Jungle");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        //Received Bundle
        //Toast.makeText(requireContext(), "receved"+townId, Toast.LENGTH_SHORT).show();
        checkIsAdmin();
    }


    private void checkIsAdmin() {

        Log.i(TAG, "checking IsAdmin.. ");

        if(mAuth.getCurrentUser() !=null){
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String userid = firebaseUser.getUid();
            Log.e(TAG, "userid: "+userid);
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
            rootRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        try{

                            if(snapshot.child("isAdmin").exists())
                            {
                                Log.i(TAG, "Admin profile.");

                            }

                        }
                        catch (Exception e){
                            Log.e(TAG, "Exception onDataChange: "+e.getLocalizedMessage() );

                        }

                        // Exist! Do something.
                        // Toast.makeText(requireContext(), " load data success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "snapshot value : "+snapshot.getValue());


                    } else {

                        // Don't exist! Do something.
                       // Toast.makeText(requireContext(), "Admin check Error ", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: "+error.getDetails() );
                }

            });

        }


    }





}