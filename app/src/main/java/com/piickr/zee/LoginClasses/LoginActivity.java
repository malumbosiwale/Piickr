package com.piickr.zee.LoginClasses;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piickr.zee.HomeClasses.Home.FeedItemHome;
import com.piickr.zee.HomeClasses.Home.PostComposer;
import com.piickr.zee.HomeClasses.IndexActivity;
import com.piickr.zee.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private static final String TAG = "FacebookLogin";
    private SQLiteDatabase db;
    private Boolean needed = false;
    private DatabaseReference profileinfo;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDatabase();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile");


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.piickr.zee",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        ImageView iv = (ImageView) findViewById(R.id.logo);

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setInterpolator(new FastOutSlowInInterpolator());
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();


        SignInButton signInButton = findViewById(R.id.signInButton);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.cid))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);

        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(LoginActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK


                // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                if (requestCode == RC_SIGN_IN) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toasty.error(LoginActivity.this, "Authentication Failed").show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]


    private void updateUI(final FirebaseUser user) {
        if (user != null) {

            profileinfo.child(String.valueOf(user.getUid())).child("uid").setValue(user.getUid());
            //profileinfo.child(String.valueOf(user.getUid())).child("username").setValue(user.getDisplayName());
            //profileinfo.child(String.valueOf(user.getUid())).child("uid").setValue(item2);
            /*
            FeedItemProfile item2 = new FeedItemProfile(String.valueOf(user.getUid()), String.valueOf(user.getPhotoUrl()), String.valueOf(user.getDisplayName()), String.valueOf(user.getEmail()));
            profileinfo.child(String.valueOf(user.getUid())).setValue(item2, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                }
            });
            */
            SharedPreferences sharedpreffacebook = getSharedPreferences("CREDENTIALS-FACEBOOK", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreffacebook.edit();
            editor.putString("UID", String.valueOf(user.getUid()));
            editor.putString("NAME", String.valueOf(user.getDisplayName()));
            editor.putString("EMAIL", String.valueOf(user.getEmail()));
            editor.commit();
            findViewById(R.id.buttonFacebookLogin).setVisibility(View.GONE);

            DatabaseReference profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile/");
            profileinfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FeedItemProfile item2 = dataSnapshot.child(user.getUid()).getValue(FeedItemProfile.class);


                    if (item2.getUsername() != null && !item2.getUsername().isEmpty()) {
                        needed = false;
                        dosomething(needed);
                    } else {
                        needed = true;
                        dosomething(needed);
                    }
                    ////////////////////////////


                    if (item2.getAge() != null && !item2.getAge().isEmpty()) {
                        needed = false;
                        dosomething(needed);
                    } else {
                        needed = true;
                        dosomething(needed);
                    }


                    ///////////////////////////

                    if (item2.getEmail() != null && !item2.getEmail().isEmpty()) {
                        needed = false;
                        dosomething(needed);
                    } else {
                        needed = true;
                        dosomething(needed);
                    }


                    ////////////////////////////

                    if (item2.getInterests() != null && !item2.getInterests().isEmpty()) {
                        needed = false;
                        dosomething(needed);
                    } else {
                        needed = true;
                        dosomething(needed);
                    }


                    //////////////////////////

                    if (item2.getPhotouri() != null && !item2.getPhotouri().isEmpty()) {
                        needed = false;
                    } else {
                        needed = true;
                        dosomething(needed);
                    }



                    //////////////////////////

                    if (item2.getLocation() != null && !item2.getLocation().isEmpty()) {
                        needed = false;
                    } else {
                        needed = true;
                        dosomething(needed);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {


            findViewById(R.id.buttonFacebookLogin).setVisibility(View.VISIBLE);
        }
    }

    protected void createDatabase() {
        db = openOrCreateDatabase("credentials", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS credentials (uid  PRIMARY KEY NOT NULL, " +
                "email VARCHAR, username VARCHAR, gender VARCHAR, cityortown VARCHAR, province VARCHAR, phone VARCHAR);");
        // db.execSQL("CREATE TABLE IF NOT EXISTS income(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, amount VARCHAR, date VARCHAR);");
    }


    public void dosomething(Boolean needed){

        if(needed){
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("firsttime", true);
            startActivity(intent);
        }
    }


}
