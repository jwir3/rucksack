package com.glasstowerstudios.rucksack.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.ui.activity.BaseActivity;
import com.glasstowerstudios.rucksack.ui.view.PastimeSelector;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} containing a {@link RecyclerView} of {@link Pastime} objects.
 */
public class PastimeRecyclerFragment
  extends Fragment
  implements SwipeRefreshLayout.OnRefreshListener {

  private static final String LOGTAG = PastimeRecyclerFragment.class.getSimpleName();

  @Bind(R.id.pastime_selector) PastimeSelector mSelector;

  @Bind(R.id.pastime_swipe_refresh)
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
    View createdView = inflater.inflate(R.layout.fragment_pastime_recycler, container, false);
    ButterKnife.bind(this, createdView);

    BaseActivity act = (BaseActivity) getContext();
    act.enableFloatingActionButton();

    ActionBar appBar = act.getSupportActionBar();
    appBar.setTitle(R.string.pastimes);

    mSwipeRefreshLayout.setOnRefreshListener(this);

    return createdView;
  }

  @Override
  public void onRefresh() {
    mSwipeRefreshLayout.setRefreshing(true);

    mSelector.refresh();

    mSwipeRefreshLayout.setRefreshing(false);
  }
}
