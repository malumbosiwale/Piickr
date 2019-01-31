package com.piickr.zee.HomeClasses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.piickr.zee.HomeClasses.Find.FindFragment;
import com.piickr.zee.HomeClasses.Home.HomeFragment;
import com.piickr.zee.HomeClasses.Liked.LikedFragment;
import com.piickr.zee.HomeClasses.Messages.Messages;
import com.piickr.zee.HomeClasses.Poll.PollFragment;
import com.piickr.zee.IntroSlider.WelcomeActivity;
import com.piickr.zee.LoginClasses.LoginActivity;
import com.piickr.zee.R;

public class IndexActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    BottomNavigationBar bottomNavigationBar;
    private FrameLayout framelayout;
    private static final String TAG = "IndexActivity";
    private LinearLayout toolbar;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Fresco.initialize(this);
        SharedPreferences sharedpreferences = getSharedPreferences("Firstcheck", Context.MODE_PRIVATE);
        Boolean letsee = sharedpreferences.getBoolean("CHECK", false);
        createDatabase();
        savingsitemget();
        checkintro(letsee);
        setContentView(R.layout.index_activity);


        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);


        // Set up the ViewPager with the sections adapter.
        framelayout = findViewById(R.id.container);
        loadFragment(new HomeFragment());


        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "Home").setActiveColorResource(R.color.black))
                .addItem(new BottomNavigationItem(R.drawable.heart, "Liked").setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.drawable.location, "Find").setActiveColorResource(R.color.brown))
                .addItem(new BottomNavigationItem(R.drawable.messages, "Messages").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.poll, "Poll").setActiveColorResource(R.color.purple))
                .setFirstSelectedPosition(0)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {

                bottomNavigationBar.getCurrentSelectedPosition();
                Fragment fragment = null;

                if (position == 0) {
                    fragment = new HomeFragment();
                } else if (position == 1) {
                    fragment = new LikedFragment();
                } else if (position == 2) {
                    fragment = new FindFragment();
                } else if (position == 3) {
                    fragment = new Messages();
                } else if (position == 4) {
                    fragment = new PollFragment();

                }

                loadFragment(fragment);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void savingsitemget() {

        SharedPreferences sharedpreferences = getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
        String namestr = sharedpreferences.getString("NAME", "EMPTY");
        String EMAIL = sharedpreferences.getString("EMAIL", "EMPTY");
        String gender = sharedpreferences.getString("GENDER", "EMPTY");



        if (namestr.equals("EMPTY") | gender.equals("EMPTY")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            //Toast.makeText(this, "Nothing", Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(this, namestr, Toast.LENGTH_SHORT).show();
        }

    }

    private void checkintro(Boolean letsee) {
        if (!letsee) {
            startActivity(new Intent(IndexActivity.this, WelcomeActivity.class));
            finish();
        }
    }


    @Override
    public void onTabSelected(final int position) {
        bottomNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    protected void createDatabase() {
        db = openOrCreateDatabase("credentials", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS credentials (uid  PRIMARY KEY NOT NULL, " +
                "email VARCHAR, username VARCHAR, gender VARCHAR, cityortown VARCHAR, province VARCHAR, phone VARCHAR);");
    }
}
