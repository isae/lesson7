package com.example.AndroidRSSReader;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 16.10.13
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class FeedsActivity extends Activity {

    final ArrayList<Feed> list = new ArrayList<Feed>();
    private FeedsReceiver receiver;
    FeedsArrayAdapter adapter;

    public FeedsActivity getInstance() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds);
        final ListView listview = (ListView) findViewById(R.id.feeds);

        Log.e("lol", "lol", null);
        Intent intent = new Intent(this, FeedsLoaderService.class);
        intent.putExtra("url", getIntent().getStringExtra("url"));
        startService(intent);




        adapter = new FeedsArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final Feed item = (Feed) parent.getItemAtPosition(position);
                Intent intent1 = new Intent(getInstance(), FeedActivity.class);
                intent1.putExtra("feed", item);
                startActivity(intent1);
                adapter.notifyDataSetChanged();
            }


        });
        receiver = new FeedsReceiver(adapter);
        IntentFilter filter = new IntentFilter(FeedsReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}