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
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.dailymotion.websdk.DMWebVideoView;

import org.trendtube.app.R;
import org.trendtube.app.adapter.PlayerDMRecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.model.DMItem;
import org.trendtube.app.model.DMModel;
import org.trendtube.app.model.VideoDetailHeaderModel;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.SearchDailyMotionVideoVolleyTask;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p/>
 * This is the preferred way of handling fullscreen because the default fullscreen implementation
 * will cause re-buffering of the video.
 */
public class PlayerDMActivity extends AppCompatActivity implements
        View.OnClickListener, PlayerDMRecyclerAdapter.SimilarVideoItemSelectedListener, SearchDailyMotionVideoVolleyTask.SearchDailyMotionVideoListener {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    private String videoId, videoTitle, views, publishDate, description;

    private RecyclerView recyclerView;
    private PlayerDMRecyclerAdapter adapter;
    private String nextPageToken;
    private View progressWheel, footerProgressWheel;//, layoutHiddenSummary;
    private VideoDetailHeaderModel headerModel;
    private DMWebVideoView mVideoView;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, PlayerDMActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_dm_activity);

        if (Build.VERSION.SDK_INT < 19) { //19 or above api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for lower api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }

        mVideoView = ((DMWebVideoView) findViewById(R.id.dmWebVideoView));
        mVideoView.getLayoutParams().height = Utils.getProportionalHeight(this);
        mVideoView.setAutoPlay(true);
        mVideoView.setAllowAutomaticNativeFullscreen(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            DMItem item = (DMItem) bundle.getSerializable(Constants.BUNDLE_VIDEO);
            videoId = item.getId();
            videoTitle = item.getTitle();
            views = String.valueOf(item.getViewsTotal());
            publishDate = Utils.formatDate(item.getCreatedTime());
            description = item.getDescription();

            mVideoView.setVideoId(videoId);

            headerModel = new VideoDetailHeaderModel();
            headerModel.setTitle(videoTitle);
            headerModel.setViews(getString(R.string.views, Utils.getCommaSeperatedNumber(views)));
            headerModel.setPublishDate("Published on " + publishDate);
            headerModel.setDescription(description);
        }

        nextPageToken = "";
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressWheel = findViewById(R.id.progress_bar);
        footerProgressWheel = findViewById(R.id.footer_progress_bar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PlayerDMRecyclerAdapter(this, headerModel, new ArrayList<DMItem>(), this);
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
    public void onBackPressed() {
        mVideoView.handleBackPress(this);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        mVideoView.destroy();
        super.onDestroy();
    }

    private void loadVideoContent() {
        if ("".equals(nextPageToken)) {
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            footerProgressWheel.setVisibility(View.VISIBLE);
        }

        SearchDailyMotionVideoVolleyTask task = new SearchDailyMotionVideoVolleyTask(this, this);
        task.execute(nextPageToken, videoTitle);
    }

    @Override
    public void onSimilarVideoItemSelected(DMItem videoItem) {
        Intent intent = new Intent(this, PlayerDMActivity.class);
        intent.putExtra(Constants.BUNDLE_VIDEO, videoItem);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(this, "up");
    }

    @Override
    public void onHeadetItemSelected() {
        MyLog.e("onHeadetItemSelected");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccessDailyMotionSearch(DMModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        /*if (adapter == null) {
            adapter = new PlayerYTRecyclerAdapter(this, headerModel, response.getYTItems(), this);
            recyclerView.setAdapter(adapter);
        } else */
        if ("".equals(nextPageToken)) {
            adapter.setItems(response.getList());
            adapter.notifyDataSetChanged();
        } else {
            adapter.addItems(response.getList());
            adapter.notifyDataSetChanged();
        }
        nextPageToken = String.valueOf(response.getPage() + 1);
    }

    @Override
    public void onErrorDailyMotionSearch(VolleyError error) {
        error.printStackTrace();
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        Utils.handleError(this, error);
    }
}
