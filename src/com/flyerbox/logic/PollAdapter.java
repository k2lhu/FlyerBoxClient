package com.flyerbox.logic;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flyerbox.R;

import java.util.ArrayList;

/**
 * Created by tmrafael on 02.12.2014.
 */
public class PollAdapter extends BaseAdapter {
    ArrayList<Poll> data = new ArrayList<Poll>();
    Context context;

    public PollAdapter(Context context, ArrayList<Poll> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Poll getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pollls_list_item, parent, false);
        }

        LinearLayout item = (LinearLayout) convertView.findViewById(R.id.polls_list_item);
        TextView title = (TextView) convertView.findViewById(R.id.pollItemTitle);
        TextView description = (TextView) convertView.findViewById(R.id.pollItemDescription);
        LinearLayout status = (LinearLayout) convertView.findViewById(R.id.pollItemStatus);

        item.setTag(data.get(position).getId());
        title.setText(data.get(position).getTitle());
        description.setText(data.get(position).getDescription());
        if (data.get(position).isWatched()){
            status.setVisibility(LinearLayout.INVISIBLE);
        } else {
            status.setVisibility(LinearLayout.VISIBLE);
        }

        return convertView;
    }
}
