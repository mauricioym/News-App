package com.yuddi.newsapp;

/**
 * Created by Mauricio on 11/1/2016.
 */
public class Story {

    private String mTitle;
    private String mSection;
    private String mDate;
    private String mUrl;

    public Story(String title, String section, String date, String url) {
        this.mTitle = title;
        this.mSection = section;
        this.mDate = date;
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
