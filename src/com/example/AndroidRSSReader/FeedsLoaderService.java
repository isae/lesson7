package com.example.AndroidRSSReader;

import android.*;
import android.R;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 25.10.13
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public class FeedsLoaderService extends IntentService {

    public FeedsLoaderService() {
        super("FeedsLoaderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean success = false;
        String xml = null;
        ArrayList<Feed> list = new ArrayList<>();
        String url = intent.getStringExtra("url");
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            Log.e("", "", e);
        }
        if (httpResponse != null) {
            HttpEntity httpEntity = httpResponse.getEntity();
            byte[] x = new byte[0];
            try {
                x = EntityUtils.toByteArray(httpEntity);
                xml = new String(x);
                String encoding;
                int i = xml.indexOf("encoding") + 10;
                int j = xml.indexOf("\"?>");
                if (i != -1 && j != -1) {
                    encoding = xml.substring(i, j);
                } else {
                    encoding = "windows-1251";
                }
                xml = new String(x, encoding);
            } catch (IOException e) {
                Log.e("", "", e);
            }
            RSSSaxParser parser = null;
            parser = new RSSSaxParser(xml);
            list = parser.parse();
            success = true;
        }


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(FeedsReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra("feeds", list);
        broadcastIntent.putExtra("success", success);
        sendBroadcast(broadcastIntent);
    }
}
