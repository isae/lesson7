package com.example.AndroidRSSReader;

import android.util.Log;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 25.10.13
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */
public class RSSSaxParser {
    private String xml;

    public RSSSaxParser(String xml) {
        this.xml = xml;
    }

    public ArrayList<Feed> parse() {
        try {

            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            RssFeedHandler handler = new RssFeedHandler();
            xmlReader.setContentHandler(handler);
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            xmlReader.parse(is);
            return handler.getFeeds();
        } catch (Exception e) {
            Log.e("", "", e);
            return null;
        }
    }
}
