package api.news.com.newsapi.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import api.news.com.newsapi.R;
import api.news.com.newsapi.helper.Utils;

public class NewsDetail extends AppCompatActivity {

    private static final String TAG = NewsDetail.class.getSimpleName();

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mNewsLink;
    private LinearLayout mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        // Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // for tittle
        getSupportActionBar().setTitle("News Detail");

        mNewsLink = getIntent().getStringExtra("newsLink");
        Log.d(TAG,"news link "+mNewsLink);
        initView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView(){
        mWebView = findViewById(R.id.webView);
        mProgressBar = findViewById(R.id.progressBar);

        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mEmptyView = findViewById(R.id.emptyView);

        loadWebContent();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utils.checkInternetConnectivity(getApplicationContext())) {
                    loadWebContent();
                    mEmptyView.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                }

                else {
                    Toast.makeText(getApplicationContext(), "Not Connected to internet , try again", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    mEmptyView.setVisibility(View.VISIBLE);
                    mWebView.setVisibility(View.GONE);
                }

            }
        });

    }

    private void loadWebContent(){
        mProgressBar.setVisibility(View.VISIBLE);

        if(Utils.checkInternetConnectivity(getApplicationContext())) {
            mWebView.loadUrl(mNewsLink);
            mEmptyView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }

        else {
            Toast.makeText(getApplicationContext(), "Not Connected to internet , try again", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        }


        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
