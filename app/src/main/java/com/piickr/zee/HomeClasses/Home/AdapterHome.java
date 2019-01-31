package com.piickr.zee.HomeClasses.Home;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.piickr.zee.CustomClasses.CustomPiickr;
import com.piickr.zee.CustomClasses.TimeAgoFile;
import com.piickr.zee.R;
import com.piickr.zee.app.AppController;

import java.util.List;


import static com.android.volley.VolleyLog.TAG;

//import com.bumptech.glide.Glide;


public class AdapterHome extends RecyclerView.Adapter<AdapterHome.MyViewHolder> {
    private Context mContext;
    private List<FeedItemHome> feedItems;
    private AdapterHome listAdapter;
    CustomPiickr customPiickr;
    private int lastPosition = -1;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtStatusMsg, timestamp, name;
        public SimpleDraweeView profilePic, feedImage1;
        public FloatingActionButton share, love, views;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            timestamp = view.findViewById(R.id.timestamp);
            txtStatusMsg = view.findViewById(R.id.txtStatusMsg);
            feedImage1 = view.findViewById(R.id.feedImage1);
            profilePic = view.findViewById(R.id.profilePic);
        }
    }


    public AdapterHome(Context mContext, List<FeedItemHome> feeditems) {
        this.mContext = mContext;
        this.feedItems = feeditems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FeedItemHome item = feedItems.get(position);

        //Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Calibri.ttf");
//holder.address.setEllipsize(end);
        //int color = mContext.getResources().getColor(R.color.primaryTextColor);

        //Uri imageUri = Uri.parse(item.getImage());
        //holder.image.setImageURI(imageUri);

        //holder.name.setTypeface(font);
        //holder.timestamp.setTypeface(font);
        //holder.txtStatusMsg.setTypeface(font);

        //String name = customPiickr.username(item.getUid()).substring(0, 1).toUpperCase() + customPiickr.username(item.getUid()).substring(1);
        String body = item.getBody();
        String timestamp = item.getDate();
        TimeAgoFile timeAgoFile = new TimeAgoFile();
        Long dsde = Long.valueOf(timestamp);
        String srtring = timeAgoFile.convertTime(dsde);

        CustomPiickr customPiickr = new CustomPiickr();

        customPiickr.username(item.getUid(), holder);
        customPiickr.profilepicture(item.getUid(), holder);



        holder.txtStatusMsg.setText(body);
        holder.timestamp.setText(srtring);


        //original aspect ratio float
        //Float fff = Float.parseFloat(item.getCi_ar());
        //holder.image.setAspectRatio(fff);

        //forced aspect ratio
        Float fff = Float.parseFloat("1.7");
        holder.profilePic.setAspectRatio(fff);





        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
