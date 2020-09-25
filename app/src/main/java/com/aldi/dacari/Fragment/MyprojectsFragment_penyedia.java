package com.aldi.dacari.Fragment;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aldi.dacari.Adapter.ProjectAdapter_penyedia;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MyprojectsFragment_penyedia extends Fragment {

    private RecyclerView recyclerView;
    private ProjectAdapter_penyedia projectAdapter_penyedia;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;

    private EditText search;

    private List<Project> mProjects;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_myprojects_penyedia, container, false);

        recyclerView = viewGroup.findViewById(R.id.projects_recycle_penyedia);
        recyclerView.setHasFixedSize(true);
        search = viewGroup.findViewById(R.id.search_jobs_penyedia);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setRecycleChildrenOnDetach(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mProjects = new ArrayList<>();

        readProjects();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchJobs(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return viewGroup;
    }

    private void searchJobs(final String searchQuery) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        Query query = firebaseDatabase.getReference("Project");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProjects.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Project project = snapshot.getValue(Project.class);

                    if (project.getTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            project.getType().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            project.getBudget().toLowerCase().contains(searchQuery.toLowerCase())) {
                        mProjects.add(project);
                    }
                }
                projectAdapter_penyedia = new ProjectAdapter_penyedia(getContext(), mProjects, false);
                recyclerView.setAdapter(projectAdapter_penyedia);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void readProjects() {

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Project");

        //  Query query = firebaseDatabase.getReference("Project");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProjects.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Project project = snapshot.getValue(Project.class);
                    mProjects.add(project);
                }
                projectAdapter_penyedia = new ProjectAdapter_penyedia(getContext(), mProjects, false);
                recyclerView.setAdapter(projectAdapter_penyedia);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
