package com.aldi.dacari.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aldi.dacari.Fragment.AccountFragment;
import com.aldi.dacari.Fragment.BrowseFragment;
import com.aldi.dacari.Model.Bid;
import com.aldi.dacari.Model.Project;
import com.aldi.dacari.Model.User;
import com.aldi.dacari.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ProjectActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;

    String mTitle;
    private TextView detail;

    private ImageView postImage;
    private CircleImageView profil;

    //delete and Edit Post
    private Button deletejob;

    private Button editjob;
    private CardView editRemoveJobcardview;

//    //laporkan Post
//    private Button laporkanJob;
//    private CardView laporkanJobcardview;

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        builder = new AlertDialog.Builder(this);

        ImageView back = (ImageView) findViewById(R.id.back);

        final TextView nama = findViewById(R.id.nama);
        final TextView title = findViewById(R.id.title);
        final TextView date = findViewById(R.id.date);
        final TextView description = findViewById(R.id.description);
        final TextView budget = findViewById(R.id.budget);
        final TextView skill = findViewById(R.id.skill);
        final TextView type = findViewById(R.id.type);
        final TextView pengalaman = findViewById(R.id.pengalaman);

        final TextView detail = findViewById(R.id.derailments);

        profil = findViewById(R.id.profil);
        
        postImage = (ImageView) findViewById(R.id.addImageBtn_project);

        editRemoveJobcardview = (CardView) findViewById(R.id.editremoveJobCard);
        deletejob = (Button) findViewById(R.id.singlejobDeleteBTN);
        editjob = (Button) findViewById(R.id.singlejobEditBTN);

//        laporkanJobcardview = (CardView) findViewById(R.id.laporkanCard);
//        laporkanJob = (Button) findViewById(R.id.LaporkanBTN);

        final Intent intent = getIntent();
        final String projectid = intent.getStringExtra("projectid");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        assert projectid != null;
        DatabaseReference reference = firebaseDatabase.getReference("Project").child(projectid);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);


                assert project != null;
                mTitle = project.getTitle();

                nama.setText(project.getNama());
                title.setText(project.getTitle());
                date.setText(project.getTime());
                description.setText(project.getDescription());
                budget.setText("Rp. " + project.getBudget());
                skill.setText(project.getSkill());
                type.setText(project.getType());
                pengalaman.setText(project.getTrack_record());

                userid = (String) dataSnapshot.child("ownerid").getValue();

                Glide.with(getApplicationContext()).load(project.getImage()).into(postImage);

                if (project.getFoto_user().equals("default")){
                    profil.setImageResource(R.drawable.all_ic_boy);
                }
                else{
                    //maybe getActivity() cause issues --changing with HomeActivity.this not a good solution!
                    //the issue is solved by changing getContext() with getApplicationContext()
                    //now the app works without issues!--send messages and view profile img and show whom you chat with
                    Glide.with(getApplicationContext()).load(project.getFoto_user()).into(profil);
                }


//                Glide.with(getApplicationContext()).load(project.getFoto_user()).into(profil);
                if (project.getOwnerid().equals(firebaseUser.getUid()))
                {
                    editRemoveJobcardview.setVisibility(View.VISIBLE);
                    detail.setVisibility(View.GONE);
//                    laporkanJobcardview.setVisibility(View.GONE);
                }
                else {
                    editRemoveJobcardview.setVisibility(View.GONE);
                }

                // Klik Post Image ke FULL IMAGES
                postImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (project.getImage() != null) {
                            Intent intent = new Intent(ProjectActivity.this, ImageViewActivity.class);
                            intent.putExtra("image", project.getImage());
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        assert firebaseUser != null;
        final DatabaseReference query = FirebaseDatabase.getInstance().getReference("Project")
                .child(projectid)
                .child("Bids");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Bid bid1 = snapshot.getValue(Bid.class);
                    assert bid1 != null;
                    if (bid1.getBidderId().equals(firebaseUser.getUid())){
                        Toast.makeText(ProjectActivity.this, "You had already bid for this project", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        Intent intent = new Intent(mContext , ProfileActivity.class);
//        intent.putExtra("userid" , user.getId());
//        mContext.startActivity(intent);


//        detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                detail.setVisibility(View.GONE);
//                FragmentManager fm = getSupportFragmentManager();
//                BrowseFragment fragment = new BrowseFragment();
//                fm.beginTransaction().replace(R.id.container,fragment).commit();
//            }
//        });

//        detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProjectActivity.this, ProfileActivity.class);
//                intent.putExtra("userid", user.getId());
//                startActivity(intent);
//            }
//        });

        // Laporkan Permasalahan
//        laporkanJob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent send = new Intent(Intent.ACTION_SENDTO);
//                String uriText = "mailto:" + "aldirenadi12@gmail.com" +
//                        "?subject=" + Uri.encode("Laporkan : "+ mTitle ) +
//                        "&body=" + Uri.encode("Ceritakan masalah yang ada di jasa tersebut!");
//                Uri uri = Uri.parse(uriText);
//
//                send.setData(uri);
//                startActivity(Intent.createChooser(send, "Kirim Email..."));
//            }
//        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userid.equals(auth.getCurrentUser().getUid())) {
                }
                else {
                    Intent intent = new Intent(ProjectActivity.this, ProfileActivity.class);
                    intent.putExtra("userid", userid);
                    startActivity(intent);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectActivity.this, Project_editActivity.class);
                intent.putExtra("projectid", projectid);
                startActivity(intent);
            }
        });

        deletejob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Konfirmasi");
                builder.setMessage("Anda yakin ingin menghapus pekerjaan ini?");

                builder.setPositiveButton("IYA", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if(projectid != null) {
                            reference.removeValue();

                            Intent intent2 = new Intent(ProjectActivity.this, AccountFragment.class);
                            startActivity(intent2);
                            finish();

                            Toast.makeText(ProjectActivity.this,
                                    "Postingan jasa telah di hapus",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }

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

    }
}
