package com.glasstowerstudios.rucksack.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.activity.TripsActivity;
import com.glasstowerstudios.rucksack.ui.view.PastimeSelector;
import com.glasstowerstudios.rucksack.util.TemporalFormatter;
import com.glasstowerstudios.rucksack.util.data.TripDataProvider;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} allowing the user to select a set of {@link Pastime}s to add to a {@link Trip}
 * currently under construction.
 */
public class TripPastimeSelectionFragment extends Fragment {
  public static final String TRIP_DESTINATION = "Trip.Destination";
  public static final String TRIP_START_DATE = "Trip.StartDate";
  public static final String TRIP_NUM_NIGHTS = "Trip.NumNights";

  private static final String LOGTAG = TripPastimeSelectionFragment.class.getSimpleName();

  private DateTime mTripStartDate;
  private int mTripNumNights;
  private String mTripDestination;

  @Bind(R.id.pastime_selector) PastimeSelector mPastimeSelector;

  @Inject TripDataProvider mTripDataProvider;

  @Override
  public void onCreate(Bundle aSavedInstanceState) {
    super.onCreate(aSavedInstanceState);

    Injector.INSTANCE.getApplicationComponent().inject(this);

    BaseActivity act = (BaseActivity) getActivity();
    ActionBar appBar = act.getSupportActionBar();
    if (appBar != null) {
      appBar.setTitle(R.string.select_pastimes);
    }

    Bundle args = getArguments();
    mTripDestination = args.getString(TRIP_DESTINATION);
    mTripNumNights = args.getInt(TRIP_NUM_NIGHTS);

    String startDateString = args.getString(TRIP_START_DATE);
    mTripStartDate = DateTime.parse(startDateString, TemporalFormatter.TRIP_DATES_FORMATTER);

    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_select_pastimes, root, false);

    ButterKnife.bind(this, v);

    return v;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.submit_action_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.confirm:
        Trip trip = createTripFromInput();
        mTripDataProvider.save(trip);
        Intent newTripsIntent =
          TripsActivity.newIntent(TripsActivity.class, getContext(), getString(R.string.trips));
        newTripsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newTripsIntent);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private Trip createTripFromInput() {
    return new Trip(mTripDestination, mTripStartDate, mTripNumNights,
                    mPastimeSelector.getSelectedPastimes());
  }
}
