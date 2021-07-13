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

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.listners.AlLoginHandler;
import com.applozic.mobicomkit.listners.AlLogoutHandler;
import com.applozic.mobicomkit.listners.AlPushNotificationHandler;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.example.animalhub.R;
import com.example.animalhub.model.UserModel;
import com.example.animalhub.ui.Admin.Admin_Activity;
import com.example.animalhub.ui.auth.LogInActivity;
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

import static com.example.animalhub.utils.Constants.ADMIN_EMAIL;

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

    @BindView(R.id.btn_bar_btn4)
    MaterialButton btn_bar_btn4;

    @BindView(R.id.btn_all_chats)
    MaterialButton btn_all_chats;


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

    UserModel userModel = new UserModel();


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

        btn_bar_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ini = new Intent(requireContext(), Admin_Activity.class);
                startActivity(ini);

            }
        });

        btn_all_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: btn_all_chats" );
                Intent intent = new Intent(requireContext(), ConversationActivity.class);
                startActivity(intent);

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
            rootRef.child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e(TAG, "onDataChange: ok" );
                    if (snapshot.exists()) {
                        Log.e(TAG, "onDataChange: data esists" );

                        try{

                            userModel = snapshot.getValue(UserModel.class);
                            if(snapshot.child("email").exists())
                            {
                                if(snapshot.child("email").getValue().equals(ADMIN_EMAIL)){
                                    Log.i(TAG, "Admin profile.");
                                    btn_bar_btn4.setVisibility(View.VISIBLE);
                                }else{
                                    Log.i(TAG, "not Admin profile.");
                                    btn_bar_btn4.setVisibility(View.GONE);
                                }


                            }else{//phone verified

                            }


                            Log.e(TAG, "going to register for appolizc " );
                            doRegistedUserOnAppolic( userModel);


                        }
                        catch (Exception e){
                            Log.e(TAG, "onDataChange: parsing error in try block" );
                            Log.e(TAG, "Exception onDataChange: "+e.getLocalizedMessage() );

                        }

                        // Exist! Do something.
                        // Toast.makeText(requireContext(), " load data success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "snapshot value : "+snapshot.getValue());


                    } else {
                        Toasty.error(requireContext(),"Your Account removed by admin!",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        doLogoutOnApplozic();
                        startActivity(new Intent(requireContext(), LogInActivity.class));
                        requireActivity().finish();
                        Log.e(TAG, "onDataChange: data not exits" );

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


    private void doRegistedUserOnAppolic(UserModel userModel) {
        Log.e(TAG, "doRegistedUserOnAppolic: called" );

        User user = new User();
        user.setUserId(userModel.getId()); //userId it can be any unique user identifier
        user.setDisplayName(userModel.getName()); //displayName is the name of the user which will be shown in chat messages
        user.setEmail(userModel.getEmail()); //optional
        user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());  //User.AuthenticationType.APPLOZIC.getValue() for password verification from Applozic server and User.AuthenticationType.CLIENT.getValue() for access Token verification from your server set access token as password
        user.setPassword(""); //optional, leave it blank for testing purpose, read this if you want to add additional security by verifying password from your server https://www.applozic.com/docs/configuration.html#access-token-url

        if(userModel.getProfileImageUrl() !=null){
            if(userModel.getProfileImageUrl().equals("default")){
                user.setImageLink("");//optional,pass your image link
            }else{
                user.setImageLink(userModel.getProfileImageUrl());//optional,pass your image link
            }
        }else{
            user.setImageLink("");//optional,pass your image link
        }

        if (Applozic.isConnected(requireContext())) {
            Log.e(TAG, "doRegistedUserOnAppolic: user is alredy logged in" );

        }

        {
            Log.e(TAG, "doRegistedUserOn Appolizc: logging user..." );
            Applozic.connectUser(requireContext(), user, new AlLoginHandler() {
                @Override
                public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                    // After successful registration with Applozic server the callback will come here
                    registerForNotification(registrationResponse);
                    UpdateUserStatusForChats();
                }

                @Override
                public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                    // If any failure in registration the callback  will come here
                }
            });
        }



    }

    private void doLogoutOnApplozic() {
        Log.e(TAG, "doLogoutOnApplozic: " );
        Applozic.logoutUser(requireContext(), new AlLogoutHandler() {
            @Override
            public void onSuccess(Context context) {
                Log.e(TAG, "onSuccess: doLogoutOnApplozic" );
            }


            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onFailure: doLogoutOnApplozic" );

            }
        });


    }

    private void registerForNotification(RegistrationResponse registrationResponse) {
        if(MobiComUserPreference.getInstance(requireContext()).isRegistered()) {

            Applozic.registerForPushNotification(requireContext(), Applozic.getInstance(requireContext()).getDeviceRegistrationId(), new AlPushNotificationHandler() {//registrationToken
                @Override
                public void onSuccess(RegistrationResponse registrationResponse) {
                    Log.e(TAG, "onSuccess: registerForPushNotification" );
                }

                @Override
                public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                    Log.e(TAG, "onFailure: registerForPushNotification" );
                }
            });
        }

    }

    private void UpdateUserStatusForChats() {
        btn_all_chats.setVisibility(View.VISIBLE);
        Log.e(TAG, "UpdateUserStatusForChats: chat btn showed" );
    }


}