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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TimetableScreen extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    ProgressDialog progressDialog;
    RecyclerView mrecyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager mlayoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String weekday_name;
    ArrayList<subjectitem> subjectList;
    String p1,p2,p3,p4,p5,p6,p7,p8,p9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_screen);
        constraintLayout = (ConstraintLayout)findViewById(R.id.timetablelayout);
        weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        getTimeTable();
    }
    void getTimeTable()
    {
        Log.i("day",weekday_name);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching todays timetable");
        progressDialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
        String yearr = sharedPreferences.getString("useryear","");
        yearr = "year" + yearr;
        Log.i("yearrr",yearr);
        String sectionnum = sharedPreferences.getString("usersecno","");
        sectionnum = "sec" + sectionnum;
        db.collection(yearr).document("timetable").collection("day").document(weekday_name.toLowerCase()).collection("secno").document(sectionnum).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("holiday").contains("yes"))
                        {
                            TextView textView = (TextView)findViewById(R.id.holiday);
                            textView.setText("Enjoy Your Holiday");
                        }
                        else
                        {
                            p1 = documentSnapshot.getString("09:00-09:50");
                            p2 = documentSnapshot.getString("09:50-10:40");
                            p3 = documentSnapshot.getString("11:00-11:50");
                            p4 = documentSnapshot.getString("11:50-12:40");
                            p5 = documentSnapshot.getString("12:40-01:30");
                            p6 = documentSnapshot.getString("01:30-02:20");
                            p7 = documentSnapshot.getString("02:20-03:10");
                            p8 = documentSnapshot.getString("03:20-04:10");
                            p9 = documentSnapshot.getString("04:10-05:00");
                            ArrayList<subjectitem> subjectList = new ArrayList<>();
                            subjectList.add(new subjectitem(p1,"09:00-09:50"));
                            subjectList.add(new subjectitem(p2,"09:50-10:40"));
                            subjectList.add(new subjectitem(p3,"11:00-11:50"));
                            subjectList.add(new subjectitem(p4,"11:50-12:40"));
                            subjectList.add(new subjectitem(p5,"12:40-01:30"));
                            subjectList.add(new subjectitem(p6,"01:30-02:20"));
                            subjectList.add(new subjectitem(p7,"02:20-03:10"));
                            subjectList.add(new subjectitem(p8,"03:20-04:10"));
                            subjectList.add(new subjectitem(p9,"04:10-05:00"));
                            mrecyclerView = findViewById(R.id.recyclerview);
                            mrecyclerView.setHasFixedSize(true);
                            mlayoutManager = new LinearLayoutManager(TimetableScreen.this);
                            madapter = new subjectadapter(subjectList);
                            mrecyclerView.setLayoutManager(mlayoutManager);
                            mrecyclerView.setAdapter(madapter);
                        }
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                TextView textView = (TextView)findViewById(R.id.holiday);
                textView.setText("Unable to fing your timetable");
            }
        });
    }
}
