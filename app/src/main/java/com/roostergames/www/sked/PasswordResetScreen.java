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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordResetScreen extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    EditText newpassword,confirmpassword;
    Button savechanges;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset_screen);
        constraintLayout = (ConstraintLayout)findViewById(R.id.passwordresetscreenlayout);
        SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
        String useridno = sharedPreferences.getString("useridno","");
        newpassword = (EditText)findViewById(R.id.editText3);
        confirmpassword = (EditText)findViewById(R.id.editText4);
        savechanges = (Button)findViewById(R.id.button3);
        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpassword.getText().toString().length() < 6)
                {
                    newpassword.setError("Password should contain atleast 6 characters");
                    newpassword.requestFocus();
                    return;
                }
                if(!newpassword.getText().toString().contains(confirmpassword.getText().toString()))
                {
                    confirmpassword.setError("Passwords does not match");
                    confirmpassword.requestFocus();
                    return;
                }
                updatePassword();
            }
        });
    }
    void updatePassword()
    {
        progressDialog = new ProgressDialog(PasswordResetScreen.this);
        progressDialog.setMessage("Updating Credentials");
        progressDialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
        String useridno = sharedPreferences.getString("useridno","");
        db.collection("users").document(useridno).update("password",confirmpassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(PasswordResetScreen.this,"Password Set Successfully",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PasswordResetScreen.this,LoginScreen.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Intent i = new Intent(PasswordResetScreen.this,OtpScreen.class);
                startActivity(i);
            }
        });
    }
}
