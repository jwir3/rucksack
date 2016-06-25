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
import com.glasstowerstudios.rucksack.ui.observer.PackingListener;
import com.glasstowerstudios.rucksack.util.data.PackableItemDataProvider;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * A {@link PackingListView} is slightly different from a {@link PackableItemRecyclerView}. It holds
 * {@link PackableItem}s, just like its recycler view companion, but it allows the user to "check
 * off" items from the list, rather than add them (or not) to another component.
 *
 * Essentially, this means that the list can be set to visually indicate which of the {@link
 * PackableItem}s are packed by moving those items to the bottom of the list and striking through
 * the text of the name of those items.
 */
public class PackingListView
  extends RecyclerView
  implements PackingListener {

  @Override
  public void onPackingStatusChanged(PackableItem item) {
    notifyPackingListenersPackableItemChangedStatus(item);
  }

  @Override
  public void onPackingStatusChanged(List<PackableItem> items) {
    notifyPackingListenerMultipleItemsChangedStatus(items);
  }

  @Inject PackableItemDataProvider mPackableItemDataProvider;

  private AttributeSet mAttrs;
  private int mDefStyleAttr;
  private int mBackgroundColor;
  private PackingListAdapter mAdapter;
  private boolean mReorganizeAfterSelection = false;
  private List<PackingListener> mPackingListeners = new LinkedList<>();

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

    mAdapter = new PackingListAdapter(new LinkedList<>(), mBackgroundColor, mReorganizeAfterSelection);

    addItemDecoration(new DividerItemDecoration(getContext()));

    setAdapter(mAdapter);
    mAdapter.setPackingListener(this);
  }

  public void addItem(PackableItem aItem) {
    mAdapter.add(aItem);
  }

  public void addItems(List<PackableItem> aItems) {
    mAdapter.addAll(aItems);
  }

  public void selectAllItems() {
    mAdapter.selectAllItems();
  }

  public void deselectAllItems() {
    mAdapter.deselectAllItems();
  }

  public boolean areAnyItemsPacked() {
    return mAdapter.getSelectedItems().size() > 0;
  }

  public void addPackingListener(PackingListener listener) {
    mPackingListeners.add(listener);
  }

  public void removePackingListener(PackingListener listener) {
    mPackingListeners.remove(listener);
  }

  public void notifyPackingListenersPackableItemChangedStatus(PackableItem item) {
    for (PackingListener listener : mPackingListeners) {
      listener.onPackingStatusChanged(item);
    }
  }

  private void notifyPackingListenerMultipleItemsChangedStatus(List<PackableItem> items) {
    for (PackingListener listener : mPackingListeners) {
      listener.onPackingStatusChanged(items);
    }
  }

  public boolean areAllItemsPacked() {
    return mAdapter.getSelectedItems().size() == mAdapter.getItemCount();
  }
}
