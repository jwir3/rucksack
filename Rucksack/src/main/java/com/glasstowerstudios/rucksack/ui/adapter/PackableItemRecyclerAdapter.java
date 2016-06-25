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

import java.util.LinkedList;
import java.util.List;

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

  private List<PackableItem> mItems = new LinkedList<>();

  private boolean mShouldAllowDelete = false;
  private int mBackgroundColor;
  private boolean mSelectable = false;

  @Inject PackableItemDataProvider mPackableItemProvider;

  public PackableItemRecyclerAdapter(List<PackableItem> items, boolean aShouldAllowDelete,
                                     int aBackgroundColor, boolean aSelectable) {
    Injector.INSTANCE.getApplicationComponent().inject(this);
    if (items != null) {
      mItems.addAll(items);
    }

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

    setupCheckStateForItem(holder, currentItem);

    holder.mPackItemNameTextView.setText(currentItem.getName());
    holder.mPackItemDeleteButton.setOnClickListener(v -> remove(currentItem));
  }

  private void setupCheckStateForItem(PackableItemViewHolder holder, PackableItem currentItem) {
    holder.mPackableItemCheckbox.setOnCheckedChangeListener(null);
    if (currentItem.isPacked()) {
      holder.mPackableItemCheckbox.setChecked(true);
    } else {
      holder.mPackableItemCheckbox.setChecked(false);
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
    notifyDataSetChanged();
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
    mPackableItemProvider.delete(item);
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
    item.setPacked(selected);

    notifyDataSetChanged();
  }
}
