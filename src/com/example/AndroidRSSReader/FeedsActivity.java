package com.example.AndroidRSSReader;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 16.10.13
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class FeedsActivity extends Activity {

    ArrayList<Feed> list = new ArrayList<Feed>();
    private final AppDatabaseDAO dao = new AppDatabaseDAO(this);
    private FeedsReceiver receiver;
    FeedsArrayAdapter adapter;
    int channelID;
    AlarmManager am;
    Intent intent;
    PendingIntent loadIntent;

    public FeedsActivity getInstance() {
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        setContentView(R.layout.feeds);
        final ListView listview = (ListView) findViewById(R.id.feeds);
        channelID = getIntent().getIntExtra("channelID", -1);
        list = dao.getAllFeeds(channelID);
        adapter = new FeedsArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        intent = new Intent(this, FeedsLoaderService.class);
        intent.putExtra("url", getIntent().getStringExtra("url"));
        startService(intent);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final Feed item = (Feed) parent.getItemAtPosition(position);
                Intent intent1 = new Intent(getInstance(), FeedActivity.class);
                intent1.putExtra("feed", item);
                startActivity(intent1);
            }


        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long l) {
                final Feed feed = list.get(position - 1);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(feed.link)));
                startActivity(browserIntent);
                return true;
            }
        });
        Button button = new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(intent);
            }
        });

        loadIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        button.setText(R.string.refresh);
        listview.addHeaderView(button);
        listview.setAdapter(adapter);
        receiver = new FeedsReceiver(this, dao, adapter);
        IntentFilter filter = new IntentFilter(FeedsReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 300000, loadIntent);
        registerReceiver(receiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        am.cancel(loadIntent);
        unregisterReceiver(receiver);
    }


}
