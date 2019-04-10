package com.example.arcius.livinghistory.data;

/*
<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>


<provider android:authorities="com.facebook.app.FacebookContentProvider313108642627564"
            android:name="com.facebook.FacebookContentProvider"
            android:exported="true"/>
 */

import android.content.Context;
import android.util.Log;

import com.example.arcius.livinghistory.data.repository.local.entity.Event;
import com.example.arcius.livinghistory.data.repository.local.entity.Location;
import com.example.arcius.livinghistory.data.repository.local.entity.Picture;
import com.example.arcius.livinghistory.data.repository.local.entity.Source;

import java.io.File;

public class Card {

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

    private Event event;
    private Location location;
    private Picture picture;
    private Source source;

    public Card( Event event, Location location, Source source ) {
        this.type = CardTypes.Classic;

        this.event = event;
        this.location = location;
        this.source = source;

        this.event.locationID = location.locationID;    //In event contructor this properties are not set
    }

    public Card( Event event, Location location) {
        this.type = CardTypes.Single;

        this.event = event;
        this.location = location;

        this.event.locationID = location.locationID;    //In event contructor this properties are not set
    }

    public Card( Event event, Picture picture, Location location, Source source ){
        this.type = CardTypes.Image;

        this.event = event;
        this.picture = picture;
        this.location = location;
        this.source = source;

        this.event.locationID = location.locationID;    //In event contructor this properties are not set
        this.event.pictureID = picture.pictureID;
    }

    public Event getEvent() {
        return this.event;
    }

    public Location getLocation() {
        return this.location;
    }

    public Picture getPicture() {
        return this.picture;
    }

    public Source getSource() {
        return this.source;
    }

    public CardTypes getType() {
        return type;
    }


    public String getTime() {
        return this.event.time;
    }

    public void setTime(String time) {
        this.event.time = time;
    }

    public String getMainTitle() {
        return this.event.mainTitle;
    }

    public String getFullText() {
        return this.event.fullText;
    }

    public void setFullText(String fullText) {
        this.event.fullText = fullText;
    }

    public Integer getEventID() {
        return this.event.eventID;
    }

    public void setEventID(int eventID) {
        this.event.eventID = eventID;
    }

    public String getLinkImage() {
        return this.picture.link;
    }

    public String getSourceImage() {
        return this.picture.source;
    }

    public String getTitleImage() {
        return this.picture.title;
    }

    public String getDate() {
        return this.event.date;
    }

    public String getSourceName() {
        return this.source.name;
    }

    public String getSourceLink() {
        return this.source.link;
    }

    public String getSourceTitle() {
        return this.source.title;
    }

    public float getLat() {
        return this.location.latitude;
    }

    public float getLng() {
        return this.location.longitude;
    }

    public String getCountry() {
        return this.location.country;
    }

    public String getLocationName() {
        return this.location.name;
    }

    public boolean isPictureReady(Context context) {
        String fileName = getDate() + "-" + getEventID();
        File file = context.getFileStreamPath(fileName);

        if(file == null || !file.exists()) {
            Log.d("Card","Picture ( " + fileName +  " ) has not been found");
            return false;
        } else {
            Log.d("Card","Picture ( " + fileName +  " ) has been found");
            return true;
        }
    }
}
