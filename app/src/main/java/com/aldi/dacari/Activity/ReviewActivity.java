package com.aldi.dacari.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aldi.dacari.Adapter.BookingAdapter;
import com.aldi.dacari.Model.Booking;
import com.aldi.dacari.Model.User;
import com.aldi.dacari.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.SmileRating;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private ImageView profile_image_;
    private Button send;
    private TextView booking_text;
    private TextView rating_text;
    private TextView name;
//    private RecyclerView recyclerView;

    private RatingBar rateStat;
    String answerValue;

    Animation charanim;
    Animation charanim_title;
    Animation text_animation;
    ImageView charplace;

    BookingAdapter bookingAdapter;
    List<Booking> mBooking;
    String userid;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        profile_image_ = findViewById(R.id.profile_image_chat_booking);
        name = findViewById(R.id.name_user_chat_booking);
        ImageView back_arrow = findViewById(R.id.back_message_booking);
        send = findViewById(R.id.send_message_btn_booking);
        booking_text = findViewById(R.id.message_Text_booking);
        rating_text = findViewById(R.id.message_Text_booking_rating);

        rateStat = (RatingBar) findViewById(R.id.rate);

        charplace = findViewById(R.id.icSprite);

        charanim_title = AnimationUtils.loadAnimation(this,R.anim.enter_from_top);
        charanim = AnimationUtils.loadAnimation(this,R.anim.zoom_in_for_review);

        charplace.startAnimation(charanim_title);

        text_animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);

//        recyclerView = findViewById(R.id.recycle_chat_booking);
//        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent1 = getIntent();
        userid = intent1.getStringExtra("userid");

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Rating
        rateStat.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                answerValue = String.valueOf((int)(rateStat.getRating()));

                if (answerValue.equals("1")){
                    charplace.setImageResource(R.drawable.ic_sangatburuk);
                    charplace.startAnimation(charanim);
                    booking_text.startAnimation(text_animation);
                    rating_text.setText("Sangat Buruk");
                    booking_text.setText("1.0");

                }else if (answerValue.equals("2")){
                    rating_text.setText("Buruk");
                    booking_text.setText("2.0");
                    charplace.setImageResource(R.drawable.ic_buruk);
                    charplace.startAnimation(charanim);
                    booking_text.startAnimation(text_animation);

                } else if (answerValue.equals("3")){
                    charplace.setImageResource(R.drawable.ic_lumayan);
                    charplace.startAnimation(charanim);
                    booking_text.startAnimation(text_animation);
                    booking_text.setText("3.0");
                    rating_text.setText("Lumayan");

                }else if (answerValue.equals("4")){
                    charplace.setImageResource(R.drawable.ic_memuaskan);
                    charplace.startAnimation(charanim);
                    booking_text.startAnimation(text_animation);
                    booking_text.setText("4.0");
                    rating_text.setText("Memuaskan");

                }else if (answerValue.equals("5")) {
                    charplace.setImageResource(R.drawable.ic_sangatmemuaskan);
                    charplace.startAnimation(charanim);
                    booking_text.startAnimation(text_animation);
                    rating_text.setText("Sangat Memuaskan");
                    booking_text.setText("5.0");

                }else {
                    Toast.makeText(getApplicationContext(), "kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = booking_text.getText().toString().trim();
                if(!msg.equals("")){
                    sendMessage(msg, firebaseUser.getUid(), userid);
//                    Toast.makeText(ReviewActivity.this, "Terima Kasih Atas Penilaiannya ", Toast.LENGTH_SHORT).show();
                    StyleableToast.makeText(getApplicationContext(), "Terima Kasih Atas Penilainnya", Toast.LENGTH_LONG, R.style.toastyle).show();
                }
//                booking_text.setText("");
            }
        });

        assert userid != null;
        reference = firebaseDatabase.getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                name.setText(user.getUsername());

                if(user.getImageURL().equals("default")){
                    profile_image_.setImageResource(R.drawable.all_ic_boy);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image_);
                }
                readMessages(firebaseUser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendMessage(String booking, String sender, final String receiver) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> map = new HashMap<>();
        map.put("booking", booking);
        map.put("sender", sender);
        map.put("receiver", receiver);

        reference.child("Review").push().setValue(map);

        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Reviewlist")
                .child(firebaseUser.getUid())
                .child(userid);
        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatref.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Yang ke-2
        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Reviewlist")
                .child(userid)
                .child(firebaseUser.getUid());
        chatRefReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRefReceiver.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readMessages(final String myid, final String userid, final String imageurl){
        mBooking = new ArrayList<Booking>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        reference = firebaseDatabase.getReference().child("Review");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mBooking.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Booking booking = snapshot.getValue(Booking.class);
                    assert booking != null;
                    if(booking.getReceiver().equals(myid) && booking.getSender().equals(userid)
                            || booking.getReceiver().equals(userid) && booking.getSender().equals(myid)){
                        mBooking.add(booking);
                    }

                    bookingAdapter = new BookingAdapter(ReviewActivity.this, mBooking, imageurl);
//                    recyclerView.setAdapter(bookingAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
