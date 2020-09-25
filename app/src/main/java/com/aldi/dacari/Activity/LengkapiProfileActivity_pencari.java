package com.aldi.dacari.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aldi.dacari.Model.User;
import com.aldi.dacari.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LengkapiProfileActivity_pencari extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lengkapi_profile_pencari);

        final EditText id = findViewById(R.id.id_lengkapi_pencari);
        final EditText firstName = findViewById(R.id.first_name_lengkapi_pencari);
        final EditText lastName = findViewById(R.id.last_name_lengkapi_pencari);
        final EditText jobTitle = findViewById(R.id.job_title_lengkapi_pencari);
        final EditText username = findViewById(R.id.username_lengkapi_pencari);
        final EditText address = findViewById(R.id.address_lengkapi_pencari);
        Button save = findViewById(R.id.save_lengkapi_pencari);

        ImageView back = findViewById(R.id.back);

        auth = FirebaseAuth.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        assert firebaseUser != null;
        DatabaseReference reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                id.setText(user.getId());
                firstName.setText(user.getName());
                lastName.setText(user.getLastname());
                jobTitle.setText(user.getHandphone());
                username.setText(user.getUsername());
                address.setText(user.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_name = firstName.getText().toString().trim();
                String last_name = lastName.getText().toString().trim();
                String job_title = jobTitle.getText().toString().trim();
                String user_name = username.getText().toString().trim();
                String address_text = address.getText().toString().trim();

                if (TextUtils.isEmpty(first_name)){
                    firstName.setError("Kosong!");
                    firstName.requestFocus();
                }
                else if (TextUtils.isEmpty(user_name)){
                    username.setError("Kosong!");
                    username.requestFocus();
                } else if (TextUtils.isEmpty(job_title)){
                    jobTitle.setError("Kosong!");
                    jobTitle.requestFocus();
                } else if (TextUtils.isEmpty(address_text)){
                    address.setError("Kosong!");
                    address.requestFocus();
                }
                else
                {
                    saveEditedData(first_name, last_name, user_name, job_title, address_text);
                }
            }
        });
    }

    private void saveEditedData(String first_name, String last_name, String user_name, String job_title, String address_text) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        assert firebaseUser != null;
        DatabaseReference reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", first_name );
        hashMap.put("lastname", last_name);
        hashMap.put("search", first_name + " " + last_name);
        hashMap.put("username", user_name);
        hashMap.put("handphone",job_title);
        hashMap.put("address", address_text);

        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LengkapiProfileActivity_pencari.this, "Akun telah dibuat! Harap verifikasi Email Anda", Toast.LENGTH_LONG).show();

                // menuju ke verifikasi email
                verifyEmail();

                Intent intent = new Intent(LengkapiProfileActivity_pencari.this , LoginActivity_pencari.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LengkapiProfileActivity_pencari.this,
                        "Error!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Verifikasi
    private void verifyEmail() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null)
            firebaseUser.sendEmailVerification();
    }
}
