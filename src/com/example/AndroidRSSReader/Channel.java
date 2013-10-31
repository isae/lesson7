package com.example.AndroidRSSReader;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 31.10.13
 * Time: 12:45
 * To change this template use File | Settings | File Templates.
 */
public class Channel {
    public int dbId;
    public String title;
    public String url;

    public Channel(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Channel(int dbId, String title, String url) {
        this.dbId = dbId;
        this.title = title;
        this.url = url;
    }
}
