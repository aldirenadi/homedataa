package com.aldi.dacari.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aldi.dacari.Model.User;
import com.aldi.dacari.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class LengkapiProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    // Post KTP
    private ImageView postktp;

    // d-post KTP
    private Uri mImageUri;
    private static final int GALLERY_CODE = 1;

    // database and store
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lengkapi_profile);

        final EditText id = findViewById(R.id.id_lengkapi);
        final EditText firstName = findViewById(R.id.first_name_lengkapi);
        final EditText lastName = findViewById(R.id.last_name_lengkapi);
        final EditText jobTitle = findViewById(R.id.job_title_lengkapi);
        final EditText username = findViewById(R.id.username_lengkapi);
        final EditText address = findViewById(R.id.address_lengkapi);
        final EditText nik = findViewById(R.id.nik_ktp);
        Button save = findViewById(R.id.save_lengkapi);
        ImageView back = findViewById(R.id.back);

        // image KTP
        postktp = (ImageView) findViewById(R.id.foto_ktp);

        mStorage = FirebaseStorage.getInstance().getReference();

        //Jika Gambar KTP Di Klik
        postktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create gallery intent to get image content
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

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
                nik.setText(user.getNik());

                if (user.getKtp().equals("default")){
                    postktp.setImageResource(R.drawable.ic_ktp_fix);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getKtp()).into(postktp);
                }
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
                String nik_text = nik.getText().toString().trim();

                if (TextUtils.isEmpty(first_name)){
                    firstName.setError("kosong!");
                    firstName.requestFocus();
                }
                else if (TextUtils.isEmpty(user_name)){
                    username.setError("kosong!");
                    username.requestFocus();
                }
                else if (TextUtils.isEmpty(job_title)){
                    jobTitle.setError("kosong!");
                    jobTitle.requestFocus();
                }
                else if (TextUtils.isEmpty(address_text)){
                    address.setError("kosong!");
                    address.requestFocus();
                }

                else if (TextUtils.isEmpty(nik_text)){
                    nik.setError("kosong!");
                    nik.requestFocus();
                }

                // untuk FOTO KTP
                else if (mImageUri != null) {

                    final StorageReference filepath = mStorage
                            .child("Post_KTP")
                            .child(mImageUri.getLastPathSegment());

                    // Ambil value dari database..
                    UploadTask uploadTask = filepath.putFile(mImageUri);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }

                            //lanjutkan dengan untuk mendapatkan URL unduhan
                            return filepath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                String downloadUrl = task.getResult().toString();

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("ktp", downloadUrl);
                                hashMap.put("name", first_name );
                                hashMap.put("lastname", last_name);
                                hashMap.put("search", first_name + " " + last_name);
                                hashMap.put("username", user_name);
                                hashMap.put("handphone",job_title);
                                hashMap.put("address", address_text);
                                hashMap.put("nik", nik_text);

                                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(LengkapiProfileActivity.this, "Akun telah dibuat! Harap verifikasi Email Anda", Toast.LENGTH_LONG).show();

                                        // menuju ke verifikasi email
                                        verifyEmail();

                                        Intent intent = new Intent(LengkapiProfileActivity.this , LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LengkapiProfileActivity.this,
                                                "Error!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            else {
                                // Tidak ada gambar, tapi belum di coba karena gambar sudah terIsi
                                Toast.makeText(LengkapiProfileActivity.this, "Masukkan Gambar KTP",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(LengkapiProfileActivity.this,
                            "Wajib! menambahkan Foto KTP!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void verifyEmail() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null)
            firebaseUser.sendEmailVerification();
    }

    // Only Gambar KTP
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if requestCode and result code are ok, get image
        if(requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK){
            //set image Uri to image retrieved from gallery
            mImageUri = data.getData();
            postktp.setImageURI(mImageUri);
        }
    }
}
