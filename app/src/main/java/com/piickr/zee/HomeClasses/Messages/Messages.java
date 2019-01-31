package com.piickr.zee.HomeClasses.Messages;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.piickr.zee.HomeClasses.Messages.chat.ChatActivity;
import com.piickr.zee.CustomClasses.model.Friend;
import com.piickr.zee.CustomClasses.model.ListFriend;
import com.piickr.zee.CustomClasses.model.data.FriendDB;
import com.piickr.zee.CustomClasses.model.data.StaticConfig;
import com.piickr.zee.CustomClasses.model.service.ServiceUtils;
import com.piickr.zee.R;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Messages extends Fragment {

    private RecyclerView recyclerview;
    private AdapterMessages listAdapter;
    public FragFriendClickFloatButton onClickFloatButton;
    private List<FeedItemMyfriends> feedItems;
    private LovelyProgressDialog dialogFindAllFriend;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CountDownTimer detectFriendOnline;
    public static int ACTION_START_CHAT = 1;
    private String UID;
    SharedPreferences fredssharedpref;
    SharedPreferences.Editor editor;

    private HashMap mapRecord;
    public static final String ACTION_DELETE_FRIEND = "com.piickr.zeecom.android.DELETE_FRIEND";

    private BroadcastReceiver deleteFriendReceiver;

    public Messages() {
        onClickFloatButton = new FragFriendClickFloatButton();

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_messages, container, false);
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("CREDENTIALS-FACEBOOK", Context.MODE_PRIVATE);
        fredssharedpref = getActivity().getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
        editor = fredssharedpref.edit();

        UID = sharedpreferences.getString("UID", "EMPTY");

        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        recyclerview = (RecyclerView) layout.findViewById(R.id.recycleListFriend);

        feedItems = new ArrayList<FeedItemMyfriends>();

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new AdapterMessages(getActivity(), feedItems);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(listAdapter);
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true);
            }
        });

        getData(false);

        return layout;
    }

    public void getData(final Boolean check) {
        FirebaseDatabase.getInstance().getReference().child("pickr/messageindex/" + UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    FeedItemMyfriends item2 = dataSnapshot1.getValue(FeedItemMyfriends.class);
                    feedItems.clear();
                    feedItems.add(item2);

                    //Toast.makeText(getActivity(), item2.getIndex(), Toast.LENGTH_SHORT).show();
                }
                listAdapter.notifyDataSetChanged();
                if (check) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


                // Getting Post failed, log a message
                Log.w("Error", "loadPost:onCancelled", databaseError.toException());
            }

        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public class FragFriendClickFloatButton {
        Context context;
        LovelyProgressDialog dialogWait;

        public FragFriendClickFloatButton() {
        }

        public FragFriendClickFloatButton getInstance(Context context) {
            this.context = context;
            dialogWait = new LovelyProgressDialog(context);
            return this;
        }


        /**
         * TIm id cua email tren server
         *
         * @param email
         */


        /**
         * Lay danh sach friend cua một UID
         */

        /**
         * Add friend
         *
         * @param idFriend
         */

        /**
         * Lay danh sach ban be tren server
         */

    }
}


