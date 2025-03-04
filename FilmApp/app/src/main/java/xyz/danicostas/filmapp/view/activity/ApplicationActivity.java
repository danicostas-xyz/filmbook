package xyz.danicostas.filmapp.view.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.service.LoginRegisterService;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.fragment.DiaryFragment;
import xyz.danicostas.filmapp.view.fragment.FriendsFragment;
import xyz.danicostas.filmapp.view.fragment.ProfileFragment;
import xyz.danicostas.filmapp.view.fragment.SearchFragment;

public class ApplicationActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    private UserSession session;
    private LoginRegisterService loginService;
    private static final String CHANNEL_ID = "1234"; // Channel ID for notifications
    private final Context CONTEXT = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        initViews();
        getInstances();
        if (session.getUsernameLiveData() != null) {
            session.getUsernameLiveData().observe(this, this::topMenuManager);
        }
        fragmentManager(savedInstanceState);
        createNotificationChannel(); // Notificaciones
    }


    // Métodos de inicialización
    private void getInstances() {
        session = UserSession.getInstance();
        loginService = LoginRegisterService.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START); // Open the hamburger menu
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Obtiene referencias del XML
    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentContainer = findViewById(R.id.fragment_container);
    }

    private void fragmentManager(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(fragmentContainer.getId(), new ProfileFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.nav_calendar) {
                selectedFragment = new DiaryFragment();
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


    // Lo que hace aquí es establecer el icono del menu hamburgesa en la linea 121
    // Después, inicializa el nav view y el headername, el headerName sera el del usuario
    // siempre y cuando este no sea null.
    // Tras esto, se abre un ItemSelectedListener, de la id navigationView, cuyo cual tiene dentro
    // el xml del menu, que a su vez tiene los items que llevara dentro el menu hamburguesa.
    // Si la id del item seleccionado coincide, con una de esas 2, realizara la accion necesaria.
    private void topMenuManager(String username) {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // Hamburger icon
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView headerName = headerView.findViewById(R.id.headerName);

        if (headerName != null && username != null) {
            headerName.setText(username);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_settings) {
                enviarNotificacion("Has pulsado Settings");

                startActivity(new Intent(ApplicationActivity.this, SettingsActivity.class));
            } else if (id == R.id.nav_logout) {
                loginService.logout(CONTEXT);
                enviarNotificacion("Te has salido de la aplicacion");
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }


// APARTADO DE CREACION DE NOTIFICACIONES
    // Metodos de la creacion para llamar a la notificacion y ponerle un mensaje.

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("General notifications for FilmApp");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void enviarNotificacion(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Notificacion recibida")
                .setContentText("Message: " + message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("ApplicationActivity", "Missing permissions for notifications");
            Toast.makeText(this, "Notification permissions missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        notificationManager.notify(1, builder.build());
    }
}
