package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.trendtube.app.fragment.DailyMotionTrendingVideosFragment;
import org.trendtube.app.fragment.VimeoVideosSearchFragment;
import org.trendtube.app.fragment.YouTubeTrendingVideosFragment;
import org.trendtube.app.utils.MyLog;

/**
 * Created by shankar on 9/12/15.
 */
public class TrendTubeSearchPagerAdapter extends FragmentStatePagerAdapter {

    private Activity activity;
    private String[] items;
    private YouTubeTrendingVideosFragment youTubeTrendingVideosFragment;
    private DailyMotionTrendingVideosFragment dailyMotionTrendingVideosFragment;
    private VimeoVideosSearchFragment vimeoVideosSearchFragment;

    public TrendTubeSearchPagerAdapter(Activity activity, String[] items, FragmentManager fm) {
        super(fm);
        this.activity = activity;
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {

        MyLog.e("Tab Position: " + position);

        switch (position) {
            case 0:
                youTubeTrendingVideosFragment = YouTubeTrendingVideosFragment.newInstance(position);
                return youTubeTrendingVideosFragment;
            case 1:
                dailyMotionTrendingVideosFragment = DailyMotionTrendingVideosFragment.newInstance(position);
                return dailyMotionTrendingVideosFragment;
            default:
                vimeoVideosSearchFragment = VimeoVideosSearchFragment.newInstance(position);
                return vimeoVideosSearchFragment;
        }
    }

    public Fragment getCurrentFragment(int position) {
        switch (position) {
            case 0:
                return youTubeTrendingVideosFragment;
            case 1:
                return dailyMotionTrendingVideosFragment;
            default:
                return vimeoVideosSearchFragment;
        }
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items[position];
    }

    public void setItems(String[] items) {
        this.items = items;
        notifyDataSetChanged();
    }
}