package com.glasstowerstudios.rucksack.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.PackableItem;
import com.glasstowerstudios.rucksack.util.data.PackableItemDataProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} for {@link PackableItem} objects.
 */
public class PackableItemRecyclerAdapter
  extends RecyclerView.Adapter<PackableItemRecyclerAdapter.PackableItemViewHolder> {

  public static class PackableItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.pack_item_name_textview) protected TextView mPackItemNameTextView;
    @Bind(R.id.pack_item_delete_button) protected ImageButton mPackItemDeleteButton;

    public PackableItemViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  private List<PackableItem> mItems;

  @Inject PackableItemDataProvider mPackableItemProvider;

  public PackableItemRecyclerAdapter(List<PackableItem> items) {
    Injector.INSTANCE.getApplicationComponent().inject(this);
    if (items != null) {
      mItems = items;
    } else {
      mItems = new ArrayList<>();
    }
  }

  // Create new views (invoked by the layout manager)
  @Override
  public PackableItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext())
                           .inflate(R.layout.pack_item_list_item, parent, false);

    PackableItemViewHolder vh = new PackableItemViewHolder(v);
    return vh;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(final PackableItemViewHolder holder,
                               int position) {
    holder.mPackItemNameTextView.setText(mItems.get(position).getName());
    holder.mPackItemDeleteButton.setOnClickListener(v -> remove(holder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  public void add(PackableItem item) {
    add(item, mItems.size());
  }

  public void add(PackableItem trip, int position) {
    mItems.add(position, trip);
    notifyDataSetChanged();
  }

  public void setItems(List<PackableItem> packableItems) {
    mItems = packableItems;
    notifyDataSetChanged();
  }

  public void remove(int position) {
    PackableItem p = mItems.get(position);
    mItems.remove(position);
    mPackableItemProvider.saveAll(mItems);
    notifyDataSetChanged();
  }
}
