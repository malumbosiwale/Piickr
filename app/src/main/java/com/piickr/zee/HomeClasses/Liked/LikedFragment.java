package com.piickr.zee.HomeClasses.Liked;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piickr.zee.HomeClasses.Home.FeedItemHome;
import com.piickr.zee.LoginClasses.FeedItemProfile;
import com.piickr.zee.LoginClasses.ProfileActivity;
import com.piickr.zee.R;

import java.util.ArrayList;
import java.util.List;


public class LikedFragment extends Fragment {
    RecyclerView recyclerview;
    SwipeRefreshLayout swipe;
    private AdapterLiked listAdapter;
    private TextView viewprofile;
    private List<FeedItemLiked> feedItems;
    private DatabaseReference friendlist;
    SimpleDraweeView my_image_view;

    public static LikedFragment newInstance() {
        return new LikedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_liked, container, false);

        recyclerview = rootview.findViewById(R.id.recyclerview);
        viewprofile = rootview.findViewById(R.id.viewprofile);
        swipe = rootview.findViewById(R.id.swipe);
        // my_image_view = rootView.findViewById(R.id.my_image_view);
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("CREDENTIALS-FACEBOOK", Context.MODE_PRIVATE);
        String UID = sharedpreferences.getString("UID", "EMPTY");
        friendlist = FirebaseDatabase.getInstance().getReference("pickr/friendlist/" + UID + "/");

        feedItems = new ArrayList<FeedItemLiked>();
        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        listAdapter = new AdapterLiked(getActivity(), feedItems);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(listAdapter);
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(true);

        viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("firsttime", true);
                getActivity().startActivity(intent);
            }
        });


        //friendlist.push().child("uid").setValue("An5GXtc0O7gGOWAHVPMSl0vnjch1");
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();
            }
        });
        getdata();


        return rootview;
    }


    private void getdata() {
        friendlist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedItems.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    FeedItemLiked item2 = dataSnapshot1.getValue(FeedItemLiked.class);

                    feedItems.add(item2);

                }
                listAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Error", "loadPost:onCancelled", databaseError.toException());
                swipe.setRefreshing(false);
            }

        });


    }
}
