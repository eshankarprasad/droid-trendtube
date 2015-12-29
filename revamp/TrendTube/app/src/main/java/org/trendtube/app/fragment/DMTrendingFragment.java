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
import org.trendtube.app.activity.PlayerDMActivity;
import org.trendtube.app.activity.TTApplication;
import org.trendtube.app.adapter.DMRecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.model.DMModel;
import org.trendtube.app.model.DMItem;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchDailyMotionTrendingVideosVolleyTask;

/**
 * Created by shankar on 9/12/15.
 */

public class DMTrendingFragment extends Fragment implements DMRecyclerAdapter.DailyMotionVideoItemSelectedListener,
        FetchDailyMotionTrendingVideosVolleyTask.FetchDailyMotionTrendingVideoListener, View.OnClickListener {
    private static final String TAB_POSITION = "tab_position";
    private View rootView;
    private int navIndex = -1;
    private RecyclerView recyclerView;
    private DMRecyclerAdapter adapter;
    private String nextPageToken;
    private View progressWheel, footerProgressWheel;
    private int tabPosition;

    public DMTrendingFragment() {

    }

    public static DMTrendingFragment newInstance(int tabPosition) {
        DMTrendingFragment fragment = new DMTrendingFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        tabPosition = args.getInt(TAB_POSITION);
        rootView = inflater.inflate(R.layout.fragment_dm_video_list, null);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //getActivity().findViewById(R.id.fab_category).setVisibility(View.GONE);
            TTApplication.fragmentIndex = 1;
            if (navIndex != TTApplication.navIndex) {
                initViews();
            }
        }
    }

    private void initViews() {
        nextPageToken = "";
        if (recyclerView == null) {
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
            progressWheel = rootView.findViewById(R.id.progress_bar);
            footerProgressWheel = rootView.findViewById(R.id.footer_progress_bar);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addOnScrollListener(new EndlessScrollVideosListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int currentPage) {
                    MyLog.e("Current Page: (DailyMotion) " + currentPage);
                    if (nextPageToken == null) {
                        MyLog.e("End of loading more daily motion video");
                    } else {
                        loadVideoContent();
                    }
                }
            });
        }
        loadVideoContent();
        navIndex = TTApplication.navIndex;
    }

    private void loadVideoContent() {
        if ("".equals(nextPageToken)) {
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            footerProgressWheel.setVisibility(View.VISIBLE);
        }

        FetchDailyMotionTrendingVideosVolleyTask task = new FetchDailyMotionTrendingVideosVolleyTask(getActivity(), this);
        task.execute(nextPageToken);
    }

    @Override
    public void onDailyMotionVideoItemSelected(DMItem video) {
        Intent intent = PlayerDMActivity.newIntent(getActivity());
        intent.putExtra(Constants.BUNDLE_VIDEO, video);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(getActivity(), "next");
    }

    private void loadVideos(DMModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        if (adapter == null) {
            adapter = new DMRecyclerAdapter(getActivity(), response.getList(), this);
            recyclerView.setAdapter(adapter);
        } else if ("".equals(nextPageToken)) {
            adapter.setItems(response.getList());
            adapter.notifyDataSetChanged();
        } else {
            adapter.addItems(response.getList());
            adapter.notifyDataSetChanged();
        }
        nextPageToken = String.valueOf(response.getPage() + 1);
    }

    private void loadError(VolleyError error) {
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
    public void onFetchedDailyMotionTrendingVideos(DMModel response) {
        loadVideos(response);
    }

    @Override
    public void onFetchedErrorDailyMotionTrendingVideos(VolleyError error) {
        loadError(error);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retry:
                loadVideoContent();
                v.setVisibility(View.GONE);
                break;
        }
    }
}
