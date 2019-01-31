package com.piickr.zee.CustomClasses;

import java.text.Format;
import java.util.Date;

public class TimeAgoFile {

    public String convertTime(long time){

        Long dsds = Long.valueOf(time);
        Date d = new Date(dsds);
        MyTimeAgo timeAgo = new MyTimeAgo();
        String result = timeAgo.getTimeAgo(d);

        return result;
    }
}
