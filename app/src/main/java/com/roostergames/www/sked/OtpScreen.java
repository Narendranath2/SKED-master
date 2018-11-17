package com.roostergames.www.sked;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class OtpScreen extends AppCompatActivity {
    ProgressDialog progressDialog;
    ConstraintLayout constraintLayout;
    EditText phoneno,otp,idno;
    Button sendotp,verify;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String codesent = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        constraintLayout = (ConstraintLayout)findViewById(R.id.otpconstraintlayout);
        mAuth = FirebaseAuth.getInstance();
        phoneno = (EditText)findViewById(R.id.otpphonenum);
        idno = (EditText)findViewById(R.id.editText5);
        otp = (EditText)findViewById(R.id.otp);
        sendotp = (Button)findViewById(R.id.verifycode);
        verify = (Button)findViewById(R.id.verifybtn);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneno.getText().toString().isEmpty())
                {
                    phoneno.setError("Phone number should not be empty");
                    phoneno.requestFocus();
                }
                if(phoneno.getText().toString().length()<10)
                {
                    phoneno.setError("Please enter a valid number");
                    phoneno.requestFocus();
                }
                if(idno.getText().toString().isEmpty())
                {
                    idno.setError("Invalid Id No");
                    idno.requestFocus();
                }
                if(idno.getText().toString().length() < 9)
                {
                    idno.setError("Invalid Id No");
                    idno.requestFocus();
                }
                db.collection("users").document(idno.getText().toString()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists())
                                {
                                    SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("useridno",idno.getText().toString());
                                    editor.apply();
                                    if(phoneno.getText().toString().contains(documentSnapshot.getString("phono")))
                                    sendVerificationCode();
                                    else
                                        Snackbar.make(constraintLayout,"Phone number doesnt match with registered Id No",Snackbar.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Snackbar.make(constraintLayout,"No such user found please register",Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(constraintLayout,"Error Occured",Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtpSent();

            }
        });
    }
    void verifyOtpSent()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying");
        if(otp.getText().toString().isEmpty())
        {
            otp.setError("Enter OTP");
            otp.requestFocus();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, otp.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }
    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //change Screen
                            progressDialog.dismiss();
                            Intent i = new Intent(OtpScreen.this,PasswordResetScreen.class);
                            startActivity(i);
                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(constraintLayout,"Invalid OTP",Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }

                });
    }
    void sendVerificationCode()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Verification Code");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneno.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            progressDialog.dismiss();
            super.onCodeSent(s, forceResendingToken);
            codesent = s;
        }
    };
}