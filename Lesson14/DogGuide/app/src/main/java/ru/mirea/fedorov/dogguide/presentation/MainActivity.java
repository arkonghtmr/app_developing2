package ru.mirea.fedorov.dogguide.presentation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.mirea.fedorov.dogguide.R;

public class MainActivity extends BaseActivity implements BreedNavigator {
    private static final String TAG_CATALOG = "catalog";
    private static final String TAG_FAVORITES = "favorites";
    private static final String TAG_RECOGNIZE = "recognize";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_DETAILS = "details";

    private CatalogFragment catalogFragment;
    private FavoritesFragment favoritesFragment;
    private RecognizeFragment recognizeFragment;
    private ProfileFragment profileFragment;
    private BottomNavigationView bottomNavigation;
    private int currentTabId = R.id.nav_catalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupChrome(false);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        restoreFragments(savedInstanceState);

        getSupportFragmentManager().addOnBackStackChangedListener(this::syncDetailsChrome);
        bottomNavigation.setOnItemSelectedListener(item -> {
            showTab(item.getItemId());
            return true;
        });

        if (savedInstanceState == null) {
            setTitle(R.string.nav_catalog);
        } else {
            currentTabId = savedInstanceState.getInt("current_tab", R.id.nav_catalog);
            bottomNavigation.getMenu().findItem(currentTabId).setChecked(true);
            syncDetailsChrome();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_tab", currentTabId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean catalogVisible = currentTabId == R.id.nav_catalog
                && getSupportFragmentManager().getBackStackEntryCount() == 0;
        menu.findItem(R.id.action_layout_linear).setVisible(catalogVisible);
        menu.findItem(R.id.action_layout_grid).setVisible(catalogVisible);
        menu.findItem(R.id.action_layout_staggered).setVisible(catalogVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_layout_linear) {
            catalogFragment.setLayoutMode(0);
            return true;
        }
        if (id == R.id.action_layout_grid) {
            catalogFragment.setLayoutMode(1);
            return true;
        }
        if (id == R.id.action_layout_staggered) {
            catalogFragment.setLayoutMode(2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openBreedDetails(String breedId) {
        Fragment tab = fragmentFor(currentTabId);
        if (tab == null) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .hide(tab)
                .add(R.id.fragment_container, BreedDetailsFragment.newInstance(breedId), TAG_DETAILS)
                .addToBackStack(TAG_DETAILS)
                .commit();
    }

    private void restoreFragments(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            catalogFragment = new CatalogFragment();
            favoritesFragment = new FavoritesFragment();
            recognizeFragment = new RecognizeFragment();
            profileFragment = new ProfileFragment();
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, catalogFragment, TAG_CATALOG)
                    .add(R.id.fragment_container, favoritesFragment, TAG_FAVORITES)
                    .hide(favoritesFragment)
                    .add(R.id.fragment_container, recognizeFragment, TAG_RECOGNIZE)
                    .hide(recognizeFragment)
                    .add(R.id.fragment_container, profileFragment, TAG_PROFILE)
                    .hide(profileFragment)
                    .commit();
            return;
        }
        catalogFragment = (CatalogFragment) fm.findFragmentByTag(TAG_CATALOG);
        favoritesFragment = (FavoritesFragment) fm.findFragmentByTag(TAG_FAVORITES);
        recognizeFragment = (RecognizeFragment) fm.findFragmentByTag(TAG_RECOGNIZE);
        profileFragment = (ProfileFragment) fm.findFragmentByTag(TAG_PROFILE);
    }

    private void showTab(int itemId) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        currentTabId = itemId;
        Fragment target = fragmentFor(itemId);
        if (target == null) {
            return;
        }
        fm.beginTransaction()
                .setReorderingAllowed(true)
                .hide(catalogFragment)
                .hide(favoritesFragment)
                .hide(recognizeFragment)
                .hide(profileFragment)
                .show(target)
                .commit();
        setTitle(titleFor(itemId));
        syncDetailsChrome();
        invalidateOptionsMenu();
    }

    private Fragment fragmentFor(int itemId) {
        if (itemId == R.id.nav_favorites) {
            return favoritesFragment;
        }
        if (itemId == R.id.nav_recognize) {
            return recognizeFragment;
        }
        if (itemId == R.id.nav_profile) {
            return profileFragment;
        }
        return catalogFragment;
    }

    private int titleFor(int itemId) {
        if (itemId == R.id.nav_favorites) {
            return R.string.nav_favorites;
        }
        if (itemId == R.id.nav_recognize) {
            return R.string.nav_recognize;
        }
        if (itemId == R.id.nav_profile) {
            return R.string.nav_profile;
        }
        return R.string.nav_catalog;
    }

    private void syncDetailsChrome() {
        boolean detailsOpen = getSupportFragmentManager().getBackStackEntryCount() > 0;
        bottomNavigation.setVisibility(detailsOpen ? View.GONE : View.VISIBLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(detailsOpen);
        }
        if (!detailsOpen) {
            setTitle(titleFor(currentTabId));
        }
        invalidateOptionsMenu();
        ViewCompat.requestApplyInsets(findViewById(R.id.contentContainer));
    }
}
