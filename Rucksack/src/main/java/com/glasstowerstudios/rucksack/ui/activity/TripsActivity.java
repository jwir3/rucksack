package com.glasstowerstudios.rucksack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.ui.fragment.AddTripFragment;
import com.glasstowerstudios.rucksack.ui.fragment.TripRecyclerFragment;

import butterknife.ButterKnife;

/**
 * A top-level menu {@link Activity} that will show fragments having operations related to
 * {@link Trip}s.
 */
public class TripsActivity extends BaseActivity {

  private static final String LOGTAG = TripsActivity.class.getSimpleName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ButterKnife.bind(this);

    showRootFragment(TripRecyclerFragment.class, null);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.trip_list, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public void onFloatingActionButtonClick(View view) {
    this.showFragment(AddTripFragment.class, new Bundle(), true);
  }

  @Override
  protected void setupActivityTitle() {
    setTitle(getString(R.string.trips));
  }

  @Override
  protected int getNavItemId() {
    return R.id.nav_trips;
  }

  @Override
  public int getFragmentContainerID() {
    return R.id.fragment_container_root;
  }
}
