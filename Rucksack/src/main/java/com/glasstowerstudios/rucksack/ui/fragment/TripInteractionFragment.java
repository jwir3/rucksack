package com.glasstowerstudios.rucksack.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.view.PackingListView;
import com.glasstowerstudios.rucksack.util.TemporalFormatter;
import com.glasstowerstudios.rucksack.util.data.TripDataProvider;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} that allows users to interact with a {@link Trip} they have previously
 * created.
 */
public class TripInteractionFragment extends Fragment {

  public static final String TRIP_KEY = "Trip";

  @Bind(R.id.trip_destination) TextView mTripDestination;
  @Bind(R.id.trip_dates) TextView mTripDates;
  @Bind(R.id.packable_item_list) PackingListView mPackingListView;

  private Trip mTrip;

  @Inject TripDataProvider mTripDataProvider;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActionBar appBar = ((BaseActivity) getActivity()).getSupportActionBar();
    if (appBar != null) {
      appBar.setTitle(R.string.packing_list);
    }

    Injector.INSTANCE.getApplicationComponent().inject(this);

    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_trip_interaction, root, false);

    ButterKnife.bind(this, v);

    populateDataFromArguments();

    BaseActivity act = (BaseActivity) getActivity();
    act.disableFloatingActionButton();

    mTripDestination.setText(mTrip.getDestinationName());

    String startDate = TemporalFormatter.TRIP_DATES_FORMATTER.print(mTrip.getStartDate());
    String endDate = TemporalFormatter.TRIP_DATES_FORMATTER.print(mTrip.getEndDate());
    String dateString = String.format(getString(R.string.trip_date_range), startDate, endDate);
    mTripDates.setText(dateString);

    return v;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.submit_with_delete, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    BaseActivity act = (BaseActivity) getActivity();

    switch (item.getItemId()) {
      case R.id.confirm:
        saveTrip();
        act.onBackPressed();
        return true;

      case R.id.delete:
        deleteTrip();
        act.onBackPressed();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void saveTrip() {
    mTripDataProvider.update(mTrip);
  }

  private void deleteTrip() {
    mTripDataProvider.delete(mTrip);
  }

  private void populateDataFromArguments() {
    Bundle arguments = getArguments();
    if (arguments != null) {
      mTrip = arguments.getParcelable(TRIP_KEY);
    }

    mPackingListView.addItems(mTrip.getPackingListItems());
  }
}
