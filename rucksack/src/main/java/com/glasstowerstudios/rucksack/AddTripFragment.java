package com.glasstowerstudios.rucksack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A {@link Fragment} displayed when the user decides to add a new trip to the system. This will
 * prompt the user for the basic information about the trip and place the new trip in the
 * {@link com.glasstowerstudios.rucksack.ui.TripListFragment}.
 */
public class AddTripFragment extends Fragment {

  private static final String LOGTAG = AddTripFragment.class.getSimpleName();

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

    setHasOptionsMenu(true);

    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_add_trip, container, false);
  }
}
