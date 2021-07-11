package com.example.animalhub.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animalhub.OldData.HomeActivityOld;
import com.example.animalhub.R;
import com.example.animalhub.model.Register_ModelClass;
import com.example.animalhub.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SignUp_Activity extends AppCompatActivity      {
    private static final String TAG = "SignUp_Activity";

    public enum SignUPChoice{
        EMAIL,
        PHONE
    }

    ImageView back;
    EditText u_name, number, u_email, u_password, u_location,confirm_password;
    Button register;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$");
    String name,email,password,phone,location;
    ProgressDialog progressDialog;


    @BindView(R.id.choiceRG)
    RadioGroup choiceRG;

    @BindView(R.id.emailRB)
    RadioButton emailRB;

    @BindView(R.id.phoneRB)
    RadioButton phoneRB;

    @BindView(R.id.email_ll)
    RelativeLayout email_ll;

    @BindView(R.id.phone_ll)
    LinearLayout phone_ll;

    SignUPChoice signUPChoice = SignUPChoice.EMAIL;


    @BindView(R.id.codeSendTimeTV)
    TextView codeSendTimeTV;

    @BindView(R.id.codeResendBtn)
    Button codeResendBtn;

    @BindView(R.id.codeSentTimePB)
    ProgressBar codeSentTimePB;

    @BindView(R.id.phone2)
    EditText phone2;

    //Phone number verification

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    //Phone UI
    private EditText editText1, editText2, editText3, editText4,editText5,editText6;
    private EditText[] editTexts;
    @BindView(R.id.ly_code_screen)
    LinearLayout codeScreenLayout;

    @BindView(R.id.varifyCodeBtn)
    Button varifyCodeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);
        ButterKnife.bind(this);
        back = findViewById(R.id.back);
        u_name = findViewById(R.id.uname);
        number = findViewById(R.id.phone);
        u_email = findViewById(R.id.email);
        u_password = findViewById(R.id.password);
        u_location = findViewById(R.id.location);
        register = findViewById(R.id.register);
        confirm_password = findViewById(R.id.confirm_password);
        mAuth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Processing...");

        //phone verification ui
        editText1 = (EditText) findViewById(R.id.et_code_p1);
        editText2 = (EditText) findViewById(R.id.et_code_p2);
        editText3 = (EditText) findViewById(R.id.et_code_p3);
        editText4 = (EditText) findViewById(R.id.et_code_p4);
        editText5 = (EditText) findViewById(R.id.et_code_p5);
        editText6 = (EditText) findViewById(R.id.et_code_p6);

        editTexts = new EditText[]{editText1, editText2, editText3, editText4,editText5,editText6};

        editText1.addTextChangedListener(new PinTextWatcher(0));
        editText2.addTextChangedListener(new PinTextWatcher(1));
        editText3.addTextChangedListener(new PinTextWatcher(2));
        editText4.addTextChangedListener(new PinTextWatcher(3));
        editText5.addTextChangedListener(new PinTextWatcher(4));
        editText6.addTextChangedListener(new PinTextWatcher(5));

        editText1.setOnKeyListener(new PinOnKeyListener(0));
        editText2.setOnKeyListener(new PinOnKeyListener(1));
        editText3.setOnKeyListener(new PinOnKeyListener(2));
        editText4.setOnKeyListener(new PinOnKeyListener(3));
        editText5.setOnKeyListener(new PinOnKeyListener(4));
        editText6.setOnKeyListener(new PinOnKeyListener(5));


        //CAllBacks
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.e(TAG, "onVerificationCompleted: called" );
                //Getting the code sent by SMS
                String code = credential.getSmsCode();
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);


                if (code != null) {
                    Log.e(TAG, "onVerificationCompleted: otp receved code: "+code );

                    String[] codeArray =new String[6] ;

                    for(int i=0; i < code.length(); i++) {
                        //num.charAt(i)
                        codeArray[i] = String.valueOf(code.charAt(i));
                        //codeArray[i] = String.valueOf(code.codePointAt(i));
                        //codeArray[i] = code.substring(i,6);
                    }

                    String codeP1 =code.substring(0,6);
                    editText1.setText(codeP1);

                    String codeP2 =code.substring(1,6);
                    editText2.setText(codeP2);

                    String codeP3 =code.substring(2,6);
                    editText3.setText(codeP3);

                    String codeP4 =code.substring(3,6);
                    editText4.setText(codeP4);

                    String codeP5 =code.substring(4,6);
                    editText5.setText(codeP5);

                    String codeP6 =code.substring(5,6);
                    editText6.setText(codeP6);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            signInWithPhoneAuthCredential(credential);
                        }
                    } ,400);
                    //verifying the code
                    //verifyVerificationCode(code);
                    Toasty.success(SignUp_Activity.this, "Success", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                showCountDownUI();
            }
        };
        // [END phone_auth_callbacks]

        choiceRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == emailRB.getId()){
                    email_ll.setVisibility(View.VISIBLE);
                    signUPChoice = SignUPChoice.EMAIL;
                    phone_ll.setVisibility(View.GONE);

                    signUPChoice = SignUPChoice.EMAIL;
                }else{
                    signUPChoice = SignUPChoice.PHONE;
                    email_ll.setVisibility(View.GONE);
                    signUPChoice = SignUPChoice.PHONE;
                    phone_ll.setVisibility(View.VISIBLE);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(signUPChoice ==SignUPChoice.EMAIL){
                    Log.e(TAG, "onClick: signUPChoice ==SignUPChoice.EMAIL" );
                    name = u_name.getText().toString().toUpperCase();
                    email = u_email.getText().toString();
                    password = u_password.getText().toString();
                    phone = number.getText().toString();
                    location = u_location.getText().toString().toUpperCase();
                    String c_pas = confirm_password.getText().toString();

                    if (email.isEmpty() || password.isEmpty() || phone.isEmpty() || name.isEmpty() || location.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Fill all Fields first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        u_email.setError("Invalid Email Address ");
                        return;
                    }else if(!c_pas.equals(password)){
                        confirm_password.setError("Password not matched");
                        return;
                    }

                    doRegisterWithEmail();

                }else if(signUPChoice ==SignUPChoice.PHONE){
                    Log.e(TAG, "onClick: signUPChoice ==SignUPChoice.PHONE" );

                    String mobileNumber = phone2.getText().toString();
                    if ( mobileNumber.isEmpty()  ) {
                        //Toast.makeText(getApplicationContext(), "Please enter your phone number!", Toast.LENGTH_SHORT).show();
                        phone2.setError("Enter phone number!");
                        phone2.requestFocus();
                        return;
                    }
                    Log.e(TAG, " mobileNumber : "+mobileNumber );
                    doRegistedWithPhone(mobileNumber);

                }

            }
        });

    }

    private void showCountDownUI() {
        codeScreenLayout.setVisibility(View.VISIBLE);
        varifyCodeBtn.setVisibility(View.VISIBLE);
        codeSendTimeTV.setVisibility(View.VISIBLE);

        register.setVisibility(View.GONE);
        phone2.setVisibility(View.GONE);

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                codeSendTimeTV.setText("Next Request after: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                codeSendTimeTV.setVisibility(View.GONE);
                codeResendBtn.setVisibility(View.VISIBLE);
                phone2.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    private void doRegistedWithPhone(String mobileNumber) {
        Log.e(TAG, "doRegistedWithPhone: called" );
        if(! mobileNumber.isEmpty()){
            startPhoneNumberVerification(mobileNumber);
        }else{
            Log.e(TAG, "doRegistedWithPhone: mobileNumber.length()>=11" );
        }
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        String value = phoneNumber.substring(1);
        String numberOk = "+92"+value;
        Toast.makeText(this,"Sending OTP code to: "+numberOk,Toast.LENGTH_SHORT).show();

        // [START start_phone_auth]
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(numberOk)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    @OnClick(R.id.codeResendBtn)
    public void resendCodeBtnClicked() {
        Log.e(TAG, "resendCodeBtnClicked: called" );
        String mobileNumber = phone2.getText().toString();
        if ( mobileNumber.isEmpty()  ) {
            //Toast.makeText(getApplicationContext(), "Please enter your phone number!", Toast.LENGTH_SHORT).show();
            phone2.setError("Enter phone number!");
            phone2.requestFocus();
            return;
        }
        Log.e(TAG, " mobileNumber : "+mobileNumber );
        resendVerificationCode(mobileNumber ,mResendToken);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            doUploadUserData(SignUPChoice.PHONE,user);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    @OnClick(R.id.varifyCodeBtn)
    public void SubmitClicked( View v){
        Log.e(TAG, "SubmitClicked: " );
        String codeP1 = editText1.getText().toString().trim();
        String codeP2 = editText2.getText().toString().trim();
        String codeP3 = editText3.getText().toString().trim();
        String codeP4 = editText4.getText().toString().trim();
        String codeP5 = editText5.getText().toString().trim();
        String codeP6 = editText6.getText().toString().trim();
        String allParts=codeP1.concat(codeP2).concat(codeP3).concat(codeP4).concat(codeP5).concat(codeP6);
        if (allParts.isEmpty() || allParts.length() < 6) {
            Toasty.error(SignUp_Activity.this,"Invalid code input!",Toasty.LENGTH_SHORT).show();
            return;
        }
        Log.e(TAG, "onClick: veriy by manul code is:"+allParts );
        //verifying the code entered manually
        verifyPhoneNumberWithCode(mVerificationId,allParts);
        Log.e(TAG, "SubmitClicked: going back :"+allParts );
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        Log.e(TAG, "verifyVerificationCode: "+credential.getSmsCode() );
        signInWithPhoneAuthCredential(credential);
    }


    private void doRegisterWithEmail() {
        progressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    doUploadUserData(signUPChoice,user);

                    Toast.makeText(getApplicationContext(), "Sign up Success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


                } else {
                    Toast.makeText(getApplicationContext(), "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
        progressDialog.dismiss();

    }

    private void doUploadUserData( SignUPChoice signUPChoice, FirebaseUser user){
        Log.e(TAG, "doUploadUserData: called" );
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        String Id = user.getUid();
        Log.e("TAG", "  user id is:"+Id);

        Register_ModelClass register = new Register_ModelClass();
        register.setId(Id);
        register.setDate(currentDate);


        if(signUPChoice == SignUPChoice.EMAIL){
            Log.e(TAG, "doUploadUserData: if" );
            register.setEmail(email);
            register.setName(name);
            register.setPhone(phone);
            register.setDelete(false);
            register.setLocation(location);

        }else if(signUPChoice == SignUPChoice.PHONE){
            Log.e(TAG, "doUploadUserData: else if" );
            register.setEmail("");
            register.setName("");
            register.setPhone(phone2.getText().toString());
            register.setDelete(false);
            register.setLocation("");
        }else{
            Log.e(TAG, "doUploadUserData: else end" );

        }


        databaseReference.child(Id).setValue(register).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Log.e("TAG", "onComplete:put data in db " );

                if(task.isSuccessful()){
                    Log.e(TAG, "onComplete: task Successful" );
                    updateUI(user);
                }else{
                    Log.e(TAG, "onComplete:doUploadUserData failure " );
                    updateUI(user);
                }

            }
        });
    }

    private void updateUI(FirebaseUser user) {
        Log.e(TAG, "updateUI: called" );

        if(user !=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finishAffinity();
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Log.e(TAG, "updateUI: user is null" );
        }

    }


    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }


}