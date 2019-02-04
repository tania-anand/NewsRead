package api.news.com.newsapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import api.news.com.newsapi.R;
import api.news.com.newsapi.activity.NewSourceTopHeadlines;
import api.news.com.newsapi.activity.NewsDetail;
import api.news.com.newsapi.model.News;

/**
 * Created by taniaanand on 03/02/19.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private ArrayList<News> mArrayList;
    private Context mContext;

    public NewsAdapter(Context context, ArrayList<News> arrayList){
        mArrayList = arrayList;
        mContext = context;
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_listitem,parent,false);
        return new MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.MyViewHolder holder, int position) {

        holder.onBind(mArrayList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, NewsDetail.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("newsLink",mArrayList.get(holder.getAdapterPosition()).getUrlContent());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTittle,txtDate;
        ImageView newsImage;
        MyViewHolder(View itemView) {
            super(itemView);
            txtTittle = itemView.findViewById(R.id.newsTittle);
            txtDate = itemView.findViewById(R.id.newsDate);
            newsImage = itemView.findViewById(R.id.newsImage);
        }

        private void onBind(News news){
            txtTittle.setText(news.getTittle());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date ;
            String str ;
            try {
                date = sdf.parse(news.getDate().substring(0,10));
                str = getFormattedDate(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
                str = getFormattedDate(date.getTime());
            }

            txtDate.setText(str);
            Picasso.with(mContext).load(news.getUrlImage()).into(newsImage);
        }

        private String getFormattedDate( long smsTimeInMilis) {
            Calendar smsTime = Calendar.getInstance();
            smsTime.setTimeInMillis(smsTimeInMilis);
            Calendar now = Calendar.getInstance();

            final String timeFormatString = "h:mm aa";
            final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
            final long HOURS = 60 * 60 * 60;
            if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
                return "Today " + DateFormat.format(timeFormatString, smsTime);
            } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
                return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
            } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
                return DateFormat.format(dateTimeFormatString, smsTime).toString();
            } else {
                return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
            }
        }

    }

}
