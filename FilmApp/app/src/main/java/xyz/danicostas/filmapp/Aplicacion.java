package xyz.danicostas.filmapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Aplicacion extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicacion);
        Intent intent = getIntent();
        String username = intent.getStringExtra("K_Usuario");


        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        // Obtener el TextView dentro del header
        TextView headerName = headerView.findViewById(R.id.headerName);

        // Establecer el nombre de usuario en el TextView
        if (headerName != null && username != null) {
            headerName.setText(username);
        }

        // Manejar clics en los elementos del menú
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    Intent intentPerfil = new Intent(Aplicacion.this, Profile.class);
                    startActivity(intentPerfil);
                    Log.i("Aplicacion", "Profile pulsado");
                } else if (id == R.id.nav_settings) {
                    // Navegar a configuració
                    Log.i("Aplicacion", "Settings pulsado");
                } else if (id == R.id.nav_logout) {
                    // Lógica para logout
                    finish(); // Opcional: cierra la actividad actual
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        // Agregar funcionalidad al botón de hamburguesa
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // Icono de hamburguesa
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
