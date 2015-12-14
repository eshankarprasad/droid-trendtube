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
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.trendtube.app.R;
import org.trendtube.app.activity.SecondActivity;
import org.trendtube.app.adapter.TopViewedVideosRecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.interfaces.FetchVideosListener;
import org.trendtube.app.model.VideoModel;
import org.trendtube.app.ui.TTProgressWheel;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchTopViewedVideosVolleyTask;

/**
 * Created by shankar on 9/12/15.
 */

public class TopViewedVideosFragment extends Fragment implements TopViewedVideosRecyclerAdapter.TopVideoItemSelectListener, FetchVideosListener {
    private static final String TAB_POSITION = "tab_position";
    private RecyclerView recyclerView;
    private TopViewedVideosRecyclerAdapter adapter;
    private String nextPageToken;
    private TTProgressWheel progressWheel, footerProgressWheel;
    private int tabPosition;

    public TopViewedVideosFragment() {

    }

    public static TopViewedVideosFragment newInstance(int tabPosition) {
        TopViewedVideosFragment fragment = new TopViewedVideosFragment();
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

        nextPageToken = "";
        View v = inflater.inflate(R.layout.fragment_topviewed_video_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        progressWheel = (TTProgressWheel) v.findViewById(R.id.progress_bar);
        footerProgressWheel = (TTProgressWheel) v.findViewById(R.id.footer_progress_bar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new EndlessScrollVideosListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                MyLog.e("Current Page: " + currentPage);
                if (nextPageToken == null) {
                    MyLog.e("End of loading");
                } else {
                    loadVideoContent(nextPageToken);
                }
            }
        });
        loadVideoContent(nextPageToken);
        return v;
    }

    private void loadVideoContent(String token) {
        if ("".equals(nextPageToken)) {
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            footerProgressWheel.setVisibility(View.VISIBLE);
        }
        FetchTopViewedVideosVolleyTask task = new FetchTopViewedVideosVolleyTask(getActivity(), this);
        task.execute(token);
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
    public void onVideoFetched(VideoModel response) {

        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        nextPageToken = response.getNextPageToken();
        if (adapter == null) {
            adapter = new TopViewedVideosRecyclerAdapter(getActivity(), response.getVideoItems(), this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.addItems(response.getVideoItems());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onVideoFetchedError(VolleyError error) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        Utils.handleError(getActivity(), error);
    }
}
