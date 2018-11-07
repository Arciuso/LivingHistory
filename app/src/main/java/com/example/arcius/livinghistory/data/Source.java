package com.example.arcius.livinghistory.data;

public class Source {


    private String sourceName;
    private String sourceLink;
    private String sourceTitle;

    public Source(String sourceName, String sourceLink, String sourceTitle) {
        this.sourceName = sourceName;
        this.sourceLink = sourceLink;
        this.sourceTitle = sourceTitle;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }
}
