package com.example.AndroidRSSReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 25.10.13
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class ChannelsActivity extends Activity {
    private FeedsLoaderService service = new FeedsLoaderService();
    ArrayList<Channel> values = new ArrayList<>();
    final AppDatabaseDAO dao = new AppDatabaseDAO(this);


    public ChannelsActivity getInstance() {
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channels);
        final ListView channels = (ListView) findViewById(R.id.channels);
        values = dao.getAllChannels();

        final ChannelArrayAdapter adapter = new ChannelArrayAdapter(this, android.R.layout.simple_list_item_1, values);
        Button addNew = new Button(this);
        addNew.setText(R.string.add_new_channel);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.channel_edit,
                        (ViewGroup) findViewById(R.id.toast_view));
                AlertDialog.Builder alert = new AlertDialog.Builder(getInstance());
                alert.setTitle(R.string.new_channel);
                alert.setView(layout);
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String title = ((EditText) layout.findViewById(R.id.editText)).getText().toString();
                        String url = ((EditText) layout.findViewById(R.id.editText1)).getText().toString();
                        Channel channel = new Channel(title, url);
                        long id = dao.addChannel(channel);
                        channel.dbId = (int) id;
                        values.add(channel);
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton(R.string.cancel, null);
                alert.show();
            }
        });
        channels.addFooterView(addNew);
        channels.setAdapter(adapter);
        // values.add(new Channel("Lenta", "http://lenta.ru/rss/"));
        //values.add(new Channel("Bash", "http://bash.im/rss/"));

        channels.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long l) {
                final Channel channel = values.get(position);
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.channel_edit,
                        (ViewGroup) findViewById(R.id.toast_view));
                AlertDialog.Builder alert = new AlertDialog.Builder(getInstance());
                alert.setTitle(R.string.edit_channel);
                ((EditText) layout.findViewById(R.id.editText)).setText(channel.title);
                ((EditText) layout.findViewById(R.id.editText1)).setText(channel.url);
                alert.setView(layout);
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String title = ((EditText) layout.findViewById(R.id.editText)).getText().toString();
                        String url = ((EditText) layout.findViewById(R.id.editText1)).getText().toString();
                        channel.title = title;
                        channel.url = url;
                        dao.editChannel(channel);
                        adapter.notifyDataSetChanged();
                    }
                });
                alert.setNeutralButton(R.string.remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dao.deleteChannel(values.get(position).dbId);
                        values.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.cancel), null);
                alert.show();
                return true;
            }
        });
        channels.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String url = values.get(position).url;
                Intent intent = new Intent(getInstance(), FeedsActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("channelID", values.get(position).dbId);

                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public class ChannelArrayAdapter extends ArrayAdapter<Channel> {

        public ChannelArrayAdapter(Context context, int resource, List<Channel> objects) {
            super(context, resource, objects);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            TextView descr = new TextView(getContext());
            descr.setText(this.getItem(pos).title);
            descr.setTextSize(40);
            return descr;
        }
    }


}
