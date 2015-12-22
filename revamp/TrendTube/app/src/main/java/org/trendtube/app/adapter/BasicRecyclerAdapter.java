package org.trendtube.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.interfaces.BasicItemSelectedListener;
import org.trendtube.app.model.BasicItem;

import java.util.List;

/**
 * Created by echessa on 7/24/15.
 */
public class BasicRecyclerAdapter extends RecyclerView.Adapter<BasicRecyclerAdapter.ViewHolder> {

    private List<BasicItem> mItems;
    private Activity activity;
    private BasicItemSelectedListener listener;
    private BasicItem seletctedItem;
    private Dialog dialog;
    private int type;

    public BasicRecyclerAdapter(Activity activity, Dialog dialog, List<BasicItem> items, BasicItem seletctedItem, BasicItemSelectedListener listener, int type) {
        this.activity = activity;
        mItems = items;
        this.dialog = dialog;
        this.seletctedItem = seletctedItem;
        this.listener = listener;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        BasicItem item = mItems.get(i);
        viewHolder.txtTitle.setText(item.getName());
        if (seletctedItem.getId().equals(item.getId())) {
            viewHolder.txtTitle.setBackgroundColor(activity.getResources().getColor(R.color.gray_clouds_dark));
        } else {
            viewHolder.txtTitle.setBackgroundColor(Color.TRANSPARENT);
        }
        viewHolder.setPosition(i);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txtTitle;
        private int position;

        ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.txt_item);
            v.setOnClickListener(this);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (seletctedItem.getId().equals(mItems.get(position).getId())) {
                dialog.dismiss();
            } else {
                listener.onListViewItemSelected(mItems.get(position), type);
                dialog.dismiss();
            }
        }
    }

}
