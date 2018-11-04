package com.example.arcius.livinghistory.data;

public class Picture {

    private int pictureID;
    private String link;
    private String source;
    private String title;

    public Picture(int pictureID, String link, String source, String title) {
        this.pictureID = pictureID;
        this.link = link;
        this.source = source;
        this.title = title;
    }

    public int getPictureID() {
        return pictureID;
    }

    public String getLink() {
        return link;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

}
