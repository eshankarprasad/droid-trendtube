package org.trendtube.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.trendtube.app.R;
import org.trendtube.app.adapter.SuggestionAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.interfaces.FetchSuggestionsListener;
import org.trendtube.app.model.SuggestionModel;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.SuggestorQueryListener;
import org.trendtube.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SearchActivity extends AppCompatActivity implements FetchSuggestionsListener {

    private SuggestionAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    private InputFilter searchInputFilter;
    //private int currentTabIndex = 0;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchInputFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {
                if (source.length() == 0) {
                    return "";
                } else if (!allowedLocalityChar(source, start, end)) {
                    return "";
                }
                return null;
            }

            private boolean allowedLocalityChar(CharSequence source,
                                                int start, int end) {
                String allowedSearchChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,'-.()/\\:\"";
                for (int i = start; i < end; ++i) {
                    if (!allowedSearchChars.contains(source
                            .subSequence(i, i + 1))) {
                        return false;
                    }
                }
                return true;
            }

        };
        //editTextSearch.setFilters(new InputFilter[]{searchInputFilter, new InputFilter.LengthFilter(50)});

        listView = (ListView) findViewById(R.id.listView1);
        List<String> list = new ArrayList<>();
        adapter = new SuggestionAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                onQueryCompleted(adapter.getItem(position));
            }
        });
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SuggestorQueryListener(this, this));
        searchView.setQueryHint(getString(R.string.hint_search));
        searchView.setQuery(TTApplication.query, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        Utils.animateActivity(this, "down");
    }

    private void onOkButtonClick(String locality) {
        hideSoftKeyBoard();
        delay(200);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("key", locality);
        setResult(android.app.Activity.RESULT_OK, returnIntent);
        finishActivityTransition();
    }

    private void finishActivityTransition() {
        this.finish();
        this.overridePendingTransition(0, 0);
    }

    private void hideSoftKeyBoard() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void delay(int time) { //Inserting delay here
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }

    }


    //@Override
    public void onSuggestorCallback(SuggestionModel result) {
        /*mkeywords.clearSuggestedKeywords();
        //if(result.isSuccess()){
        try {
            if ((null != result) && result.getSuggestions().size() > 0) {

                final ListView listview = (ListView) findViewById(R.id.listView1);
                mkeywords.setSuggestedKeywords(result.getSuggestions());
                if (mIsAdapterBuilt) {
                    clearAutoSuggestor();
                    adapter.addData(mkeywords.getSuggestedKeywords());
                } else {
                    mIsAdapterBuilt = true;
                    adapter = new SuggestionAdapter(this, mkeywords.getSuggestedKeywords());
                    listview.setAdapter(adapter);

                }

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {
                        *//*SuggestionModel.Suggestion sug = adapter.getItem(position);
                        onOkButtonClick(sug.getName());*//*
                    }
                });
            } else {
                clearAutoSuggestor();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }*/

    }

    public void clearAutoSuggestor() {
        if (adapter != null) {
            adapter.clearData();
        }
    }

    @Override
    public void onFetchedSuggestor(SuggestionModel result) {
        if ((null != result) && result.getSuggestions().size() > 0) {
            adapter.setmItems(result.getSuggestions());
        } else {
            clearAutoSuggestor();
        }
    }

    @Override
    public void onErrorFetchedSuggestor(VolleyError error) {
        adapter.clearData();
    }

    @Override
    public void onQueryCompleted(String query) {
        MyLog.e("Query: " + query);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setQuery(query, false);
        Utils.hideKeyboard(this);
        if ("".equals(query.trim())) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            intent.putExtra(Constants.BUNDLE_QUERY, query);
            //intent.putExtra(Constants.BUNDLE_FRAGMENT_INDEX, currentTabIndex);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.sendTracker(Constants.SCREEN_SEARCH);
    }
}
