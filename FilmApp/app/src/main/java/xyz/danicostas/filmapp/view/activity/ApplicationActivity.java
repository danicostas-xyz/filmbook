package xyz.danicostas.filmapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import xyz.danicostas.filmapp.view.fragment.CalendarFragment;
import xyz.danicostas.filmapp.view.fragment.FriendsFragment;
import xyz.danicostas.filmapp.view.fragment.ProfileFragment;
import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.view.fragment.SearchFragment;

public class ApplicationActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        Intent intent = getIntent();
        String username = intent.getStringExtra("K_Usuario");

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); //Icon hamburgesa

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView headerName = headerView.findViewById(R.id.headerName);
        if (headerName != null && username != null) {
            headerName.setText(username);
        }

        // Menu hamburgesa
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_settings) {
                Intent i = new Intent(ApplicationActivity.this, SettingsActivity.class);
                startActivity(i);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        // Fragments..
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .commit();
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.nav_calendar) {
                selectedFragment = new CalendarFragment();
            } else if (item.getItemId() == R.id.nav_search) {
                selectedFragment = new SearchFragment();
            } else if (item.getItemId() == R.id.nav_friends) {
                selectedFragment = new FriendsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START); // Abre el menu hambuguesa
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
