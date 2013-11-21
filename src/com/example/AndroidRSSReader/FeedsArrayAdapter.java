package com.example.AndroidRSSReader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 25.10.13
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public class FeedsArrayAdapter extends ArrayAdapter<Feed> {
    private HashMap<CharSequence, Feed> map = new HashMap<>();

    public FeedsArrayAdapter(FeedsActivity context, int textViewResourceId,
                             ArrayList<Feed> objects) {
        super(context, textViewResourceId, objects);
        for (Feed f : objects) {
            map.put(String.valueOf(f.link), f);
        }
    }

    @Override
    public void add(Feed f) {
        if (!map.containsKey(f.link)) {
            map.put(String.valueOf(f.link), f);
            super.add(f);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Feed feed = getItem(position);
        TextView descr = new TextView(getContext());
        descr.setText(feed.title);
        return descr;
    }

}
