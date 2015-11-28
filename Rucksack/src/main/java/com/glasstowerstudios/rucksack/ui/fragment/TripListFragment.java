package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class TripListFragment extends Fragment {

  private static final String LOGTAG = TripListFragment.class.getSimpleName();

  @Bind(R.id.trip_recycler_view)
  protected RecyclerView mRecyclerView;
  private TripRecyclerAdapter mAdapter;

  @Bind(R.id.empty_view)
  protected View mEmptyView;

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

    refresh();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View createdView = inflater.inflate(R.layout.fragment_trip_list, container, false);
    ButterKnife.bind(this, createdView);

    TripsActivity act = (TripsActivity) getContext();
    act.enableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    appBar.setTitle(R.string.trips);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(layoutManager);

    mAdapter = new TripRecyclerAdapter(getTrips());
    mRecyclerView.setAdapter(mAdapter);

    // sample code used to add a trip every 5 seconds
//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        while (true) {
//          try {
//            Thread.sleep(5000);
//          } catch (InterruptedException e) {
//            e.printStackTrace();
//          }
//
//          Trip t = new Trip("Dummy Trip " + new Random(System.currentTimeMillis()).nextInt());
//          t.save();
//        }
//      }
//    }).start();

    return createdView;
  }

  private List<Trip> getTrips() {
    // Get all trips from the database.
    List<Trip> trips = Trip.getAll();
    return trips;
  }

  private void refresh() {
    List<Trip> trips = getTrips();

    if (trips.size() > 0) {
      mRecyclerView.setVisibility(View.VISIBLE);
      mEmptyView.setVisibility(View.GONE);
    } else {
      mRecyclerView.setVisibility(View.GONE);
      mEmptyView.setVisibility(View.VISIBLE);
    }

    mAdapter.setTrips(trips);
  }
}
