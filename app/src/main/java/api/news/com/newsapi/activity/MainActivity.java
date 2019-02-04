package api.news.com.newsapi.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import api.news.com.newsapi.adapter.NewsCategoryAdapter;
import api.news.com.newsapi.helper.Config;
import api.news.com.newsapi.helper.Utils;
import api.news.com.newsapi.model.NewsSource;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    // for storing  list of news source against category
    private HashMap<String,ArrayList<NewsSource>> mapCategory;
    // for storing names of different categories
    private ArrayList<String> mCategoryArrayList;
    NewsCategoryAdapter mAdapter;
    private LinearLayout mEmptyView;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for tittle
        getSupportActionBar().setTitle("Home");

        initView();

        if(Utils.checkInternetConnectivity(getApplicationContext())) {
            fetchNewsContent();
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(getApplicationContext(), "Not Connected to internet , try again", Toast.LENGTH_LONG).show();
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private  void initView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mEmptyView = findViewById(R.id.emptyView);


        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utils.checkInternetConnectivity(getApplicationContext())) {
                    fetchNewsContent();
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
        mapCategory = new HashMap<>();
        mCategoryArrayList = new ArrayList<>();
        mAdapter = new NewsCategoryAdapter(MainActivity.this, mCategoryArrayList, mapCategory);
        mRecyclerView.setAdapter(mAdapter);


    }


    private void fetchNewsContent(){
        mapCategory = new HashMap<>();
        mCategoryArrayList = new ArrayList<>();
        mProgressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.URL_RETREIVE_NEWS_SOURCES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG,"response is "+response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    parserResponse(jsonObj);
                } catch (JSONException e) {
                    mProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
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
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("language","en");
                map.put("country","in");

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

                JSONArray jsonArray = object.getJSONArray("sources");

                Log.d(TAG,"Json object size "+jsonArray.length());

                for(int i = 0;i<jsonArray.length();i++){

                    JSONObject element = jsonArray.getJSONObject(i);
                    NewsSource newsSource = new NewsSource();
                    newsSource.setDescription(element.getString("description"));
                    newsSource.setUrlContent(element.getString("url"));
                    newsSource.setId(element.getString("id"));
                    newsSource.setName(element.getString("name"));
                    newsSource.setCategory(element.getString("category"));
                    newsSource.setLanguage(element.getString("language"));

                    if(mapCategory.containsKey(newsSource.getCategory())){
                        ArrayList<NewsSource> tempArrayList = mapCategory.get(newsSource.getCategory());
                        tempArrayList.add(newsSource);
                        mapCategory.put(newsSource.getCategory(),tempArrayList);
                    }else{
                        ArrayList<NewsSource> tempArrayList = new ArrayList<>();
                        tempArrayList.add(newsSource);
                        mapCategory.put(newsSource.getCategory(),tempArrayList);
                    }

                    if(i == jsonArray.length() - 1 ) {
                        mProgressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mCategoryArrayList.addAll(0,mapCategory.keySet());
                        mAdapter.setmHashMap(mapCategory);
                        mAdapter.setmCategoryList(mCategoryArrayList);
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
