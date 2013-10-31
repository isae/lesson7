package com.example.AndroidRSSReader;

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


        ArrayList<Feed> list = new ArrayList<>();
        String url = intent.getStringExtra("url");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        HttpEntity httpEntity = httpResponse.getEntity();

        RSSSaxParser parser = null;
        try {
            parser = new RSSSaxParser(EntityUtils.toString(httpEntity));
            list = parser.parse();
            for (Feed f : list) {
                Log.e("hihih", String.valueOf(f.title), null);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(FeedsReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra("feeds", list);
        broadcastIntent.putExtra("success", true);
        sendBroadcast(broadcastIntent);
    }
}
