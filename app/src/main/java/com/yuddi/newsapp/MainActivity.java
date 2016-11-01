package com.yuddi.newsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private NewsAdapter mAdapter;

    private Bundle mTerms;

    private ProgressBar mProgressBar;
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mEmptyView = (TextView) findViewById(R.id.empty_view);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setEmptyView(mEmptyView);

        mAdapter = new NewsAdapter(this, new ArrayList<Story>());

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Story currentStory = mAdapter.getItem(position);
                String url = currentStory.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        if (mTerms == null) {
            mTerms = new Bundle();
            mTerms.putString("terms", "");
        }
        getSupportLoaderManager().initLoader(0, mTerms, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mTerms.putString("terms", query);
                    getSupportLoaderManager().restartLoader(0, mTerms, MainActivity.this);
                } else {
                    mAdapter.clear();
                    mEmptyView.setText(R.string.no_internet);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public Loader<List<Story>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, args.getString("terms"));
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> data) {
        mProgressBar.setVisibility(View.GONE);
        if (mTerms.getString("terms").isEmpty()){
            mEmptyView.setText(R.string.type_to_find);
        } else {
            mEmptyView.setText(R.string.no_stories);
        }
        mAdapter.clear();
        if(data != null && !data.isEmpty()){
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
