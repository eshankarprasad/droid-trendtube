package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.model.YouTubeVideoItem;
import org.trendtube.app.utils.Utils;

import java.util.List;

/**
 * Created by echessa on 7/24/15.
 */
public class YouTubeRecyclerAdapter extends RecyclerView.Adapter<YouTubeRecyclerAdapter.ViewHolder> {

    private List<YouTubeVideoItem> mItems;
    private Activity activity;
    private YouTubeVideoItemSelectedListener listener;

    public YouTubeRecyclerAdapter(Activity activity, List<YouTubeVideoItem> items, YouTubeVideoItemSelectedListener listener) {
        this.activity = activity;
        mItems = items;
        this.listener = listener;
    }

    public void addItems(List<YouTubeVideoItem> items) {
        mItems.addAll(items);
    }

    public void setItems(List<YouTubeVideoItem> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_youtube_video, viewGroup, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        YouTubeVideoItem item = mItems.get(i);
        //Utils.displayImage(activity, item.getSnippet().getThumbnails().getMediumImage().getUrl(), R.drawable.image, viewHolder.imgThumbnail);
        Utils.displayImage(activity, item.getSnippet().getThumbnails().getMediumImage().getUrl(), viewHolder.imgThumbnail);
        viewHolder.txtTitle.setText(item.getSnippet().getTitle());
        viewHolder.txtChannelTitle.setText(item.getSnippet().getChannelTitle());
        viewHolder.txtAgeAndViews.setText(Utils.calculateAge(item.getSnippet().getPublishedAt()) + " . " + Utils.calculateViewCount(item.getStatistics().getViewCount()) + " views");
        viewHolder.setVideoItem(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public interface YouTubeVideoItemSelectedListener {
        public void onYouTubeVideoSelected(YouTubeVideoItem videoItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imgThumbnail;
        private final TextView txtTitle;
        private final TextView txtChannelTitle;
        private final TextView txtAgeAndViews;
        private YouTubeVideoItem videoItem;

        public void setVideoItem(YouTubeVideoItem videoItem) {
            this.videoItem = videoItem;
        }

        private final YouTubeVideoItemSelectedListener listener;

        ViewHolder(View v, YouTubeVideoItemSelectedListener listener) {
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
            listener.onYouTubeVideoSelected(videoItem);
        }
    }

}
