package com.example.AndroidRSSReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 25.10.13
 * Time: 18:11
 * To change this template use File | Settings | File Templates.
 */
public class RssFeedHandler extends DefaultHandler {

    private ArrayList<Feed> feeds;
    private StringBuilder temp;
    private Feed tempFeed;

    public RssFeedHandler() {
        feeds = new ArrayList<Feed>();
    }

    public ArrayList<Feed> getFeeds() {

        return feeds;
    }

    @Override
    public void startElement(String uri, String localName, String tag,
                             Attributes attributes) throws SAXException {
        temp= new StringBuilder();
        if (tag.equalsIgnoreCase("item")) {
            tempFeed = new Feed();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        temp.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String tag)
            throws SAXException {
        if (tag.equalsIgnoreCase("item")) {
            feeds.add(tempFeed);
        } else if (tempFeed != null) {
            if (tag.equalsIgnoreCase("title")) {
                tempFeed.title = temp;
            } else if (tag.equalsIgnoreCase("link")) {
                    tempFeed.link = temp.toString();
            } else if (tag.equalsIgnoreCase("description")) {
                tempFeed.description = temp;
            } else if (tag.equalsIgnoreCase("pubDate")) {
                tempFeed.publicationDate = parseDate(temp.toString());
            } else if (tag.equalsIgnoreCase("guid")) {
                tempFeed.guid = temp;
            }
        }
    }

    //Fri, 25 Oct 2013 12:46:01 +0400
    private Date parseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("E, d M y H:m:s Z");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
