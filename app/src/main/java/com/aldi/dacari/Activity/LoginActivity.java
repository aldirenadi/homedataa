package com.aldi.dacari.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aldi.dacari.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public static FirebaseUser user_log = null;

    private ImageView back;
    private TextView register, forgetpassword;

    private EditText email;
    private EditText password;
    public CheckBox remember;

    private Button login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.back_ID);
        register = findViewById(R.id.register_ID);

        email = findViewById(R.id.user_reg_ID);
        password = findViewById(R.id.pass_reg_ID);
        remember = findViewById(R.id.remember_ID);
        forgetpassword = findViewById(R.id.forget_ID);


        login = findViewById(R.id.signupbtn_reg_ID);
        auth = FirebaseAuth.getInstance();

        // Lupa Password Activity
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , ResetPasswordActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , MainActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_txt = email.getText().toString().trim();
                String password_txt = password.getText().toString().trim();

                if(TextUtils.isEmpty(email_txt)){
                    Toast.makeText(LoginActivity.this , "Masukkan Email!" , Toast.LENGTH_SHORT).show();
                    email.setError("kosong!");
                    email.requestFocus();
                }
                else if(TextUtils.isEmpty(password_txt)){
                    Toast.makeText(LoginActivity.this , "Masukkan kata sandi!" , Toast.LENGTH_SHORT).show();
                    password.setError("kosong!");
                    password.requestFocus();
                }
                else if (remember.isChecked()){
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE)
                            .edit();
                    editor.putString("checked", "1");
                    editor.apply();
                    LoginUser(email_txt,password_txt);
                }
                else{
                    LoginUser(email_txt,password_txt);
                }
            }
        });
    }

    // Validasi Email - Mengecek Kesempurnaan Email
    private boolean validate(String email, String password) {
        if(!email.contains("@") || !email.contains(".")){
            Toast.makeText(this,"Email tidak valid", Toast.LENGTH_LONG).show();
            return false;
        }

        if(password.length() < 6){
            Toast.makeText(this,"Kata Sandi Kurang dari 6 karakter", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void LoginUser(String email, String password){
        if(!validate(email, password))
            return;

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this , "Email belum terdaftar, silahkan coba lagi" , Toast.LENGTH_SHORT).show();
                        } else {
                            // menuju ke Verifikasi Email
                            checkIfEmailVerified();
                        }
                    }
                });
    }

    // Proses Verifikasi Email
    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Berhasil
        if (user.isEmailVerified()) {
            Toast.makeText(LoginActivity.this , "Login Sukses" , Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this , HomeActivity.class));
            finish();
        }

        // Mengecek Verifikasi email apakah sudah atau belum
        else if (!user.isEmailVerified()) {
            Toast.makeText(LoginActivity.this , "Anda Belum Memverifikasi Email" , Toast.LENGTH_SHORT).show();
        }

        // Gagal
        else
        {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(LoginActivity.this,"Email atau kata sandi salah, Silakan coba lagi", Toast.LENGTH_LONG).show();
        }
    }
}
