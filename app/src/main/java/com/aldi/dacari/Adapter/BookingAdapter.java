package com.aldi.dacari.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aldi.dacari.Model.Booking;
import com.aldi.dacari.Model.Message;
import com.aldi.dacari.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context mContext;
    private List<Booking> mMessage;

    public final int MSG_LEFT = 0;
    public final int MSG_RIGHT = 1;

    public String imageurl;

    FirebaseUser firebaseUser;

    public BookingAdapter(Context mContext, List<Booking> mMessage, String imageurl){
        this.mContext = mContext;
        this.mMessage = mMessage;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_RIGHT ) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_item_right, parent, false);
            return new BookingAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_item_left, parent, false);
            return new BookingAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
        Booking message = mMessage.get(position);
        holder.message_text.setText(message.getBooking());

        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.drawable.all_ic_boy);
        }
        else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile_image;
        public TextView message_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message_text = itemView.findViewById(R.id.msg_text);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mMessage.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_RIGHT;
        }
        else {
            return MSG_LEFT;
        }
    }
}