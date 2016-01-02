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
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.trendtube.app.R;
import org.trendtube.app.adapter.PlayerYTRecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.model.IndYTItem;
import org.trendtube.app.model.IndYTModel;
import org.trendtube.app.model.VideoDetailHeaderModel;
import org.trendtube.app.model.YTItem;
import org.trendtube.app.model.YTModel;
import org.trendtube.app.ui.TTProgressDialog;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchIndDMVolleyTask;
import org.trendtube.app.volleytasks.FetchIndYTVolleyTask;
import org.trendtube.app.volleytasks.SearchYTVolleyTask;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p/>
 * This is the preferred way of handling fullscreen because the default fullscreen implementation
 * will cause re-buffering of the video.
 */
public class PlayerYTActivity extends YTFailureRecoveryActivity implements
        View.OnClickListener, YouTubePlayer.OnFullscreenListener, YouTubePlayer.PlayerStateChangeListener,
        SearchYTVolleyTask.SearchYouTubeVideoListener, PlayerYTRecyclerAdapter.SimilarVideoItemSelectedListener,
        FetchIndYTVolleyTask.FetchIndVideosListener {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    private LinearLayout baseLayout;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private View otherViews;
    private boolean fullscreen;
    private String videoId, videoTitle, views, publishDate, description;

    private RecyclerView recyclerView;
    private PlayerYTRecyclerAdapter adapter;
    private int tabPosition;
    private String nextPageToken;
    private View progressWheel, footerProgressWheel;//, layoutHiddenSummary;
    //private RecyclerViewHeader recyclerHeader;
    private VideoDetailHeaderModel headerModel;
    private TTProgressDialog ttProgressDialog;
    private String videoUrl;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, PlayerYTActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_yt_activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            YTItem item = (YTItem) bundle.getSerializable(Constants.BUNDLE_VIDEO);
            videoId = item.getId();
            videoTitle = item.getSnippet().getTitle();
            views = item.getStatistics().getViewCount();
            publishDate = Utils.formatDate(item.getSnippet().getPublishedAt());
            description = item.getSnippet().getDescription();

            headerModel = new VideoDetailHeaderModel();
            headerModel.setTitle(videoTitle);
            headerModel.setViews(getString(R.string.views, Utils.getCommaSeperatedNumber(views)));
            headerModel.setPublishDate("Published on " + publishDate);
            headerModel.setDescription(description);
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

        adapter = new PlayerYTRecyclerAdapter(this, headerModel, new ArrayList<YTItem>(), this);
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

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
        this.player = player;
        //setControlsEnabled();
        // Specify that we want to handle fullscreen behavior ourselves.
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        player.setPlayerStateChangeListener(this);
        if (!wasRestored) {
            player.cueVideo(videoId);
        }

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player.play();
            }
        }, 5000);*/
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

    /*@Override
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
    }*/

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
        }
    }

    private void showProgressDialog() {
        if (ttProgressDialog == null) {
            ttProgressDialog = new TTProgressDialog(this);
        }

        if (!isFinishing() && !ttProgressDialog.isShowing()) {
            ttProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {

        if (ttProgressDialog != null && ttProgressDialog.isShowing()) {
            ttProgressDialog.dismiss();
            ttProgressDialog = null;
        }
    }

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
        Utils.animateActivity(this, "down");
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

        SearchYTVolleyTask task = new SearchYTVolleyTask(this, this);
        task.execute(nextPageToken, videoTitle);
    }

    @Override
    public void onSuccessYouTubeSearch(YTModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        if ("".equals(nextPageToken)) {
            adapter.setItems(response.getYTItems());
            adapter.notifyDataSetChanged();
        } else {
            adapter.addItems(response.getYTItems());
            adapter.notifyDataSetChanged();
        }
        nextPageToken = response.getNextPageToken();
    }

    @Override
    public void onErrorYouTubeSearch(VolleyError error) {
        error.printStackTrace();
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        Utils.handleError(this, error);
    }

    @Override
    public void onSimilarVideoItemSelected(YTItem videoItem) {
        Intent intent = new Intent(this, PlayerYTActivity.class);
        intent.putExtra(Constants.BUNDLE_VIDEO, videoItem);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(this, "up");
    }

    @Override
    public void onHeadetItemSelected() {

        if (player.isPlaying()) {
            player.pause();
        }

        showProgressDialog();
        if (videoUrl == null) {
            FetchIndYTVolleyTask task = new FetchIndYTVolleyTask(this, this);
            task.execute(videoId);
        } else {
            openSharingIntent();
        }
    }

    @Override
    public void onLoading() {
        MyLog.e("onLoading");
        player.play();
    }

    @Override
    public void onLoaded(String s) {
        MyLog.e("onLoaded");
        player.play();
    }

    @Override
    public void onAdStarted() {
        MyLog.e("onAdStarted");
    }

    @Override
    public void onVideoStarted() {
        MyLog.e("onVideoStarted");
    }

    @Override
    public void onVideoEnded() {
        MyLog.e("onVideoEnded");
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        MyLog.e("errorReason: " + errorReason.name());
    }

    @Override
    public void onIndVideoFetched(IndYTModel response) {
        MyLog.e(response.toString());
        dismissProgressDialog();
        if (response.getItems() != null && response.getItems().size() > 0) {
            videoUrl = response.getItems().get(0).getVideoUrl();
            openSharingIntent();
        }
    }

    @Override
    public void onIndVideoFetchedError(VolleyError error) {
        Utils.handleError(this, error);
        dismissProgressDialog();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void openSharingIntent() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, videoUrl);
        sendIntent.setType("text/plain");
        startActivityForResult(Intent.createChooser(sendIntent, getResources().getText(R.string.title_share)), Constants.REQUEST_SHARE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SHARE) {
            dismissProgressDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.sendTracker(Constants.SCREEN_YOUTUBE);
    }
}
