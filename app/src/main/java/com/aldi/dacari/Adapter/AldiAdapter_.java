package com.aldi.dacari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aldi.dacari.Activity.BookingActivity;
import com.aldi.dacari.Activity.BookingActivity_penyedia;
import com.aldi.dacari.Model.Aldi;
import com.aldi.dacari.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class AldiAdapter_ extends RecyclerView.Adapter<AldiAdapter_.ViewHolder> {

    private Context mContext;
    private List<Aldi> mUsers;
    private boolean isChat;

    public AldiAdapter_(Context mContext, List<Aldi> mUsers, boolean isChat){
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.booking_item_penyedia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Aldi user_ = mUsers.get(position);
        holder.username.setText(user_.getUsername());
        holder.noHp.setText(user_.getHandphone());

        if (user_.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.drawable.all_ic_boy);
        } else {
            Glide.with(mContext).load(user_.getImageURL()).into(holder.profile_image);
        }

        if (isChat){
            if (user_.getStatus().equals("online")){
                holder.on_img.setVisibility(View.VISIBLE);
                holder.off_img.setVisibility(View.GONE);
            }
            else {
                holder.on_img.setVisibility(View.GONE);
                holder.off_img.setVisibility(View.VISIBLE);
            }
        } else {
            holder.on_img.setVisibility(View.GONE);
            holder.off_img.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext , BookingActivity_penyedia.class);
                intent.putExtra("userid" , user_.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_image;
        public TextView username;
        public TextView noHp;
        private ImageView on_img, off_img;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username_browse_booking_penyedia);
            noHp = itemView.findViewById(R.id.hp_browse_booking_penyedia);
            profile_image = itemView.findViewById(R.id.profile_img_booking_penyedia);
            on_img = itemView.findViewById(R.id.on_img_booking_penyedia);
            off_img = itemView.findViewById(R.id.off_img_booking_penyedia);
        }
    }
}
