package com.yuddi.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Mauricio on 11/1/2016.
 */
public class NewsLoader extends AsyncTaskLoader<List<Story>> {

    private Context mContext;
    private String mTerms;

    public NewsLoader(Context context, String terms) {
        super(context);
        this.mContext = context;
        this.mTerms = terms;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Story> loadInBackground() {
        if (mTerms.isEmpty()) {
            return null;
        }
        return SearchUtils.fetchSearchData(mContext, mTerms);
    }
}
