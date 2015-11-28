package com.glasstowerstudios.rucksack.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.ui.fragment.AddTripFragment;
import com.glasstowerstudios.rucksack.ui.fragment.TripListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripsActivity extends BaseActivity {

  private static final String LOGTAG = TripsActivity.class.getSimpleName();

  @Bind(R.id.fab) protected FloatingActionButton mFloatingActionButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ButterKnife.bind(this);

    showRootFragment(TripListFragment.class, null);
  }

  @Override
  public void onBackPressed() {
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.trip_list, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public void disableFloatingActionButton() {
    mFloatingActionButton.setVisibility(View.GONE);
  }

  public void enableFloatingActionButton() {
    mFloatingActionButton.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.fab)
  public void onFloatingActionButtonClick(View view) {
    this.showFragment(AddTripFragment.class, new Bundle(), true);
  }

  @Override
  public int getFragmentContainerID() {
    return R.id.trips_activity_root;
  }
}
