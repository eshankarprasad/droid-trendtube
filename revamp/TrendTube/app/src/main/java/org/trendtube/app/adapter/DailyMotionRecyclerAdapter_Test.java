package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.model.DailyMotionVideoModel.Item;
import org.trendtube.app.utils.Utils;

import java.util.List;

/**
 * Created by echessa on 7/24/15.
 */
public class DailyMotionRecyclerAdapter_Test extends RecyclerView.Adapter<DailyMotionRecyclerAdapter_Test.ViewHolder> {

    private List<Item> mItems;
    private Activity activity;
    private TopVideoItemSelectListener listener;

    public DailyMotionRecyclerAdapter_Test(Activity activity, List<Item> items, TopVideoItemSelectListener listener) {
        this.activity = activity;
        mItems = items;
        this.listener = listener;
    }

    public void addItems(List<Item> items) {
        mItems.addAll(items);
    }

    public void setItems(List<Item> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_dailymotion_video, viewGroup, false);
        v.setTag(mItems.get(i).getTitle());
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Item item = mItems.get(i);
        Utils.displayImage(activity, item.getThumbnailLargeUrl(), R.drawable.image, viewHolder.imgThumbnail);
        viewHolder.txtTitle.setText(item.getTitle());
        viewHolder.txtChannelTitle.setText(item.getChannel());
        viewHolder.txtAgeAndViews.setText(Utils.calculateAge(item.getCreatedTime()) + " . " + Utils.calculateViewCount(item.getViewsTotal() + "") + " views");
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface TopVideoItemSelectListener {
        public void onTopVideoItemSelected(Object videoId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imgThumbnail;
        private final TextView txtTitle;
        private final TextView txtChannelTitle;
        private final TextView txtAgeAndViews;
        private final TopVideoItemSelectListener listener;

        ViewHolder(View v, TopVideoItemSelectListener listener) {
            super(v);
            imgThumbnail = (ImageView) v.findViewById(R.id.img_thumbnail);
            txtTitle = (TextView) v.findViewById(R.id.txt_title);
            txtChannelTitle = (TextView) v.findViewById(R.id.txt_channel_title);
            txtAgeAndViews = (TextView) v.findViewById(R.id.txt_age_and_views);
            v.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onTopVideoItemSelected(v.getTag());
        }
    }

}
