package com.roostergames.www.sked;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuoteScreen extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    TextView quoteArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_screen);
        constraintLayout = (ConstraintLayout)findViewById(R.id.quoteConstraintlayout);
        quoteArea = (TextView)findViewById(R.id.textView3);
        loadQuote();
    }
    void loadQuote()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Todays Quote");
        progressDialog.show();
        db.collection("quoteandmeme").document("quote").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String quote = documentSnapshot.getString("todaysquote");
                        quoteArea.setText(quote);
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
