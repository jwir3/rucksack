package com.glasstowerstudios.rucksack.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.PackableItem;
import com.glasstowerstudios.rucksack.ui.adapter.PackableItemRecyclerAdapter;
import com.glasstowerstudios.rucksack.ui.base.DividerItemDecoration;
import com.glasstowerstudios.rucksack.util.data.PackableItemDataProvider;

import java.util.List;

import javax.inject.Inject;

/**
 *
 */
public class PackableItemRecyclerView extends RecyclerView {

  @Inject PackableItemDataProvider mPackableItemDataProvider;

  private AttributeSet mAttrs;
  private int mDefStyleAttr;
  private boolean mAllowDeletion = false;
  private int mBackgroundColor;
  private PackableItemRecyclerAdapter mAdapter;
  private boolean mSelectable = false;

  public PackableItemRecyclerView(Context context) {
    super(context);
    init();
  }

  public PackableItemRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mAttrs = attrs;
    init();
  }

  public PackableItemRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mAttrs = attrs;
    mDefStyleAttr = defStyle;
    init();
  }

  private void init() {
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    setLayoutManager(layoutManager);

    if (mAttrs != null) {
      TypedArray a =
        getContext().obtainStyledAttributes(mAttrs, R.styleable.PackableItemRecyclerView,
                                            mDefStyleAttr, 0);

      if (a.hasValue(R.styleable.PackableItemRecyclerView_allowDeletion)) {
        mAllowDeletion = a.getBoolean(R.styleable.PackableItemRecyclerView_allowDeletion, false);
      }

      if (a.hasValue(R.styleable.PackableItemRecyclerView_backgroundColor)) {
        mBackgroundColor = a.getColor(R.styleable.PackableItemRecyclerView_backgroundColor,
                                      getResources().getColor(android.R.color.white));
      } else {
        mBackgroundColor = getResources().getColor(android.R.color.white);
      }

      if (a.hasValue(R.styleable.PackableItemRecyclerView_selectable)) {
        mSelectable = a.getBoolean(R.styleable.PackableItemRecyclerView_selectable, false);
      }

      a.recycle();
    }

    Injector.INSTANCE.getApplicationComponent().inject(this);

    List<PackableItem> items = getItems();
    mAdapter = new PackableItemRecyclerAdapter(items, mAllowDeletion, mBackgroundColor,
                                               mSelectable);

    addItemDecoration(new DividerItemDecoration(getContext()));

    setAdapter(mAdapter);
  }

  public void registerAdapterDataObserver(AdapterDataObserver aObserver) {
    mAdapter.registerAdapterDataObserver(aObserver);
  }

  public void addItem(PackableItem aItem) {
    mAdapter.add(aItem);
  }

  public int getItemCount() {
    return mAdapter.getItemCount();
  }

  public void refresh() {
    List<PackableItem> items = getItems();
    mAdapter.setItems(items);

    if (mAdapter.getItemCount() > 0) {
      setVisibility(View.VISIBLE);
    } else {
      setVisibility(View.GONE);
    }
  }
  private List<PackableItem> getItems() {
    return mPackableItemDataProvider.getAll();
  }
}
