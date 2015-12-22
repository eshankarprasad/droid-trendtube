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
import org.trendtube.app.activity.SecondActivity;
import org.trendtube.app.activity.TTApplication;
import org.trendtube.app.adapter.DailyMotionRecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.interfaces.NetworkChangeListener;
import org.trendtube.app.model.DailyMotionTrendingVideoModel;
import org.trendtube.app.model.DailyMotionVideoItem;
import org.trendtube.app.receiver.NetworkChangeReceiver;
import org.trendtube.app.ui.TTProgressWheel;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchDailyMotionTrendingVideosVolleyTask;

/**
 * Created by shankar on 9/12/15.
 */

public class DailyMotionTrendingVideosFragment extends Fragment implements DailyMotionRecyclerAdapter.DailyMotionVideoItemSelectedListener,
        FetchDailyMotionTrendingVideosVolleyTask.FetchDailyMotionTrendingVideoListener, NetworkChangeListener {
    private static final String TAB_POSITION = "tab_position";
    private View rootView;
    private int navIndex = -1;
    private RecyclerView recyclerView;
    private DailyMotionRecyclerAdapter adapter;
    private String nextPageToken;
    private TTProgressWheel progressWheel;
    private View footerProgressWheel;
    private TextView txtRemainingVideos;
    private int tabPosition;
    private NetworkChangeReceiver receiver;
    private IntentFilter intentFilter;

    public DailyMotionTrendingVideosFragment() {

    }

    public static DailyMotionTrendingVideosFragment newInstance(int tabPosition) {
        DailyMotionTrendingVideosFragment fragment = new DailyMotionTrendingVideosFragment();
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
            progressWheel = (TTProgressWheel) rootView.findViewById(R.id.progress_bar);
            footerProgressWheel = rootView.findViewById(R.id.layout_footer_progress);
            txtRemainingVideos = (TextView) rootView.findViewById(R.id.txt_remaining_videos);
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
    public void onDailyMotionVideoItemSelected(DailyMotionVideoItem video) {

        Toast.makeText(getActivity(), video.getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), SecondActivity.class);
        intent.putExtra(Constants.BUNDLE_VIDEO, video);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(getActivity(), "next");
    }

    private void loadVideos(DailyMotionTrendingVideoModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        txtRemainingVideos.setText(getString(R.string.label_remaining_videos, response.getTotal()));
        if (adapter == null) {
            adapter = new DailyMotionRecyclerAdapter(getActivity(), response.getList(), this);
            recyclerView.setAdapter(adapter);
        } else if ("".equals(nextPageToken)) {
            adapter.setItems(response.getList());
            adapter.notifyDataSetChanged();
        } else {
            adapter.addItems(response.getList());
            adapter.notifyDataSetChanged();
        }
        nextPageToken = String.valueOf(response.getPage() + 1);
        unregisterReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterReceiver();
    }

    private void loadError(VolleyError error) {
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
    public void onFetchedDailyMotionTrendingVideos(DailyMotionTrendingVideoModel response) {
        loadVideos(response);
    }

    @Override
    public void onFetchedErrorDailyMotionTrendingVideos(VolleyError error) {
        loadError(error);
    }
}
