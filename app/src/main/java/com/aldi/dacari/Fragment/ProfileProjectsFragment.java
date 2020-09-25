package com.aldi.dacari.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aldi.dacari.Adapter.ProjectAdapter;
import com.aldi.dacari.Model.Project;
import com.aldi.dacari.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProfileProjectsFragment extends Fragment {


    private ProjectAdapter projectAdapter;
    private RecyclerView recyclerView;
    private List<Project> mProjects;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_profile_projects, container, false);

        recyclerView = viewGroup.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        mProjects = new ArrayList<>();

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("Settings", 0);
        String setting = sharedPreferences.getString("userId", "");

        readMyProjects(setting);

        return viewGroup;
    }

    private void readMyProjects(final String user){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Project");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProjects.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Project project = snapshot.getValue(Project.class);
                    assert project != null;
                    assert firebaseUser != null;
                    if (project.getOwnerid().equals(user)){
                        mProjects.add(project);
                    }
                }
                projectAdapter = new ProjectAdapter(getContext(), mProjects, false);
                recyclerView.setAdapter(projectAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
