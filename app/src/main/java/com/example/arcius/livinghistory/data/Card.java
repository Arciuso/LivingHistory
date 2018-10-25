package com.example.arcius.livinghistory.data;


import android.graphics.Bitmap;

import java.io.Serializable;

public class Card implements Serializable {

    public CardTypes getType() {
        return type;
    }

    public enum CardTypes {     //TODO only with main title
        Classic(0), Image(1), Single(2);

        private final int value;

        CardTypes(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private CardTypes type;

    private String eventID;

    private String date;

    private String time;
    private String mainTitle;
    private String fullText;

    private String linkImage;
    private boolean isPictureReady = false;
    private Bitmap image;

    private Location location;

    public Card( String eventID,String date, String time, String mainTitle, String fullText, Location location ){
        this.type = CardTypes.Classic;
        this.eventID = eventID;
        this.date = date;
        this.time = time;
        this.mainTitle = mainTitle;
        this.fullText = fullText;
        this.location = location;
    }

    public Card( String eventID, String time, String mainTitle, Location location){
        this.type = CardTypes.Single;
        this.eventID = eventID;
        this.time = time;
        this.mainTitle = mainTitle;
        this.location = location;
    }

    public Card( String eventID, String date, String time, String mainTitle, String fullText, String linkImage, Location location ){
        this.type = CardTypes.Image;
        this.eventID = eventID;
        this.date = date;
        this.time = time;
        this.mainTitle = mainTitle;
        this.fullText = fullText;
        this.linkImage = linkImage;
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public Location getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
        isPictureReady = true;
    }


    public boolean isPictureReady() {
        return isPictureReady;
    }
}
