package com.example.AndroidRSSReader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 28.10.13
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class FeedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        Feed feed = (Feed) getIntent().getSerializableExtra("feed");
        TextView title = (TextView) findViewById(R.id.feedTitle);
        WebView body = (WebView) findViewById(R.id.feedContent);
        title.setText(feed.title);
        body.loadData("<?xml version='1.0' encoding='utf-8' ?>"+feed.description, "text/html", null);
    }

}
