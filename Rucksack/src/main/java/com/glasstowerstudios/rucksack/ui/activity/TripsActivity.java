package com.glasstowerstudios.rucksack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.ui.fragment.AddTripDetailsFragment;
import com.glasstowerstudios.rucksack.ui.fragment.LicenseAgreementDialogFragment;
import com.glasstowerstudios.rucksack.ui.fragment.TripRecyclerFragment;
import com.glasstowerstudios.rucksack.util.data.PastimeDataProvider;
import com.glasstowerstudios.rucksack.util.prefs.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * A top-level menu {@link Activity} that will show fragments having operations related to
 * {@link Trip}s.
 */
public class TripsActivity extends BaseActivity<TripRecyclerFragment> {

  private static final String LOGTAG = TripsActivity.class.getSimpleName();

  @Inject AppPreferences prefs;
  @Inject PastimeDataProvider mPastimeDataProvider;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ButterKnife.bind(this);
    Injector.INSTANCE.getApplicationComponent().inject(this);

    performFirstRunActions();
  }

  private void performFirstRunActions() {
    if (prefs.isFirstRun()) {
      populateInitialData();

      prefs.setLastAppVersion();
    }

    if (!prefs.hasAgreedToLicense()) {
      DialogFragment newFragment = new LicenseAgreementDialogFragment();
//      newFragment.setCancelable(false);
      newFragment.show(getSupportFragmentManager(), "license");
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.trip_list, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public void onFloatingActionButtonClick(View view) {
    this.showFragment(AddTripDetailsFragment.class, new Bundle(), true);
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

  public void populateInitialData() {
    List<Pastime> allPastimes = mPastimeDataProvider.getAll();
    if (allPastimes.isEmpty()) {
      populateInitialPastimeData();
    }
  }

  private void populateInitialPastimeData() {
    Pastime work = new Pastime("Work", "ic_pastime_work", new ArrayList<>());
    Pastime diving = new Pastime("Diving", "ic_pastime_diving", new ArrayList<>());
    Pastime dining = new Pastime("Dining", "ic_pastime_dining", new ArrayList<>());
    Pastime athletics = new Pastime("Athletics", "ic_pastime_athletics", new ArrayList<>());

    List<Pastime> pastimes = new ArrayList<>();
    pastimes.add(work);
    pastimes.add(diving);
    pastimes.add(dining);
    pastimes.add(athletics);
    mPastimeDataProvider.saveAll(pastimes);
  }
}
