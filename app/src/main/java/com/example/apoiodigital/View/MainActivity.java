package com.example.apoiodigital.View;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.apoiodigital.R;
import com.example.apoiodigital.Fragment.HistoryFragment;
import com.example.apoiodigital.Fragment.HomeFragment;
import com.example.apoiodigital.Fragment.SettingsFragment;
import com.example.apoiodigital.ViewModel.modal.ModalViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomTabBar;
    private View bottomLineViewHome, bottomLineViewSettings, bottomLineViewHistory;

    private Fragment activeFragment;
    private HomeFragment homeFragment;
    private HistoryFragment historyFragment;
    private SettingsFragment settingsFragment;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1);

        }

        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

        String[] permissoes = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissoes, 200);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE); // cor de fundo da barra
        }

        bottomTabBar = findViewById(R.id.bottomNavigationTabbar);
        bottomLineViewHome = findViewById(R.id.lineHome);
        bottomLineViewHistory = findViewById(R.id.lineHistory);
        bottomLineViewSettings = findViewById(R.id.lineSettings);

        setLinesGray();
        bottomLineViewHome.setBackgroundColor(getColor(R.color.LightBlueAD));

        if (savedInstanceState == null) {
            homeFragment = new HomeFragment(this);
            replaceFragment(homeFragment);
            activeFragment = homeFragment;
            bottomTabBar.setSelectedItemId(R.id.home_ic);
        } else {
            activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HOME");
            historyFragment = (HistoryFragment) getSupportFragmentManager().findFragmentByTag("HISTORY");
            settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("SETTINGS");
        }

        setupBottomNavigation();

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }


    private void setupBottomNavigation() {
        bottomTabBar.setOnItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.home_ic) {
                setLinesGray();
                bottomLineViewHome.setBackgroundColor(getColor(R.color.LightBlueAD));

                if (homeFragment == null) homeFragment = new HomeFragment(this);
                switchFragment(homeFragment);

                Log.d("BottomNav", "Home");
                return true;

            } else if (id == R.id.history_ic) {
                setLinesGray();
                bottomLineViewHistory.setBackgroundColor(getColor(R.color.LightBlueAD));

                if (historyFragment == null) historyFragment = new HistoryFragment();
                switchFragment(historyFragment);

                Log.d("BottomNav", "History");
                return true;

            } else if (id == R.id.settings_ic) {
                setLinesGray();
                bottomLineViewSettings.setBackgroundColor(getColor(R.color.LightBlueAD));

                if (settingsFragment == null) settingsFragment = new SettingsFragment();
                switchFragment(settingsFragment);

                Log.d("BottomNav", "Settings");
                return true;
            }

            return false;
        });
    }

    private void setLinesGray() {
        bottomLineViewHome.setBackgroundColor(getColor(R.color.DarkGrayAD));
        bottomLineViewSettings.setBackgroundColor(getColor(R.color.DarkGrayAD));
        bottomLineViewHistory.setBackgroundColor(getColor(R.color.DarkGrayAD));
    }

    private void switchFragment(Fragment fragment) {
        if (activeFragment != fragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            activeFragment = fragment;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            String fragmentToOpen = intent.getStringExtra("open_fragment");
            if ("history".equals(fragmentToOpen)) {
                setLinesGray();
                bottomLineViewHistory.setBackgroundColor(getColor(R.color.LightBlueAD));

                if (historyFragment == null) historyFragment = new HistoryFragment();
                switchFragment(historyFragment);
                bottomTabBar.setSelectedItemId(R.id.history_ic);
            }
        }
    }

}
