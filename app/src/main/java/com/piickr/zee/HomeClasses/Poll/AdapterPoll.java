package com.piickr.zee.HomeClasses.Poll;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.android.volley.toolbox.ImageLoader;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.collection.LLRBNode;
import com.piickr.zee.CustomClasses.CustomPiickr;
import com.piickr.zee.CustomClasses.TimeAgoFile;
import com.piickr.zee.R;
import com.piickr.zee.app.AppController;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;


//import com.bumptech.glide.Glide;


public class AdapterPoll extends RecyclerView.Adapter<AdapterPoll.MyViewHolder> {
    private Context mContext;
    private List<FeedItemPoll> feedItems;
    private AdapterPoll listAdapter;
    CustomPiickr customPiickr;
    private int lastPosition = -1;
    public final static int MODEL_COUNT = 4;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    String votee1;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, name, firstvalue, secondvalue, thirdvalue, fourthvalue;
        public LinearLayout pollholder;
        public NumberProgressBar number_progress_bar;
        public RoundCornerProgressBar firstresult, secondresult, thirdresult, fourthresult;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            pollholder = view.findViewById(R.id.pollholder);
            number_progress_bar = view.findViewById(R.id.number_progress_bar);
            firstresult = view.findViewById(R.id.firstresult);
            firstvalue = view.findViewById(R.id.firstvalue);
            secondvalue = view.findViewById(R.id.secondvalue);
            secondresult = view.findViewById(R.id.secondresult);
            thirdvalue = view.findViewById(R.id.thirdvalue);
            thirdresult = view.findViewById(R.id.thirdresult);
            fourthvalue = view.findViewById(R.id.fourthvalue);
            fourthresult = view.findViewById(R.id.fourthresult);
        }
    }


    public AdapterPoll(Context mContext, List<FeedItemPoll> feeditems) {
        this.mContext = mContext;
        this.feedItems = feeditems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_poll, parent, false);
        customPiickr = new CustomPiickr();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FeedItemPoll item = feedItems.get(position);


        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        if (item.getField1votes() == 1) {
            votee1 = " vote";
        } else {
            votee1 = " votes";
        }

        //original aspect ratio float
        //Float fff = Float.parseFloat(item.getCi_ar());
        //holder.image.setAspectRatio(fff);

        int maxvalue = 100;
        float floatmax = maxvalue;

        int firstint = customPiickr.getpercentage(item.getField1votes(), item.getField2votes(), item.getField3votes(), item.getField4votes());
        int secondint = customPiickr.getpercentage(item.getField2votes(), item.getField1votes(), item.getField3votes(), item.getField4votes());
        int thirdint = customPiickr.getpercentage(item.getField3votes(), item.getField1votes(), item.getField2votes(), item.getField4votes());
        int fourthint = customPiickr.getpercentage(item.getField4votes(), item.getField1votes(), item.getField2votes(), item.getField3votes());

        //forced aspect ratio
        float floatprogressone = firstint;
        float floatprogresssecond = secondint;
        float floatprogresssthird = thirdint;
        float floatprogresssfourth = fourthint;

        holder.number_progress_bar.setMax(100);
        holder.number_progress_bar.setProgress(firstint);

        Log.d(TAG, "onBindViewHolder: the stuff " + String.valueOf(firstint));


        holder.firstresult.setMax(floatmax);
        holder.firstresult.setProgress(floatprogressone);
        holder.firstvalue.setText(item.getField1title() + " • " + String.valueOf(firstint) + mContext.getString(R.string.percentage) + " • " + String.valueOf(item.getField1votes() + votee1));

        holder.secondresult.setMax(floatmax);
        holder.secondresult.setProgress(floatprogresssecond);
        holder.secondvalue.setText(item.getField2title() + " • " + String.valueOf(secondint) + mContext.getString(R.string.percentage) + " • " + String.valueOf(item.getField2votes() + votee1));

        holder.thirdresult.setMax(floatmax);
        holder.thirdresult.setProgress(floatprogresssthird);
        holder.thirdvalue.setText(item.getField3title() + " • " + String.valueOf(thirdint) + mContext.getString(R.string.percentage) + " • " + String.valueOf(item.getField3votes() + votee1));

        holder.fourthresult.setMax(floatmax);
        holder.fourthresult.setProgress(floatprogresssfourth);
        holder.fourthvalue.setText(item.getField4title() + " • " + String.valueOf(fourthint) + mContext.getString(R.string.percentage) + " • " + String.valueOf(item.getField4votes() + votee1));

        holder.pollholder.setOnClickListener(new View.OnClickListener() {
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
