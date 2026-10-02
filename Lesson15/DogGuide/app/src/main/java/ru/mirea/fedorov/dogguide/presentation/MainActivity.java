package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.mirea.fedorov.dogguide.R;
import ru.mirea.fedorov.dogguide.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity implements BreedNavigator {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private BreedListViewModel catalogViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupChrome(false);

        catalogViewModel = new ViewModelProvider(
                this,
                new DogGuideViewModelFactory(getApplication())
        ).get(BreedListViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_catalog,
                R.id.nav_favorites,
                R.id.nav_recognize,
                R.id.nav_profile
        ).setOpenableLayout(binding.drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            boolean details = destination.getId() == R.id.nav_breed_details;
            binding.bottomNavigation.setVisibility(details ? View.GONE : View.VISIBLE);
            binding.drawerLayout.setDrawerLockMode(
                    details
                            ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                            : DrawerLayout.LOCK_MODE_UNLOCKED
            );
            invalidateOptionsMenu();
            View content = findViewById(R.id.contentContainer);
            if (content != null) {
                content.requestApplyInsets();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean catalogVisible = navController != null
                && navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() == R.id.nav_catalog;
        menu.findItem(R.id.action_layout_linear).setVisible(catalogVisible);
        menu.findItem(R.id.action_layout_grid).setVisible(catalogVisible);
        menu.findItem(R.id.action_layout_staggered).setVisible(catalogVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_layout_linear) {
            catalogViewModel.setLayoutMode(0);
            return true;
        }
        if (id == R.id.action_layout_grid) {
            catalogViewModel.setLayoutMode(1);
            return true;
        }
        if (id == R.id.action_layout_staggered) {
            catalogViewModel.setLayoutMode(2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openBreedDetails(String breedId) {
        Bundle args = new Bundle();
        args.putString(BreedDetailsFragment.ARG_BREED_ID, breedId);
        navController.navigate(R.id.action_open_breed_details, args);
    }
}
