package com.glasstowerstudios.rucksack.ui.adapter;

import android.graphics.Paint;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.PackableItem;
import com.glasstowerstudios.rucksack.ui.observer.PackingListener;
import com.glasstowerstudios.rucksack.util.data.PackableItemDataProvider;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} for {@link PackableItem} objects.
 */
public class PackingListAdapter
  extends RecyclerView.Adapter<PackableItemRecyclerAdapter.PackableItemViewHolder> {

  private Comparator<PackableItem> mPackableItemComparator = new Comparator<PackableItem>() {
    @Override
    public int compare(PackableItem lhs, PackableItem rhs) {
      if (mReorganizeAfterSelection) {
        return lhs.compareIncludingPacking(rhs);
      }

      return lhs.compareExcludingPacking(rhs);
    }
  };

  private SortedList.Callback<PackableItem> sortCallback = new SortedList.Callback<PackableItem>() {
    @Override
    public int compare(PackableItem o1, PackableItem o2) {
      return mPackableItemComparator.compare(o1, o2);
    }

    @Override
    public void onInserted(int position, int count) {
    }

    @Override
    public void onRemoved(int position, int count) {
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
    }

    @Override
    public void onChanged(int position, int count) {
    }

    @Override
    public boolean areContentsTheSame(PackableItem oldItem, PackableItem newItem) {
      return (mPackableItemComparator.compare(oldItem, newItem) == 0);
    }

    @Override
    public boolean areItemsTheSame(PackableItem item1, PackableItem item2) {
      return (mPackableItemComparator.compare(item1, item2) == 0);
    }
  };

  // XXX_jwir3: This is a _sorted_ list. mItems.contains() uses a binary search, which means that if
  //            the two items are sorted differently (e.g. one is packed and one is not), it will not
  //            return what you expect!
  private SortedList<PackableItem> mItems = new SortedList<>(PackableItem.class, sortCallback);

  private int mBackgroundColor;
  private boolean mReorganizeAfterSelection;
  private PackingListener mPackingListener;

  @Inject PackableItemDataProvider mPackableItemProvider;

  public PackingListAdapter(List<PackableItem> items, int aBackgroundColor,
                            boolean aReorganizeAfterSelection) {
    Injector.INSTANCE.getApplicationComponent().inject(this);
    if (items != null) {
      mItems.addAll(items);
    }

    mBackgroundColor = aBackgroundColor;
    mReorganizeAfterSelection = aReorganizeAfterSelection;
  }

  // Create new views (invoked by the layout manager)
  @Override
  public PackableItemRecyclerAdapter.PackableItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                                               int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext())
                           .inflate(R.layout.packable_item_list_item, parent, false);

    v.setBackgroundColor(mBackgroundColor);
    PackableItemRecyclerAdapter.PackableItemViewHolder vh =
      new PackableItemRecyclerAdapter.PackableItemViewHolder(v);

    vh.mPackItemDeleteButton.setVisibility(View.GONE);

    vh.mPackableItemCheckbox.setVisibility(View.VISIBLE);
    vh.mPackableItemIcon.setVisibility(View.GONE);

    return vh;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(final PackableItemRecyclerAdapter.PackableItemViewHolder holder,
                               int position) {
    PackableItem currentItem = mItems.get(position);

    setupCheckStateForItem(holder, currentItem);

    holder.mPackItemNameTextView.setText(currentItem.getName());
    holder.mPackItemDeleteButton.setOnClickListener(v -> remove(currentItem));
  }

  private void setupCheckStateForItem(PackableItemRecyclerAdapter.PackableItemViewHolder holder,
                                      PackableItem currentItem) {
    holder.mPackableItemCheckbox.setOnCheckedChangeListener(null);
    if (currentItem.isPacked()) {
      holder.mPackableItemCheckbox.setChecked(true);
      if (mReorganizeAfterSelection) {
        holder.mPackItemNameTextView.setPaintFlags(holder.mPackItemNameTextView.getPaintFlags()
                                                   | Paint.STRIKE_THRU_TEXT_FLAG);
      }
    } else {
      holder.mPackableItemCheckbox.setChecked(false);
      if (mReorganizeAfterSelection) {
        holder.mPackItemNameTextView.setPaintFlags(holder.mPackItemNameTextView.getPaintFlags()
                                                   & (~Paint.STRIKE_THRU_TEXT_FLAG));
      }
    }

    holder.mPackableItemCheckbox.setOnCheckedChangeListener(
      (buttonView, isChecked) -> {
        PackableItem item = currentItem;
        selectItem(item, isChecked);
      });
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  public void add(PackableItem item) {
    mItems.add(item);
  }

  public void addAll(List<PackableItem> items) {
    for (PackableItem item : items) {
      mItems.add(item);
    }

    notifyDataSetChanged();
  }

  public void setItems(List<PackableItem> packableItems) {
    mItems.clear();
    mItems.addAll(packableItems);
    notifyDataSetChanged();
  }

  public void remove(PackableItem item) {
    mItems.remove(item);
    notifyDataSetChanged();
  }

  /**
   * Retrieve an immutable {@link List} of the {@link PackableItem}s that a user has selected from
   * this {@link PackableItemRecyclerAdapter}.
   *
   * @return A {@link List} containing the {@link PackableItem}s the user has selected.
   */
  public List<PackableItem> getSelectedItems() {
    List<PackableItem> selectedItems = new LinkedList<>();
    for (int i = 0; i < mItems.size(); i++) {
      PackableItem item = mItems.get(i);
      if (item.isPacked()) {
        selectedItems.add(item);
      }
    }

    return selectedItems;
  }

  public void selectItems(List<PackableItem> aItems) {
    for (PackableItem item : aItems) {
      int position = mItems.indexOf(item);
      if (position >= 0) {
        mItems.get(position).setPacked(true);
      }
    }

    notifyDataSetChanged();
  }

  public void selectItem(PackableItem item, boolean selected) {
    // We add and then remove the item so it gets re-sorted.
    mItems.remove(item);
    item.setPacked(selected);
    mItems.add(item);

    notifyPackingListenerPackableItemChangedStatus(item);

    notifyDataSetChanged();
  }

  public void selectAllItems() {
    for (int i = 0; i < mItems.size(); i++) {
      PackableItem item = mItems.get(i);
      item.setPacked(true);
    }

    notifyPackingListenerAllItemsChangedStatus();
    notifyDataSetChanged();
  }

  public void deselectAllItems() {
    for (int i = 0; i < mItems.size(); i++) {
      PackableItem item = mItems.get(i);
      item.setPacked(false);
    }

    notifyPackingListenerAllItemsChangedStatus();
    notifyDataSetChanged();
  }

  private void notifyPackingListenerAllItemsChangedStatus() {
    List<PackableItem> changedItems = new LinkedList<>();
    for (int i = 0; i < mItems.size(); i++) {
      changedItems.add(mItems.get(i));
    }

    mPackingListener.onPackingStatusChanged(changedItems);
  }

  private void notifyPackingListenerMultipleItemsChangedStatus(List<PackableItem> items) {
    mPackingListener.onPackingStatusChanged(items);
  }

  public void setPackingListener(PackingListener listener) {
    mPackingListener = listener;
  }

  public void removePackingListener() {
    mPackingListener = null;
  }

  private void notifyPackingListenerPackableItemChangedStatus(PackableItem item) {
    mPackingListener.onPackingStatusChanged(item);
  }
}
