package com.roostergames.www.sked;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SubjectsScreen extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    String mysubjects[];
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_screen);
        mysubjects = new String[10];
        constraintLayout = findViewById(R.id.subconstraintlayout);
        displaySubjects();
    }
    void displaySubjects()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
        String yearr = sharedPreferences.getString("useryear","");
        yearr = "year" + yearr;
        db.collection(yearr).document("subjects").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for(int i=1;i<=10;i++) {
                            if (documentSnapshot.getString(String.valueOf(i)) != "") {
                                mysubjects[i] = documentSnapshot.getString(String.valueOf(i));
                                Log.i("subjects", mysubjects[i]);
                            }
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
