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
import com.glasstowerstudios.rucksack.ui.view.PastimeSelector;
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

  private int mSelectedItem = -1;

  private PastimeSelector.SelectionAttribute mSelectionAttribute =
    PastimeSelector.SelectionAttribute.NONE;

  public PastimeRecyclerAdapter(PastimeSelector.SelectionAttribute selectability) {
    this(null, selectability);
    Injector.INSTANCE.getApplicationComponent().inject(this);
  }

  public PastimeRecyclerAdapter(List<Pastime> pastimes,
                                PastimeSelector.SelectionAttribute selectability) {
    mSelectionAttribute = selectability;

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

    PastimeViewHolder vh = new PastimeViewHolder(card);
    return vh;
  }

  private void toggleCardsBasedOnSelectability(View v1, int viewLayoutPosition) {
    PastimeCard cardReference = (PastimeCard) v1;

    switch (mSelectionAttribute) {
      case NONE:
        // Don't need to do anything here.
        break;

      case SINGLE:
        if (mSelectedItem > -1) {
          notifyItemChanged(mSelectedItem);
        }

        if (viewLayoutPosition == mSelectedItem) {
          mSelectedItem = -1;
          notifyItemChanged(viewLayoutPosition);
        } else {

          mSelectedItem = viewLayoutPosition;
          notifyItemChanged(mSelectedItem);
        }

        break;

      case MULTI:
      default:
        cardReference.toggle();
    }
  }

  @Override
  public void onBindViewHolder(PastimeViewHolder holder, int position) {
    PastimeCard card = (PastimeCard) holder.itemView;

    if (isInSingleSelectionMode()) {
      ensureOnlyOneItemSelected(card, position);
    }

    card.setOnClickListener(v1 -> {
      toggleCardsBasedOnSelectability(v1, holder.getLayoutPosition());
    });

    Pastime pastime = mPastimes.get(position);
    holder.mPastimeNameTextView.setText(pastime.getName());

    Context c = holder.mPastimeIconImageView.getContext();
    Drawable pastimeIcon = pastime.getIcon(c);
    holder.mPastimeIconImageView.setImageDrawable(pastimeIcon);

  }

  private void ensureOnlyOneItemSelected(PastimeCard card, int position) {
    if (mSelectedItem != position) {
      card.setChecked(false);
    } else {
      card.setChecked(true);
    }
  }

  private boolean isInSingleSelectionMode() {
    return mSelectionAttribute == PastimeSelector.SelectionAttribute.SINGLE;
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
