package com.roostergames.www.sked;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button qod,mod;
    TextView annarea;
    TextView day;
    static int c = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hview = navigationView.getHeaderView(0);
        TextView username = (TextView)hview.findViewById(R.id.nav_name);
        TextView idno = (TextView)hview.findViewById(R.id.nav_idno);
        SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
        username.setText(sharedPreferences.getString("userrealname",null));
        idno.setText(sharedPreferences.getString("useridno",null));
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        annarea = (TextView)findViewById(R.id.announcements);
        day = (TextView)findViewById(R.id.todaysday);
        c = 0;

        qod = (Button)findViewById(R.id.button5);
        mod = (Button)findViewById(R.id.button6);
        setAnnouncements();
        setToDaysDayPhrase();
        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this,MemeScreen.class);
                startActivity(i);
            }
        });
        qod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this,QuoteScreen.class);
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    public void onBackPressed() {

        SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
        if(sharedPreferences.getString("loginstatus","").contains("yes"))
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                c += 1;
                if(c == 2)
                {
                    finish();
                    System.exit(0);
                }
                /*AlertDialog.Builder exitDialouge = new AlertDialog.Builder(this);
                exitDialouge.setTitle("Exit");
                exitDialouge.setMessage("Do you want to close");
                exitDialouge.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                });
                exitDialouge.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                exitDialouge.show();*/
                //return;
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("loginstatus","no");
            editor.apply();
            Intent i = new Intent(HomeScreen.this,LoginScreen.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.klefmoodle) {
            Intent i = new Intent(HomeScreen.this,KlefMoodle.class);
            startActivity(i);
        } else if (id == R.id.klefsite) {
            Intent i = new Intent(HomeScreen.this,KlefSite.class);
            startActivity(i);

        } else if (id == R.id.share) {

        } else if (id == R.id.subjects) {
            Intent i = new Intent(HomeScreen.this,SubjectContenetScreen.class);
            startActivity(i);


        } else if (id == R.id.contactandsupport) {

        } else if (id == R.id.timetable) {
            Intent i = new Intent(HomeScreen.this,TimetableScreen.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setAnnouncements()
    {
        db.collection("announcements").document("publicannouncements").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String an = "";
                        for(int i=1;i<=10;i++)
                        {
                            if(!documentSnapshot.getString(String.valueOf(i)).isEmpty())
                            an += documentSnapshot.getString(String.valueOf(i)) + "\n\n";
                        }
                        annarea.setText(an);
                        /*annarea.append(documentSnapshot.getString("2" + "\n\n"));
                        annarea.append(documentSnapshot.getString("3"+ "\n\n"));
                        annarea.append(documentSnapshot.getString("4"+ "\n\n"));
                        annarea.append(documentSnapshot.getString("5"+ "\n\n"));
                        annarea.append(documentSnapshot.getString("6"+ "\n\n"));
                        annarea.append(documentSnapshot.getString("7"+ "\n\n"));
                        annarea.append(documentSnapshot.getString("8"+ "\n\n"));
                        annarea.append(documentSnapshot.getString("9"+ "\n\n"));
                        annarea.append(documentSnapshot.getString("10"+ "\n\n"));*/
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                annarea.setText("Cannot fetch announcements");
            }
        });
    }
    public void setToDaysDayPhrase()
    {
        String dayname = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        if(dayname.toLowerCase().contains("monday"))
        {
            day.setText("Mourning Monday");
        }
        else if(dayname.toLowerCase().contains("tuesday"))
        {
            day.setText("toughest Tuesday");
        }
        else if(dayname.toLowerCase().contains("wednesday"))
        {
            day.setText("Wrecking Wednesday");
        }
        else if(dayname.toLowerCase().contains("thursday"))
        {
            day.setText("Trendiest Thursday");
        }
        else if(dayname.toLowerCase().contains("friday"))
        {
            day.setText("Fantastic Friday");
        }
        else if(dayname.toLowerCase().contains("saturday"))
        {
            day.setText("Slurpy Saturday");
        }
        else
        {
            day.setText("Seccy Sunday");
        }
    }
}