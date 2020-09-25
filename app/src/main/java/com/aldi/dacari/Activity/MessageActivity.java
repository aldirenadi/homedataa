package com.aldi.dacari.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aldi.dacari.notifications.APIService;
import com.aldi.dacari.notifications.Client;
import com.aldi.dacari.notifications.Data;
import com.aldi.dacari.notifications.Sender;
import com.aldi.dacari.notifications.Token;
import com.bumptech.glide.Glide;
import com.aldi.dacari.Adapter.MessageAdapter;
import com.aldi.dacari.Model.Message;
import com.aldi.dacari.Model.User;
import com.aldi.dacari.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    private ImageView profile_image_, send;
    private EditText message_text;
    private TextView name;
    private RecyclerView recyclerView;

    private ImageButton attachBtn;

    private TextView userStatusTv;

    MessageAdapter messageAdapter;
    List<Message> mMessage;
    String userid;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;

    //
    APIService apiService;
    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profile_image_ = findViewById(R.id.profile_image_chat);
        name = findViewById(R.id.name_user_chat);
        ImageView back_arrow = findViewById(R.id.back_message);
        send = findViewById(R.id.send_message_btn);
        message_text = findViewById(R.id.message_Text);

        userStatusTv = findViewById(R.id.custom_bar_seen);

        recyclerView = findViewById(R.id.recycle_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent1 = getIntent();
        userid = intent1.getStringExtra("userid");

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //
//        attachBtn = findViewById(R.id.send_files_btn);

        //
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = message_text.getText().toString().trim();
                if(!msg.equals("")){
                    sendMessage(msg, firebaseUser.getUid(), userid);
                }
                else{
                    Toast.makeText(MessageActivity.this ,
                            "Tidak dapat mengirim pesan kosong..." ,
                            Toast.LENGTH_LONG)
                            .show();
                }
                message_text.setText("");
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

                // online atau tidak
                if (user.getStatus().equals("online")) {
                    userStatusTv.setText(user.getStatus());
                }
                else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM HH:mm");
                    String dateTime = simpleDateFormat.format(calendar.getTime());
                    userStatusTv.setText("Terakhir dilihat: "+ dateTime);
                }
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

    private void sendMessage(String message, String sender, final String receiver) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("sender", sender);
        map.put("receiver", receiver);

        reference.child("Messages").push().setValue(map);

        // Yang ke-1
        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist")
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
        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(firebaseUser.getUid());
        chatRefReceiver.child("id").setValue(firebaseUser.getUid());

        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    sendNotifiaction(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //sendNotifiaction
    private void sendNotifiaction(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.drawable.all_ic_boy, username+": "+message, "Pesan Baru", userid);

                    Sender sender = new Sender(data, token.getToken());

                    // Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // v.vibrate(400);

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<com.aldi.dacari.notifications.Response>() {
                                @Override
                                public void onResponse(Call<com.aldi.dacari.notifications.Response> call, Response<com.aldi.dacari.notifications.Response> response) {

                                    //Notification sound
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.play();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            // Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<com.aldi.dacari.notifications.Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readMessages(final String myid, final String userid, final String imageurl){
        mMessage = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        reference = firebaseDatabase.getReference().child("Messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessage.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    assert message != null;
                    if(message.getReceiver().equals(myid) && message.getSender().equals(userid)
                            || message.getReceiver().equals(userid) && message.getSender().equals(myid)){
                        mMessage.add(message);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mMessage, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
