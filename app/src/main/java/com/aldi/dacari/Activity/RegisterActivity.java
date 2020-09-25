package com.aldi.dacari.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aldi.dacari.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, email, password, copassowrd, hp;
    private Button signUp;

    private ImageView back;
    private TextView login;

    private FirebaseAuth auth;

    DatabaseReference reference;

    private ProgressBar postProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.user_reg_ID);
        email = findViewById(R.id.email_reg_ID);
        password = findViewById(R.id.pass_reg_ID);
        copassowrd = findViewById(R.id.pass_reg_ID2);
        hp = findViewById(R.id.hp_reg_ID);

        back = findViewById(R.id.back_ID);
        login = findViewById(R.id.loginlink_ID);
        signUp = findViewById(R.id.signupbtn_reg_ID);

        auth = FirebaseAuth.getInstance();

        //Setup indeterminate progress bar, set invisible
        postProgress = findViewById(R.id.postProgressBar_register);
        postProgress.setVisibility(View.INVISIBLE);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this , MainActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String username_txt = username.getText().toString().trim();
                String email_txt = email.getText().toString().trim();
                String password_txt = password.getText().toString().trim();
                String copassword_txt = copassowrd.getText().toString().trim();
                String hp_txt = hp.getText().toString().trim();

                if(TextUtils.isEmpty(username_txt)){
                    Toast.makeText(RegisterActivity.this , "Masukkan Username!" , Toast.LENGTH_LONG).show();
                    username.setError("kosong!");
                    username.requestFocus();
                } else if (username_txt.contains(" ")){
                    Toast.makeText(RegisterActivity.this , "Username tidak boleh mengandung spasi!" , Toast.LENGTH_LONG).show();
                    username.setError("Nama pengguna tidak boleh mengandung spasi!");
                    username.requestFocus();
                }
                else if(TextUtils.isEmpty(email_txt)){
                    Toast.makeText(RegisterActivity.this , "Masukkan Email!" , Toast.LENGTH_LONG).show();
                    email.setError("kosong!");
                    email.requestFocus();
                }
                else if(TextUtils.isEmpty(hp_txt)){
                    Toast.makeText(RegisterActivity.this , "Masukkan Nomor Hp!" , Toast.LENGTH_LONG).show();
                    hp.setError("kosong!");
                    hp.requestFocus();
                }
                else if(TextUtils.isEmpty(password_txt) || password_txt.length() < 6){
                    Toast.makeText(RegisterActivity.this , "Pastikan kata sandi lebih panjang dari 6 karakter" ,
                            Toast.LENGTH_LONG).show();
                    password.setError("tulis ulang kata sandi!");
                    password.requestFocus();
                }
                else if(!copassword_txt.equals(password_txt)){
                    Toast.makeText(RegisterActivity.this , "kata sandi tidak sama" ,
                            Toast.LENGTH_LONG).show();
                    password.requestFocus();
                    copassowrd.requestFocus();
                }
                else {
                    Register(username_txt, email_txt, password_txt, hp_txt);
                }
            }
        });
    }


    private void Register(final String user, String email, String password, final String hp){

        // progress
        postProgress.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if(task.isSuccessful()){
                        //get current user data
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null; //its not important ! you can remove it
                        String userid = firebaseUser.getUid();

                        //make reference for database name = Users
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        reference = database.getReference("Users").child(userid);

                        //create hash map and insert it to realtime db
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("name", "");      // user di ganti dengan ""
                        hashMap.put("lastname", "");
                        hashMap.put("username", user);
                        hashMap.put("imageURL", "default");
                        hashMap.put("ktp", "default");
                        hashMap.put("handphone", hp);
                        hashMap.put("address", "");
                        hashMap.put("nik","");
                        hashMap.put("status", "online");
                        hashMap.put("peran_akun", "penyedia_jasa");
                        hashMap.put("search", user.toLowerCase());

                        //insert hash map to firebase realtime database
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this , "Lengkapi Profilmu" , Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this , LengkapiProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    postProgress.setVisibility(View.INVISIBLE);
                                    finish();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(RegisterActivity.this ,
                                "Anda tidak dapat mendaftar sekarang, coba lagi nanti!" ,
                                Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(RegisterActivity.this ,
                            e.getLocalizedMessage().toString() , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
