package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.activity.TripsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A {@link Fragment} displayed when the user decides to add a new trip to the system. This will
 * prompt the user for the basic information about the trip and place the new trip in the
 * {@link TripListFragment}.
 */
public class AddTripFragment extends Fragment {

  private static final String LOGTAG = AddTripFragment.class.getSimpleName();

  @Bind(R.id.destinationInput)
  protected EditText mDestinationInput;

  public AddTripFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    TripsActivity act = (TripsActivity) getActivity();
    act.disableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    appBar.setDisplayHomeAsUpEnabled(true);
    appBar.setDisplayShowHomeEnabled(false);

    appBar.setTitle(R.string.addTrip);

    setHasOptionsMenu(true);

    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_add_trip, container, false);
    ButterKnife.bind(this, v);

    return v;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.add_trip, menu);
  }

  @Override
  public void onResume() {
    super.onResume();
    Activity act = getActivity();
    BaseActivity baseAct = (BaseActivity) act;
    baseAct.lockNavigationDrawer();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.confirm:
        Trip trip = createTripFromInput();
        trip.save();
        TripsActivity act = (TripsActivity) getActivity();
        act.onBackPressed();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private Trip createTripFromInput() {
    String destName = mDestinationInput.getText().toString();
    return new Trip(destName);
  }
}
