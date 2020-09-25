package com.aldi.dacari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aldi.dacari.Activity.BookingActivity;
import com.aldi.dacari.Activity.ReviewActivity;
import com.aldi.dacari.Model.Aldi;
import com.aldi.dacari.Model.Booking;
import com.aldi.dacari.Model.Message;
import com.aldi.dacari.Model.Review;
import com.aldi.dacari.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context mContext;
    private List<Aldi> mUsers;
    private boolean isChat;

    String theLastMessage;
    private ImageView review_image;

    public ReviewAdapter(Context mContext, List<Aldi> mUsers, boolean isChat){
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Aldi user = mUsers.get(position);
        holder.username.setText(user.getUsername());

        if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.drawable.all_ic_boy);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }

        if (isChat){
            lastMessage(user.getId(), holder.last_msg);
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_image;
        private ImageView review_image;

        public TextView username;

        //
        public TextView last_msg;
        private ImageView on_img, off_img;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username_browse_booking);

            //
            last_msg = itemView.findViewById(R.id.review_browse_booking);
            profile_image = itemView.findViewById(R.id.profile_img_booking);
            review_image = itemView.findViewById(R.id.image_review);
            on_img = itemView.findViewById(R.id.on_img_booking);
            off_img = itemView.findViewById(R.id.off_img_booking);
        }
    }


    // periksa pesan terakhir
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Review");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Review review = snapshot.getValue(Review.class);
                    if (firebaseUser != null && review != null) {
                        if (review.getReceiver().equals(firebaseUser.getUid()) && review.getSender().equals(userid) ||
                                review.getReceiver().equals(userid) && review.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = review.getBooking();
                        }
                    }

//                    if (review.getBooking().equals("Lumayan")){
//                        review_image.setImageResource(R.drawable.anggry);
//                    }
                }


                switch (theLastMessage){
                    case  "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
