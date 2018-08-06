package com.example.tomato.assignmentnotetdgiang.model;

public class Note {

    private int mID;
    private String mTitle;
    private String mContent;
    private String mDate;
    private String mTime;
    private byte[] mImage;
    private String mColor;
    private String mTimeNow;

    public Note() {
    }

    public Note(String title, String content, String date, String time, byte[] image, String color, String timeNow) {
        mTitle = title;
        mContent = content;
        mDate = date;
        mTime = time;
        mImage = image;
        mColor = color;
        mTimeNow = timeNow;
    }

    public Note(int ID, String title, String content, String date, String time, byte[] image, String color, String timeNow) {
        mID = ID;
        mTitle = title;
        mContent = content;
        mDate = date;
        mTime = time;
        mImage = image;
        mColor = color;
        mTimeNow = timeNow;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public byte[] getImage() {
        return mImage;
    }

    public void setImage(byte[] image) {
        mImage = image;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getTimeNow() {
        return mTimeNow;
    }

    public void setTimeNow(String timeNow) {
        mTimeNow = timeNow;
    }
}
