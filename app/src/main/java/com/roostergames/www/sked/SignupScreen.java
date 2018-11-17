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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupScreen extends AppCompatActivity {
    boolean passjudge=false,secnojudge=false,phnojudge=false,yearjudge=false,idnojudge=false;
    ConstraintLayout constraintLayout;
    Button signupbtn;
    EditText idno,passwowrd,sectionno,phoneno,name,year;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        constraintLayout = (ConstraintLayout)findViewById(R.id.constraintlayout);
        idno = (EditText)findViewById(R.id.idno);
        name = (EditText)findViewById(R.id.name);
        sectionno = (EditText)findViewById(R.id.secno);
        passwowrd = (EditText)findViewById(R.id.password);
        phoneno = (EditText)findViewById(R.id.phno);
        year = (EditText)findViewById(R.id.year);
        signupbtn = (Button)findViewById(R.id.button2);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                hadValidDetails(idno.getText().toString(),passwowrd.getText().toString(),sectionno.getText().toString(),phoneno.getText().toString(),year.getText().toString());
                if(idnojudge == true && yearjudge == true && phnojudge == true && passjudge == true && secnojudge == true)

                {
                    addUser();
                }
                else
                {
                    if(idnojudge == false)
                    {
                        idno.setError("Please enter a valid university id no");
                        idno.requestFocus();
                    }
                    if(yearjudge == false)
                    {
                        year.setError("Year should be b/w 1 and 4");
                        year.requestFocus();
                    }
                    if(phnojudge == false)
                    {
                        phoneno.setError("Please enter a valid phone number");
                        phoneno.requestFocus();
                    }
                    if(passjudge == false)
                    {
                        passwowrd.setError("Password should contain minimum 6 characters");
                        passwowrd.requestFocus();
                    }
                    if(secnojudge == false)
                    {
                        sectionno.setError("Pleasee enter correct section number");
                        sectionno.requestFocus();
                    }
                }
            }
        });

    }
    public void addUser()
    {
        progressDialog = new ProgressDialog(SignupScreen.this);
        progressDialog.setMessage("Signing Up");
        progressDialog.show();
        final Map<String, Object> user = new HashMap<>();
        user.put("idno", idno.getText().toString());
        user.put("name",name.getText().toString());
        user.put("year", year.getText().toString());
        user.put("secno",sectionno.getText().toString());
        user.put("password", passwowrd.getText().toString());
        user.put("phono", phoneno.getText().toString());
        db.collection("users").document(idno.getText().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            progressDialog.dismiss();
                            Snackbar.make(constraintLayout,"User Already Exists",Snackbar.LENGTH_LONG).show();
                        }
                        else
                        {
                            db.collection("users").document(idno.getText().toString()).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignupScreen.this,"Registeration Successful",Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(SignupScreen.this,LoginScreen.class);
                                            startActivity(i);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Snackbar.make(constraintLayout,"Error Occured",Snackbar.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(constraintLayout,"Error Occured",Snackbar.LENGTH_LONG).show();
            }
        });

    }
    public void hadValidDetails(String idno,String pass,String secno,String phno,String year)
    {
        int idc = 0,phc = 0,secc = 0;
        if(pass.length() > 6)
        {
            passjudge = true;
        }
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
            idnojudge = true;
        }
        char b[] = phno.toCharArray();
        for(int i=0;i<b.length;i++)
        {
            if((int)b[i] >=48 && (int)b[i] <=57)
            {
                phc++;
            }
        }
        if(phc == 10)
        {
            phnojudge = true;
        }
        if(Integer.valueOf(secno) >=1 && Integer.valueOf(secno) <=55)
        {
            secnojudge = true;
        }
        if(Integer.parseInt(year)>=1 && Integer.parseInt(year)<=4)
        {
            yearjudge = true;
        }
    }





























}
