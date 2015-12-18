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
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.trendtube.app.R;
import org.trendtube.app.activity.SecondActivity;
import org.trendtube.app.activity.TTApplication;
import org.trendtube.app.adapter.DailyMotionRecyclerAdapter_Test;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.interfaces.FragmentListener;
import org.trendtube.app.interfaces.NetworkChangeListener;
import org.trendtube.app.model.BasicItem;
import org.trendtube.app.model.DailyMotionVideoModel;
import org.trendtube.app.receiver.NetworkChangeReceiver;
import org.trendtube.app.ui.TTProgressWheel;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchDailyMotionVideosVolleyTask;

/**
 * Created by shankar on 9/12/15.
 */

public class DailyMotionVideosFragment extends Fragment implements DailyMotionRecyclerAdapter_Test.TopVideoItemSelectListener,
        FetchDailyMotionVideosVolleyTask.FetchDailyMotionVideoListener, FragmentListener, NetworkChangeListener {
    private static final String TAB_POSITION = "tab_position";
    private View rootView;
    private int mTrend = -1;
    private RecyclerView recyclerView;
    private DailyMotionRecyclerAdapter_Test adapter;
    private String nextPageToken;
    private TTProgressWheel progressWheel, footerProgressWheel;
    private int tabPosition;
    private NetworkChangeReceiver receiver;
    private IntentFilter intentFilter;

    public DailyMotionVideosFragment() {

    }

    public static DailyMotionVideosFragment newInstance(int tabPosition) {
        DailyMotionVideosFragment fragment = new DailyMotionVideosFragment();
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
        rootView = inflater.inflate(R.layout.fragment_dailymotion_video_list, null);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            getActivity().findViewById(R.id.fab_category).setVisibility(View.GONE);
            TTApplication.fragmentIndex = 1;

            if (mTrend == TTApplication.navIndex) {

            } else {
                nextPageToken = "";
                recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
                progressWheel = (TTProgressWheel) rootView.findViewById(R.id.progress_bar);
                footerProgressWheel = (TTProgressWheel) rootView.findViewById(R.id.footer_progress_bar);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.addOnScrollListener(new EndlessScrollVideosListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int currentPage) {
                        MyLog.e("Current Page: " + currentPage);
                        if (nextPageToken == null) {
                            MyLog.e("End of loading");
                        } else {
                            loadVideoContent();
                        }
                    }
                });
                rootView.setTag(Constants.TAG_TOPVIEWED_VIDEO);
                loadVideoContent();
                mTrend = TTApplication.navIndex;
            }
        }
    }

    private void loadVideoContent() {
        if ("".equals(nextPageToken)) {
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            footerProgressWheel.setVisibility(View.VISIBLE);
        }
        FetchDailyMotionVideosVolleyTask task = new FetchDailyMotionVideosVolleyTask(getActivity(), this);
        task.execute(nextPageToken);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_REFRESH && resultCode == getActivity().RESULT_OK) {
            nextPageToken = "";
            adapter = null;
            loadVideoContent();
        }
    }

    private void registerReceiver() {
        if (receiver == null) {
            receiver = new NetworkChangeReceiver(this);
            intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);
            intentFilter.addAction(Constants.INTENT_FILTER_WI_FI_STATE_CHANGE);
        }
        getActivity().registerReceiver(receiver, intentFilter);
    }

    private void unregisterReceiver() {
        MyLog.e("unregisterReceiver");
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTopVideoItemSelected(Object videoId) {

        String video = (String) videoId;
        Toast.makeText(getActivity(), video, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), SecondActivity.class);
        intent.putExtra(Constants.BUNDLE_VIDEO_ID, video);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
    }

    @Override
    public void callRegionUpdate(BasicItem selectedRegion) {
        MyLog.e("callRegionUpdate");
        nextPageToken = "";
        adapter = null;
        loadVideoContent();
    }

    @Override
    public void callCategoryUpdate(BasicItem selectedCategory) {
        MyLog.e("callCategoryUpdate");
        nextPageToken = "";
        adapter = null;
        loadVideoContent();
    }

    @Override
    public void onFetchedDailyMotionVideos(DailyMotionVideoModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        nextPageToken = String.valueOf(response.getPage() + 1);
        if (adapter == null) {
            adapter = new DailyMotionRecyclerAdapter_Test(getActivity(), response.getList(), this);
            recyclerView.setAdapter(adapter);
        } else if ("".equals(nextPageToken)) {
            adapter.setItems(response.getList());
            adapter.notifyDataSetChanged();
        } else {
            adapter.addItems(response.getList());
            adapter.notifyDataSetChanged();
        }
        unregisterReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterReceiver();
    }

    @Override
    public void onFetchedErrorDailyMotionVideos(VolleyError error) {
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
}
