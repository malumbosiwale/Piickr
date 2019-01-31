package com.piickr.zee.HomeClasses.Find.swipe;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.LongClick;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;
import com.piickr.zee.CustomClasses.CustomPiickr;
import com.piickr.zee.LoginClasses.FeedItemProfile;
import com.piickr.zee.R;

/**
 * Created by janisharali on 19/08/16.
 */
@NonReusable
@Layout(R.layout.tinder_card_view)
public class TinderCard {
    private String uid, name, location, propic, gender, age;
    @View(R.id.profileImageView)
    SimpleDraweeView profileImageView;

    @View(R.id.nameAgeTxt)
    TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    TextView locationNameTxt;

    @SwipeView
    android.view.View view;

    public TinderCard(String uid, String name, String propic, String location, String age, String gender) {
        super();
        this.uid = uid;
        this.name = name;
        this.propic = propic;
        this.location = location;
        this.age = age;
        this.gender = gender;

    }


    @Resolve
    public void onResolve() {
        CustomPiickr customPiickr = new CustomPiickr();
        nameAgeTxt.setText(name + ", " + customPiickr.getage(age));
        locationNameTxt.setText(location);

        profileImageView.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(profileImageView.getController())
                        .build());
        Uri imageUri = Uri.parse(propic);
        profileImageView.setImageURI(imageUri);

        checkthisposition();

    }

    public String checkthisposition() {

        return age;
    }

    @SwipeOut
    public void onSwipedOut() {
        Log.d("DEBUG", "onSwipedOut");
    }

    @SwipeCancelState
    public void onSwipeCancelState() {
        Log.d("DEBUG", "onSwipeCancelState");
    }

    @SwipeIn
    public void onSwipeIn() {
        Log.d("DEBUG", "onSwipedIn");
    }

    @SwipeInState
    public void onSwipeInState() {
        Log.d("DEBUG", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState() {
        Log.d("DEBUG", "onSwipeOutState");
    }

    @SwipeHead
    public void onSwipeHead() {
        //profileImageView.setBackgroundColor(Color.BLUE);
        Log.d("DEBUG", "onSwipeHead");
    }

    @Click(R.id.profileImageView)
    public void onProfileImageViewClick() {
        Log.d("DEBUG", "onProfileImageViewClick");
    }

    @LongClick(R.id.profileImageView)
    public void onProfileImageViewLongClick() {
        Log.d("DEBUG", "onProfileImageViewLongClick");
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
