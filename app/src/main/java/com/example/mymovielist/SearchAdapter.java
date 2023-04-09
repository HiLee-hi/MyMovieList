package com.example.mymovielist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{
    ArrayList<Search> items = new ArrayList<>();
    Context mContext;

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.search_item, parent, false);
        SearchAdapter.SearchViewHolder viewHolder = new SearchAdapter.SearchViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Search 객체들을 items 에 저장
    public void setSearchList(ArrayList<Search> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView textView_title, textView_pubDate;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_pubDate = itemView.findViewById(R.id.textView_pubDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(mContext, MovieContentActivity.class);
                        intent.putExtra("item", items.get(position)); // 클릭한 list 객체 전달
                        mContext.startActivity(intent);
                    }
                }
            });
        }
        void onBind(Search search) {
            textView_title.setText(search.getTitle());
            textView_pubDate.setText(search.getPubDate());
        }
    }
}
