package org.trendtube.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import org.trendtube.app.R;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.model.DailyMotionVideoItem;
import org.trendtube.app.model.VimeoVideoItem;
import org.trendtube.app.model.YouTubeVideoItem;
import org.trendtube.app.utils.Utils;

public class SecondActivity extends AppCompatActivity {

    private String videoId;
    private String videoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            Object object = bundle.getSerializable(Constants.BUNDLE_VIDEO);

            if (object instanceof YouTubeVideoItem) {
                YouTubeVideoItem item = (YouTubeVideoItem) object;
                videoId = item.getId();
                videoTitle = item.getSnippet().getTitle();
            } else if (object instanceof DailyMotionVideoItem) {
                DailyMotionVideoItem item = (DailyMotionVideoItem) object;
                videoId = item.getId();
                videoTitle = item.getTitle();
            } else if (object instanceof VimeoVideoItem) {
                VimeoVideoItem item = (VimeoVideoItem) object;
                videoId = item.getId();
                videoTitle = item.getTitle();
            }

            Intent intent = null;
            intent = YouTubeStandalonePlayer.createVideoIntent(this, Constants.DEVELOPER_KEY, videoId, 0, true, true);
            startActivityForResult(intent, 1);

            /*CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(videoTitle);*/
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        Utils.animateActivity(this, "back");
    }
}
