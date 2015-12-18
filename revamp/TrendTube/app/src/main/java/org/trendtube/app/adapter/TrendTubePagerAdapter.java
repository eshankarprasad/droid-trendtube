package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.trendtube.app.fragment.DailyMotionVideosFragment;
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
            default:
                dailyMotionVideosFragment = DailyMotionVideosFragment.newInstance(position);
                return dailyMotionVideosFragment;
        }
    }

    public Fragment getCurrentFragment(int position) {
        switch (position) {
            case 0:
                return youTubeVideosFragment;
            default:
                return dailyMotionVideosFragment;
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
}