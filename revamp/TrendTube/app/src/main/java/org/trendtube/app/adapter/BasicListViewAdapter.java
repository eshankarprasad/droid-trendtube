package org.trendtube.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.model.BasicItem;

import java.util.List;

/**
 * Created by ankitgarg on 24/2/14.
 */
public class BasicListViewAdapter extends ArrayAdapter<BasicItem> {

    private List<BasicItem> mItems;
    private Context mContext;
    private BasicItem selected;

    public BasicListViewAdapter(Context context, List<BasicItem> items, BasicItem selectedItem) {
        super(context, 0, items);
        mContext = context;
        mItems = items;
        selected = selectedItem;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public BasicItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getPosition(BasicItem item) {
        return mItems.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.txt_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(mItems.get(position).getName());

        if (selected.getId().equals(mItems.get(position).getId())) {
            holder.textView.setSelected(true);
        } else {
            holder.textView.setSelected(false);
        }

        return convertView;
    }


    private class ViewHolder {
        TextView textView;
    }

}
