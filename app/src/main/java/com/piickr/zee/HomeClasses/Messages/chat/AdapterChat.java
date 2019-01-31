package com.piickr.zee.HomeClasses.Messages.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piickr.zee.R;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;

//import com.bumptech.glide.Glide;


public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FeedItemMessages> feedItems;
    private Context mContext;
    private SQLiteDatabase db;
    private String phone = null, UID;

    public AdapterChat(Context applicationContext, List<FeedItemMessages> feedItems) {
        this.feedItems = feedItems;
    }

    @Override
    public int getItemViewType(int position) {
        FeedItemMessages item = feedItems.get(position);

        if (item.getIdSender() != null) {
            if (item.getIdSender().equals(UID))
                return 1;
            else
                return 2;
        }
        return 1;
        //return super.getIte;mViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        if (viewType == 1) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View itemView = inflater.inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageUserHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences("CREDENTIALS-FACEBOOK", Context.MODE_PRIVATE);
        UID = sharedpreferences.getString("UID", "EMPTY");
        switch (holder.getItemViewType()) {
            case 1: {
                ItemMessageUserHolder viewHolder = (ItemMessageUserHolder) holder;
                FeedItemMessages item = feedItems.get(position);


                viewHolder.txtContent.setText(item.getText());

            }
            break;
            case 2: {
                ItemMessageUserHolder viewHolder2 = (ItemMessageUserHolder) holder;
                FeedItemMessages item = feedItems.get(position);
                viewHolder2.txtContent.setText(item.getText());


            }
            break;

        }

    }

    public int getItemCount() {
        return feedItems.size();
    }


    private void send(final FeedItemMessages item) {
    }






}
