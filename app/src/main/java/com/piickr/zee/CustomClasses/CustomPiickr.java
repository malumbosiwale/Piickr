package com.piickr.zee.CustomClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piickr.zee.HomeClasses.Home.AdapterHome;
import com.piickr.zee.HomeClasses.Home.FeedItemHome;
import com.piickr.zee.HomeClasses.Liked.AdapterLiked;
import com.piickr.zee.HomeClasses.Messages.AdapterMessages;
import com.piickr.zee.LoginClasses.FeedItemProfile;

import java.util.Calendar;

import static com.android.volley.VolleyLog.TAG;

public class CustomPiickr {
    String teststr;

    public void username(final String uid, final AdapterHome.MyViewHolder holder) {

        DatabaseReference profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile/");
        profileinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FeedItemProfile item2 = dataSnapshot.child(uid).getValue(FeedItemProfile.class);

                String picis = item2.getUsername();
                holder.name.setText(picis);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void profilepicture(final String uid, final AdapterHome.MyViewHolder holder) {
        DatabaseReference profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile/");
        profileinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FeedItemProfile item2 = dataSnapshot.child(uid).getValue(FeedItemProfile.class);
                String picis = item2.getPhotouri();
                holder.profilePic.setController(
                        Fresco.newDraweeControllerBuilder()
                                .setOldController(holder.profilePic.getController())
                                .build());
                Uri imageUri = Uri.parse(picis);
                holder.profilePic.setImageURI(imageUri);

                Log.d("Get Picture", "Picture URL: " + picis);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getage(String year) {
        int currentyear = Calendar.getInstance().get(Calendar.YEAR);
        int birthyear = Integer.parseInt(year);
        int currentage = currentyear - birthyear;
        String ageString = String.valueOf(currentage);
        return ageString;

    }

    public void profilepictureliked(final String uid, final AdapterLiked.MyViewHolder holder) {
        DatabaseReference profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile/");
        profileinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FeedItemProfile item2 = dataSnapshot.child(uid).getValue(FeedItemProfile.class);
                String picis = item2.getPhotouri();
                holder.image.setController(
                        Fresco.newDraweeControllerBuilder()
                                .setOldController(holder.image.getController())
                                .build());
                Uri imageUri = Uri.parse(picis);
                holder.image.setImageURI(imageUri);

                Log.d("Get Picture", "Picture URL: " + picis);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void profilepicturemessages(final String uid, final AdapterMessages.MyViewHolder holder) {
        DatabaseReference profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile/");
        profileinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FeedItemProfile item2 = dataSnapshot.child(uid).getValue(FeedItemProfile.class);
                String picis = item2.getPhotouri();
                holder.avata.setController(
                        Fresco.newDraweeControllerBuilder()
                                .setOldController(holder.avata.getController())
                                .build());
                Uri imageUri = Uri.parse(picis);
                holder.avata.setImageURI(imageUri);

                Log.d("Get Picture", "Picture URL: " + picis);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void usernameliked(final String uid, final AdapterLiked.MyViewHolder holder, final Context context) {

        DatabaseReference profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile/");
        profileinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FeedItemProfile item2 = dataSnapshot.child(uid).getValue(FeedItemProfile.class);

                String picis = item2.getUsername();
                holder.name.setText(picis);

                SharedPreferences sharedpreferences = context.getSharedPreferences("USERNAMES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(uid, picis);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void usernamemymessages(final String uid, final AdapterMessages.MyViewHolder holder, final Context context) {

        DatabaseReference profileinfo = FirebaseDatabase.getInstance().getReference("pickr/profile/");
        profileinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FeedItemProfile item2 = dataSnapshot.child(uid).getValue(FeedItemProfile.class);

                String picis = item2.getUsername();
                holder.txtName.setText(picis);

                SharedPreferences sharedpreferences = context.getSharedPreferences("USERNAMES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(uid, picis);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public Integer getpercentage(int first, int second, int third, int fourth) {

        int total = first + second + third + fourth;

        Log.d(TAG, "getpercentage: totlal" + String.valueOf(total));
        Log.d(TAG, "getpercentage: first" + String.valueOf(first));

        int rawn = 100 * first / total;

        Log.d(TAG, "getpercentage: rawn" + String.valueOf(rawn));

        return rawn;

    }


}
