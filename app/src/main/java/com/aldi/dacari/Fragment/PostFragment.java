package com.aldi.dacari.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aldi.dacari.Activity.HomeActivity;
import com.aldi.dacari.Model.User;
import com.aldi.dacari.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import me.bendik.simplerangeview.SimpleRangeView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    // Post Image
    private ImageView postImg;
    private ProgressBar postProgress;
    private Button submitBtn;

    // edit Text
    private EditText title, description, skills;

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

    // database and store
    private DatabaseReference reference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    private DatabaseReference workhubUsers;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = auth.getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    // date
    Date currentTime;
    String time;

    String name, imageURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);

        workhubUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        currentTime = Calendar.getInstance().getTime();
        //time = currentTime.toString();

        typeSpinner = (Spinner) viewGroup.findViewById(R.id.type_spinner);

        //seekbar_range
        budgetSeekbar = (SimpleRangeView) viewGroup.findViewById(R.id.seekbar_seekbar_range);
        text_seekbar_range = (TextView) viewGroup.findViewById (R.id.textView_seekbar_bayaran);


        //seekbar
        pengalamanSeekbar = (SeekBar) viewGroup.findViewById(R.id.pengalaman_type_seekbar);
        text_seekbar = (TextView) viewGroup.findViewById (R.id.text_seekbar);

        title = (EditText) viewGroup.findViewById(R.id.title_txt);
        description = (EditText) viewGroup.findViewById(R.id.description_txt);
        skills = (EditText) viewGroup.findViewById(R.id.skill_txt);

        // image
        postImg = (ImageView) viewGroup.findViewById(R.id.addImageBtn);

        // Button
        submitBtn = (Button) viewGroup.findViewById(R.id.post_project);

        mStorage = FirebaseStorage.getInstance().getReference();

        //Setup indeterminate progress bar, set invisible
        postProgress = (ProgressBar) viewGroup.findViewById(R.id.postProgressBar);
        postProgress.setVisibility(View.INVISIBLE);


        //image button onclick listener
        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create gallery intent to get image content
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        //submit post button onclick listener
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });


        String[] types = new String[]{
                "Jasa Laundry", "Jasa Pembersihan", "Servis AC",
                "Servis Elektronik", "Tukang Harian", "Tukang Ledeng",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()),
                R.layout.spinner_item_project_type, types);
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

        // o
        workhubUsers.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.child("username").getValue();
                imageURL = (String) dataSnapshot.child("imageURL").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String project_title = title.getText().toString().trim();
                String project_description = description.getText().toString().trim();
                String project_type = typeSpinner.getSelectedItem().toString();
                String project_budget = text_seekbar_range.getText().toString();
                String project_money = text_seekbar.getText().toString();
                String project_skill = skills.getText().toString().trim();

                if (TextUtils.isEmpty(project_title)){
                    title.setError("Wajib diisi!");
                    title.requestFocus();
                }
                else if (TextUtils.isEmpty(project_description)){
                    description.setError("Wajib diisi!");
                    description.requestFocus();
                }

                if (mImageUri == null) {
                    Toast.makeText(getActivity(), "Wajib! menambahkan gambar",Toast.LENGTH_SHORT).show();
                }
                else {
                    startPosting();

                }
            }
        });

        return viewGroup;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if requestCode and result code are ok, get image
        if(requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK){
            //set image Uri to image retrieved from gallery
            mImageUri = data.getData();
            postImg.setImageURI(mImageUri);
        }
    }

    private void startPosting() {
        // progress
        postProgress.setVisibility(View.VISIBLE);

        //-- getText and getSelectedItem
        final String project_title = title.getText().toString().trim();
        final String project_description = description.getText().toString();
        final String project_type = typeSpinner.getSelectedItem().toString();
        final String project_budget = text_seekbar_range.getText().toString();
        final String project_money = text_seekbar.getText().toString();
        final String project_skill = skills.getText().toString().trim();

        //check
        if (!TextUtils.isEmpty(project_title) && !TextUtils.isEmpty(project_description) && !TextUtils.isEmpty(project_type) &&
                !TextUtils.isEmpty(project_budget) && !TextUtils.isEmpty(project_money) && !TextUtils.isEmpty(project_skill) && mImageUri != null) {

            final StorageReference filepath = mStorage
                    .child("Post_Images")
                    .child(mImageUri.getLastPathSegment());

            //put file on storage
            UploadTask uploadTask = filepath.putFile(mImageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    //continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //get download url is sukses
                        Uri downloadUrl = task.getResult();

                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference();

                        currentTime = Calendar.getInstance().getTime();
                        //time = currentTime.toString();
                        time = (DateFormat.format("dd MMMM yyyy - kk:mm", new Date())).toString();

                        String id = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", id);
                        hashMap.put("nama", name);
                        hashMap.put("foto_user", imageURL);
                        hashMap.put("title", project_title);
                        hashMap.put("ownerid", firebaseUser.getUid());
                        hashMap.put("description", project_description);
                        hashMap.put("type", project_type);
                        hashMap.put("budget", project_budget);
                        hashMap.put("track_record", project_money);
                        hashMap.put("skill", project_skill);
                        hashMap.put("time", time + " WIB" );
                        hashMap.put("image",downloadUrl.toString());

                        assert id != null;
                        reference.child("Project").child(id).setValue(hashMap, new DatabaseReference.CompletionListener() {
                            @SuppressLint("ShowToast")
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(getContext(),"Kesalahan saat mengupload postingan.. Silahkan coba lagi", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(),"Postingan berhasil ditambahkan", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                    title.setText("");
                                    description.setText("");
                                    skills.setText("");
                                    pengalamanSeekbar.setProgress(0);
                                    // int progress = 0;                            Di Hapus
                                    // text_seekbar.setText(progress+" tahun");     Di Hapus
                                }
                            }
                        });

                        Toast.makeText(getActivity(), "Postingan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        postProgress.setVisibility(View.INVISIBLE);

                    } else {
                        //no input in one or both fields, notify user, hide progress bar
                        postProgress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Masukkan Nama Usaha dan Deskripsi pekerjaan",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
