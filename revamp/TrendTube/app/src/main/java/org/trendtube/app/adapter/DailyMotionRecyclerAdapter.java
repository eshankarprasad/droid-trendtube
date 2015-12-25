package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.model.DailyMotionVideoItem;
import org.trendtube.app.utils.Utils;

import java.util.List;

/**
 * Created by echessa on 7/24/15.
 */
public class DailyMotionRecyclerAdapter extends RecyclerView.Adapter<DailyMotionRecyclerAdapter.ViewHolder> {

    private List<DailyMotionVideoItem> mDailyMotionVideoItems;
    private Activity activity;
    private DailyMotionVideoItemSelectedListener listener;

    public DailyMotionRecyclerAdapter(Activity activity, List<DailyMotionVideoItem> dailyMotionVideoItems, DailyMotionVideoItemSelectedListener listener) {
        this.activity = activity;
        mDailyMotionVideoItems = dailyMotionVideoItems;
        this.listener = listener;
    }

    public void addItems(List<DailyMotionVideoItem> dailyMotionVideoItems) {
        mDailyMotionVideoItems.addAll(dailyMotionVideoItems);
    }

    public void setItems(List<DailyMotionVideoItem> dailyMotionVideoItems) {
        mDailyMotionVideoItems.clear();
        mDailyMotionVideoItems.addAll(dailyMotionVideoItems);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_youtube_video, viewGroup, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        DailyMotionVideoItem dailyMotionVideoItem = mDailyMotionVideoItems.get(i);
        //Utils.displayImage(activity, dailyMotionVideoItem.getThumbnailLargeUrl(), R.drawable.image, viewHolder.imgThumbnail);
        Utils.displayImage(activity, dailyMotionVideoItem.getThumbnailLargeUrl(), viewHolder.imgThumbnail);
        viewHolder.txtTitle.setText(dailyMotionVideoItem.getTitle());
        viewHolder.txtChannelTitle.setText(dailyMotionVideoItem.getChannel());
        viewHolder.txtAgeAndViews.setText(Utils.calculateAge(dailyMotionVideoItem.getCreatedTime()) + " . " + Utils.calculateViewCount(dailyMotionVideoItem.getViewsTotal() + "") + " views");
        viewHolder.txtDuration.setText(Utils.calculateDuration(dailyMotionVideoItem.getDuration()));
        viewHolder.setVideoItem(dailyMotionVideoItem);
    }

    @Override
    public int getItemCount() {
        return mDailyMotionVideoItems.size();
    }

    public interface DailyMotionVideoItemSelectedListener {
        public void onDailyMotionVideoItemSelected(DailyMotionVideoItem videoItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imgThumbnail;
        private final TextView txtTitle;
        private final TextView txtChannelTitle;
        private final TextView txtAgeAndViews;
        public final TextView txtDuration;
        private DailyMotionVideoItem videoItem;
        private final DailyMotionVideoItemSelectedListener listener;

        public void setVideoItem(DailyMotionVideoItem videoItem) {
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
