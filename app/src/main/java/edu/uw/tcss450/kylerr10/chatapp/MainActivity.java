package edu.uw.tcss450.kylerr10.chatapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.tcss450.kylerr10.chatapp.ui.setting.AboutFragment;

/**
 * Main activity of the application.
 *
 * @author Kyler Robison
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Bottom navigation
     */
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeBottomNav();
    }

    /**
     * Helper method to set up the bottom navigation bar.
     *
     * @author Kyler Robison
     */
    private void initializeBottomNav() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_chat,
                R.id.navigation_contacts,
                R.id.navigation_weather
        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AboutFragment aboutDialog = new AboutFragment();
                aboutDialog.show(getSupportFragmentManager(), "about_dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}