package com.example.mymovielist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import java.util.List;

public class LoginAdapter extends RecyclerView.Adapter<LoginAdapter.LoginViewHolder> {
   ArrayList<Login> items;
   Context mContext;
   Login item;

   public class LoginViewHolder extends RecyclerView.ViewHolder {
       TextView id;
       ImageView image;
       Button button_menu;

       public LoginViewHolder(@NonNull View itemView) {
           super(itemView);

           id = itemView.findViewById(R.id.textView);
           image = itemView.findViewById(R.id.imageView);
           button_menu = itemView.findViewById(R.id.button_menu);

           // item 클릭 이벤트 설정
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   int position = getAdapterPosition();
                   if(position != RecyclerView.NO_POSITION) {
                       // 최근 선택 받은 노트가 제일 위로 가도록 변경
                       LoginBaseActivity activity = new LoginBaseActivity();
                       activity.changeLogin(items.get(position));

                       Intent intent = new Intent(mContext, ListActivity.class);
                       intent.putExtra("item", items.get(position)); // 클릭한 login 객체 전달
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
                       popup.getMenuInflater().inflate(R.menu.login_menu, popup.getMenu());

                       // popup menu click event 처리
                       popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                           @Override
                           public boolean onMenuItemClick(MenuItem menuItem) {
                               if (menuItem.getItemId() == R.id.action_delete) {
                                   Log.d("클릭한 버튼: ", (String) menuItem.getTitle());

                                   item = items.get(position);
                                   ListBaseActivity listBaseActivity = new ListBaseActivity();
                                   listBaseActivity.deleteAll(item);

                                   LoginBaseActivity activity = new LoginBaseActivity();
                                   activity.deleteLogin(items.get(position));
                                   items.remove(position);
                                   notifyDataSetChanged();
                               } else if (menuItem.getItemId() == R.id.action_modify) {
                                   Log.d("클릭한 버튼: ", (String) menuItem.getTitle());
                                   Intent intent = new Intent(mContext, ModifyActivity.class);
                                   intent.putExtra("item", items.get(position));
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
    // adapter constructor
    public LoginAdapter(ArrayList<Login> items) {
        this.items = items;
    }
    public LoginAdapter(ArrayList<Login> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    // make xml as an object using LayoutInflater & create viewHolder with the object
    @NonNull
    @Override
    public LoginViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_item, parent, false);
        return new LoginViewHolder(view);
    }
    // put date of item list into xml widgets
    @Override
    public void onBindViewHolder(@NonNull LoginViewHolder holder, int position) {
        String profile = items.get(position).getImage();
        // String to Bitmap
//        byte[] byteArray = Base64.decode(profile, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

//        holder.image.setImageBitmap(bitmap);
        Glide.with(mContext)
                .load(profile)
                .into(holder.image);
        holder.id.setText(items.get(position).getId());
    }
    // return the size of the item list
    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }
}
