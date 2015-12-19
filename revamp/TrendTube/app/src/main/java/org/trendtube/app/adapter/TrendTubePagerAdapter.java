package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.trendtube.app.fragment.DailyMotionVideosFragment;
import org.trendtube.app.fragment.VimeoVideosFragment;
import org.trendtube.app.fragment.YouTubeVideosFragment;
import org.trendtube.app.utils.MyLog;

/**
 * Created by shankar on 9/12/15.
 */
public class TrendTubePagerAdapter extends FragmentStatePagerAdapter {

    private Activity activity;
    private String[] items;
    private YouTubeVideosFragment youTubeVideosFragment;
    private DailyMotionVideosFragment dailyMotionVideosFragment;
    private VimeoVideosFragment vimeoVideosFragment;

    public TrendTubePagerAdapter(Activity activity, String[] items, FragmentManager fm) {
        super(fm);
        this.activity = activity;
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {

        MyLog.e("Tab Position: " + position);

        switch (position) {
            case 0:
                youTubeVideosFragment = YouTubeVideosFragment.newInstance(position);
                return youTubeVideosFragment;
            case 1:
                dailyMotionVideosFragment = DailyMotionVideosFragment.newInstance(position);
                return dailyMotionVideosFragment;
            default:
                vimeoVideosFragment = VimeoVideosFragment.newInstance(position);
                return vimeoVideosFragment;
        }
    }

    public Fragment getCurrentFragment(int position) {
        switch (position) {
            case 0:
                return youTubeVideosFragment;
            case 1:
                return dailyMotionVideosFragment;
            default:
                return vimeoVideosFragment;
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