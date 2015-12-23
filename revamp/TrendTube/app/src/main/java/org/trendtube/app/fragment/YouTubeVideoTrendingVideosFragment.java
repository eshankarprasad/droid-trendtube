package org.trendtube.app.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.trendtube.app.R;
import org.trendtube.app.activity.FullscreenDemoActivity;
import org.trendtube.app.activity.SecondActivity;
import org.trendtube.app.activity.TTApplication;
import org.trendtube.app.activity.TrendTubeActivity;
import org.trendtube.app.adapter.YouTubeRecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.interfaces.FetchVideosListener;
import org.trendtube.app.interfaces.NetworkChangeListener;
import org.trendtube.app.model.YouTubeVideoItem;
import org.trendtube.app.model.YouTubeVideoModel;
import org.trendtube.app.receiver.NetworkChangeReceiver;
import org.trendtube.app.ui.TTProgressDialog;
import org.trendtube.app.ui.TTProgressWheel;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchYouTubeTrendingVideosVolleyTask;

/**
 * Created by shankar on 9/12/15.
 */

public class YouTubeVideoTrendingVideosFragment extends Fragment
        implements YouTubeRecyclerAdapter.YouTubeVideoItemSelectedListener,
        FetchVideosListener, NetworkChangeListener, View.OnClickListener {
    private static final String TAB_POSITION = "tab_position";
    private View rootView;
    private RecyclerView recyclerView;
    private YouTubeRecyclerAdapter adapter;
    private int tabPosition;
    private String nextPageToken;
    private TTProgressWheel progressWheel;
    private View footerProgressWheel;
    private TextView txtRemainingVideos;
    private TTProgressDialog ttProgressDialog;
    private NetworkChangeReceiver receiver;
    private IntentFilter intentFilter;

    public YouTubeVideoTrendingVideosFragment() {

    }
    public static YouTubeVideoTrendingVideosFragment newInstance(int tabPosition) {
        YouTubeVideoTrendingVideosFragment fragment = new YouTubeVideoTrendingVideosFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyLog.e("onCreateView");
        Bundle args = getArguments();
        tabPosition = args.getInt(TAB_POSITION);
        rootView = inflater.inflate(R.layout.fragment_youtube_video_list, container, false);
        initView();
        loadVideoContent();
        return rootView;
    }

    private void initView() {
        nextPageToken = "";
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        progressWheel = (TTProgressWheel) rootView.findViewById(R.id.progress_bar);
        footerProgressWheel = rootView.findViewById(R.id.layout_footer_progress);
        txtRemainingVideos = (TextView) rootView.findViewById(R.id.txt_remaining_videos);

        View categoryButton = rootView.findViewById(R.id.fab_category);
        categoryButton.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            TTApplication.fragmentIndex = 0;
            //fabCategory.setVisibility(View.VISIBLE);
            if (adapter != null && adapter.getItemCount() == 0) {
                registerReceiver();
            }
        }
    }

    private void showProgressDialog() {
        if (ttProgressDialog == null) {
            ttProgressDialog = new TTProgressDialog(getActivity());
        }

        if (!getActivity().isFinishing() && !ttProgressDialog.isShowing()) {
            ttProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {

        if (ttProgressDialog != null && ttProgressDialog.isShowing()) {
            ttProgressDialog.dismiss();
            ttProgressDialog = null;
        }
    }

    private void registerReceiver() {
        /*if (receiver == null) {
            receiver = new NetworkChangeReceiver(this);
            intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);
            intentFilter.addAction(Constants.INTENT_FILTER_WI_FI_STATE_CHANGE);
        }
        getActivity().registerReceiver(receiver, intentFilter);*/
    }

    private void unregisterReceiver() {
        /*MyLog.e("unregisterReceiver");
        if (receiver != null) {
            try {
                getActivity().unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterReceiver();
    }

    private void loadVideoContent() {
        if ("".equals(nextPageToken)) {
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            footerProgressWheel.setVisibility(View.VISIBLE);
        }
        recyclerView.scrollToPosition(0);
        FetchYouTubeTrendingVideosVolleyTask task = new FetchYouTubeTrendingVideosVolleyTask(getActivity(), this);
        task.execute(nextPageToken);
    }

    @Override
    public void onYouTubeVideoSelected(YouTubeVideoItem video) {

        Toast.makeText(getActivity(), video.getSnippet().getTitle(), Toast.LENGTH_SHORT).show();

        /*Intent intent = new Intent(getActivity(), SecondActivity.class);
        intent.putExtra(Constants.BUNDLE_VIDEO, video);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(getActivity(), "next");*/

        Intent intent = new Intent(getActivity(), FullscreenDemoActivity.class);
        intent.putExtra(Constants.BUNDLE_VIDEO, video);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(getActivity(), "next");
    }

    @Override
    public void onVideoFetched(YouTubeVideoModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        txtRemainingVideos.setText(getString(R.string.label_remaining_videos, response.getPageInfo().getTotalResults()));
        if (adapter == null) {
            adapter = new YouTubeRecyclerAdapter(getActivity(), response.getYouTubeVideoItems(), this);
            recyclerView.setAdapter(adapter);
        } else if ("".equals(nextPageToken)) {
            adapter.setItems(response.getYouTubeVideoItems());
            adapter.notifyDataSetChanged();
        } else {
            adapter.addItems(response.getYouTubeVideoItems());
            adapter.notifyDataSetChanged();
        }
        nextPageToken = response.getNextPageToken();
        //MyLog.e("nextPageToken: " + nextPageToken);
        unregisterReceiver();
    }

    @Override
    public void onVideoFetchedError(VolleyError error) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        Utils.handleError(getActivity(), error);
        registerReceiver();
    }

    @Override
    public void onNetworkConnected() {
        //Toast.makeText(getActivity(), "Network Available Do operations", Toast.LENGTH_SHORT).show();
        loadVideoContent();
    }

    @Override
    public void onNetworkDisconnected() {
        //Toast.makeText(getActivity(), "Network Unavailable Do operations", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_category:
                ((TrendTubeActivity) getActivity()).categoryButtonClicked();
                break;
        }
    }
}
