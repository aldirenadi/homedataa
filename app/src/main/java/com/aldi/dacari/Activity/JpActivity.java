package com.aldi.dacari.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aldi.dacari.Adapter.ProjectAdapter_jp;
import com.aldi.dacari.Model.Project;
import com.aldi.dacari.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JpActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProjectAdapter_jp projectAdapter_jp;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private List<Project> mProjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_jp);

        ImageView back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.kategori_jp);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setRecycleChildrenOnDetach(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mProjects = new ArrayList<>();

        readProjects();

    }

    private void readProjects() {

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Project");

        Query query= reference.orderByChild("type").equalTo("Jasa Pembersihan");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProjects.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Project project = snapshot.getValue(Project.class);
                    mProjects.add(project);
                }
                projectAdapter_jp = new ProjectAdapter_jp(JpActivity.this, mProjects, false);
                recyclerView.setAdapter(projectAdapter_jp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
