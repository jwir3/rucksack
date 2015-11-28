package com.glasstowerstudios.rucksack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
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

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.ui.base.FragmentPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Base activity class that all {@link Activity}s within Rucksack should derive from. Provides basic
 * {@link Fragment} handling logic, as well as a navigation drawer.
 */
public abstract class BaseActivity extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener,
             FragmentManager.OnBackStackChangedListener {
  private static final String LOGTAG = BaseActivity.class.getSimpleName();

  private FragmentPresenter mFragmentPresenter;

  @Bind(R.id.toolbar)
  protected Toolbar mToolbar;

  @Bind(R.id.drawer_layout) protected DrawerLayout mDrawerLayout;
  ActionBarDrawerToggle mDrawerToggle;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_base);

    ButterKnife.bind(this);

    mFragmentPresenter = new FragmentPresenter(this, getSupportFragmentManager());

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

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

  /**
   * Retrieves a reference to the currently displayed {@link Fragment}, if one exists.
   *
   * @return The currently displayed {@link Fragment} in the
   *         {@link android.support.v4.app.FragmentManager}, if one is currently being displayed;
   *         null, otherwise.
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

    if (id == R.id.nav_trip_list) {
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
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
   *         be presented to the user when they tap the home button; false, otherwise.
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
}
