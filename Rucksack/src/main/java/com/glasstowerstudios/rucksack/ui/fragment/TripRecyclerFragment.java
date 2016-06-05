package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Activity;
import android.content.Context;
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
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.activity.TripsActivity;
import com.glasstowerstudios.rucksack.ui.adapter.TripRecyclerAdapter;
import com.glasstowerstudios.rucksack.ui.base.DividerItemDecoration;
import com.glasstowerstudios.rucksack.util.data.TripDataProvider;
import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

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

  @Inject TripDataProvider mTripDataProvider;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Injector.INSTANCE.getApplicationComponent().inject(this);
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
    if (appBar != null) {
      appBar.setTitle(R.string.trips);
    }

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(layoutManager);

    mAdapter = new TripRecyclerAdapter(getTrips());
    mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
    mRecyclerView.setAdapter(mAdapter);

    final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener =
      new SwipeToDismissTouchListener<>(
        new RecyclerViewAdapter(mRecyclerView),
        new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
          @Override
          public boolean canDismiss(int position) {
            return true;
          }

          @Override
          public void onDismiss(RecyclerViewAdapter view, int position) {
            mAdapter.remove(position);

            // Check to see if we should display the empty view.
            refreshTripVisibility();
          }
        });

    mRecyclerView.setOnTouchListener(touchListener);
    mRecyclerView.setOnScrollListener((RecyclerView.OnScrollListener)touchListener.makeScrollListener());
    mRecyclerView.addOnItemTouchListener(new FixSwipeableItemClickListener(getContext(),
                                                                           (view, position) -> {
                                                                             if (view.getId() == R.id.txt_delete) {
                                                                               touchListener.processPendingDismisses();
                                                                             } else if (view.getId() == R.id.txt_undo) {
                                                                               touchListener.undoPendingDismiss();
                                                                             }
                                                                           }));

    mSwipeRefreshLayout.setOnRefreshListener(this);

    return createdView;
  }

  private List<Trip> getTrips() {
    List<Trip> trips = mTripDataProvider.getAll();
    return trips;
  }

  @Override
  public void onRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);

    List<Trip> trips = getTrips();
    mAdapter.setTrips(trips);

    refreshTripVisibility();

    mSwipeRefreshLayout.setRefreshing(false);
  }

  private void refreshTripVisibility() {
    if (mAdapter.getItemCount() > 0) {
      mRecyclerView.setVisibility(View.VISIBLE);
      mEmptyView.setVisibility(View.GONE);
    } else {
      mRecyclerView.setVisibility(View.GONE);
      mEmptyView.setVisibility(View.VISIBLE);
    }

  }

  private static class FixSwipeableItemClickListener extends SwipeableItemClickListener {

    public FixSwipeableItemClickListener(Context context, OnItemClickListener listener) {
      super(context, listener);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
  }
}
