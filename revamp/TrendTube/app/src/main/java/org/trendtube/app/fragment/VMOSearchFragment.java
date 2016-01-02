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
import org.trendtube.app.activity.PlayerVMOActivity;
import org.trendtube.app.activity.TTApplication;
import org.trendtube.app.adapter.VMORecyclerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.model.VMOItem;
import org.trendtube.app.model.VMOModel;
import org.trendtube.app.utils.EndlessScrollVideosListener;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.SearchVMOVolleyTask;

/**
 * Created by shankar on 9/12/15.
 */

public class VMOSearchFragment extends Fragment implements VMORecyclerAdapter.VimeoVideoItemSelectedListener,
        SearchVMOVolleyTask.SearchVimeoVideoListener, View.OnClickListener {
    private static final String TAB_POSITION = "tab_position";
    private View rootView;
    private RecyclerView recyclerView;
    private VMORecyclerAdapter adapter;
    private String nextPageToken;
    private View progressWheel, footerProgressWheel;
    private int tabPosition;
    private String searchQuery = "";

    public VMOSearchFragment() {

    }

    public static VMOSearchFragment newInstance(int tabPosition) {
        VMOSearchFragment fragment = new VMOSearchFragment();
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
        rootView = inflater.inflate(R.layout.fragment_vmo_video_list, null);
        if (!searchQuery.equals(TTApplication.query)) {
            searchQuery = TTApplication.query;
            initViews();
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            TTApplication.currentTabIndex = 2;
            if (!searchQuery.equals(TTApplication.query) && rootView != null) {
                searchQuery = TTApplication.query;
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
                    MyLog.e("Current Page: (Vimeo) " + currentPage);
                    if (nextPageToken == null) {
                        MyLog.e("End of loading");
                    } else {
                        loadVideoContent();
                    }
                }
            });
        }
        loadVideoContent();
        searchQuery = TTApplication.query;
        Utils.sendTracker(Constants.SCREEN_VIMEO_SEARCH_LIST);
    }

    private void loadVideoContent() {
        if ("".equals(nextPageToken)) {
            progressWheel.setVisibility(View.VISIBLE);
        } else {
            footerProgressWheel.setVisibility(View.VISIBLE);
        }
        SearchVMOVolleyTask task = new SearchVMOVolleyTask(getActivity(), this);
        task.execute(nextPageToken, searchQuery);
    }

    @Override
    public void onVimeoVideoItemSelected(VMOItem video) {
        Intent intent = PlayerVMOActivity.newIntent(getActivity());
        intent.putExtra(Constants.BUNDLE_VIDEO, video);
        startActivityForResult(intent, Constants.REQUEST_VIDEO_DETAIL);
        Utils.animateActivity(getActivity(), "next");
    }

    @Override
    public void onSuccessVimeoSearch(VMOModel response) {
        progressWheel.setVisibility(View.GONE);
        footerProgressWheel.setVisibility(View.GONE);
        //txtRemainingVideos.setText(getString(R.string.label_remaining_videos, response.getTotal()));

        if (adapter == null) {
            adapter = new VMORecyclerAdapter(getActivity(), response.getList(), this);
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

    @Override
    public void onErrorVimeoSearch(VolleyError error) {
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
            case R.id.btn_retry:
                loadVideoContent();
                v.setVisibility(View.GONE);
                break;
        }
    }
}
