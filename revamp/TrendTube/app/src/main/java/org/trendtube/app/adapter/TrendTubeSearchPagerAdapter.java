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

    private String[] items;

    public TrendTubeSearchPagerAdapter(String[] items, FragmentManager fm) {
        super(fm);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {

        MyLog.e("Tab Position: " + position);

        switch (position) {
            case 0:
                return YouTubeTrendingVideosFragment.newInstance(position);
            case 1:
                return DailyMotionTrendingVideosFragment.newInstance(position);
            default:
                return VimeoVideosSearchFragment.newInstance(position);
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