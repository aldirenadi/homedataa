package com.aldi.dacari.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aldi.dacari.Fragment.ReviewFragment;
import com.bumptech.glide.Glide;
import com.aldi.dacari.Fragment.ProfileProjectsFragment;
import com.aldi.dacari.Model.User;
import com.aldi.dacari.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, username, jobtitle, address;
    private Button contact;
    private Button booking;
    private Button callBTN;
    private ImageView profile_image;

    String userid;
    String tele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // tab layout *****************************************************
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        // tab layout // ******************************************************

        name = findViewById(R.id.name_profile);
        username = findViewById(R.id.username);
        profile_image = findViewById(R.id.profile_img);
        jobtitle = findViewById(R.id.job_title);
        address = findViewById(R.id.address);
        contact = findViewById(R.id.contact);
        booking = findViewById(R.id.bookinglBTN);
        callBTN = (Button) findViewById(R.id.singleProfileCallBTN);
        ImageView back = findViewById(R.id.back);

        final Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        // let userID stored in memory, then get in ProfileProjectFragment to use it
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userid);
        editor.apply();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                user.setId(userid);
                name.setText(user.getName());
                username.setText("@" + user.getUsername());

                tele = user.getHandphone();
                jobtitle.setText(user.getHandphone());
                address.setText(user.getAddress());

                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.drawable.all_ic_boy);
                }
                else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);

                }

                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = getIntent();
                        intent1 = new Intent(getBaseContext(), MessageActivity.class);
                        intent1.putExtra("userid" , userid);
                        startActivity(intent1);
                    }
                });

                booking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = getIntent();
                        intent1 = new Intent(getBaseContext(),BookingActivity.class);
                        intent1.putExtra("userid" , userid);
                        startActivity(intent1);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileProjectsFragment(), "PROJECTS");
//        adapter.addFragment(new ReviewFragment(), "REVIEWS");         //sengaja di hapus
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
