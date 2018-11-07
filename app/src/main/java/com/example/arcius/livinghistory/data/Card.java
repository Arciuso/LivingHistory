package com.example.arcius.livinghistory.data;


import android.content.Context;

import java.io.File;

public class Card {

    public CardTypes getType() {
        return type;
    }

    public enum CardTypes {
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

    private Picture picture = null;
    private Location location;
    private Source source;

    public Card( String eventID,String date, String time, String mainTitle, String fullText, Location location, Source source ){
        this.type = CardTypes.Classic;
        this.eventID = eventID;
        this.date = date;
        this.time = time;
        this.mainTitle = mainTitle;
        this.fullText = fullText;
        this.location = location;
        this.source = source;
    }

    public Card( String eventID, String time, String mainTitle, Location location){
        this.type = CardTypes.Single;
        this.eventID = eventID;
        this.time = time;
        this.mainTitle = mainTitle;
        this.location = location;
    }

    public Card( String eventID, String date, String time, String mainTitle, String fullText, Picture picture, Location location, Source source ){
        this.type = CardTypes.Image;
        this.eventID = eventID;
        this.date = date;
        this.time = time;
        this.mainTitle = mainTitle;
        this.fullText = fullText;
        this.picture = picture;
        this.location = location;
        this.source = source;
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
        return picture.getLink();
    }

    public String getSourceImage() {
        return picture.getSource();
    }

    public String getTitleImage() {
        return picture.getTitle();
    }

    public Location getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getSourceName() {
        return this.source.getSourceName();
    }

    public String getSourceLink() {
        return this.source.getSourceLink();
    }

    public String getSourceTitle() {
        return this.source.getSourceTitle();
    }
    public boolean isPictureReady(Context context) {
        File file = context.getFileStreamPath(getDate() + "-" + getEventID());
        if(file == null || !file.exists()) {
            System.out.println("Picture has NOT been found !");
            return false;
        } else {
            System.out.println("Picture has been found !");
            return true;
        }
    }
}
