package com.roostergames.www.sked;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
public class LoginScreen extends AppCompatActivity {
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView createaccount,forgotPassword,registerComplaint;
    ConstraintLayout constraintLayout;
    EditText username,password;
    Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        constraintLayout = (ConstraintLayout)findViewById(R.id.constraintLayout2);
        createaccount = (TextView)findViewById(R.id.textView);
        forgotPassword = (TextView)findViewById(R.id.textView2);
        username = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);
        registerComplaint = (TextView)findViewById(R.id.supportassistance);
        loginbtn = (Button)findViewById(R.id.button);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correctForm(username.getText().toString(),password.getText().toString()))
                {
                    login();
                }
                else
                {
                    Snackbar.make(constraintLayout,"Enter valid details",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginScreen.this,SignupScreen.class);
                startActivity(i);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
                String useridno = sharedPreferences.getString("useridno","");
                    Intent i = new Intent(LoginScreen.this,OtpScreen.class);
                    startActivity(i);
            }
        });
        registerComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginScreen.this,RegisterComplaintScreen.class);
                startActivity(i);
            }
        });
    }
    public void login()
    {
        progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setMessage("Logging in");
        progressDialog.show();
        final String uname = username.getText().toString();
        final String pass = password.getText().toString();
        db.collection("users").document(username.getText().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            if(uname.contains(documentSnapshot.getString("idno")) && pass.contains(documentSnapshot.getString("password")))
                            {
                                progressDialog.dismiss();
                                saveUserDetails();
                                Intent i = new Intent(LoginScreen.this,HomeScreen.class);
                                startActivity(i);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Snackbar.make(constraintLayout,"Invalid Details",Snackbar.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Snackbar.make(constraintLayout,"No Such User Found Please Register",Snackbar.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(constraintLayout,"Error Occured",Snackbar.LENGTH_LONG).show();

            }
        });
    }
    public boolean correctForm(String idno,String password)
    {
        int correctCount = 0,idc = 0;
        char a[] = idno.toCharArray();
        for(int i=0;i<a.length;i++)
        {
            if((int)a[i] >=48 && (int)a[i] <=57)
            {
                idc++;
            }
        }
        if(idc == 9)
        {
            correctCount++;
        }
        if(password.length() > 6)
        {
            correctCount++;
        }
        if(correctCount == 2)
        {
            return  true;
        }
        else
        {
            return false;
        }
    }
    void saveUserDetails()
    {
        db.collection("users").document(username.getText().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loginstatus","yes");
                        editor.putString("useridno",username.getText().toString());
                        editor.putString("userrealname",documentSnapshot.getString("name"));
                        editor.putString("useryear",documentSnapshot.getString("year"));
                        editor.putString("usersecno",documentSnapshot.getString("secno"));
                        editor.apply();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(constraintLayout,"Error Occured",Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
