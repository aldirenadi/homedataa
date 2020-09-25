package com.aldi.dacari.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aldi.dacari.Model.Project;
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

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import me.bendik.simplerangeview.SimpleRangeView;

public class Project_editActivity extends AppCompatActivity {

    // Post Image
    private ImageView postImage;

    //ProgressBar
    private ProgressBar postProgress;

    // Button
    private Button submit_edit_Btn;

    // edit Text
    private EditText title, description, skills;

    String mTitle = null;

    // Spinner
    private Spinner typeSpinner;

    // Seekbar
    private SeekBar pengalamanSeekbar;
    private TextView text_seekbar;

    // seekbar Range
    private SimpleRangeView budgetSeekbar;
    private TextView text_seekbar_range;

    // d-post
    private Uri mImageUri;
    private static final int GALLERY_CODE = 1;

    // date
    Date currentTime;
    String time;

    // Builder
    private AlertDialog.Builder builder;

    // database and store
    private StorageReference mStorage;
    private String job_key = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_edit);

        builder = new AlertDialog.Builder(this);

        int time;
        postProgress = (ProgressBar) findViewById(R.id.postProgressBar_edit);
        postProgress.setVisibility(View.INVISIBLE);

        currentTime = Calendar.getInstance().getTime();
        //time = currentTime.toString();

        typeSpinner = (Spinner) findViewById(R.id.type_spinner_edit);

        // Untuk type TextView
        final TextView type_edit = findViewById(R.id.type_edit);

        //seekbar_range
        budgetSeekbar = (SimpleRangeView) findViewById(R.id.seekbar_seekbar_range_edit);
        text_seekbar_range = (TextView) findViewById (R.id.textView_seekbar_bayaran_edit);

        //seekbar
        pengalamanSeekbar = (SeekBar) findViewById(R.id.pengalaman_type_seekbar_edit);
        text_seekbar = (TextView) findViewById (R.id.text_seekbar_edit);

        title = (EditText) findViewById(R.id.title_txt_edit);
        description = (EditText) findViewById(R.id.description_txt_edit);
        skills = (EditText) findViewById(R.id.skill_txt_edit);

        // image
        postImage = (ImageView) findViewById(R.id.addImageBtn_edit);

        // Button
        submit_edit_Btn = (Button) findViewById(R.id.post_project_edit);

        mStorage = FirebaseStorage.getInstance().getReference();

        // Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        //Jika Gambar Di Klik
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create gallery intent to get image content
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        String[] types = new String[]{
                "Jasa Laundry", "Jasa Pembersihan", "Servis AC",
                "Servis Elektronik", "Tukang Harian", "Tukang Ledeng",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getApplicationContext()), R.layout.spinner_item_project_type, types);
        adapter.setDropDownViewResource(R.layout.spinner_item_project_type);
        typeSpinner.setAdapter(adapter);

        // Seekbar Range
        budgetSeekbar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                text_seekbar_range.setText(String.valueOf(i)+"00.000"+" - "+String.valueOf(i1)+"00.000");
            }
        });

        budgetSeekbar.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                text_seekbar_range.setText(String.valueOf(i));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                text_seekbar_range.setText(String.valueOf(i));
            }
        });

        budgetSeekbar.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return String.valueOf(i);
            }
        });


        // Seekbar
        pengalamanSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_seekbar.setText(progress+" tahun");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        job_key = getIntent().getExtras().getString("projectid");

        DatabaseReference reference = firebaseDatabase.getReference("Project").child(job_key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);
                assert project != null;
                title.setText(project.getTitle());
                description.setText(project.getDescription());
                skills.setText(project.getSkill());
                text_seekbar_range.setText(project.getBudget());
                text_seekbar.setText(project.getTrack_record());
                type_edit.setText(project.getType());
                Glide.with(getApplicationContext()).load(project.getImage()).into(postImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submit_edit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String budget_p = text_seekbar_range.getText().toString();
                final String deskripsi_p = description.getText().toString();
                final String title_p = title.getText().toString();
                final String skills_p = skills.getText().toString();
                final String text_seekbar_p = text_seekbar.getText().toString();
                final String project_type_p = typeSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(budget_p)){
                    text_seekbar_range.setError("Kosong!");
                    text_seekbar_range.requestFocus();
                }
                else if (TextUtils.isEmpty(deskripsi_p)){
                    description.setError("Kosong!");
                    description.requestFocus();
                }
                else if (TextUtils.isEmpty(title_p)){
                    title.setError("Kosong!");
                    title.requestFocus();
                }
                else if (TextUtils.isEmpty(skills_p)){
                    skills.setError("Kosong!");
                    skills.requestFocus();
                }
                else if (TextUtils.isEmpty(text_seekbar_p)){
                    text_seekbar.setError("Kosong!");
                    text_seekbar.requestFocus();
                }

                else if (mImageUri != null) {

                    final StorageReference filepath = mStorage
                            .child("Post_Images")
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
                                job_key = getIntent().getExtras().getString("projectid");

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference reference = firebaseDatabase.getReference("Project").child(job_key);

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("image", downloadUrl);

                                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Project_editActivity.this,
                                                "Postingan Jasa Anda telah berhasil diubah",
                                                Toast.LENGTH_LONG).show();
                                        postProgress.setVisibility(View.INVISIBLE);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Project_editActivity.this,
                                                "Error!",
                                                Toast.LENGTH_LONG).show();
                                        postProgress.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }

                            else {
                                // Tidak ada gambar, tapi belum di coba karena gambar sudah terIsi
                                postProgress.setVisibility(View.INVISIBLE);
                                Toast.makeText(Project_editActivity.this, "Masukkan Gambar Pamflet / Promosi",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    // Kecuali Gambar
                    saveEditedData(budget_p, deskripsi_p, title_p, skills_p, text_seekbar_p, project_type_p);
                }
            }
        });

    }

    private void saveEditedData(String budget_p, String deskripsi_p, String title_p, String skills_p, String text_seekbar_p, String project_type_p) {

        postProgress.setVisibility(View.VISIBLE);

        job_key = getIntent().getExtras().getString("projectid");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Project").child(job_key);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("budget", budget_p);
        hashMap.put("description", deskripsi_p);
        hashMap.put("title", title_p);
        hashMap.put("skill", skills_p);
        hashMap.put("track_record", text_seekbar_p);
        hashMap.put("type", project_type_p);

        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Project_editActivity.this, "Postingan Jasa Anda telah berhasil diubah", Toast.LENGTH_LONG).show();
                postProgress.setVisibility(View.INVISIBLE);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Project_editActivity.this, "Error!", Toast.LENGTH_LONG).show();
                postProgress.setVisibility(View.INVISIBLE);
            }
        });

    }


    // Only Gambar
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if requestCode and result code are ok, get image
        if(requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK){
            //set image Uri to image retrieved from gallery
            mImageUri = data.getData();
            postImage.setImageURI(mImageUri);
        }
    }

}
