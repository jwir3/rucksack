package com.glasstowerstudios.rucksack.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.ui.base.FragmentPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Base activity class that all {@link Activity}s within Rucksack should derive from. Provides basic
 * {@link Fragment} handling logic, as well as a navigation drawer.
 *
 * Note: Only {@link Activity}s that should be shown in the navigation drawer should derive from
 *       this class. If the UI for a given item should not be shown in the navigation drawer, it is
 *       probably better represented as a {@link Fragment} underneath some other
 *       {@link BaseActivity}. Similarly, if something _should_ appear in the navigation drawer,
 *       then it is likely a top-level functionality of the application, and should have a {@link
 *       BaseActivity} implementation.
 */
public abstract class BaseActivity extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener,
  FragmentManager.OnBackStackChangedListener {
  private static final String LOGTAG = BaseActivity.class.getSimpleName();
  public static final String TITLE_KEY = "Title";

  private FragmentPresenter mFragmentPresenter;

  @Bind(R.id.toolbar)
  protected Toolbar mToolbar;

  @Bind(R.id.drawer_layout) protected DrawerLayout mDrawerLayout;
  ActionBarDrawerToggle mDrawerToggle;

  @Bind(R.id.fab) protected FloatingActionButton mFloatingActionButton;

  public static Intent newIntent(Class<? extends BaseActivity> actClass, Context context,
                                 String title) {
    Bundle b = new Bundle();
    b.putString(TITLE_KEY, title);

    Intent i = new Intent(context, actClass);
    i.putExtras(b);

    return i;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupActivityTitle();

    setContentView(R.layout.activity_base);

    ButterKnife.bind(this);

    mFragmentPresenter = new FragmentPresenter(this, getSupportFragmentManager());

    setSupportActionBar(mToolbar);

    Bundle extras = getIntent().getExtras();
    String title = getTitle().toString();
    if (extras != null) {
      title = extras.getString(TITLE_KEY);
    }

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setTitle(title);

    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                                              R.string.navigation_drawer_open,
                                              R.string.navigation_drawer_close);
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    mDrawerToggle.syncState();

    getSupportFragmentManager().addOnBackStackChangedListener(this);
  }

  /**
   * Show a {@link Fragment} within the application which serves as a root fragment for a given
   * activity (i.e. don't add the new {@link FragmentTransaction} to the back stack.
   *
   * Note: This method should be used to display all {@link Fragment}s after the first (root) one
   * for an activity.
   *
   * @param aType      The concrete type of the {@link Fragment} you wish to display. Cannot be
   *                   null.
   * @param aArguments A {@link Bundle} of arguments to pass to the newly created {@link Fragment}.
   *                   Can be null, but in the case that it is null, {@link
   *                   Fragment#setArguments(android.os.Bundle)} will not be called.
   */
  public void showNonRootFragment(Class<? extends Fragment> aType, Bundle aArguments) {
    showFragment(aType, aArguments, true);
  }

  /**
   * Show a {@link Fragment} within the application.
   *
   * This should be used to display a {@link Fragment} of a given type. If you use this method
   * instead of re-implementing it in a child class, you will automatically get the animated slide
   * behavior.
   *
   * If you choose to use something other than the default behavior in your implementing class, you
   * should override this method so that all users of your new implementation can use a standard
   * animation (unless you want it only to happen for a specific fragment).
   *
   * @param aType           The concrete type of the {@link Fragment} you wish to display. Cannot be
   *                        null.
   * @param aArguments      A {@link Bundle} of arguments to pass to the newly created {@link
   *                        Fragment}. Can be null, but in the case that it is null, {@link
   *                        Fragment#setArguments(android.os.Bundle)} will not be called.
   * @param aAddToBackStack If true, the created {@link FragmentTransaction} will be added to the
   *                        back stack.
   */
  public void showFragment(Class<? extends Fragment> aType, Bundle aArguments,
                           boolean aAddToBackStack) {
    mFragmentPresenter.showFragmentAnimated(aType, aArguments, aAddToBackStack,
                                            getFragmentContainerID());
  }

  /**
   * Show a {@link Fragment} within the application by replacing another {@link Fragment} already on
   * the back stack.
   *
   * This will pop a {@link Fragment} already on the back stack and replace it with another {@link
   * Fragment} of a given type. This behavior is desirable, when, for example, you are creating a
   * new object and don't wish to offer the ability to go back to the creation {@link Fragment} once
   * the API has processed it.
   *
   * This should be used to display a {@link Fragment} of a given type. If you use this method
   * instead of re-implementing this in a child class, you will automatically get the animated slide
   * behavior.
   *
   * If you choose to use something other than the default behavior in your implementing class, you
   * should override this method so that all users of your new implementation can use a standard
   * animation (unless you want it only to happen for a specific fragment).
   *
   * @param aReplacedFragmentType The concrete type of the {@link Fragment} already on the back
   *                              stack that you wish to replace with a new one. Cannot be null.
   * @param aNewFragmentType      The concrete type of the {@link Fragment} you wish to place on the
   *                              back stack. Cannot be null.
   * @param aArguments            A {@link Bundle} of arguments to pass to the newly created {@link
   *                              Fragment}. Can be null, but in the case that it is null, {@link
   *                              Fragment#setArguments(android.os.Bundle)} will not be called.
   */
  public void replaceFragmentOnBackStack(Class<? extends Fragment> aReplacedFragmentType,
                                         Class<? extends Fragment> aNewFragmentType,
                                         Bundle aArguments) {
    FragmentManager manager = getSupportFragmentManager();
    manager.popBackStackImmediate(aReplacedFragmentType.getSimpleName(),
                                  FragmentManager.POP_BACK_STACK_INCLUSIVE);

    showFragment(aNewFragmentType, aArguments, true);
  }

  /**
   * Show a {@link Fragment} within the application which serves as a root fragment for a given
   * activity (i.e. don't add the new {@link FragmentTransaction} to the back stack.
   *
   * Note: This method should only be called by classes implementing {@link BaseActivity} when
   * displaying the first {@link Fragment} for that activity.
   *
   * @param aType      The concrete type of the {@link Fragment} you wish to display. Cannot be
   *                   null.
   * @param aArguments A {@link Bundle} of arguments to pass to the newly created {@link Fragment}.
   *                   Can be null, but in the case that it is null, {@link
   *                   Fragment#setArguments(android.os.Bundle)} will not be called.
   */
  public void showRootFragment(Class<? extends Fragment> aType, Bundle aArguments) {
    showFragment(aType, aArguments, false);
  }

  /**
   * Retrieves the ID of the base fragment container (the container into which all {@link Fragment}
   * objects are loaded.
   *
   * @return The ID of the fragment container for this activity.
   */
  public abstract int getFragmentContainerID();

  @OnClick(R.id.fab)
  public abstract void onFloatingActionButtonClick(View view);

  /**
   * Call {@link Activity#setTitle} on a string representing this {@link BaseActivity}.
   */
  protected abstract void setupActivityTitle();

  /**
   * Retrieves a reference to the currently displayed {@link Fragment}, if one exists.
   *
   * @return The currently displayed {@link Fragment} in the {@link android.support.v4.app.FragmentManager},
   * if one is currently being displayed; null, otherwise.
   */
  public Fragment getCurrentlyDisplayedFragment() {
    return getSupportFragmentManager().findFragmentById(getFragmentContainerID());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        if (shouldNavDrawerBeOpenedOnHomePress()) {
          openNavigationDrawer();
          return true;
        } else {
          getSupportFragmentManager().popBackStack();
          return true;
        }
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (getNavItemId() != id) {
      if (id == R.id.nav_trips) {
        startActivity(newIntent(TripsActivity.class, this, getString(R.string.trips)));
      } else if (id == R.id.nav_items) {
        startActivity(
          newIntent(PackableItemsActivity.class, this, getString(R.string.packable_items)));
      } else if (id == R.id.nav_pastimes) {
        startActivity(newIntent(PastimesActivity.class, this, getString(R.string.pastimes)));
      }
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  /**
   * Retrieve the id of the nav drawer menu item representing this {@link BaseActivity}.
   *
   * @return An integer id of the nav drawer item representing this {@link BaseActivity}.
   */
  protected abstract int getNavItemId();

  @Override
  public void onBackPressed() {
    dismissKeyboardIfOpen();

    FragmentManager fragMan = getSupportFragmentManager();
    if (fragMan.getBackStackEntryCount() >= 1) {
      fragMan.popBackStack();
      return;
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  /**
   * Ensures that the correct navigation drawer icon is set. For non-root fragments, this is a back
   * arrow. For all other fragments, this is a hamburger icon.
   */
  private void ensureNavigationDrawerIconSet() {
    if (!shouldNavDrawerBeOpenedOnHomePress()) {
      mDrawerToggle.setDrawerIndicatorEnabled(false);
    } else {
      mDrawerToggle.setDrawerIndicatorEnabled(true);
    }
  }

  /**
   * Determines if the navigation drawer should be opened when the home app bar button is pressed.
   *
   * @return true, if this is the only fragment (or activity), and thus the navigation drawer should
   * be presented to the user when they tap the home button; false, otherwise.
   */
  protected boolean shouldNavDrawerBeOpenedOnHomePress() {
    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
      return false;
    }

    return true;
  }

  /**
   * Opens the navigation drawer.
   */
  public void openNavigationDrawer() {
    mDrawerLayout.openDrawer(GravityCompat.START);
  }

  /**
   * Closes the navigation drawer.
   */
  public void closeNavigationDrawer() {
    mDrawerLayout.closeDrawer(GravityCompat.START);
  }

  @Override
  public void onBackStackChanged() {
    ensureNavigationDrawerIconSet();
  }

  public void lockNavigationDrawer() {
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
  }

  public void unlockNavigationDrawer() {
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
  }

  /**
   * Closes the soft keyboard if it's currently open.
   */
  public void dismissKeyboardIfOpen() {
    View view = this.getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  public void disableFloatingActionButton() {
    mFloatingActionButton.setVisibility(View.GONE);
  }

  public void enableFloatingActionButton() {
    mFloatingActionButton.setVisibility(View.VISIBLE);
  }
}
