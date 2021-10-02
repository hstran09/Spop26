package com.hstran09.spop26.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hstran09.spop26.R;
import com.hstran09.spop26.common.image.CircleTransform;
import com.hstran09.spop26.model.SearchResponse;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.Setter;

public class SearchResponseResultAdapter extends RecyclerView.Adapter {

    private Context context;
    private final List<SearchResponse.Result> results;

    private static ItemOnClick itemOnClick;

    public SearchResponseResultAdapter(Context context, List<SearchResponse.Result> results) {
        this.context = context;
        this.results = results;
    }

    public void setItemOnClick(ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_search_response, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SearchResponse.Result result = results.get(position);

        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.title.setText(result.getTitleOriginal());
        viewHolder.podcastTitle.setText(result.getPodcast().getTitleOriginal());
        viewHolder.publisher.setText(result.getPodcast().getPublisherOriginal());

        Picasso.get()
                .load(result.getThumbnail())
                .centerCrop()
                .transform(new CircleTransform(10, 0))
                .fit()
                .into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return Objects.isNull(results)
                ? 0
                : results.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View view;
        private final TextView title;
        private final TextView podcastTitle;
        private final TextView by;
        private final TextView publisher;
        private final ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.title = view.findViewById(R.id.item_search_response_title);
            this.podcastTitle = view.findViewById(R.id.item_search_response_podcast_title);
            this.by = view.findViewById(R.id.item_search_response_by);
            this.publisher = view.findViewById(R.id.item_search_response_publisher);
            this.thumbnail = view.findViewById(R.id.item_search_response_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemOnClick.onClick(v, getAdapterPosition());
        }
    }

    public interface ItemOnClick {
        void onClick(View view, int position);
    }
}
