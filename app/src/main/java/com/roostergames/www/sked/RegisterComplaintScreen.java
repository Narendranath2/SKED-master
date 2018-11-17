package com.roostergames.www.sked;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterComplaintScreen extends AppCompatActivity {
    EditText phoneno,idno,complaint;
    Button submitComplaint;
    ProgressDialog progressDialog;
    ConstraintLayout constraintLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint_screen);
        constraintLayout = (ConstraintLayout)findViewById(R.id.complaintlayout);
        phoneno = (EditText)findViewById(R.id.editText6);
        idno = (EditText)findViewById(R.id.editText7);
        complaint = (EditText)findViewById(R.id.editText8);
        submitComplaint = (Button)findViewById(R.id.button4);
        submitComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetailsandRegister();
            }
        });
    }
    void registerComplaint()
    {
        progressDialog = new ProgressDialog(RegisterComplaintScreen.this);
        progressDialog.setMessage("Registering Complaint");
        progressDialog.show();
        final Map<String, Object> userrcomplaint = new HashMap<>();
        userrcomplaint.put("complaintdesc", complaint.getText().toString());
        db.collection("complaints").document(idno.getText().toString()).set(userrcomplaint)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterComplaintScreen.this,"Complaint submitted successfully",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegisterComplaintScreen.this,LoginScreen.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(constraintLayout,"Error Occured",Snackbar.LENGTH_LONG).show();
            }
        });
    }
    void validateDetailsandRegister()
    {
        db.collection("users").document(idno.getText().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            if(phoneno.getText().toString().contains(documentSnapshot.getString("phono")))
                            {
                                registerComplaint();
                            }
                            else
                            {
                                Snackbar.make(constraintLayout,"Phone number doesnt match with registered Id No",Snackbar.LENGTH_LONG).show();
                            }

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
}
