package com.piickr.zee.HomeClasses.Find;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;
import com.piickr.zee.HomeClasses.Find.swipe.TinderCard;
import com.piickr.zee.HomeClasses.Home.FeedItemHome;
import com.piickr.zee.LoginClasses.FeedItemProfile;
import com.piickr.zee.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends Fragment {

    private static final String TAG = "ActivityTinder";
    @BindView(R.id.swipeView)
    SwipePlaceHolderView mSwipView;
    DatabaseReference profile;
    //TinderCard ttt = new TinderCard.checkthisposition();

    public FindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        profile = FirebaseDatabase.getInstance().getReference("pickr/profile");

        ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mSwipView.disableTouchSwipe();
        mSwipView.addItemRemoveListener(new ItemRemovedListener() {

            @Override
            public void onItemRemoved(int count) {
                Log.d(TAG, "onItemRemoved: " + count);
                /*if (count == 0) {
                    mSwipView.addView(new TinderCard())
                            .addView(new TinderCard())
                            .addView(new TinderCard())
                            .addView(new TinderCard())
                            .addView(new TinderCard())
                            .addView(new TinderCard())
                            .addView(new TinderCard())
                            .addView(new TinderCard())
                            .addView(new TinderCard())
                            .addView(new TinderCard());
                } */
            }

        });
        mSwipView.getBuilder()
//                .setSwipeType(SwipePlaceHolderView.SWIPE_TYPE_VERTICAL)
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setWidthSwipeDistFactor(4)
                .setHeightSwipeDistFactor(6)
                .setSwipeDecor(new SwipeDecor()
//                        .setMarginTop(300)
//                        .setMarginLeft(100)
//                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setSwipeMaxChangeAngle(2f)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        getdata("");
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                    mSwipView.enableTouchSwipe();
//                    mSwipView.lockViews();
//                    Thread.currentThread().sleep(4000);
//                    mSwipView.unlockViews();
//                    Thread.currentThread().sleep(4000);
//                    mSwipView.lockViews();
//                    Thread.currentThread().sleep(4000);
//                    mSwipView.unlockViews();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        */
    }

    @OnClick(R.id.rejectBtn)
    public void onRejectClick() {
        mSwipView.doSwipe(false);
    }

    @OnClick(R.id.acceptBtn)
    public void onAcceptClick() {

        mSwipView.doSwipe(true);
        //ttt.getName().
        //Toast.makeText(getActivity(), , Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.undoBtn)
    public void onUndoClick() {
        //mSwipView.undoLastSwipe();

        getdata("clear");
    }


    public void getdata(final String clear) {
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    FeedItemProfile item2 = dataSnapshot1.getValue(FeedItemProfile.class);
                    if(clear.equals("clear")){

                    }
                    mSwipView.addView(new TinderCard(item2.getUid(), item2.getUsername(), item2.getPhotouri(), item2.getLocation(), item2.getAge(), item2.getGender()));
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Error", "loadPost:onCancelled", databaseError.toException());
            }

        });
    }
}
