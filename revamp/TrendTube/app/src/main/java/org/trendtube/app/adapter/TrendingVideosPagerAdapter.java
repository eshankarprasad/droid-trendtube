package org.trendtube.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.trendtube.app.fragment.DailyMotionTrendingVideosFragment;
import org.trendtube.app.fragment.YouTubeVideoTrendingVideosFragment;
import org.trendtube.app.utils.MyLog;

/**
 * Created by shankar on 9/12/15.
 */
public class TrendingVideosPagerAdapter extends FragmentStatePagerAdapter {

    private String[] items;

    public TrendingVideosPagerAdapter(String[] items, FragmentManager fm) {
        super(fm);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {

        MyLog.e("Tab Position: " + position);

        switch (position) {
            case 0:
                return YouTubeVideoTrendingVideosFragment.newInstance(position);
            default:
                return DailyMotionTrendingVideosFragment.newInstance(position);
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