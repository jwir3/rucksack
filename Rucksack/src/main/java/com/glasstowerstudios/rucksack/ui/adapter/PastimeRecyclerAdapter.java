package com.glasstowerstudios.rucksack.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.PackItem;
import com.glasstowerstudios.rucksack.model.Pastime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} for {@link PackItem} objects.
 */
public class PastimeRecyclerAdapter extends RecyclerView.Adapter<PastimeRecyclerAdapter.PastimeViewHolder> {
  public static class PastimeViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.pastime_icon) protected ImageView mPastimeIconImageView;
    @Bind(R.id.pastime_name) protected TextView mPastimeNameTextView;

    public PastimeViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  private List<Pastime> mPastimes;

  public PastimeRecyclerAdapter() {
    this(null);
  }

  public PastimeRecyclerAdapter(List<Pastime> pastimes) {
    if (pastimes != null) {
      setItems(pastimes);
    } else {
      setItems(new ArrayList<Pastime>());
    }
  }

  // Create new views (invoked by the layout manager)
  @Override
  public PastimeRecyclerAdapter.PastimeViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext())
                           .inflate(R.layout.pastime_card, parent, false);

    PastimeViewHolder vh = new PastimeViewHolder(v);
    return vh;
  }

  @Override
  public void onBindViewHolder(PastimeViewHolder holder, int position) {
    Pastime pastime = mPastimes.get(position);
    holder.mPastimeNameTextView.setText(pastime.getName());

    Context c = holder.mPastimeIconImageView.getContext();
    Drawable pastimeIcon = pastime.getIcon(c);
    holder.mPastimeIconImageView.setImageDrawable(pastimeIcon);
  }

  @Override
  public int getItemCount() {
    return mPastimes.size();
  }

  public void add(Pastime pastime) {
    add(pastime, mPastimes.size());
  }

  public void add(Pastime pastime, int position) {
    mPastimes.add(position, pastime);
    notifyDataSetChanged();
  }

  public void setItems(List<Pastime> pastimes) {
    mPastimes = pastimes;
    notifyDataSetChanged();
  }

  public void remove(int position) {
    Pastime p = mPastimes.get(position);
    mPastimes.remove(position);
    p.delete();
    notifyDataSetChanged();
  }
}
