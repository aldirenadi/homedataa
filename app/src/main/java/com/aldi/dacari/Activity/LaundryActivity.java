package com.aldi.dacari.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aldi.dacari.Adapter.ProjectAdapter_laundry;
import com.aldi.dacari.Adapter.ProjectAdapter_sac;
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

public class LaundryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProjectAdapter_laundry projectAdapter_laundry;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private List<Project> mProjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_laundry);

        ImageView back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.kategori_laundry);
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

        Query query= reference.orderByChild("type").equalTo("Jasa Laundry");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProjects.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Project project = snapshot.getValue(Project.class);
                    mProjects.add(project);
                }
                projectAdapter_laundry = new ProjectAdapter_laundry (LaundryActivity.this, mProjects, false);
                recyclerView.setAdapter(projectAdapter_laundry);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
