package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.model.DMItem;
import org.trendtube.app.utils.Utils;

import java.util.List;

/**
 * Created by echessa on 7/24/15.
 */
public class DMRecyclerAdapter extends RecyclerView.Adapter<DMRecyclerAdapter.ViewHolder> {

    private List<DMItem> mDMItems;
    private Activity activity;
    private DailyMotionVideoItemSelectedListener listener;

    public DMRecyclerAdapter(Activity activity, List<DMItem> DMItems, DailyMotionVideoItemSelectedListener listener) {
        this.activity = activity;
        mDMItems = DMItems;
        this.listener = listener;
    }

    public void addItems(List<DMItem> DMItems) {
        mDMItems.addAll(DMItems);
    }

    public void setItems(List<DMItem> DMItems) {
        mDMItems.clear();
        mDMItems.addAll(DMItems);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_yt_video, viewGroup, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        DMItem DMItem = mDMItems.get(i);
        //Utils.displayImage(activity, DMItem.getThumbnailLargeUrl(), R.drawable.image, viewHolder.imgThumbnail);
        Utils.displayImage(activity, DMItem.getThumbnailLargeUrl(), viewHolder.imgThumbnail);
        viewHolder.txtTitle.setText(DMItem.getTitle());
        viewHolder.txtChannelTitle.setText(DMItem.getChannel());
        viewHolder.txtAgeAndViews.setText(Utils.calculateAge(DMItem.getCreatedTime()) + " . " + Utils.calculateViewCount(DMItem.getViewsTotal() + "") + " views");
        viewHolder.txtDuration.setText(Utils.calculateDuration(DMItem.getDuration()));
        viewHolder.setVideoItem(DMItem);
    }

    @Override
    public int getItemCount() {
        return mDMItems.size();
    }

    public interface DailyMotionVideoItemSelectedListener {
        public void onDailyMotionVideoItemSelected(DMItem videoItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imgThumbnail;
        private final TextView txtTitle;
        private final TextView txtChannelTitle;
        private final TextView txtAgeAndViews;
        public final TextView txtDuration;
        private DMItem videoItem;
        private final DailyMotionVideoItemSelectedListener listener;

        public void setVideoItem(DMItem videoItem) {
            this.videoItem = videoItem;
        }

        ViewHolder(View v, DailyMotionVideoItemSelectedListener listener) {
            super(v);
            imgThumbnail = (ImageView) v.findViewById(R.id.img_thumbnail);
            txtTitle = (TextView) v.findViewById(R.id.txt_title);
            txtChannelTitle = (TextView) v.findViewById(R.id.txt_channel_title);
            txtAgeAndViews = (TextView) v.findViewById(R.id.txt_age_and_views);
            txtDuration = (TextView) v.findViewById(R.id.txt_duration);
            v.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onDailyMotionVideoItemSelected(videoItem);
        }
    }

}
