package com.glasstowerstudios.rucksack.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.PackableItem;
import com.glasstowerstudios.rucksack.util.data.PackableItemDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} for {@link PackableItem} objects.
 */
public class PackableItemRecyclerAdapter
  extends RecyclerView.Adapter<PackableItemRecyclerAdapter.PackableItemViewHolder> {

  private static final String LOGTAG = PackableItemRecyclerAdapter.class.getSimpleName();

  public static class PackableItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.packable_item_checkbox) protected CheckBox mPackableItemCheckbox;
    @Bind(R.id.packable_item_icon) protected ImageView mPackableItemIcon;
    @Bind(R.id.pack_item_name_textview) protected TextView mPackItemNameTextView;
    @Bind(R.id.pack_item_delete_button) protected ImageButton mPackItemDeleteButton;

    public PackableItemViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  private List<PackableItem> mItems;
  private List<PackableItem> mSelectedItems = new ArrayList<>();
  private boolean mShouldAllowDelete = false;
  private int mBackgroundColor;
  private boolean mSelectable = false;

  @Inject PackableItemDataProvider mPackableItemProvider;

  public PackableItemRecyclerAdapter(List<PackableItem> items, boolean aShouldAllowDelete,
                                     int aBackgroundColor, boolean aSelectable) {
    Injector.INSTANCE.getApplicationComponent().inject(this);
    if (items != null) {
      mItems = items;
    } else {
      mItems = new ArrayList<>();
    }

    sort();

    mShouldAllowDelete = aShouldAllowDelete;
    mBackgroundColor = aBackgroundColor;
    mSelectable = aSelectable;
  }

  // Create new views (invoked by the layout manager)
  @Override
  public PackableItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext())
                           .inflate(R.layout.packable_item_list_item, parent, false);

    v.setBackgroundColor(mBackgroundColor);
    PackableItemViewHolder vh = new PackableItemViewHolder(v);

    if (!mShouldAllowDelete) {
      vh.mPackItemDeleteButton.setVisibility(View.GONE);
    }

    if (!mSelectable) {
      vh.mPackableItemCheckbox.setVisibility(View.GONE);
      vh.mPackableItemIcon.setVisibility(View.VISIBLE);
    } else {
      vh.mPackableItemCheckbox.setVisibility(View.VISIBLE);
      vh.mPackableItemIcon.setVisibility(View.GONE);
    }

    return vh;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(final PackableItemViewHolder holder,
                               int position) {
    PackableItem currentItem = mItems.get(position);
    if (mSelectedItems.contains(currentItem)) {
      holder.mPackableItemCheckbox.setChecked(true);
    }

    holder.mPackItemNameTextView.setText(currentItem.getName());
    holder.mPackItemDeleteButton.setOnClickListener(v -> remove(holder.getAdapterPosition()));
    holder.mPackableItemCheckbox.setOnCheckedChangeListener(
      (buttonView, isChecked) -> {
        PackableItem item = mItems.get(position);
        if (isChecked) {
          mSelectedItems.add(item);
        } else {
          mSelectedItems.remove(item);
        }
      });
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  public void add(PackableItem item) {
    add(item, mItems.size());
  }

  public void add(PackableItem packableItem, int position) {
    mItems.add(position, packableItem);
    sort();
    notifyDataSetChanged();
  }

  public void addAll(List<PackableItem> items) {
    for (PackableItem item : items) {
      mItems.add(item);
    }

    sort();

    notifyDataSetChanged();
  }

  public void setItems(List<PackableItem> packableItems) {
    mItems = packableItems;
    sort();
    notifyDataSetChanged();
  }

  public void remove(int position) {
    mItems.remove(position);
    mPackableItemProvider.saveAll(mItems);
    sort();
    notifyDataSetChanged();
  }

  /**
   * Retrieve an immutable {@link List} of the {@link PackableItem}s that a user has selected from
   * this {@link PackableItemRecyclerAdapter}.
   *
   * @return A {@link List} containing the {@link PackableItem}s the user has selected.
   */
  public List<PackableItem> getSelectedItems() {
    return mSelectedItems;
  }

  public void selectItems(List<PackableItem> aItems) {
    mSelectedItems = aItems;
  }

  private void sort() {
    removeDuplicates();
    Collections.sort(mItems, (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));
  }

  private void removeDuplicates() {
    Set<PackableItem> packableSet = new HashSet<>();
    packableSet.addAll(mItems);
    mItems.clear();
    mItems.addAll(packableSet);
  }
}
