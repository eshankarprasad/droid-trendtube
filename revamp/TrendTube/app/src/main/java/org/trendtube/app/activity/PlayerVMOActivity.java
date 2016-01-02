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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.VolleyError;

import org.trendtube.app.R;
import org.trendtube.app.adapter.PlayerVMORecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.model.IndVMOModel;
import org.trendtube.app.model.VMOItem;
import org.trendtube.app.model.VideoDetailHeaderModel;
import org.trendtube.app.model.VMOModel;
import org.trendtube.app.ui.TTProgressDialog;
import org.trendtube.app.ui.VideoEnabledWebChromeClient;
import org.trendtube.app.ui.VideoEnabledWebView;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchIndVMOVolleyTask;
import org.trendtube.app.volleytasks.SearchVMOVolleyTask;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p/>
 * This is the preferred way of handling fullscreen because the default fullscreen implementation
 * will cause re-buffering of the video.
 */
public class PlayerVMOActivity extends AppCompatActivity implements
        View.OnClickListener, PlayerVMORecyclerAdapter.SimilarVideoItemSelectedListener,
        SearchVMOVolleyTask.SearchVimeoVideoListener, FetchIndVMOVolleyTask.FetchIndVideosListener {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    private String videoId, videoTitle, views, publishDate, description;

    private RecyclerView recyclerView;
    private PlayerVMORecyclerAdapter adapter;
    private String nextPageToken;
    private View progressWheel, footerProgressWheel;//, layoutHiddenSummary;
    private VideoDetailHeaderModel headerModel;
    private VideoEnabledWebView mWebview;
    private VideoEnabledWebChromeClient webChromeClient;
    private TTProgressDialog ttProgressDialog;
    private String videoUrl;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, PlayerVMOActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_vmo_activity);

        if (Build.VERSION.SDK_INT < 19) { //19 or above api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for lower api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }

        mWebview = (VideoEnabledWebView) findViewById(R.id.webView);
        mWebview.getLayoutParams().height = Utils.getProportionalHeight(this);
        mWebview.setWebViewClient(new WebViewClient());
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
        View loadingView = getLayoutInflater().inflate(R.layout.tt_progessbar_dailog, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, mWebview) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        mWebview.setWebChromeClient(webChromeClient);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            VMOItem item = (VMOItem) bundle.getSerializable(Constants.BUNDLE_VIDEO);
            videoId = item.getId();
            videoTitle = item.getTitle();
            views = String.valueOf(item.getViewsCount());
            publishDate = Utils.formatDate(item.getCreatedTime());
            description = item.getDescription();

            headerModel = new VideoDetailHeaderModel();
            headerModel.setTitle(videoTitle);
            headerModel.setViews(getString(R.string.views, Utils.getCommaSeperatedNumber(views)));
            headerModel.setPublishDate("Published on " + publishDate);
            headerModel.setDescription(description);
        }

        mWebview.loadUrl("http://player.vimeo.com/video/"+videoId+"?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800");

        nextPageToken = "";
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressWheel = findViewById(R.id.progress_bar);
        footerProgressWheel = findViewById(R.id.footer_progress_bar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PlayerVMORecyclerAdapter(this, headerModel, new ArrayList<VMOItem>(), this);
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
        if (mWebview.canGoBack()) {
            mWebview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebview.onResume();
        Utils.sendTracker(Constants.SCREEN_VIMEO);
    }

    @Override
    protected void onDestroy() {
        mWebview.destroy();
        super.onDestroy();
    }

    private void loadVideoContent() {
        if ("".equals(nextPageToken)) {
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            footerProgressWheel.setVisibility(View.VISIBLE);
        }

        SearchVMOVolleyTask task = new SearchVMOVolleyTask(this, this);
        task.execute(nextPageToken, videoTitle);
    }

    @Override
    public void onSimilarVideoItemSelected(VMOItem videoItem) {
        Intent intent = new Intent(this, PlayerVMOActivity.class);
        intent.putExtra(Constants.BUNDLE_VIDEO, videoItem);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(this, "up");
    }

    @Override
    public void onHeadetItemSelected() {

        showProgressDialog();
        mWebview.onPause();
        if (videoUrl == null) {
            FetchIndVMOVolleyTask task = new FetchIndVMOVolleyTask(this, this);
            task.execute(videoId);
        } else {
            openSharingIntent();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {

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
    public void onSuccessVimeoSearch(VMOModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
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
    public void onErrorVimeoSearch(VolleyError error) {
        error.printStackTrace();
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        Utils.handleError(this, error);
    }

    @Override
    public void onIndVideoFetched(IndVMOModel response) {
        MyLog.e(response.toString());
        videoUrl = response.getVideoUrl();
        openSharingIntent();
    }

    @Override
    public void onIndVideoFetchedError(VolleyError error) {
        dismissProgressDialog();
        Utils.handleError(this, error);
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
            mWebview.onResume();
        }
    }
}
