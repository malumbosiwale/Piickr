package com.piickr.zee.HomeClasses.Home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.piickr.zee.R;

public class PostComposer extends AppCompatActivity {
    EditText posttext;
    TextView post;
    private DatabaseReference newsfeed;
    String namestr, UID, EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_composer);
        showInputMethod();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        posttext = findViewById(R.id.posttext);
        post = findViewById(R.id.post);

        SharedPreferences sharedpreferences = getSharedPreferences("CREDENTIALS-FACEBOOK", Context.MODE_PRIVATE);
        namestr = sharedpreferences.getString("NAME", "EMPTY");
        UID = sharedpreferences.getString("UID", "EMPTY");
        EMAIL = sharedpreferences.getString("EMAIL", "EMPTY");
        newsfeed = FirebaseDatabase.getInstance().getReference("pickr/newsfeed");


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posttext.getText().toString().replaceAll("\\s+", "").equals("")) {
                    Toast.makeText(PostComposer.this, "Text Required", Toast.LENGTH_SHORT).show();
                } else {
                    String currenttime = String.valueOf(System.currentTimeMillis());
                    DatabaseReference pushedPostRef = newsfeed.push();


                    String postId = pushedPostRef.getKey();
                    FeedItemHome item2 = new FeedItemHome(postId, UID, posttext.getText().toString(), currenttime, "", "");
                    newsfeed.child(postId).setValue(item2, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(PostComposer.this, "Something went wrong, try again later...", Toast.LENGTH_SHORT).show();
                            } else {
                               posttext.setText("");
                                close();
                            }
                        }
                    });
                }
            }
        });



    }

    public void close() {
        this.finish();
    }

    public void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

}
