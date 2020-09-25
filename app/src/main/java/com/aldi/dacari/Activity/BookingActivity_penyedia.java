package com.aldi.dacari.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BookingActivity_penyedia extends AppCompatActivity {

    private ImageView profile_image_, send;
    private EditText booking_text;
    private TextView name;
    private TextView alamat;
    private RecyclerView recyclerView;

    private ImageView callBTN;

    Boolean isClickedDummy;

//    // Time
//    private Button timeP;
//    private TimePickerDialog.OnTimeSetListener timeSetListener;
//
//    // calendar
//    private Button ValueOfButton;
//    private DatePickerDialog.OnDateSetListener mDateSetListener;

    // Booking for Penyedia Jasa
    private  Button ok, tidak, otw, kerja, selesai;

    BookingAdapter bookingAdapter;
    List<Booking> mBooking;

    String userid;
    String tele;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_penyedia);

        profile_image_ = findViewById(R.id.profile_image_chat_booking_penyedia);
        name = findViewById(R.id.name_user_chat_booking_penyedia);

        callBTN = (ImageView) findViewById(R.id.telephon_booking);

        //Text Berjalan

//        alamat = findViewById(R.id.alamat_chat_booking_penyedia);

        TextView alamat = (TextView) this.findViewById(R.id.alamat_chat_booking_penyedia);
        alamat.setSelected(true);


        ImageView back_arrow = findViewById(R.id.back_message_booking_penyedia);

        //Button booking for penyedia jasa
        ok = findViewById(R.id.ButtonOke_penyedia);

        tidak = findViewById(R.id.ButtonTidak_penyedia);

        otw = findViewById(R.id.ButtonOtw_penyedia);

        kerja = findViewById(R.id.ButtonKerja_penyedia);

        selesai = findViewById(R.id.ButtonSelesai_penyedia);

        recyclerView = findViewById(R.id.recycle_chat_booking_penyedia);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent1 = getIntent();
        userid = intent1.getStringExtra("userid");

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.parseColor("#9DA3B3"));
                ok.setBackgroundColor(getResources().getColor(R.color.RelativeLayout));
                tidak.setEnabled(false);
                tidak.setClickable(false);

                sendMessage("Bookingan Kamu di Terima", firebaseUser.getUid(), userid);

            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.parseColor("#e81852"));

                ok.setEnabled(false);
                ok.setClickable(false);

                otw.setEnabled(false);
                otw.setClickable(false);

                kerja.setEnabled(false);
                kerja.setClickable(false);

                selesai.setEnabled(false);
                selesai.setClickable(false);

                sendMessage("Bookingan Kamu di Tolak", firebaseUser.getUid(), userid);
            }
        });

        otw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.parseColor("#9DA3B3"));

                sendMessage("Lagi OTW", firebaseUser.getUid(), userid);
            }
        });

        kerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.parseColor("#9DA3B3"));
                sendMessage("Lagi tahap pengerjaan", firebaseUser.getUid(), userid);
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.parseColor("#9DA3B3"));

                sendMessage("Sudah selesai", firebaseUser.getUid(), userid);
            }
        });

        callBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + tele.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);

            }
        });


//        // CALENDAR
//        ValueOfButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal=Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog dialog = new DatePickerDialog(
//                        BookingActivity_penyedia.this,
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                        mDateSetListener,
//                        year,month,day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//
//        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int day) {
//                month=month+1;
//                //Log.d(TAG, "onDateSet: mm/dd/yyy: "+month+"/"+day+"/"+year);
//                String date = month+"/"+day+"/"+year;
//                booking_text.setText("Tanggal : " + date);
//            }
//        };
//
//        // TIME
//        timeP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Calendar c = Calendar.getInstance();
//                int hour = c.get(Calendar.HOUR_OF_DAY);
//                int min = c.get(Calendar.MINUTE);
//                TimePickerDialog dialog = new TimePickerDialog(BookingActivity_penyedia.this, R.style.TimePickerTheme,
//                                        timeSetListener,
//                                        hour, min, false);
//
//                dialog.show();
//            }
//        });
//
//        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                String currTime = hourOfDay + ":" + minute;
//                booking_text.setText("Jam : " + currTime + " wib");
//            }
//        };


        assert userid != null;
        reference = firebaseDatabase.getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;

                name.setText(user.getUsername());
                alamat.setText("Alamat: "+user.getAddress());

                tele = user.getHandphone();

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

        reference.child("Booking").push().setValue(map);

        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Bookinglist")
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
        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Bookinglist")
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

        reference = firebaseDatabase.getReference().child("Booking");
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

                    bookingAdapter = new BookingAdapter(BookingActivity_penyedia.this, mBooking, imageurl);
                    recyclerView.setAdapter(bookingAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
