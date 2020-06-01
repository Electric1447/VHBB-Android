package eparon.vhbb_android;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import eparon.vhbb_android.Constants.VitaDB;

public class MainActivity extends AppCompatActivity {

    public static final String sourcecodeURL = "https://github.com/Electric1447";

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_homebrew, R.id.nav_plugins)
                .setDrawerLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        MenuItem navSettingsItem = navigationView.getMenu().findItem(R.id.nav_settings);
        navSettingsItem.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        });
        MenuItem navVitaDBItem = navigationView.getMenu().findItem(R.id.nav_vitadb);
        navVitaDBItem.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VitaDB.PARENT_URL)));
            return true;
        });
        MenuItem navSourceCodeItem = navigationView.getMenu().findItem(R.id.nav_github);
        navSourceCodeItem.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sourcecodeURL)));
            return true;
        });

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public boolean onSupportNavigateUp () {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

}