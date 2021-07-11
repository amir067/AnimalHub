package com.example.animalhub.ui.dashboard.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.animalhub.Interface.OnAlertDialogButtonClickListener;
import com.example.animalhub.R;
import com.example.animalhub.model.UserModel;
import com.example.animalhub.utils.MyUtils;
import com.example.animalhub.utils.PreferenceHelperDemo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


public class ProfileFragment extends Fragment implements LocationListener {

    private static final String TAG = "ProfileFragment";


    //Binding
    @BindView(R.id.cv_profile)
    CardView userProfileCV;

    @BindView(R.id.iv_user_dp)
    ImageView userProfileIV;

    @BindView(R.id.addLocationBtn)
    Button addLocationBtn;


    //UI
    private TextView userName;
    private TextView email;
    private TextView phone;
    private TextView address;
    private TextView cityTV;
    private TextView bio;
    private ImageView userImage;
    private Button logoutButton;

    //PermissionsCode
    public static final int IMAGE_SELECT_CODE = 1001;
    //StoreCodes
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Location request
    private static final int ACCESS_FINE_LOCATION = 1122;
    private static final int REQ_LOCATION_FOR_BTN = 1122;

    //Helpers
    private PreferenceHelperDemo preferenceHelperDemo;
    private NavController navController;
    private int backPress = 0;

    // user
    private String usr_name;
    private String usr_phone;
    private String usr_Address;
    private String usr_city;
    private String usr_biography;
    private String imageURL;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    //Edit Profile
    private EditText et_Name;
    private EditText et_Phone;
    private EditText et_Address;
    private EditText et_City;
    private EditText et_biography;

    private ImageView update_imageView;
    private Uri image_uri = null;
    private BottomSheetDialog bottomSheetDialog;

    LocationManager locationManager;
    String provider;

    //Strings
    private String s_email;
    private String s_name;
    private String s_phone;
    private String s_address;
    private String s_city;
    private String s_biography;

    // latitude and longitude
    double latitudeLocal = 0;
    double longitudeLocal = 0;

    // The map object for reference for ease of adding points and such
    private GoogleMap mGoogleMap;
    private MapView mMapView;


    // The camera position tells us where the view is on the map
    // Current latitude and longitude of user


    //Loading Containers
    LottieAnimationView loadingAnimationViewDots;
    LottieAnimationView loadingAnimationViewBio;
    private ProgressBar progressBar;
    private AlertDialog loading_dialog;

    UserModel userModel = new UserModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        // The google map...
        MapsInitializer.initialize(requireActivity());
        mMapView = (MapView) view.findViewById(R.id.mv_place_map);
        mMapView.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // getActivity().setTheme(R.style.AppTheme);
        super.onViewCreated(view, savedInstanceState);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        loading_dialog = MyUtils.getLoadingDialog(requireActivity());
        logoutButton = view.findViewById(R.id.btn_sign_out);
        loadingAnimationViewDots = view.findViewById(R.id.loading_animation_view_full);
        loadingAnimationViewBio = view.findViewById(R.id.loading_animation_view_bio);

        // The google map...
        //mMapView =  view.findViewById(R.id.mv_place_map);
        //mMapView.onCreate(savedInstanceState);

