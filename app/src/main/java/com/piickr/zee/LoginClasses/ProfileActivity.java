package com.piickr.zee.LoginClasses;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.piickr.zee.CustomClasses.CustomPiickr;
import com.piickr.zee.HomeClasses.IndexActivity;
import com.piickr.zee.R;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1113;
    TextView username, location, age, interests, gender;
    EditText usernameedittext, locationedittext, interestsedittext, ageedittext;
    SimpleDraweeView profile;
    StorageReference mstoragereference;
    DatabaseReference profileinfo;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    CardView carddisplay;
    ImageView selectimage, next_icon;
    Uri picUri;
    String filePath, uid;
    Uri fileuri;
    static final int PICK_IMAGE_COVER = 2;
    CustomPiickr customPiickr;
    Boolean firsttime;
    NiceSpinner niceSpinner;
    List<String> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);


        customPiickr = new CustomPiickr();
        sharedpreferences = getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        SharedPreferences sharedpreffacebook = getSharedPreferences("CREDENTIALS-FACEBOOK", Context.MODE_PRIVATE);
        uid = sharedpreffacebook.getString("UID", "");
        profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile/");

        mstoragereference = FirebaseStorage.getInstance().getReference();

        Intent intent = new Intent();
        firsttime = intent.getBooleanExtra("firsttime", false);

        gender = findViewById(R.id.gender);
        niceSpinner = findViewById(R.id.nice_spinner);
        dataset = new LinkedList<>(Arrays.asList("Female", "Male"));
        niceSpinner.attachDataSource(dataset);

        next_icon = findViewById(R.id.next_icon);



        selectimage = findViewById(R.id.selectimage);
        profile = findViewById(R.id.profile);
        carddisplay = findViewById(R.id.carddisplay);

        username = findViewById(R.id.username);
        usernameedittext = findViewById(R.id.usernameedittext);
        usernameedittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        location = findViewById(R.id.location);
        locationedittext = findViewById(R.id.locationedittext);
        locationedittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        age = findViewById(R.id.age);
        ageedittext = findViewById(R.id.ageedittext);
        ageedittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);

        interests = findViewById(R.id.interests);
        interestsedittext = findViewById(R.id.interestsedittext);
        interestsedittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        checknewdata();
        backbutton();

        hide();


        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    insertDummyContactWrapper();
                } else {
                    imageBrowse();
                }


            }
        });

    }

    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_COVER);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_IMAGE_COVER) {
                picUri = data.getData();

                filePath = getPath(picUri);
                fileuri = picUri;

                if (!Uri.EMPTY.equals(picUri)) {

                    profile.setImageURI(picUri);
                    uploadhere();
                }
            }

        }

    }


    public void savedata() {
        /*
            SharedPreferences sharedpreferences = getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("UID", String.valueOf(user.getUid()));
            editor.putString("NAME", String.valueOf(user.getDisplayName()));
            editor.putString("EMAIL", String.valueOf(user.getEmail()));
            editor.putString("AGE", String.valueOf(user.getEmail()));
            editor.putString("LOCATION", String.valueOf(user.getEmail()));
            editor.commit();
            */
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    public void hide() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carddisplay.getVisibility() == View.VISIBLE) {
                    carddisplay.setVisibility(View.GONE);

                } else {
                    carddisplay.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        imageBrowse();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ProfileActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    //imageBrowse();
                } else {
                    // Permission Denied
                    Toast.makeText(ProfileActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }


    private void uploadhere() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        if (!Uri.EMPTY.equals(picUri)) {

            final StorageReference coverimageref = mstoragereference.child("pickr/profileimages/" + uid + "/profile.jpg");
            coverimageref.putFile(picUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mstoragereference.child("pickr/profileimages/" + uid + "/profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'

                            profileinfo.child(uid).child("photouri").setValue(String.valueOf(uri));
                            progressDialog.dismiss();
                            checknewdata();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toasty.error(getApplicationContext(), "Something went wrong, try again later...").show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress) + "% Uploaded...");

                }
            });

        } else {
            Toasty.warning(getApplicationContext(), "All fields required!!", Toast.LENGTH_SHORT, true).show();

        }
    }

    public void checknewdata() {

        DatabaseReference profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile/");
        profileinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FeedItemProfile item2 = dataSnapshot.child(uid).getValue(FeedItemProfile.class);
                if (item2.getPhotouri() != null && !item2.getPhotouri().isEmpty()) {

                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(item2.getPhotouri()))
                            //.setResizeOptions(new ResizeOptions(300, 300))
                            .build();
                    profile.setController(
                            Fresco.newDraweeControllerBuilder()
                                    .setOldController(profile.getController())
                                    .setImageRequest(request)
                                    .build());

                    editor.putString("PROFILEPIC", item2.getPhotouri());
                    editor.apply();

                } else {
                    //ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.khalid).build();
                    //profile.setImageURI(imageRequest.getSourceUri());
                }

                if (item2.getUsername() != null && !item2.getUsername().isEmpty()) {

                    username.setText(item2.getUsername());
                    editor.putString("NAME", item2.getUsername());
                    editor.apply();
                } else {
                    username.setText("Username Required");
                }

                if (item2.getLocation() != null && !item2.getLocation().isEmpty()) {
                    location.setText("Location: " + item2.getLocation());
                    editor.putString("LOCATION", item2.getLocation());
                    editor.apply();
                } else {
                    location.setText("Location: Not Set");
                }

                if (item2.getAge() != null && !item2.getAge().isEmpty()) {
                    age.setText("Age: " + customPiickr.getage(item2.getAge()));
                    editor.putString("AGE", item2.getAge());
                    editor.apply();
                } else {
                    age.setText("Age: Not Set");
                }

                if (item2.getInterests() != null && !item2.getInterests().isEmpty()) {
                    interests.setText(item2.getInterests());
                    editor.putString("INTERESTS", item2.getInterests());
                    editor.apply();
                } else {
                    interests.setText("Interests not set.");
                }

                if (item2.getGender() != null && !item2.getGender().isEmpty()) {
                    gender.setText("Gender: " + item2.getGender());
                    editor.putString("GENDER", item2.getGender());
                    editor.apply();
                } else {
                    gender.setText("Gender: Not Set");
                }

                onclickers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void backbutton() {
        //if (firsttime) {

            final String nnname = sharedpreferences.getString("NAME", "EMPTY");
            final String ppprofilepic = sharedpreferences.getString("PROFILEPIC", "EMPTY");
            final String lllocation = sharedpreferences.getString("LOCATION", "EMPTY");
            final String aaage = sharedpreferences.getString("AGE", "EMPTY");
            final String iiinterests = sharedpreferences.getString("INTERESTS", "EMPTY");
            final String gggender = sharedpreferences.getString("GENDER", "EMPTY");

            next_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!nnname.equals("EMPTY") && !ppprofilepic.equals("EMPTY") && !lllocation.equals("EMPTY")
                            && !aaage.equals("EMPTY") && !iiinterests.equals("EMPTY") && !gggender.equals("EMPTY")) {


                        Intent intent = new Intent(ProfileActivity.this, IndexActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    } else {
                        Toast.makeText(ProfileActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                    }
                }
            });


       // }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onclickers() {
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setVisibility(View.GONE);
                usernameedittext.setVisibility(View.VISIBLE);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setVisibility(View.GONE);
                locationedittext.setVisibility(View.VISIBLE);
            }
        });

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age.setVisibility(View.GONE);
                ageedittext.setVisibility(View.VISIBLE);
            }
        });

        interests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interests.setVisibility(View.GONE);
                interestsedittext.setVisibility(View.VISIBLE);
            }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender.setVisibility(View.GONE);
                niceSpinner.setVisibility(View.VISIBLE);
            }
        });

        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selecteddata = dataset.get(position);

                profileinfo.child(uid).child("gender").setValue(selecteddata);
                gender.setVisibility(View.VISIBLE);
                niceSpinner.setVisibility(View.GONE);
                checknewdata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        usernameedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (usernameedittext.getRight() - usernameedittext.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (!usernameedittext.getText().toString().equals("")) {
                            profileinfo.child(uid).child("username").setValue(usernameedittext.getText().toString());
                            username.setVisibility(View.VISIBLE);
                            usernameedittext.setVisibility(View.GONE);
                            checknewdata();
                        }

                        return true;
                    }
                }
                return false;
            }
        });


        locationedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (locationedittext.getRight() - locationedittext.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (!locationedittext.getText().toString().equals("")) {
                            profileinfo.child(uid).child("location").setValue(locationedittext.getText().toString());
                            location.setVisibility(View.VISIBLE);
                            locationedittext.setVisibility(View.GONE);
                            checknewdata();
                        }

                        return true;
                    }
                }
                return false;
            }
        });


        ageedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (ageedittext.getRight() - ageedittext.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (!ageedittext.getText().toString().equals("")) {
                            profileinfo.child(uid).child("age").setValue(ageedittext.getText().toString());
                            age.setVisibility(View.VISIBLE);
                            ageedittext.setVisibility(View.GONE);
                            checknewdata();
                        }

                        return true;
                    }
                }
                return false;
            }
        });


        interestsedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (interestsedittext.getRight() - interestsedittext.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (!interestsedittext.getText().toString().equals("")) {
                            profileinfo.child(uid).child("interests").setValue(interestsedittext.getText().toString());
                            interests.setVisibility(View.VISIBLE);
                            interestsedittext.setVisibility(View.GONE);
                            checknewdata();
                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }


}
