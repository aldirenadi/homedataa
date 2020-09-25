package com.aldi.dacari.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.aldi.dacari.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        CardView cardView_tentang = findViewById(R.id.about_name);
        CardView cardView_support = findViewById(R.id.support_name);
        CardView cardView_share = findViewById(R.id.share_name);
        CardView cardView_aturan = findViewById(R.id.aturan_name);


        //Map
//        CardView cardView_map = findViewById(R.id.map_name);

        cardView_tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this , TentangActivity.class));
                finish();
            }
        });

        ImageView back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cardView_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=6287842095877"));
                startActivity(sendIntent);
            }
        });

        cardView_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "https://drive.google.com/file/d/11oiQqMKlkaza_hEl625Qm7PyI2K-jZf7/view?usp=sharing";
                String shareSub = "Your Subject here";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Bagikan dengan"));
            }
        });

        cardView_aturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this , ListActivity.class));
                finish();
            }
        });

//        cardView_map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MenuActivity.this, NavMapsActivity.class));
//            }
//        });

    }
}
