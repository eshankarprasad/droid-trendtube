package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.trendtube.app.R;
import org.trendtube.app.model.VideoDetailHeaderModel;
import org.trendtube.app.model.YTItem;
import org.trendtube.app.utils.Utils;

import java.util.List;

/**
 * Created by echessa on 7/24/15.
 */
public class PlayerYTRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private VideoDetailHeaderModel headerModel;
    private List<YTItem> mItems;
    private Activity activity;
    private SimilarVideoItemSelectedListener listener;

    public PlayerYTRecyclerAdapter(Activity activity, VideoDetailHeaderModel headerModel, List<YTItem> items, SimilarVideoItemSelectedListener listener) {
        this.activity = activity;
        mItems = items;
        this.headerModel = headerModel;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header_video_detail, viewGroup, false);
            return new VHHeader(v, listener);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_yt_video, viewGroup, false);
            return new VHItem(v, listener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof VHHeader) {
            VHHeader viewHeader = (VHHeader) holder;
            viewHeader.txtTitle.setText(headerModel.getTitle());
            viewHeader.txtViews.setText(headerModel.getViews());
            viewHeader.txtPublishDate.setText(headerModel.getPublishDate());
            viewHeader.txtDescription.setText(Html.fromHtml(headerModel.getDescription()));
        } else {
            YTItem item = (YTItem) mItems.get(i-1);
            VHItem viewItem = (VHItem) holder;
            //Utils.displayImage(activity, item.getSnippet().getThumbnails().getMediumImage().getUrl(), R.drawable.image, VHItem.imgThumbnail);
            Utils.displayImage(activity, item.getSnippet().getThumbnails().getMediumImage().getUrl(), viewItem.imgThumbnail);
            viewItem.txtTitle.setText(item.getSnippet().getTitle());
            viewItem.txtChannelTitle.setText(item.getSnippet().getChannelTitle());
            viewItem.txtAgeAndViews.setText(Utils.calculateAge(item.getSnippet().getPublishedAt()) + " . " + Utils.calculateViewCount(item.getStatistics().getViewCount()) + " views");
            viewItem.txtDuration.setText(Utils.calculateDuration(item.getContentDetails().getDuration()));
            viewItem.setVideoItem(item);
        }
    }

    @Override
    public int getItemCount() {
        // Since header will be present atleast incase of 0 item; so return +1;
        return mItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == TYPE_HEADER ? TYPE_HEADER : TYPE_ITEM;
    }
    public interface SimilarVideoItemSelectedListener {
        public void onSimilarVideoItemSelected(YTItem videoItem);
        public void onHeadetItemSelected();
    }

    public class VHHeader extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txtTitle;
        private final TextView txtViews;
        private final TextView txtPublishDate;
        private final TextView txtDescription;
        private final View btnShowHiddenSummary;
        private final View viewHiddenSummary;
        private final View btnShare;

        private final SimilarVideoItemSelectedListener listener;

        VHHeader(View v, SimilarVideoItemSelectedListener listener) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.txt_title);
            txtViews = (TextView) v.findViewById(R.id.txt_views);
            txtPublishDate = (TextView) v.findViewById(R.id.txt_published_date);
            txtDescription = (TextView) v.findViewById(R.id.txt_description);
            btnShare = v.findViewById(R.id.btn_share);
            btnShare.setOnClickListener(this);
            btnShowHiddenSummary = v.findViewById(R.id.layout_visible_header);
            btnShowHiddenSummary.setOnClickListener(this);
            viewHiddenSummary = v.findViewById(R.id.layout_hidden_summary);
            //txtDescription.setMovementMethod(LinkMovementMethod.getInstance());
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_visible_header:
                    if (viewHiddenSummary.getVisibility() == View.VISIBLE) {
                        viewHiddenSummary.setVisibility(View.GONE);
                        txtPublishDate.setCompoundDrawablesWithIntrinsicBounds(null, null, activity.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black), null);
                    } else {
                        viewHiddenSummary.setVisibility(View.VISIBLE);
                        txtPublishDate.setCompoundDrawablesWithIntrinsicBounds(null, null, activity.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black), null);
                    }
                    break;

                case R.id.btn_share:
                    listener.onHeadetItemSelected();
                    break;
            }
         }
    }

    public class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imgThumbnail;
        private final TextView txtTitle;
        private final TextView txtChannelTitle;
        private final TextView txtAgeAndViews;
        private final TextView txtDuration;
        private YTItem videoItem;

        public void setVideoItem(YTItem videoItem) {
            this.videoItem = videoItem;
        }

        private final SimilarVideoItemSelectedListener listener;

        VHItem(View v, SimilarVideoItemSelectedListener listener) {
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
            listener.onSimilarVideoItemSelected(videoItem);
        }
    }
}
