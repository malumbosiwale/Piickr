package com.piickr.zee.HomeClasses.Poll;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piickr.zee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class PollFragment extends Fragment {
    RecyclerView recyclerview;
    SwipeRefreshLayout swipe;
    private AdapterPoll listAdapter;
    private List<FeedItemPoll> feedItems;
    private DatabaseReference pollfeed;

    public PollFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_poll, container, false);
        recyclerview = rootView.findViewById(R.id.recyclerview);


        swipe = rootView.findViewById(R.id.swipe);
        // my_image_view = rootView.findViewById(R.id.my_image_view);
        pollfeed = FirebaseDatabase.getInstance().getReference("pickr/polls");

        feedItems = new ArrayList<FeedItemPoll>();
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new AdapterPoll(getActivity(), feedItems);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(listAdapter);
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(true);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();
                //putdata();
            }
        });

        getdata();
        //putdata();
        return rootView;

    }

    private void putdata() {

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
        String UID = sharedpreferences.getString("UID", "EMPTY");

        String postId = UUID.randomUUID().toString();

        pollfeed.child(postId).child("title").setValue("testone");
        pollfeed.child(postId).child("uid").setValue(postId);
        pollfeed.child(postId).child("description").setValue("hello world we test this like this lol");
        pollfeed.child(postId).child("date").setValue("Yesterday");

        pollfeed.child(postId).child("field1title").setValue("item 1");
        pollfeed.child(postId).child("field1votes").setValue(20);

        pollfeed.child(postId).child("field2title").setValue("item 2");
        pollfeed.child(postId).child("field2votes").setValue(10);

        pollfeed.child(postId).child("field3title").setValue("item 3");
        pollfeed.child(postId).child("field3votes").setValue(16);

        pollfeed.child(postId).child("field4title").setValue("item 4");
        pollfeed.child(postId).child("field4votes").setValue(54);

    }


    private void getdata() {
        pollfeed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedItems.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    FeedItemPoll item2 = dataSnapshot1.getValue(FeedItemPoll.class);
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
