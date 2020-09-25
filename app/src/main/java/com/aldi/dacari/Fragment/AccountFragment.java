package com.aldi.dacari.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aldi.dacari.Activity.JpActivity;
import com.aldi.dacari.Activity.MainActivity;
import com.aldi.dacari.Activity.MenuActivity;
import com.aldi.dacari.Activity.ProjectActivity;
import com.bumptech.glide.Glide;
import com.aldi.dacari.Activity.EditProfileActivity;
import com.aldi.dacari.Model.User;
import com.aldi.dacari.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class  AccountFragment extends Fragment {

    private AlertDialog.Builder builder;

    private CircleImageView profile_image1;
    private TextView username, name, jobtitle, address;
    private Button editProfile;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    private StorageReference mStorageRef;
    private Uri imageUri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    private static final int GALLERY_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_account , container , false);

        // tab layout *****************************************************
        TabLayout tabLayout = viewGroup.findViewById(R.id.tab_layout);
        ViewPager viewPager = viewGroup.findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        // tab layout // ******************************************************

        ImageView signout = viewGroup.findViewById(R.id.signoutID);
        ImageView menu = viewGroup.findViewById(R.id.menuID);
        profile_image1 = viewGroup.findViewById(R.id.profile_img);
        username = viewGroup.findViewById(R.id.username);
        name = viewGroup.findViewById(R.id.name_profile);
        jobtitle = viewGroup.findViewById(R.id.job_title);
        address = viewGroup.findViewById(R.id.address);

        //
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        editProfile = viewGroup.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference("profile_Images");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "RestrictedApi"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //give the variables in User class values from database
                //the name of variables must be same as in database
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                name.setText(user.getName() + " " + user.getLastname());
                username.setText("@"+user.getUsername());
                jobtitle.setText(user.getHandphone());
                address.setText(user.getAddress());

                if (user.getImageURL().equals("default")){
                    profile_image1.setImageResource(R.drawable.all_ic_boy);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        // Button Untuk klik Menu
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MenuActivity.class));
            }
        });



        signout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah anda yakin ingin keluar ?");

                builder.setPositiveButton("IYA", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("login", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("isChecked", "0");
                        editor.apply();
                        auth.signOut();
                        startActivity(new Intent(Objects.requireNonNull(getActivity()).
                                getApplicationContext()
                                ,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        Toast.makeText(getActivity() , "logged out" , Toast.LENGTH_LONG);
                        getActivity().finish();

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        return viewGroup;


    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = Objects.requireNonNull(getContext()).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final  StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        String mUri =   downloadUri.toString();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    }
                    else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
        else {
            Toast.makeText(getContext(), "No image Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            //get new image uri
            imageUri = data.getData();

            //uploadTask is StorageTask form snapshot
            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload...", Toast.LENGTH_SHORT).show();
            }
            else {
                uploadImage();
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new BookingFragment_penyedia(), "BOOKING");
        adapter.addFragment(new ProjectsFragment(), "PROJECTS");
        adapter.addFragment(new ReviewFragment(), "REVIEWS");
        viewPager.setAdapter(adapter);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
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