        progressBar = view.findViewById(R.id.pb_profile);
        userName = view.findViewById(R.id.tv_user_name);
        userImage = view.findViewById(R.id.iv_user_dp);
        bio = view.findViewById(R.id.tv_user_bio);
        email = view.findViewById(R.id.tv_email);
        phone = view.findViewById(R.id.tv_phone);
        address = view.findViewById(R.id.tv_address);
        cityTV = view.findViewById(R.id.tv_city);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        preferenceHelperDemo = new PreferenceHelperDemo(requireContext());
        navController = Navigation.findNavController(view);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);
        } else {
            locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            // Getting LocationManager object
            statusCheck();
        }

        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toasty.warning(requireContext(),"Plz allow location permissions!",Toasty.LENGTH_SHORT).show();
                    // Check Permissions Now
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION_FOR_BTN);
                } else {

                    if(!isGPSEnabled() && !isNetworkEnabled()){
                        Toasty.warning(requireContext(),"Plz Turn On Gps !",Toasty.LENGTH_SHORT).show();
                        showDialogToTurnOnLocation();
                    }else{
                        putLocation();
                    }
                }
            }
        });


        getUserData();
    }
    private void showDialogToTurnOnLocation() {
        MyUtils.showDialogToTurnOnLocation(requireContext(), new OnAlertDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick(DialogInterface d) {
                d.dismiss();
            }

            @Override
            public void onNegativeButtonClick(DialogInterface d) {
                d.dismiss();
                showDialogToTurnOnLocation();
            }
        });
    }

    private void putLocation() {
        Log.e(TAG, "putLocation: putting lat long in user profile");
        if (userModel.getId() != null) {

            Log.e(TAG, "putLocation: current local values:");
            Log.e(TAG, "latitudeLocal: " + latitudeLocal);
            Log.e(TAG, "longitudeLocal: " + longitudeLocal);

            if (latitudeLocal != 0 && longitudeLocal != 0) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User").child(userModel.getId());

                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("latMAP", latitudeLocal);
                userMap.put("longMAP", longitudeLocal);

                rootRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "onComplete:set user location lat long success");
                            addLocationBtn.setVisibility(View.GONE);
                        } else {
                            Log.e(TAG, "onComplete:set user location lat long failure");
                            addLocationBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
                progressBar.setVisibility(View.VISIBLE);
                getUserData();

            } else {
                Log.e(TAG, "latitudeLocal != 0 && latitudeLocal != 0 > else called");
                addLocationBtn.setVisibility(View.VISIBLE);
                Toasty.info(requireContext(), "Enable GPS First!", Toasty.LENGTH_SHORT).show();
                Log.e(TAG, "latitudeLocal: " + latitudeLocal);
                Log.e(TAG, "longitudeLocal: " + longitudeLocal);
            }

        } else {
            Log.e(TAG, "onLocationChanged: get ui else called");
        }


    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) requireActivity().getSystemService(
                Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            // Creating an empty criteria object
            Criteria criteria = new Criteria();
            // Getting the name of the provider that meets the criteria
            provider = locationManager.getBestProvider(criteria, false);

            if (provider != null && !provider.equals("")) {
                if (!provider.contains("gps")) { // if gps is disabled
                    final Intent poke = new Intent();
                    poke.setClassName("com.android.settings",
                            "com.android.settings.widget.SettingsAppWidgetProvider");
                    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                    poke.setData(Uri.parse("3"));
                    getActivity().sendBroadcast(poke);
                }
                // Get the location from the given provider
                if (ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ACCESS_FINE_LOCATION);
                }else{
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, this::onLocationChanged);

                    if (location != null)
                        onLocationChanged(location);
                    else
                        location = locationManager.getLastKnownLocation(provider);

                    if (location != null)
                        onLocationChanged(location);
                    else
                        Toast.makeText(requireContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(requireContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(
                "Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: inside" );

        if(location!=null){
            // Setting Current Longitude
            Log.e(TAG, "onLocationChanged: "+location.getLongitude() );
            Log.e(TAG, "onLocationChanged: "+ location.getLatitude());

            latitudeLocal = location.getLatitude();
            longitudeLocal = location.getLongitude();
            Log.e(TAG, "onLocationChanged: provider: "+  location.getProvider());

           // putLocation();

        }else{
            Log.e(TAG, "onLocationChanged: null location received" );
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    0);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        //putLocation();

        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void getUserData() {

        loadingAnimationViewDots.setVisibility(View.VISIBLE);
        //loading_dialog.show();
        //progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "getUserData: getting.." );


        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser!=null){

            String userid = firebaseUser.getUid();
            Log.e(TAG, "userid: "+userid);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");

            rootRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e(TAG, "onDataChange: data " );

                    if (snapshot.exists()) {
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "onDataChange: data exists" );
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingAnimationViewDots.setVisibility(View.GONE);
                            }
                        }, 1000); // for 3 second

                        try{

                            userModel = snapshot.getValue(UserModel.class);

                            if( userModel.getName() !=null){
                                userName.setText(userModel.getName());
                                usr_name= userModel.getName();
                                s_name= userModel.getName();
                                Log.i(TAG, "parsing-Check1: "+usr_name);
                            }
                            if(userModel.getEmail() !=null){
                                email.setText(userModel.getEmail());
                                s_email= userModel.getEmail();
                                Log.i(TAG, "parsing-Check2: "+s_email);
                            }
                            if(userModel.getPhone() !=null){
                                phone.setText(userModel.getPhone());
                                s_phone= userModel.getPhone();
                                Log.i(TAG, "parsing-Check3: "+s_phone);
                            }
                            if(userModel.getAddress() !=null){
                                address.setText(userModel.getAddress());
                                s_address= userModel.getAddress();
                                Log.i(TAG, "parsing-Check4: "+s_address);
                            }

                            if(userModel.getCity() !=null){
                                cityTV.setText(userModel.getCity());
                                usr_city= userModel.getCity();
                                s_city= userModel.getCity();
                                Log.i(TAG, "parsing-Check4: "+usr_city);
                            }

                            if(userModel.getUserBio() != null){
                                s_biography= userModel.getUserBio();
                                Log.i(TAG, "parsing-Check5: "+s_biography);

                                loadingAnimationViewBio.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingAnimationViewBio.setVisibility(View.GONE);
                                        bio.setText(userModel.getUserBio());
                                    }
                                }, 1000); // for 3 second


                            }else{
                                loadingAnimationViewBio.setVisibility(View.GONE);
                            }

                            if(userModel.getProfileImageUrl() != null){
                                Log.i(TAG, "parsing-Check5: "+s_biography);

                                imageURL=userModel.getProfileImageUrl();

                                userProfileCV.setAlpha(0);
                                userProfileCV.setVisibility(View.VISIBLE);
                                userProfileCV.animate().alpha(1).setDuration(1000).start();

                                Glide.with(requireContext())
                                        .load( userModel.getProfileImageUrl() )////getResources().getDrawable(R.drawable.ic_std_avatar_male)
                                        .placeholder(R.drawable.ic_wait_white)
                                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                                        .error(R.drawable.ic_std_avatar_male)
                                        .into(userProfileIV);

                            }else{

                                userProfileCV.setVisibility(View.VISIBLE);

                                if(userModel.getGender() !=null){

                                    if(userModel.getGender().equals("Male")){
                                        userProfileIV.setImageResource(R.drawable.ic_std_avatar_male);
                                    }
                                    else{
                                        userProfileIV.setImageResource(R.drawable.ic_std_avatar_female);
                                    }
                                }else{
                                    userProfileIV.setImageResource(R.drawable.ic_std_avatar_male);
                                }

                            }

                            if((userModel.getLatMAP()!=0) && (userModel.getLongMAP()!=0)){

                                LatLng latLng;
                                latLng = new LatLng((userModel.getLatMAP()),(userModel.getLongMAP()));

                                mMapView.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        mMapView.setVisibility(View.VISIBLE);
                                        mGoogleMap = googleMap;

                                        mMapView.canScrollHorizontally(0);
                                        mMapView.canScrollVertically(0);

                                        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
                                        mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
                                        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);

                                        // LatLng latLng = new LatLng(31.4156498,74.2714092);

                                        //String tittle = placeData.getName();

                                        Log.i(TAG, "onMapReady: lat long :"+latLng );

                                        //gmap.setMyLocationEnabled(true);
                                        //To add marker
                                        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(userModel.getName()).icon(bitmapDescriptorFromVector(requireContext(),R.drawable.ic_mark_map_circle)));
                                        // For zooming functionality
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
                                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        //gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                                    }
                                });

                                addLocationBtn.setVisibility(View.GONE);
                            }
                            else{
                                mMapView.setVisibility(View.GONE);
                                addLocationBtn.setVisibility(View.VISIBLE);
                            }

                        }
                        catch (Exception e){
                            Log.e(TAG, "Exception onDataChange: "+e.getLocalizedMessage() );

                        }
                        // Exist! Do something.
                        // Toast.makeText(requireContext(), " load data success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "snapshot value : "+snapshot.getValue());
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "onDataChange: data not exits" );
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingAnimationViewDots.setVisibility(View.GONE);
                            }
                        }, 1000); // for 3 second

                        // Don't exist! Do something.
                        Toast.makeText(requireContext(), "data not found ", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: "+error.getDetails() );
                }

            });

        }

    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        //startActivity(new Intent(requireActivity(), LoginActivity.class));
        requireActivity().finish();
        //navController.navigate(R.id.action_profileFragment_to_navigation_login_fragment);

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @OnClick(R.id.btn_et)
    public void editProfile() {

        Log.i(TAG, "editProfile: ");

        @SuppressLint("InflateParams") View view1 = this.getLayoutInflater().inflate(R.layout.activity_edit_profile, null);

        et_Name = view1.findViewById(R.id.et_Name);
        et_Name.setText(s_name);

        et_Address = view1.findViewById(R.id.et_address);
        et_Address.setText(s_address);

        et_City = view1.findViewById(R.id.et_city);
        et_City.setText(s_city);

        et_Phone = view1.findViewById(R.id.et_Phone);
        et_Phone.setText(s_phone);

        et_biography = view1.findViewById(R.id.et_bio);
        et_biography.setText(s_biography);

        TextView tv_email = view1.findViewById(R.id.tv_email);
        tv_email.setText(s_email);

        update_imageView = view1.findViewById(R.id.update_imageView);
        update_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        if(imageURL !=null){
            Glide.with(this)
                    .load(imageURL)
                    .placeholder(R.drawable.ic_wait_white)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .error(R.drawable.bg_no_image)
                    .into(update_imageView);
        }


        Button button = view1.findViewById(R.id.btnSaveButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s_name = et_Name.getText().toString();
                s_phone = et_Phone.getText().toString();
                s_address = et_Address.getText().toString();
                s_city = et_City.getText().toString();
                s_biography = et_biography.getText().toString();

                if (TextUtils.isEmpty(s_name)) {
                    et_Name.setError("Enter Name");
                    et_Name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(s_phone)) {
                    et_Phone.setError("Enter Phone Number");
                    et_Phone.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(s_address)) {
                    et_Address.setError("Enter Address");
                    et_Address.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(s_city)) {
                    et_City.setError("Enter City");
                    et_City.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(s_biography)) {
                    et_biography.setError("Enter some about yourself");
                    et_biography.requestFocus();
                    return;
                }

                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                String userid = firebaseUser.getUid();

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User").child(userid);

                loading_dialog.show();
                loading_dialog.setTitle("Please wait");
                loading_dialog.setCancelable(false);

                if (image_uri != null) {

                    mStorageRef.child("users/" + userid).putFile(image_uri)

                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d(TAG, "onViewClicked: " + "photo upload");

                                    Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl()

                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    Map<String, Object> blog_update = new HashMap<>();
                                                    blog_update.put("name", s_name);
                                                    blog_update.put("phone", s_phone);
                                                    blog_update.put("address", s_address);
                                                    blog_update.put("city", s_city);
                                                    blog_update.put("userBio", s_biography);
                                                    blog_update.put("profileImageUrl", uri.toString());
                                                    blog_update.put("updated_at", new Date().getTime());

                                                    rootRef.updateChildren(blog_update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            loading_dialog.dismiss();
                                                            bottomSheetDialog.dismiss();
                                                            Log.d(TAG, "onSuccess: update ");
                                                            et_Name.setText("");
                                                            et_Phone.setText("");
                                                            et_Address.setText("");
                                                            et_Address.setText("");
                                                            et_biography.setText("");

                                                            getUserData();

                                                            Toasty.success(requireContext(), "Update Profile Successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    loading_dialog.dismiss();
                                                                    if (e instanceof IOException)
                                                                        Toast.makeText(requireContext(), "internet connection error", Toast.LENGTH_SHORT).show();
                                                                    else
                                                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                                                }
                                                            });

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    loading_dialog.dismiss();
                                                    if (e instanceof IOException)
                                                        Toast.makeText(requireContext(), "internet connection error", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                                }
                                            });
                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loading_dialog.dismiss();
                                    if (e instanceof IOException)
                                        Toast.makeText(requireContext(), "internet connection error", Toast.LENGTH_SHORT).show();
                                    else
                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });

                } else {

                    Map<String, Object> blog_update = new HashMap<>();
                    blog_update.put("name", s_name);
                    blog_update.put("phone", s_phone);
                    blog_update.put("address", s_address);
                    blog_update.put("city", s_city);
                    blog_update.put("userBio", s_biography);
                    blog_update.put("updated_at", new Date().getTime());

                    rootRef.updateChildren(blog_update)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loading_dialog.dismiss();
                                    bottomSheetDialog.dismiss();
                                    Log.d(TAG, "onSuccess: update ");

                                    getUserData();

                                    et_Name.setText("");
                                    et_Phone.setText("");
                                    et_Address.setText("");
                                    et_City.setText("");
                                    et_biography.setText("");

                                    Toasty.success(requireContext(), "Profile Updated Successfully ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loading_dialog.dismiss();
                                    if (e instanceof IOException)
                                        Toast.makeText(requireContext(), "internet connection error", Toast.LENGTH_SHORT).show();
                                    else
                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });

                }

            }
        });
        DisplayMetrics displayMetrics = requireContext().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int maxHeight = (int) (height*1.80);

        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();

        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setCancelable(true);

    }

    private void pickImage() {
        requestStoragePermission();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_SELECT_CODE);
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
            requestStoragePermission();

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECT_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    Toast.makeText(requireContext(), "Unable to handle image.", Toast.LENGTH_SHORT).show();
                    image_uri = null;
                    update_imageView.setImageResource(R.drawable.bg_no_image);
                    return;
                }
                image_uri = data.getData();
                {
                    Log.i(TAG, "onActivityResult: setting image");
                    update_imageView.setImageURI(image_uri);


                }
            } else {
                image_uri = null;
                update_imageView.setImageResource(R.drawable.ic_std_avatar_male);
            }
        }else if(requestCode ==ACCESS_FINE_LOCATION){
            Log.e(TAG, "onActivityResult: permision code called" );
            if (resultCode == Activity.RESULT_OK) {
                //mMapView.setVisibility(View.VISIBLE);
                addLocationBtn.setVisibility(View.GONE);

            } else {
                addLocationBtn.setVisibility(View.VISIBLE);
            }

        }
    }


    public boolean isGPSEnabled(){
        LocationManager lm = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }

    public boolean isNetworkEnabled(){
        LocationManager lm = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = false;

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

        }
        return network_enabled;
    }



}