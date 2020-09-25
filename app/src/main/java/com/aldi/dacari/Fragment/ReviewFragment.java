package com.aldi.dacari.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aldi.dacari.Adapter.ReviewAdapter;
import com.aldi.dacari.Model.Aldi;
import com.aldi.dacari.Model.Bookinglist;
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


public class ReviewFragment extends Fragment {

    private RecyclerView recyclerView;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    private ReviewAdapter reviewAdapter;
    private List<Aldi> mUsers;

    private List<Bookinglist> aldiList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_review, container, false);

        recyclerView = viewGroup.findViewById(R.id.recycle_view_review);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        aldiList = new ArrayList<Bookinglist>();

        reference = FirebaseDatabase.getInstance().getReference("Reviewlist").child(firebaseUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aldiList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bookinglist bookinglist = snapshot.getValue(Bookinglist.class);
                    aldiList.add(bookinglist);
                }
                bookingList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        return viewGroup;
    }

    private void bookingList() {
        mUsers = new ArrayList<Aldi>();
        firebaseUser = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Aldi message = snapshot.getValue(Aldi.class);
                    for (Bookinglist bookinglist : aldiList){
                        assert message != null;
                        if(message.getId().equals(bookinglist.getId()) ){
                            mUsers.add(message);
                        }
                    }
                }
                reviewAdapter = new ReviewAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

