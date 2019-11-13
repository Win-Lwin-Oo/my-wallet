package com.winlwinoo.mywallet;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.winlwinoo.mywallet.Fragment.DashboardFragment;
import com.winlwinoo.mywallet.Fragment.HomeFragment;
import com.winlwinoo.mywallet.Fragment.MoreFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FrameLayout nav_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        nav_frame = findViewById(R.id.nav_frame);

        loadFragment(new HomeFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_more:
                    fragment = new MoreFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    //For Load Fragment
    private boolean loadFragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_frame,fragment)
                    .commit();
            return true;
        }
        return true;
    }

}
