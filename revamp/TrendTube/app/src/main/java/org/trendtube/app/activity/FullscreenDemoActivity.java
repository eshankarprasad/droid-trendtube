/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.trendtube.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.trendtube.app.R;
import org.trendtube.app.adapter.VideoDetailRecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.model.DailyMotionVideoItem;
import org.trendtube.app.model.VideoDetailHeaderModel;
import org.trendtube.app.model.VimeoVideoItem;
import org.trendtube.app.model.YouTubeVideoItem;
import org.trendtube.app.model.YouTubeVideoModel;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.SearchYouTubeVideoVolleyTask;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p/>
 * This is the preferred way of handling fullscreen because the default fullscreen implementation
 * will cause re-buffering of the video.
 */
public class FullscreenDemoActivity extends YouTubeFailureRecoveryActivity implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, YouTubePlayer.OnFullscreenListener,
        SearchYouTubeVideoVolleyTask.SearchYouTubeVideoListener, VideoDetailRecyclerAdapter.SimilarVideoItemSelectedListener {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    private LinearLayout baseLayout;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private View otherViews;
    private boolean fullscreen;
    private String videoId, videoTitle, views;
    private int videoSearchFlag = 0;

    private RecyclerView recyclerView;
    private VideoDetailRecyclerAdapter adapter;
    private int tabPosition;
    private String nextPageToken;
    private View progressWheel, footerProgressWheel;//, layoutHiddenSummary;
    //private RecyclerViewHeader recyclerHeader;
    private VideoDetailHeaderModel headerModel;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, FullscreenDemoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fullscreen_demo);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            Object object = bundle.getSerializable(Constants.BUNDLE_VIDEO);
            if (object instanceof YouTubeVideoItem) {
                YouTubeVideoItem item = (YouTubeVideoItem) object;
                videoId = item.getId();
                videoTitle = item.getSnippet().getTitle();
                views = item.getStatistics().getViewCount();
            } else if (object instanceof DailyMotionVideoItem) {
                DailyMotionVideoItem item = (DailyMotionVideoItem) object;
                videoId = item.getId();
                videoTitle = item.getTitle();
                views = String.valueOf(item.getViewsTotal());
            } else if (object instanceof VimeoVideoItem) {
                VimeoVideoItem item = (VimeoVideoItem) object;
                videoId = item.getId();
                videoTitle = item.getTitle();
                views = String.valueOf(item.getViewsCount());
            }

            initHeaderModel();
            videoSearchFlag = bundle.getInt(Constants.BUNDLE_VIDEO_FLAG, 0);
        }

        baseLayout = (LinearLayout) findViewById(R.id.layout);
        playerView = (YouTubePlayerView) findViewById(R.id.player);
        otherViews = findViewById(R.id.other_views);
        playerView.initialize(Constants.DEVELOPER_KEY, this);
        doLayout();

        nextPageToken = "";
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressWheel = findViewById(R.id.progress_bar);
        footerProgressWheel = findViewById(R.id.footer_progress_bar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new VideoDetailRecyclerAdapter(this, headerModel, new ArrayList<YouTubeVideoItem>(), this);
        recyclerView.setAdapter(adapter);
        //initHeader();
        recyclerView.addOnScrollListener(new EndlessScrollVideosListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                MyLog.e("Current Page: (YouTube) " + currentPage);
                if (nextPageToken == null) {
                    MyLog.e("End of loading");
                } else {
                    loadVideoContent();
                }
            }
        });
        loadVideoContent();
    }

    private void initHeaderModel() {
        headerModel = new VideoDetailHeaderModel();
        headerModel.setTitle(videoTitle);
        headerModel.setViews(getString(R.string.views, Utils.getCommaSeperatedNumber(views)));
        headerModel.setPublishDate("Published on 13 Jan, 2001");
    }

    /*private void initHeader() {

        recyclerHeader = RecyclerViewHeader.fromXml(this, R.layout.header_video_detail);
        recyclerHeader.attachTo(recyclerView);
        TextView txtTitle = (TextView) recyclerHeader.findViewById(R.id.txt_title);
        txtTitle.setText(videoTitle);

        TextView txtViews = (TextView) recyclerHeader.findViewById(R.id.txt_views);
        txtViews.setText(getString(R.string.views, Utils.getCommaSeperatedNumber(views)));

        layoutHiddenSummary = recyclerHeader.findViewById(R.id.layout_hidden_summary);
    }*/

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        this.player = player;
        //setControlsEnabled();
        // Specify that we want to handle fullscreen behavior ourselves.
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        if (!wasRestored) {
            player.cueVideo(videoId);
            player.play();
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.layout_visible_header:
                TextView txtViews = (TextView) findViewById(R.id.txt_views);
                if (layoutHiddenSummary.getVisibility() == View.VISIBLE) {
                    layoutHiddenSummary.setVisibility(View.GONE);
                    txtViews.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_arrow_drop_down_black), null);
                } else {
                    layoutHiddenSummary.setVisibility(View.VISIBLE);
                    txtViews.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_arrow_drop_up_black), null);
                }
                break;*/
            default:
                player.setFullscreen(!fullscreen);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int controlFlags = player.getFullscreenControlFlags();
        if (isChecked) {
            setRequestedOrientation(PORTRAIT_ORIENTATION);
            controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        }
        player.setFullscreenControlFlags(controlFlags);
    }

    private void doLayout() {
        LinearLayout.LayoutParams playerParams = (LinearLayout.LayoutParams) playerView.getLayoutParams();
        if (fullscreen) {
            // When in fullscreen, the visibility of all other views than the player should be set to
            // GONE and the player should be laid out across the whole screen.
            playerParams.width = LayoutParams.MATCH_PARENT;
            playerParams.height = LayoutParams.MATCH_PARENT;

            otherViews.setVisibility(View.GONE);
        } else {
            // This layout is up to you - this is just a simple example (vertically stacked boxes in
            // portrait, horizontally stacked in landscape).
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            otherViews.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams otherViewsParams = otherViews.getLayoutParams();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerParams.width = otherViewsParams.width = 0;
                playerParams.height = WRAP_CONTENT;
                otherViewsParams.height = MATCH_PARENT;
                playerParams.weight = 1;
                baseLayout.setOrientation(LinearLayout.HORIZONTAL);
            } else {
                playerParams.width = otherViewsParams.width = MATCH_PARENT;
                playerParams.height = WRAP_CONTENT;
                playerParams.weight = 0;
                otherViewsParams.height = 0;
                baseLayout.setOrientation(LinearLayout.VERTICAL);
            }
            //setControlsEnabled();
        }
    }

    /*private void setControlsEnabled() {
        checkbox.setEnabled(player != null
                && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        fullscreenButton.setEnabled(player != null);
    }*/

    @Override
    public void onFullscreen(boolean isFullscreen) {
        fullscreen = isFullscreen;
        doLayout();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doLayout();
    }

    @Override
    public void finish() {
        super.finish();
        Utils.animateActivity(this, "back");
    }

    @Override
    public void onBackPressed() {
        if (fullscreen) {
            onFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    private void loadVideoContent() {
        if ("".equals(nextPageToken)) {
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            footerProgressWheel.setVisibility(View.VISIBLE);
        }

        SearchYouTubeVideoVolleyTask task = new SearchYouTubeVideoVolleyTask(this, this);
        task.execute(nextPageToken, videoTitle);
    }

    @Override
    public void onSuccessYouTubeSearch(YouTubeVideoModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        /*if (adapter == null) {
            adapter = new VideoDetailRecyclerAdapter(this, headerModel, response.getYouTubeVideoItems(), this);
            recyclerView.setAdapter(adapter);
        } else */
        if ("".equals(nextPageToken)) {
            adapter.setItems(response.getYouTubeVideoItems());
            adapter.notifyDataSetChanged();
        } else {
            adapter.addItems(response.getYouTubeVideoItems());
            adapter.notifyDataSetChanged();
        }
        nextPageToken = response.getNextPageToken();
    }

    @Override
    public void onErrorYouTubeSearch(VolleyError error) {
        error.printStackTrace();
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        Utils.handleError(this, error);
    }

    @Override
    public void onSimilarVideoItemSelected(YouTubeVideoItem videoItem) {
        Toast.makeText(this, videoItem.getSnippet().getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, FullscreenDemoActivity.class);
        intent.putExtra(Constants.BUNDLE_VIDEO, videoItem);
        finish();
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(this, "next");
    }

    @Override
    public void onHeadetItemSelected() {
        MyLog.e("onHeadetItemSelected");
    }
}
