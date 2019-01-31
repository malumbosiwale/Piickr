package com.piickr.zee.HomeClasses.Messages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.piickr.zee.CustomClasses.CustomPiickr;
import com.piickr.zee.CustomClasses.MyTimeAgo;
import com.piickr.zee.CustomClasses.TimeAgoFile;
import com.piickr.zee.CustomClasses.model.data.StaticConfig;
import com.piickr.zee.HomeClasses.Messages.chat.ChatActivity;
import com.piickr.zee.R;
import com.piickr.zee.app.AppController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.bumptech.glide.Glide;


public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.MyViewHolder> {
    private Context mContext;
    private List<FeedItemMyfriends> feedItems;
    private AdapterMessages listAdapter;
    CustomPiickr customPiickr;
    TimeAgoFile timeAgoFile;
    private int lastPosition = -1;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView avata;
        public CardView cardmessage;
        public TextView txtName, txtTime, txtMessage;

        public MyViewHolder(View view) {
            super(view);
            avata = view.findViewById(R.id.icon_avata);
            cardmessage = view.findViewById(R.id.cardmessage);
            txtName = view.findViewById(R.id.txtName);
            txtTime = view.findViewById(R.id.txtTime);
            txtMessage = view.findViewById(R.id.txtMessage);
        }
    }


    public AdapterMessages(Context mContext, List<FeedItemMyfriends> feeditems) {
        this.mContext = mContext;
        this.feedItems = feeditems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        customPiickr = new CustomPiickr();
        timeAgoFile = new TimeAgoFile();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_friend, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FeedItemMyfriends item = feedItems.get(position);


        holder.txtMessage.setText(item.getIndex());
        customPiickr.usernamemymessages(item.getIdReceiver(), holder, mContext);
        customPiickr.profilepicturemessages(item.getIdReceiver(), holder);

        String time = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(item.getTimestamp()));
        String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));

        if (today.equals(time)) {
            holder.txtTime.setText(new SimpleDateFormat("HH:mm").format(new Date(item.getTimestamp())));
        } else {
            holder.txtTime.setText(new SimpleDateFormat("MMM d").format(new Date(item.getTimestamp())));
        }

        //holder.txtTime.setText(timeAgoFile.convertTime(item.getTimestamp()));


        holder.cardmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedpreferences = mContext.getSharedPreferences("USERNAMES", Context.MODE_PRIVATE);
                String nameFriend = sharedpreferences.getString(item.getIdReceiver(), "not set");

                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ID, item.getIdReceiver());
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, item.getIdReceiver());
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
