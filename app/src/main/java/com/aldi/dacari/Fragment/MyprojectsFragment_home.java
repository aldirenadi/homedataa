package com.aldi.dacari.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aldi.dacari.Activity.EditProfileActivity;
import com.aldi.dacari.Activity.ElektronikActivity;
import com.aldi.dacari.Activity.JpActivity;
import com.aldi.dacari.Activity.LaundryActivity;
import com.aldi.dacari.Activity.NavMapsHomeActivity;
import com.aldi.dacari.Activity.SacActivity;
import com.aldi.dacari.Activity.TukangharianActivity;
import com.aldi.dacari.Activity.TukangledengActivity;
import com.aldi.dacari.Adapter.ProjectAdapter;
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
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyprojectsFragment_home extends Fragment {

    private RecyclerView recyclerView;
    private ProjectAdapter projectAdapter;

    private CardView btn1;
    private CardView btn3;
    private CardView btn2;
    private CardView btn4;
    private CardView btn5;
    private CardView btn6;

    private CardView toko;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private List<Project> mProjects;

    SliderLayout sliderLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_myprojects_home, container, false);

//        recyclerView = viewGroup.findViewById(R.id.projects_recycle);
//        recyclerView.setHasFixedSize(true);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setRecycleChildrenOnDetach(false);
//        recyclerView.setLayoutManager(linearLayoutManager);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mProjects = new ArrayList<>();

        // CardView Maps Lokasi Toko
        toko = viewGroup.findViewById(R.id.maps_lokasi_toko);
        toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NavMapsHomeActivity.class));
            }
        });

        // CardView jasa pembersihan
        btn1 = viewGroup.findViewById(R.id.jasapembersihan_btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), JpActivity.class));
            }
        });

        // CardView jasa Laundry
        btn2 = viewGroup.findViewById(R.id.laundry_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LaundryActivity.class));
            }
        });

        // CardView jasa Servis AC
        btn3 = viewGroup.findViewById(R.id.servisac_btn);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SacActivity.class));
            }
        });

        // CardView jasa Servis Elektronik
        btn4 = viewGroup.findViewById(R.id.servis_el_btn);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ElektronikActivity.class));
            }
        });

        // CardView Tukang Harian
        btn5 = viewGroup.findViewById(R.id.tukang_harian_btn);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TukangharianActivity.class));
            }
        });

        // CardView Tukang Ledeng
        btn6 = viewGroup.findViewById(R.id.tukang_ledeng_btn);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TukangledengActivity.class));
            }
        });

        //image slider
        sliderLayout = viewGroup.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderLayout.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderLayout.setScrollTimeInSec(3); // beberapa detik

        setSliderView();

//        readProjects();

        return viewGroup;

    }

    private void setSliderView() {
        for (int i = 0; i <3; i++) {
            DefaultSliderView sliderView = new DefaultSliderView(getContext());

            switch(i) {
                case 0 :
                    sliderView.setImageDrawable(R.drawable.slider1);
                    break;

                case 1 :
                    sliderView.setImageDrawable(R.drawable.slider2);
                    break;

                case 2 :
                    sliderView.setImageDrawable(R.drawable.slider3);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderLayout.addSliderView(sliderView);
        }
    }

//    private void readProjects() {
//
//        auth = FirebaseAuth.getInstance();
//        firebaseUser = auth.getCurrentUser();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        reference = firebaseDatabase.getReference("Project");
//
//        //  Query query = firebaseDatabase.getReference("Project");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mProjects.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Project project = snapshot.getValue(Project.class);
//                    mProjects.add(project);
//                }
//                projectAdapter = new ProjectAdapter(getContext(), mProjects, false);
//                recyclerView.setAdapter(projectAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

}
