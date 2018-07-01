package com.example.arcius.livinghistory.data;

public class Card {

    public CardTypes getType() {
        return type;
    }

    public int getResourceImage() {
        return resourceImage;
    }

    public enum CardTypes {
        Classic(0), Image(1);

        private final int value;

        private CardTypes(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private CardTypes type;

    private String eventID;

    private String time;
    private String mainTitle;
    private String fullText;

    private int resourceImage;

    public Card( String eventID, String time, String mainTitle, String fullText ){
        this.type = CardTypes.Classic;
        this.eventID = eventID;
        this.time = time;
        this.mainTitle = mainTitle;
        this.fullText = fullText;
    }

    public Card( String eventID, String time, String mainTitle, String fullText, int resourceImage ){
        this.type = CardTypes.Image;
        this.eventID = eventID;
        this.time = time;
        this.mainTitle = mainTitle;
        this.fullText = fullText;
        this.resourceImage = resourceImage;
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
}
