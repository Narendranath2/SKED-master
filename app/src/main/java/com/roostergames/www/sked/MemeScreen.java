package com.roostergames.www.sked;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MemeScreen extends AppCompatActivity {
    ProgressDialog progressDialog;
    ConstraintLayout constraintLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView memeArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_screen);
        constraintLayout = (ConstraintLayout)findViewById(R.id.memeconstraintLayout);
        memeArea = (ImageView)findViewById(R.id.meme);
        loadImage();
    }
    void loadImage()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Todays Meme");
        progressDialog.show();
        db.collection("quoteandmeme").document("meme").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String imgurl = documentSnapshot.getString("todaysmeme");
                        Picasso.get().load(imgurl).networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(memeArea);
                        progressDialog.dismiss();
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
