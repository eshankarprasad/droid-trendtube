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
import org.trendtube.app.adapter.YouTubeRecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.interfaces.NetworkChangeListener;
import org.trendtube.app.model.YouTubeVideoItem;
import org.trendtube.app.model.YouTubeVideoModel;
import org.trendtube.app.receiver.NetworkChangeReceiver;
import org.trendtube.app.ui.TTProgressWheel;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.SearchYouTubeVideoVolleyTask;

/**
 * Created by shankar on 9/12/15.
 */

public class YouTubeVideoVideosSearchFragment extends Fragment implements YouTubeRecyclerAdapter.YouTubeVideoItemSelectedListener,
        NetworkChangeListener, SearchYouTubeVideoVolleyTask.SearchYouTubeVideoListener {
    private static final String TAB_POSITION = "tab_position";
    private View rootView;
    private RecyclerView recyclerView;
    private YouTubeRecyclerAdapter adapter;
    private int tabPosition;
    private String nextPageToken;
    private View progressWheel, footerProgressWheel;
    private NetworkChangeReceiver receiver;
    private IntentFilter intentFilter;

    public YouTubeVideoVideosSearchFragment() {

    }

    public static YouTubeVideoVideosSearchFragment newInstance(int tabPosition) {
        YouTubeVideoVideosSearchFragment fragment = new YouTubeVideoVideosSearchFragment();
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
        rootView = inflater.inflate(R.layout.fragment_youtube_search_video_list, null);
        nextPageToken = "";
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        progressWheel = rootView.findViewById(R.id.progress_bar);
        footerProgressWheel = rootView.findViewById(R.id.footer_progress_bar);
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
        loadVideoContent();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            TTApplication.fragmentIndex = 0;
            if (adapter != null && adapter.getItemCount() == 0) {
                registerReceiver();
            }
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
        SearchYouTubeVideoVolleyTask task = new SearchYouTubeVideoVolleyTask(getActivity(), this);
        task.execute(nextPageToken, TTApplication.query);
    }

    @Override
    public void onYouTubeVideoSelected(YouTubeVideoItem video) {

        Toast.makeText(getActivity(), video.getSnippet().getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), SecondActivity.class);
        intent.putExtra(Constants.BUNDLE_VIDEO, video);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(getActivity(), "next");
    }


    @Override
    public void onNetworkConnected() {
        //Toast.makeText(getActivity(), "Network Available Do operations", Toast.LENGTH_SHORT).show();
        //loadVideoContent();
    }

    @Override
    public void onNetworkDisconnected() {
        //Toast.makeText(getActivity(), "Network Unavailable Do operations", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessYouTubeSearch(YouTubeVideoModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
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
        unregisterReceiver();
    }

    @Override
    public void onErrorYouTubeSearch(VolleyError error) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        Utils.handleError(getActivity(), error);
        registerReceiver();
    }
}
