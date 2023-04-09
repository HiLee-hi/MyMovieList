package com.example.mymovielist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    ArrayList<List> items;
    Context mContext;
    float num;
    String page = "";
    String search = "";

    public class ListViewHolder extends RecyclerView.ViewHolder {
        // itemView 를 저장하는 ListViewHolder 생성
        // findViewById, 각종 event 작업

        TextView title, rating, date;
        ImageView star;
        Button button_menu;

        List item;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView_title);
            rating = itemView.findViewById(R.id.textView_rating);
            date = itemView.findViewById(R.id.textView_date);
            star = itemView.findViewById(R.id.imageView_rating);
            button_menu = itemView.findViewById(R.id.button_menu);

            // item 클릭 이벤트 설정
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(mContext, ReadingActivity.class);
                        intent.putExtra("item", items.get(position)); // 클릭한 list 객체 전달
                        mContext.startActivity(intent);
                    }
                }
            });

            // menu button 클릭 이벤트 설정
            button_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // popup menu 생성
                        PopupMenu popup = new PopupMenu(mContext, view);
                        popup.getMenuInflater().inflate(R.menu.list_menu, popup.getMenu());

                        // popup menu click event 처리
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                if (menuItem.getItemId() == R.id.action_delete) {
                                    Log.d("클릭한 버튼: ", (String) menuItem.getTitle());
                                    ListBaseActivity activity = new ListBaseActivity();
                                    activity.deleteList(items.get(position));
                                    items.remove(position);
                                    notifyDataSetChanged();
                                } else if (menuItem.getItemId() == R.id.action_modify) {
                                    Log.d("클릭한 버튼: ", (String) menuItem.getTitle());
                                    Intent intent = new Intent(mContext, ModifyContentActivity.class);
                                    item = items.get(position);
                                    intent.putExtra("item", item);
                                    intent.putExtra("page", page);
                                    intent.putExtra("search", search);
//                                    intent.putExtra("item", items.get(position));
                                    mContext.startActivity(intent);
                                }
                                return false;
                            }
                        });
                        popup.show();
                    }
                }
            });
        }
    }
    // adapter 생성자 1
    public ListAdapter(ArrayList<List> items) {
        this.items = items;
    }
    // adapter 생성자 2
    public ListAdapter(ArrayList<List> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    public ListAdapter(ArrayList<List> items, Context mContext, String page, String search) {
        this.items = items;
        this.mContext = mContext;
        this.page = page;
        this.search = search;
    }

    // make xml as an object using LayoutInflater & create viewHolder with the object
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }
    // put date of item list into xml widgets
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.rating.setText(items.get(position).getRating());
        holder.date.setText(items.get(position).getDate());

        num = Float.parseFloat(items.get(position).getRating());
        if(num >= 0 && num <= 1) {
            Glide.with(mContext)
                    .load(R.drawable.red_star)
                    .into(holder.star);
        } else if(num > 1 && num < 4) {
            Glide.with(mContext)
                    .load(R.drawable.orange_star)
                    .into(holder.star);
        }
    }
    // return the size of the item list
    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }
}