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
import com.glasstowerstudios.rucksack.ui.observer.PastimeSelectionListener;
import com.glasstowerstudios.rucksack.ui.view.PastimeCard;
import com.glasstowerstudios.rucksack.ui.view.PastimeSelector;
import com.glasstowerstudios.rucksack.util.data.PastimeDataProvider;

import java.util.ArrayList;
import java.util.LinkedList;
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

  private List<Integer> mSelectedItems;

  private List<PastimeSelectionListener> mPastimeSelectionListeners = new LinkedList<>();

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

    mSelectedItems = new ArrayList<>();
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
        if (mSelectedItems.isEmpty()) {
          mSelectedItems.add(-1);
        }

        if (mSelectedItems.get(0) > -1) {
          notifyItemChanged(mSelectedItems.get(0));
        }

        if (viewLayoutPosition == mSelectedItems.get(0)) {
          mSelectedItems.add(0, -1);
          notifyItemChanged(viewLayoutPosition);
        } else {
          mSelectedItems.add(0, viewLayoutPosition);
          notifyItemChanged(mSelectedItems.get(0));
        }

        break;

      case MULTI:
      default:
        if (cardReference.isChecked()) {
          mSelectedItems.remove(Integer.valueOf(viewLayoutPosition));
        } else {
          mSelectedItems.add(viewLayoutPosition);
        }

        cardReference.toggle();
    }
  }

  public List<Pastime> getSelectedPastimes() {
    List<Pastime> selectedPastimes = new LinkedList<>();
    for (Integer i : mSelectedItems) {
      selectedPastimes.add(mPastimes.get(i));
    }

    return selectedPastimes;
  }

  @Override
  public void onBindViewHolder(PastimeViewHolder holder, int position) {
    PastimeCard card = (PastimeCard) holder.itemView;

    if (isInSingleSelectionMode()) {
      ensureOnlyOneItemSelected(card, position);
    }

    card.setOnClickListener(v1 -> {
      toggleCardsBasedOnSelectability(v1, holder.getLayoutPosition());
      notifyPastimeSelectionListeners(mPastimes.get(holder.getLayoutPosition()));
    });

    Pastime pastime = mPastimes.get(position);
    holder.mPastimeNameTextView.setText(pastime.getName());

    Context c = holder.mPastimeIconImageView.getContext();
    Drawable pastimeIcon = pastime.getIcon(c);
    holder.mPastimeIconImageView.setImageDrawable(pastimeIcon);

  }

  private void ensureOnlyOneItemSelected(PastimeCard card, int position) {
    if (mSelectedItems.isEmpty()) {
      return;
    }

    if (mSelectedItems.get(0) != position) {
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

  public void addPastimeSelectionListener(PastimeSelectionListener listener) {
    mPastimeSelectionListeners.add(listener);
  }

  public void removePastimeSelectionListener(PastimeSelectionListener listener) {
    mPastimeSelectionListeners.remove(listener);
  }

  private void notifyPastimeSelectionListeners(Pastime pastime) {
    for (PastimeSelectionListener listener : mPastimeSelectionListeners) {
      listener.onPastimeSelected(pastime);
    }
  }

  private void sortPastimesByName() {
    mPastimes = Observable.from(mPastimes).toSortedList().toBlocking().first();
  }
}
