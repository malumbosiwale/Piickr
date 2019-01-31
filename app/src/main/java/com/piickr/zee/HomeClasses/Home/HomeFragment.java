package com.piickr.zee.HomeClasses.Home;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.piickr.zee.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {
    RecyclerView recyclerview;
    SwipeRefreshLayout swipe;
    private AdapterHome listAdapter;
    private List<FeedItemHome> feedItems;
    private DatabaseReference newsfeed;
    SimpleDraweeView my_image_view;
    TextView text_field_click;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        View rootView = inflater.inflate(R.layout.hoome_main, container, false);
        ButterKnife.bind(getActivity());
        recyclerview = rootView.findViewById(R.id.recyclerview);
        text_field_click = rootView.findViewById(R.id.text_field_click);
        swipe = rootView.findViewById(R.id.swipe);
        // my_image_view = rootView.findViewById(R.id.my_image_view);
        newsfeed = FirebaseDatabase.getInstance().getReference("pickr/newsfeed");

        feedItems = new ArrayList<FeedItemHome>();
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new AdapterHome(getActivity(), feedItems);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(listAdapter);
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);


        text_field_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postdialog();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();
            }
        });
        getdata();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getdata();
    }

    @Override
    public void onStart() {
        super.onStart();
        //getdata();
    }

    private void postdialog() {
        Intent intent = new Intent(getActivity(), PostComposer.class);
        startActivity(intent);

    }


    private void getdata() {
        newsfeed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedItems.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    FeedItemHome item2 = dataSnapshot1.getValue(FeedItemHome.class);
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
