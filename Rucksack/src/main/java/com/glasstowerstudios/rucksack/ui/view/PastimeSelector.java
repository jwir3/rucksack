package com.glasstowerstudios.rucksack.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.ui.adapter.PastimeRecyclerAdapter;
import com.glasstowerstudios.rucksack.ui.observer.PastimeSelectionListener;
import com.glasstowerstudios.rucksack.util.data.PastimeDataProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link View} that allows the selection of one or more {@link Pastime} objects. It also contains
 * an empty view.
 */
public class PastimeSelector extends LinearLayout {

  public enum SelectionAttribute {
    NONE, SINGLE, MULTI
  }

  @Inject PastimeDataProvider mPastimeDataProvider;

  @Bind(R.id.pastime_recycler_view)
  protected RecyclerView mRecyclerView;

  @Bind(R.id.empty_view)
  protected View mEmptyView;

  private SelectionAttribute mSelectability = SelectionAttribute.NONE;

  private PastimeRecyclerAdapter mAdapter;

  public PastimeSelector(Context context) {
    super(context);
    init();
  }

  public PastimeSelector(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    setupSelectionAttributes(attrs, 0);
    init();
  }

  public PastimeSelector(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setupSelectionAttributes(attrs, defStyle);
    init();
  }

  public void refresh() {
    setPastimes(populatePastimes());
  }

  public void setPastimes(List<Pastime> pastimes) {
    mAdapter.setItems(pastimes);
    mAdapter.notifyDataSetChanged();
  }

  public List<Pastime> getSelectedPastimes() {
    return mAdapter.getSelectedPastimes();
  }

  private void init() {
    inflate(getContext(), R.layout.pastime_selector, this);

    Injector.INSTANCE.getApplicationComponent().inject(this);

    ButterKnife.bind(this);

    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

    mAdapter = new PastimeRecyclerAdapter(mSelectability);

    RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
        refreshVisibility();
      }
    };

    mAdapter.registerAdapterDataObserver(observer);

    mRecyclerView.setAdapter(mAdapter);

    refresh();
  }

  private void setupSelectionAttributes(AttributeSet attrs, int defStyle) {
    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PastimeSelector,
                                                       defStyle, 0);

    int selectionAttr = a.getInt(R.styleable.PastimeSelector_selection, 0);

    switch (selectionAttr) {
      case 0:
        mSelectability = SelectionAttribute.NONE;
        break;

      case 1:
        mSelectability = SelectionAttribute.SINGLE;
        break;

      default:
        mSelectability = SelectionAttribute.MULTI;
    }
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
  private List<Pastime> populatePastimes() {
    List<Pastime> allPastimes = mPastimeDataProvider.getAll();

    if (allPastimes.isEmpty()) {
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

      allPastimes = mPastimeDataProvider.getAll();
    }

    return allPastimes;
  }

  public void addPastimeSelectionListener(PastimeSelectionListener listener) {
    mAdapter.addPastimeSelectionListener(listener);
  }

  public void removePastimeSelectionListener(PastimeSelectionListener listener) {
    mAdapter.removePastimeSelectionListener(listener);
  }
}
