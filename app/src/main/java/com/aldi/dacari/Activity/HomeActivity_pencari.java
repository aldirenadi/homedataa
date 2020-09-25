package com.aldi.dacari.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aldi.dacari.Fragment.AccountFragment;
import com.aldi.dacari.Fragment.AccountFragment_pencari;
import com.aldi.dacari.Fragment.MessagesFragment;
import com.aldi.dacari.Fragment.MyprojectsFragment;
import com.aldi.dacari.Fragment.MyprojectsFragment_home;
import com.aldi.dacari.Fragment.PostFragment;
import com.aldi.dacari.Fragment.SearchFragment;
import com.aldi.dacari.R;
import com.aldi.dacari.notifications.Token;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class HomeActivity_pencari extends AppCompatActivity {

    BottomNavigationView nav_View;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    //Firebase Auth
    FirebaseAuth firebaseAuth;

    String myid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pencari);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        nav_View = findViewById(R.id.bottom_nav_pencari);

        final MessagesFragment msgfragment = new MessagesFragment();
        final MyprojectsFragment_home myprojectfragment = new MyprojectsFragment_home();
        // final PostFragment postFragment = new PostFragment();
        final SearchFragment searchFragment = new SearchFragment();
        final AccountFragment_pencari accountFragment_pencari = new AccountFragment_pencari();

        checkUserStatus();

        // update = token
        updateToken(FirebaseInstanceId.getInstance().getToken());

        if (savedInstanceState == null){
            setFragment(msgfragment);
        }

        nav_View.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.msgID_pencari:
                        setFragment(msgfragment);
                        return true;
                    case R.id.projectID_pencari:
                        setFragment(myprojectfragment);
                        return  true;
                    case R.id.searchID_pencari:
                        setFragment(searchFragment);
                        return true;
                    case R.id.accountID_pencari:
                        setFragment(accountFragment_pencari);
                        return true;
                }
                return true;
            }
        });
    }

    // NOTIFIKASI
    public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        ref.child(myid).setValue(mToken);
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameID_pencari , fragment);
        fragmentTransaction.commit();
    }

    void status(String status){
        reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);

        reference.updateChildren(map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    private void checkUserStatus(){
        // Get Current User
        if (firebaseUser != null) {
            // user is signed in staty here
            // set email of logged in user
            // mProfileTv.setText(user.getEmail());
            myid = firebaseUser.getUid();

            // save uid of currently signed in user in shared preference
            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID", myid);
            editor.apply();
        }
        else {
            // user not signed in, go to main activity
            startActivity(new Intent(HomeActivity_pencari.this, MainActivity.class));
            finish();
        }
    }
}
