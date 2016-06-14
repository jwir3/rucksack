package com.glasstowerstudios.rucksack.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.PackableItem;
import com.glasstowerstudios.rucksack.ui.adapter.PackingListAdapter;
import com.glasstowerstudios.rucksack.ui.base.DividerItemDecoration;
import com.glasstowerstudios.rucksack.util.data.PackableItemDataProvider;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 *
 */
public class PackingListView extends RecyclerView {
  @Inject PackableItemDataProvider mPackableItemDataProvider;

  private AttributeSet mAttrs;
  private int mDefStyleAttr;
  private int mBackgroundColor;
  private PackingListAdapter mAdapter;
  private boolean mReorganizeAfterSelection = false;

  public PackingListView(Context context) {
    super(context);
    init();
  }

  public PackingListView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mAttrs = attrs;
    init();
  }

  public PackingListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
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
        getContext().obtainStyledAttributes(mAttrs, R.styleable.PackingListView,
                                            mDefStyleAttr, 0);

      if (a.hasValue(R.styleable.PackingListView_backgroundColor)) {
        mBackgroundColor = a.getColor(R.styleable.PackingListView_backgroundColor,
                                      getResources().getColor(android.R.color.white));
      } else {
        mBackgroundColor = getResources().getColor(android.R.color.white);
      }

      if (a.hasValue(R.styleable.PackingListView_reorganizeAfterSelection)) {
        mReorganizeAfterSelection =
          a.getBoolean(R.styleable.PackingListView_reorganizeAfterSelection, false);
      }

      a.recycle();
    }

//    Injector.INSTANCE.getApplicationComponent().inject(this);
//
//    List<PackableItem> items = getItems();
    mAdapter = new PackingListAdapter(new LinkedList<>(), mBackgroundColor, mReorganizeAfterSelection);

    addItemDecoration(new DividerItemDecoration(getContext()));

    setAdapter(mAdapter);
  }

  public void addItem(PackableItem aItem) {
    mAdapter.add(aItem);
  }

  public void addItems(List<PackableItem> aItems) {
    mAdapter.addAll(aItems);
  }
}
