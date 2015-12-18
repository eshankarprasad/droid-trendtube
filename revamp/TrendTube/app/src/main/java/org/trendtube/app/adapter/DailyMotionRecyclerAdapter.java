package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.model.VideoItem;
import org.trendtube.app.utils.Utils;

import java.util.List;

/**
 * Created by echessa on 7/24/15.
 */
public class DailyMotionRecyclerAdapter extends RecyclerView.Adapter<DailyMotionRecyclerAdapter.ViewHolder> {

    private List<VideoItem> mItems;
    private Activity activity;
    private TopVideoItemSelectListener listener;

    public DailyMotionRecyclerAdapter(Activity activity, List<VideoItem> items, TopVideoItemSelectListener listener) {
        this.activity = activity;
        mItems = items;
        this.listener = listener;
    }

    public void addItems(List<VideoItem> items) {
        mItems.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_topviewed_video, viewGroup, false);
        v.setTag(mItems.get(i).getSnippet().getTitle());
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        VideoItem item = mItems.get(i);
        Utils.displayImage(activity, item.getSnippet().getThumbnails().getHighImage().getUrl(), R.drawable.image, viewHolder.imgThumbnail);
        viewHolder.txtTitle.setText(item.getSnippet().getTitle());
        viewHolder.txtChannelTitle.setText(item.getSnippet().getChannelTitle());
        viewHolder.txtAgeAndViews.setText(Utils.calculateAge(item.getSnippet().getPublishedAt()) + " . " + Utils.calculateViewCount(item.getStatistics().getViewCount()) + " views");
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