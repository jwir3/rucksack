package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.activity.TripsActivity;
import com.glasstowerstudios.rucksack.ui.adapter.TripRecyclerAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A listing of trips within the system.
 */
public class TripRecyclerFragment
  extends Fragment
  implements SwipeRefreshLayout.OnRefreshListener {

  private static final String LOGTAG = TripRecyclerFragment.class.getSimpleName();

  @Bind(R.id.trip_recycler_view)
  protected RecyclerView mRecyclerView;
  private TripRecyclerAdapter mAdapter;

  @Bind(R.id.empty_view)
  protected View mEmptyView;

  @Bind(R.id.trips_swipe_refresh)
  protected SwipeRefreshLayout mSwipeRefreshLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    Activity act = getActivity();
    BaseActivity baseAct = (BaseActivity) act;
    baseAct.unlockNavigationDrawer();

    onRefresh();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View createdView = inflater.inflate(R.layout.fragment_trip_recycler, container, false);
    ButterKnife.bind(this, createdView);

    TripsActivity act = (TripsActivity) getContext();
    act.enableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    appBar.setTitle(R.string.trips);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(layoutManager);

    mAdapter = new TripRecyclerAdapter(getTrips());
    mRecyclerView.setAdapter(mAdapter);

    mSwipeRefreshLayout.setOnRefreshListener(this);

    return createdView;
  }

  private List<Trip> getTrips() {
    // Get all trips from the database.
    List<Trip> trips = Trip.getAll();
    return trips;
  }

  @Override
  public void onRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);

    List<Trip> trips = getTrips();

    if (trips.size() > 0) {
      mRecyclerView.setVisibility(View.VISIBLE);
      mEmptyView.setVisibility(View.GONE);
    } else {
      mRecyclerView.setVisibility(View.GONE);
      mEmptyView.setVisibility(View.VISIBLE);
    }

    mAdapter.setTrips(trips);

    mSwipeRefreshLayout.setRefreshing(false);
  }
}
