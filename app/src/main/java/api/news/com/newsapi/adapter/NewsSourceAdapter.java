package api.news.com.newsapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import api.news.com.newsapi.R;
import api.news.com.newsapi.activity.NewSourceTopHeadlines;
import api.news.com.newsapi.model.NewsSource;

/**
 * Created by taniaanand on 04/02/19.
 */

public class NewsSourceAdapter extends RecyclerView.Adapter<NewsSourceAdapter.MyViewHolder> {

    private ArrayList<NewsSource> mArrayList;
    private Context mContext;
    private int[] colors;


    NewsSourceAdapter(Context context, ArrayList<NewsSource> arrayList){
        mArrayList = arrayList;
        mContext = context;
        colors = mContext.getResources().getIntArray(R.array.randomColors);


    }
    @Override
    public NewsSourceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_source_listitem,parent,false);
        return new NewsSourceAdapter.MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(final NewsSourceAdapter.MyViewHolder holder, int position) {

        if(mArrayList.size() == 1){
            holder.cardView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        holder.onBind(mArrayList.get(position),position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, NewSourceTopHeadlines.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("newsSource",mArrayList.get(holder.getAdapterPosition()).getId());
                i.putExtra("tittle",mArrayList.get(holder.getAdapterPosition()).getName());
                mContext.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTittle,txtContent,txtCategory;
        CardView cardView;
        MyViewHolder(View itemView) {
            super(itemView);
            txtTittle = itemView.findViewById(R.id.newsTittle);
            txtContent = itemView.findViewById(R.id.newsContent);
            txtCategory = itemView.findViewById(R.id.newCategory);
            cardView = itemView.findViewById(R.id.cardView);

        }


        void onBind(NewsSource object ,int position){
            txtTittle.setText(object.getName());

            int colorIndex = position % colors.length;
            int indexColor = colors[colorIndex];

            txtTittle.setBackgroundColor(indexColor);

            if(object.getDescription().length()>50)
                txtContent.setText(object.getDescription().substring(0,50)+" ...");
            else
                txtContent.setText(object.getDescription());

            txtCategory.setText(object.getCategory().toUpperCase());

        }
    }
}
