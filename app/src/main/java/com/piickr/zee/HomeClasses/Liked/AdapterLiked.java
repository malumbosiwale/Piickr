package com.piickr.zee.HomeClasses.Liked;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.piickr.zee.CustomClasses.CustomPiickr;
import com.piickr.zee.CustomClasses.TimeAgoFile;
import com.piickr.zee.CustomClasses.model.data.StaticConfig;
import com.piickr.zee.HomeClasses.Liked.FeedItemLiked;
import com.piickr.zee.HomeClasses.Messages.chat.ChatActivity;
import com.piickr.zee.R;
import com.piickr.zee.app.AppController;

import java.util.List;

//import com.bumptech.glide.Glide;


public class AdapterLiked extends RecyclerView.Adapter<AdapterLiked.MyViewHolder> {
    private Context mContext;
    private List<FeedItemLiked> feedItems;
    private AdapterLiked listAdapter;
    CustomPiickr customPiickr;
    private int lastPosition = -1;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, agelocation, interests;
        public SimpleDraweeView image;
        public CardView card;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            interests = view.findViewById(R.id.interests);
            agelocation = view.findViewById(R.id.agelocation);
            image = view.findViewById(R.id.image);
            card = view.findViewById(R.id.card);
        }
    }


    public AdapterLiked(Context mContext, List<FeedItemLiked> feeditems) {
        this.mContext = mContext;
        this.feedItems = feeditems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_liked, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FeedItemLiked item = feedItems.get(position);

        CustomPiickr customPiickr = new CustomPiickr();

        customPiickr.usernameliked(item.getUid(), holder, mContext);
        customPiickr.profilepictureliked(item.getUid(), holder);


        //original aspect ratio float
        //Float fff = Float.parseFloat(item.getCi_ar());
        //holder.image.setAspectRatio(fff);

        //forced aspect ratio


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedpreferences = mContext.getSharedPreferences("USERNAMES", Context.MODE_PRIVATE);
                String nameFriend = sharedpreferences.getString(item.getUid(), "not set");

                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ID, item.getUid());
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, item.getUid());
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, nameFriend);
                mContext.startActivity(intent);

            }
        });


        //Toast.makeText(mContext, item.getUsername(), Toast.LENGTH_SHORT).show();;


        //setAnimation(holder.listish, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }
}
