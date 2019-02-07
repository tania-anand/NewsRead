package api.news.com.newsapi.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import api.news.com.newsapi.R;
import api.news.com.newsapi.adapter.NewsAdapter;
import api.news.com.newsapi.helper.Config;
import api.news.com.newsapi.helper.Utils;
import api.news.com.newsapi.model.News;

public class NewSourceTopHeadlines extends AppCompatActivity {

    private static final String TAG = NewSourceTopHeadlines.class.getSimpleName();
    private ProgressBar mProgressBar;
    private ArrayList<News> mArrayList;
    private NewsAdapter mAdapter;
    private String mNewsSource;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mEmptyView;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_source_top_headlines);

        String mCategoryTittle = getIntent().getStringExtra("tittle");
        mNewsSource = getIntent().getStringExtra("newsSource");

        // Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // for tittle
        getSupportActionBar().setTitle(mCategoryTittle);


        initViews();
        if(Utils.checkInternetConnectivity(getApplicationContext())) {
            fetchNewsSourceTopHeadlines();
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(getApplicationContext(), "Not Connected to internet , try again", Toast.LENGTH_LONG).show();
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
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


    private void initViews(){
        mRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);

        mEmptyView = findViewById(R.id.emptyView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utils.checkInternetConnectivity(getApplicationContext())) {
                    fetchNewsSourceTopHeadlines();
                    mEmptyView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not Connected to internet , try again", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    mEmptyView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        mProgressBar = findViewById(R.id.progressBar);
        mArrayList = new ArrayList<>();
        mAdapter = new NewsAdapter(NewSourceTopHeadlines.this,mArrayList);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void fetchNewsSourceTopHeadlines(){

        String URL = "https://newsapi.org/v2/top-headlines?sourcesgit status ="+mNewsSource+"&apiKey=c20fa497f8ca40338d33aa8bbbae1bbe";

        mProgressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG,"reponse is "+response);

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    parserResponse(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"error is  "+error);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("Content-Language", "en-US");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("content-type", "application/json;  charset=utf-8");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                HashMap<String, String> map = new HashMap<>();
                map.put("source",mNewsSource);

                return map;

            }


        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void parserResponse(JSONObject object){

        try {
            String status = object.getString("status");

            if(status.equals("ok")){

                JSONArray jsonArray = object.getJSONArray("articles");

                Log.d(TAG,"Json object size "+jsonArray.length());

                for(int i = 0;i<jsonArray.length();i++){

                    JSONObject element = jsonArray.getJSONObject(i);
                    News news = new News();

                    news.setTittle(element.getString("title"));
                    news.setDescription(element.getString("description"));
                    news.setUrlContent(element.getString("url"));
                    news.setUrlImage(element.getString("urlToImage"));
                    news.setDate(element.getString("publishedAt"));
                    news.setId(element.getJSONObject("source").getString("id"));
                    news.setName(element.getJSONObject("source").getString("name"));

                    mArrayList.add(news);

                    if(i == jsonArray.length() -1 ) {
                        mProgressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                    }

                }

                if(jsonArray.length() == 0 ){
                    mProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            mProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
