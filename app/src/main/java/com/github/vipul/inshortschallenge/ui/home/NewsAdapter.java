package com.github.vipul.inshortschallenge.ui.home;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipul.inshortschallenge.R;
import com.github.vipul.inshortschallenge.model.News;
import com.github.vipul.inshortschallenge.utils.LocalStoreUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public interface Callbacks {
        public void onNewsClick(@NonNull News news);
        public void onNewsBookmarkClick(@NonNull String newsId, boolean value);
    }

    private Callbacks mCallbacks;
    private Context context;
    private List<News> mFeedList;

    public NewsAdapter(List<News> feedList) {
        this.mFeedList = feedList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        final News news = mFeedList.get(position);

        holder.newsPublisher.setText(news.getPublisher());
        holder.newsCategory.setText(news.getCategory());
        holder.newsHeadline.setText(news.getTitle());
        holder.newsTimeStamp.setText(DateUtils.getRelativeTimeSpanString(news.getTimestamp()));

        holder.newsCategory.getBackground().setColorFilter(ContextCompat.getColor(context, news.getCategoryColor()), PorterDuff.Mode.SRC_ATOP);

        if(LocalStoreUtils.hasInBookmarks(context, news.getId()))
            holder.newsBookmark.setSelected(true);
        else
            holder.newsBookmark.setSelected(false);

        holder.newsBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.newsBookmark.setSelected(!holder.newsBookmark.isSelected());
                if(mCallbacks!=null)
                    mCallbacks.onNewsBookmarkClick(news.getId(), holder.newsBookmark.isSelected());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallbacks!=null)
                    mCallbacks.onNewsClick(news);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_news_publisher)
        AppCompatTextView newsPublisher;

        @BindView(R.id.text_news_category)
        AppCompatTextView newsCategory;

        @BindView(R.id.text_news_headline)
        AppCompatTextView newsHeadline;

        @BindView(R.id.text_news_time)
        AppCompatTextView newsTimeStamp;

        @BindView(R.id.image_news_bookmark)
        AppCompatImageButton newsBookmark;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
