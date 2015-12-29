package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.model.YTItem;
import org.trendtube.app.utils.Utils;

import java.util.List;

/**
 * Created by echessa on 7/24/15.
 */
public class YTRecyclerAdapter extends RecyclerView.Adapter<YTRecyclerAdapter.ViewHolder> {

    private List<YTItem> mItems;
    private Activity activity;
    private YouTubeVideoItemSelectedListener listener;

    public YTRecyclerAdapter(Activity activity, List<YTItem> items, YouTubeVideoItemSelectedListener listener) {
        this.activity = activity;
        mItems = items;
        this.listener = listener;
    }

    public void addItems(List<YTItem> items) {
        mItems.addAll(items);
    }

    public void setItems(List<YTItem> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_yt_video, viewGroup, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        YTItem item = mItems.get(i);
        //Utils.displayImage(activity, item.getSnippet().getThumbnails().getMediumImage().getUrl(), R.drawable.image, viewHolder.imgThumbnail);
        Utils.displayImage(activity, item.getSnippet().getThumbnails().getMediumImage().getUrl(), viewHolder.imgThumbnail);
        viewHolder.txtTitle.setText(item.getSnippet().getTitle());
        viewHolder.txtChannelTitle.setText(item.getSnippet().getChannelTitle());
        viewHolder.txtAgeAndViews.setText(Utils.calculateAge(item.getSnippet().getPublishedAt()) + " . " + Utils.calculateViewCount(item.getStatistics().getViewCount()) + " views");
        viewHolder.txtDuration.setText(Utils.calculateDuration(item.getContentDetails().getDuration()));
        viewHolder.setVideoItem(item);
        //MyLog.e(Utils.calculateDuration(item.getContentDetails().getDuration()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public interface YouTubeVideoItemSelectedListener {
        public void onYouTubeVideoSelected(YTItem videoItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imgThumbnail;
        private final TextView txtTitle;
        private final TextView txtChannelTitle;
        private final TextView txtAgeAndViews;
        private final TextView txtDuration;
        private YTItem videoItem;

        public void setVideoItem(YTItem videoItem) {
            this.videoItem = videoItem;
        }

        private final YouTubeVideoItemSelectedListener listener;

        ViewHolder(View v, YouTubeVideoItemSelectedListener listener) {
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
            listener.onYouTubeVideoSelected(videoItem);
        }
    }

}
