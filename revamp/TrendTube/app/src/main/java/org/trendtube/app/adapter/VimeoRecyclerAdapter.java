package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.model.VimeoVideoItem;
import org.trendtube.app.utils.Utils;

import java.util.List;

/**
 * Created by echessa on 7/24/15.
 */
public class VimeoRecyclerAdapter extends RecyclerView.Adapter<VimeoRecyclerAdapter.ViewHolder> {

    private List<VimeoVideoItem> mItems;
    private Activity activity;
    private VimeoVideoItemSelectedListener listener;

    public VimeoRecyclerAdapter(Activity activity, List<VimeoVideoItem> items, VimeoVideoItemSelectedListener listener) {
        this.activity = activity;
        mItems = items;
        this.listener = listener;
    }

    public void addItems(List<VimeoVideoItem> items) {
        mItems.addAll(items);
    }

    public void setItems(List<VimeoVideoItem> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_vimeo_video, viewGroup, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        VimeoVideoItem item = mItems.get(i);
        //Utils.displayImage(activity, item.getThumbnail(), R.drawable.image, viewHolder.imgThumbnail);
        Utils.displayImage(activity, item.getThumbnail(), viewHolder.imgThumbnail);
        viewHolder.txtTitle.setText(item.getTitle());
        //viewHolder.txtChannelTitle.setText(item.getChannel());
        viewHolder.txtAgeAndViews.setText(Utils.calculateAge(item.getCreatedTime()) + " . " + Utils.calculateViewCount(item.getViewsCount() + "") + " views");
        viewHolder.txtDuration.setText(Utils.calculateDuration(item.getDuration()));
        viewHolder.setVideoItem(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface VimeoVideoItemSelectedListener {
        public void onVimeoVideoItemSelected(VimeoVideoItem videoItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imgThumbnail;
        private final TextView txtTitle;
        //private final TextView txtChannelTitle;
        private final TextView txtAgeAndViews;
        private final VimeoVideoItemSelectedListener listener;
        private VimeoVideoItem videoItem;
        public final TextView txtDuration;

        public void setVideoItem(VimeoVideoItem videoItem) {
            this.videoItem = videoItem;
        }

        ViewHolder(View v, VimeoVideoItemSelectedListener listener) {
            super(v);
            imgThumbnail = (ImageView) v.findViewById(R.id.img_thumbnail);
            txtTitle = (TextView) v.findViewById(R.id.txt_title);
            //txtChannelTitle = (TextView) v.findViewById(R.id.txt_channel_title);
            txtAgeAndViews = (TextView) v.findViewById(R.id.txt_age_and_views);
            txtDuration = (TextView) v.findViewById(R.id.txt_duration);
            v.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onVimeoVideoItemSelected(videoItem);
        }
    }

}
