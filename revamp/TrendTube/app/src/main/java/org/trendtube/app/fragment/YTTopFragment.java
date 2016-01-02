package org.trendtube.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.VolleyError;

import org.trendtube.app.R;
import org.trendtube.app.activity.PlayerYTActivity;
import org.trendtube.app.activity.TTActivity;
import org.trendtube.app.activity.TTApplication;
import org.trendtube.app.adapter.YTRecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.interfaces.FetchVideosListener;
import org.trendtube.app.model.YTItem;
import org.trendtube.app.model.YTModel;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchYTTopVolleyTask;

/**
 * Created by shankar on 9/12/15.
 */

public class YTTopFragment extends Fragment
        implements YTRecyclerAdapter.YouTubeVideoItemSelectedListener,
        FetchVideosListener, View.OnClickListener {

    private static final String TAB_POSITION = "tab_position";
    private View rootView;
    private RecyclerView recyclerView;
    private YTRecyclerAdapter adapter;
    private int tabPosition;
    private String nextPageToken;
    private View progressWheel, footerProgressWheel;

    public YTTopFragment() {

    }

    public static YTTopFragment newInstance(int tabPosition) {
        YTTopFragment fragment = new YTTopFragment();
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
        rootView = inflater.inflate(R.layout.fragment_yt_video_list, container, false);
        initView();
        loadVideoContent();
        return rootView;
    }

    private void initView() {
        nextPageToken = "";
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        progressWheel = rootView.findViewById(R.id.progress_bar);
        footerProgressWheel = rootView.findViewById(R.id.footer_progress_bar);

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

        Utils.sendTracker(Constants.SCREEN_YOUTUBE_TOP_LIST);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            TTApplication.currentTabIndex = 0;
        }
    }

    private void loadVideoContent() {
        if ("".equals(nextPageToken)) {
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            footerProgressWheel.setVisibility(View.VISIBLE);
        }
        FetchYTTopVolleyTask task = new FetchYTTopVolleyTask(getActivity(), this);
        task.execute(nextPageToken);
    }

    @Override
    public void onYouTubeVideoSelected(YTItem video) {
        Intent intent = PlayerYTActivity.newIntent(getActivity());
        intent.putExtra(Constants.BUNDLE_VIDEO, video);
        intent.putExtra(Constants.BUNDLE_VIDEO_FLAG, Constants.FLAG_YOUTUBE_SEARCH_VIDEO);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(getActivity(), "next");
    }

    @Override
    public void onVideoFetched(YTModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        if (adapter == null) {
            adapter = new YTRecyclerAdapter(getActivity(), response.getYTItems(), this);
            recyclerView.setAdapter(adapter);
        } else if ("".equals(nextPageToken)) {
            adapter.setItems(response.getYTItems());
            adapter.notifyDataSetChanged();
        } else {
            adapter.addItems(response.getYTItems());
            adapter.notifyDataSetChanged();
        }
        nextPageToken = response.getNextPageToken();
    }

    @Override
    public void onVideoFetchedError(VolleyError error) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        if (Utils.isNetworkError(error) && "".equals(nextPageToken)) {
            Button btnRetry = (Button) rootView.findViewById(R.id.btn_retry);
            btnRetry.setOnClickListener(this);
            btnRetry.setVisibility(View.VISIBLE);
        } else {
            Utils.handleError(getActivity(), error);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_category:
                ((TTActivity) getActivity()).categoryButtonClicked();
                break;
            case R.id.btn_retry:
                loadVideoContent();
                v.setVisibility(View.GONE);
                break;
        }
    }
}
