package com.example.AndroidRSSReader;

import java.io.Serializable;
import java.net.URL;
import java.security.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 25.10.13
 * Time: 17:59
 * To change this template use File | Settings | File Templates.
 */
public class Feed implements Serializable {
    public CharSequence title;
    public URL link;
    public CharSequence description;
    public Date publicationDate;
    public CharSequence guid;

    public Feed() {
    }

    public Feed(CharSequence title) {
        this.title = title;
    }

    public Feed(CharSequence title, CharSequence description) {
        this.title = title;
        this.description = description;
    }
}
