package xyz.danicostas.filmapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.fragment.CalendarFragment;
import xyz.danicostas.filmapp.view.fragment.FriendsFragment;
import xyz.danicostas.filmapp.view.fragment.ProfileFragment;
import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.view.fragment.SearchFragment;

public class ApplicationActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        Intent intent = getIntent();
        String username = intent.getStringExtra("K_Usuario");

        initViews();
        getInstances();
        topMenuManager(session.getUsername());
        fragmentManager(savedInstanceState);

    }

    private void getInstances() {
        session = UserSession.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START); // Abre el menu hambuguesa
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentContainer = findViewById(R.id.fragment_container);
    }

    private void fragmentManager(Bundle savedInstanceState) {

        // Replaces the fragment container with a ProfileFragment if there's nothing saved
        // on the Bundle
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(fragmentContainer.getId(), new ProfileFragment())
                    .commit();
        }

        // Bottom Menu Listener to Switch Fragments
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
                        .replace(fragmentContainer.getId(), selectedFragment)
                        .commit();
            }
            return true;
        });
    }

    private void topMenuManager(String username) {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); //Icon hamburgesa


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
    }
}
