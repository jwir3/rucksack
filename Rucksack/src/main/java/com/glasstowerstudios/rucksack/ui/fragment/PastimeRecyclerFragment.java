package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.activity.TripsActivity;
import com.glasstowerstudios.rucksack.ui.adapter.PastimeRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} containing a {@link RecyclerView} of {@link Pastime} objects.
 */
public class PastimeRecyclerFragment
  extends Fragment
  implements SwipeRefreshLayout.OnRefreshListener {

  private static final String LOGTAG = PastimeRecyclerFragment.class.getSimpleName();

  @Bind(R.id.pastime_recycler_view)
  protected RecyclerView mRecyclerView;

  @Bind(R.id.pastime_swipe_refresh)
  protected SwipeRefreshLayout mSwipeRefreshLayout;

  @Bind(R.id.empty_view)
  protected View mEmptyView;

  private PastimeRecyclerAdapter mAdapter;

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
    View createdView = inflater.inflate(R.layout.fragment_pastime_recycler, container, false);
    ButterKnife.bind(this, createdView);

    TripsActivity act = (TripsActivity) getContext();
    act.disableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    appBar.setTitle(R.string.pastimes);

    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

    mAdapter = new PastimeRecyclerAdapter();

    RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
        refreshVisibility();
      }
    };

    mAdapter.registerAdapterDataObserver(observer);

    mRecyclerView.setAdapter(mAdapter);

    mSwipeRefreshLayout.setOnRefreshListener(this);

    return createdView;
  }

  @Override
  public void onRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);

    List<Pastime> pastimes = initPastimes();
    for (Pastime nextPastime : pastimes) {
      Log.d(LOGTAG, "Pastime: " + nextPastime.getName());
    }

    mAdapter.setItems(pastimes);

    refreshVisibility();

    mSwipeRefreshLayout.setRefreshing(false);
  }

  private void refreshVisibility() {
    if (mAdapter.getItemCount() > 0) {
      mRecyclerView.setVisibility(View.VISIBLE);
      mEmptyView.setVisibility(View.GONE);
    } else {
      mRecyclerView.setVisibility(View.GONE);
      mEmptyView.setVisibility(View.VISIBLE);
    }
  }

  // TODO: Remove this method in favor of a one-time data initialization.
  private List<Pastime> initPastimes() {
    Pastime work = new Pastime("Work", R.drawable.ic_pastime_work);
    Pastime diving = new Pastime("Diving", R.drawable.ic_pastime_diving);
    Pastime dining = new Pastime("Dining", R.drawable.ic_pastime_dining);
    Pastime athletics = new Pastime("Athletics", R.drawable.ic_pastime_athletics);

    ArrayList<Pastime> pastimes = new ArrayList<>();
    pastimes.add(work);
    pastimes.add(dining);
    pastimes.add(diving);
    pastimes.add(athletics);
    return pastimes;
  }
}
