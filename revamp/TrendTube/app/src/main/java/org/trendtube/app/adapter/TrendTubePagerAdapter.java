package org.trendtube.app.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.trendtube.app.fragment.TopViewedVideosFragment;
import org.trendtube.app.fragment.TrendingVideosFragment;

/**
 * Created by shankar on 9/12/15.
 */
public class TrendTubePagerAdapter extends FragmentStatePagerAdapter {

    private Activity activity;

    public TrendTubePagerAdapter(Activity activity, FragmentManager fm) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return TrendingVideosFragment.newInstance(position);
            default:
                return TopViewedVideosFragment.newInstance(position);
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    /*@Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                activity.setTitle(activity.getString(R.string.nav_item_trending_videos));
                //return activity.getString(R.string.nav_item_trending_videos);
                break;
            default:
                activity.setTitle(activity.getString(R.string.nav_item_top_viewed_videos));
                //return activity.getString(R.string.nav_item_top_viewed_videos);
                break;
        }

        return "";
    }*/
}