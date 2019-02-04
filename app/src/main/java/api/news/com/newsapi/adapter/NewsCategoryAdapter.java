package api.news.com.newsapi.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import api.news.com.newsapi.R;
import api.news.com.newsapi.model.NewsSource;

/**
 * Created by taniaanand on 04/02/19.
 */

public class NewsCategoryAdapter extends RecyclerView.Adapter<NewsCategoryAdapter.MyViewHolder>{

    private ArrayList<String> mCategoryList;
    private Context mContext ;
    private HashMap<String,ArrayList<NewsSource>> mHashMap;

    public NewsCategoryAdapter(Context context ,ArrayList<String> arrayList ,HashMap<String,ArrayList<NewsSource>> hashMap){
        mCategoryList = arrayList;
        mContext = context;
        mHashMap = hashMap;

    }
    @Override
    public NewsCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_wise_listitem,parent,false);
        return new NewsCategoryAdapter.MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(NewsCategoryAdapter.MyViewHolder holder, int position) {

        holder.bindData(mCategoryList.get(position));

    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public void setmCategoryList(ArrayList<String> mCategoryList) {
        this.mCategoryList = mCategoryList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView categoryRv;
        TextView txtCategoryName;
        MyViewHolder(View itemView) {
            super(itemView);
            categoryRv = itemView.findViewById(R.id.categoryRv);
            categoryRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true));
            categoryRv.setItemAnimator(new DefaultItemAnimator());
            txtCategoryName = itemView.findViewById(R.id.txtCategory);
        }


        void bindData(String categoryName){
            txtCategoryName.setText(categoryName.toUpperCase());
            NewsSourceAdapter mAdapter;
            if(mHashMap.containsKey(categoryName)) {
                ArrayList<NewsSource> arrayList = mHashMap.get(categoryName);
                mAdapter = new NewsSourceAdapter(mContext, arrayList);
                categoryRv.setAdapter(mAdapter);
            }
        }
    }



    public void setmHashMap(HashMap<String, ArrayList<NewsSource>> mHashMap) {
        this.mHashMap = mHashMap;
    }
}
