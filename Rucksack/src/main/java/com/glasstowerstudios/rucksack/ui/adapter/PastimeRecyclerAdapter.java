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
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.PackableItem;
import com.glasstowerstudios.rucksack.model.Pastime;
import com.glasstowerstudios.rucksack.ui.view.PastimeCard;
import com.glasstowerstudios.rucksack.util.data.PastimeDataProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} for {@link PackableItem} objects.
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

  @Inject PastimeDataProvider mPastimeDataProvider;
  private List<Pastime> mPastimes;

  public PastimeRecyclerAdapter() {
    this(null);
    Injector.INSTANCE.getApplicationComponent().inject(this);
  }

  public PastimeRecyclerAdapter(List<Pastime> pastimes) {
    if (pastimes != null) {
      setItems(pastimes);
    } else {
      setItems(new ArrayList<>());
    }
  }

  // Create new views (invoked by the layout manager)
  @Override
  public PastimeRecyclerAdapter.PastimeViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
    PastimeCard card = (PastimeCard) LayoutInflater.from(parent.getContext())
                                                .inflate(R.layout.pastime_card, parent, false);

    card.setOnClickListener(v1 -> {
      PastimeCard cardReference = (PastimeCard) v1;
      cardReference.toggle();
    });

    PastimeViewHolder vh = new PastimeViewHolder(card);
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
    sortPastimesByName();
    notifyDataSetChanged();
  }

  public void add(Pastime pastime, int position) {
    mPastimes.add(position, pastime);
    sortPastimesByName();
    notifyDataSetChanged();
  }

  public void setItems(List<Pastime> pastimes) {
    mPastimes = pastimes;
    sortPastimesByName();
    notifyDataSetChanged();
  }

  public void remove(int position) {
    Pastime p = mPastimes.get(position);
    mPastimes.remove(position);
    mPastimeDataProvider.delete(p);
    sortPastimesByName();
    notifyDataSetChanged();
  }

  private void sortPastimesByName() {
    mPastimes = Observable.from(mPastimes).toSortedList().toBlocking().first();
  }
}
