package com.glasstowerstudios.rucksack.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.PackItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} for {@link PackItem} objects.
 */
public class PackItemRecyclerAdapter extends RecyclerView.Adapter<PackItemRecyclerAdapter.PackItemViewHolder> {
  public static class PackItemViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.pack_item_name_textview) protected TextView mPackItemNameTextView;
    @Bind(R.id.pack_item_delete_button) protected ImageButton mPackItemDeleteButton;

    public PackItemViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  private List<PackItem> mItems;

  public PackItemRecyclerAdapter(List<PackItem> items) {
    if (items != null) {
      mItems = items;
    } else {
      mItems = new ArrayList<>();
    }
  }

  // Create new views (invoked by the layout manager)
  @Override
  public PackItemRecyclerAdapter.PackItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext())
                           .inflate(R.layout.pack_item_list_item, parent, false);

    PackItemViewHolder vh = new PackItemViewHolder(v);
    return vh;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(final PackItemRecyclerAdapter.PackItemViewHolder holder,
                               int position) {
    holder.mPackItemNameTextView.setText(mItems.get(position).getName());
    holder.mPackItemDeleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        remove(holder.getAdapterPosition());
      }
    });
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  public void add(PackItem item) {
    add(item, mItems.size());
  }

  public void add(PackItem trip, int position) {
    mItems.add(position, trip);
    notifyDataSetChanged();
  }

  public void setItems(List<PackItem> packItems) {
    mItems = packItems;
    notifyDataSetChanged();
  }

  public void remove(int position) {
    PackItem p = mItems.get(position);
    mItems.remove(position);
    p.delete();
    notifyDataSetChanged();
  }
}
