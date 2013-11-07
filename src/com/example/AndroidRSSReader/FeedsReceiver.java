package com.example.AndroidRSSReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 25.10.13
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public class FeedsReceiver extends BroadcastReceiver {
    public static final String ACTION_RESP = "feeds_loaded";
    private final FeedsArrayAdapter list;
    private AppDatabaseDAO dao;
    private final int channelID;

    public FeedsReceiver(Integer channelID, FeedsArrayAdapter list, AppDatabaseDAO dao) {
        super();
        this.list = list;
        this.dao = dao;
        this.channelID = channelID;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Feed> text = (ArrayList<Feed>) intent.getSerializableExtra("feeds");
        try {
            dao.deleteAllFeeds(channelID);
            for (Feed seq : text) {
                list.add(seq);
            }
            list.notifyDataSetChanged();
            dao.addFeeds(channelID, text);
        } catch (SQLException e) {
            Log.e("111", "111", e);
        }
    }
}