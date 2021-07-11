package com.example.animalhub.model;

import java.io.Serializable;
import java.util.UUID;

public class Model_FeedBack implements Serializable {
    String feedBackId,Id,AdId,feedBack,UName,Date,Time;

    public String getId() {
        return Id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setId(String id) {
        Id = id;
    }

    public Model_FeedBack() {

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFeedBackId() {
        return feedBackId;
    }

    public void setFeedBackId(String feedBackId) {
        this.feedBackId = feedBackId;
    }


    public String getUName() {
        char[] inWordChars = UName.toCharArray();
        if(inWordChars.length>3){
            for (int i = 3; i < inWordChars.length; i++) {
                inWordChars[i] = '*';
            }
        }

        String outputStr = new String(inWordChars);
        return outputStr;

    }//ni

    public void setUName(String UName) {
        this.UName = UName;
    }




    public String getAdId() {
        return AdId;
    }

    public void setAdId(String adId) {
        AdId = adId;
    }


    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }
}
