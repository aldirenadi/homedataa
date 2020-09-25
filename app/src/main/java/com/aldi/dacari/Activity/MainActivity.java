package com.aldi.dacari.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aldi.dacari.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        SharedPreferences sharedPreferences = getSharedPreferences("login", 0);
        String isChecked = sharedPreferences.getString("checked", "0");
        if (isChecked.equals("1")){
            if(firebaseUser != null){
                startActivity(new Intent(MainActivity.this , HomeActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnlogin = findViewById(R.id.login_ID);
        Button BtnLogin_pencari = findViewById(R.id.login_ID_pencari);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this , LoginActivity.class));
                    finish();
            }
        });

        BtnLogin_pencari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , LoginActivity_pencari.class));
                finish();
            }
        });

        // CALL getInternetStatus() berfungsi untuk memeriksa internet dan menampilkan dialog kesalahan
        if(new InternetDialog(this).getInternetStatus()){
            Toast.makeText(this, "Selamat Datang", Toast.LENGTH_SHORT).show();
        }
    }
}
