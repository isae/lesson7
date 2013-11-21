package com.example.AndroidRSSReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
    private final FeedsArrayAdapter adapter;
    private AppDatabaseDAO dao;
    private final int channelID;
    public FeedsActivity view;

    public FeedsReceiver(FeedsActivity activity, AppDatabaseDAO dao, FeedsArrayAdapter adapter) {
        super();
        this.adapter = adapter;
        this.view = activity;
        this.dao = dao;
        this.channelID = activity.channelID;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ArrayList<Feed> text = (ArrayList<Feed>) intent.getSerializableExtra("feeds");

        Boolean success = intent.getBooleanExtra("success", false);
        Toast toast = Toast.makeText(view, "", 5);
        if (success) {
            if (text != null) {
                for (Feed seq : text) {
                    adapter.add(seq);
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
                toast.setText(context.getResources().getString(R.string.feeds_reloaded));
                dao.addFeeds(channelID, text);
            } else {
                toast.setText(context.getResources().getString(R.string.problems));
            }

        } else {
            toast.setText(context.getResources().getString(R.string.no_connection));
        }
        toast.show();
    }
}