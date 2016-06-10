package com.glasstowerstudios.rucksack.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.di.Injector;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.ui.observer.TripSelectionListener;
import com.glasstowerstudios.rucksack.util.TemporalFormatter;
import com.glasstowerstudios.rucksack.util.data.TripDataProvider;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link RecyclerView.Adapter} that adapts {@link Trip} objects to be presented in a
 * {@link RecyclerView}. Typically used by the {@link com.glasstowerstudios.rucksack.ui.fragment.TripRecyclerFragment}.
 */
public class TripRecyclerAdapter extends RecyclerView.Adapter<TripRecyclerAdapter.TripViewHolder> {
  private static final String LOGTAG = TripRecyclerAdapter.class.getSimpleName();

  private List<TripSelectionListener> mListeners = new LinkedList<>();

  public static class TripViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.trip_destination_name) protected TextView mDestinationName;
    @Bind(R.id.trip_date_range) protected TextView mTripDateRange;

    public TripViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  @Inject TripDataProvider mTripDataProvider;

  private List<Trip> mTrips;

  public TripRecyclerAdapter(List<Trip> trips) {
    mTrips = trips;
    Injector.INSTANCE.getApplicationComponent().inject(this);
  }

  // Create new views (invoked by the layout manager)
  @Override
  public TripRecyclerAdapter.TripViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext())
                           .inflate(R.layout.trip_list_item, parent, false);

    TripViewHolder vh = new TripViewHolder(v);

    return vh;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(TripRecyclerAdapter.TripViewHolder holder, int position) {
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    holder.mDestinationName.setText(mTrips.get(position).getDestinationName());

    DateTime startDate = mTrips.get(position).getStartDate();
    DateTime endDate = mTrips.get(position).getEndDate();

    DateTimeFormatter formatter = TemporalFormatter.TRIP_DATES_FORMATTER;
    String dateRangeTemplate =
      String.format(holder.mTripDateRange.getResources().getString(R.string.trip_date_range),
                    formatter.print(startDate),
                    formatter.print(endDate));
    holder.mTripDateRange.setText(dateRangeTemplate);
  }

  @Override
  public int getItemCount() {
    return mTrips.size();
  }

  public void add(Trip trip) {
    add(trip, mTrips.size());
  }

  public void add(Trip trip, int position) {
    mTrips.add(position, trip);
    notifyDataSetChanged();
  }

  public void setTrips(List<Trip> trips) {
    mTrips = trips;
    notifyDataSetChanged();
  }

  public void remove(int position) {
    Trip t = mTrips.get(position);
    mTrips.remove(position);
    mTripDataProvider.delete(t);
    notifyDataSetChanged();
  }

  public void clearTripSelectionListeners() {
    mListeners.clear();
  }

  public void addTripSelectionListener(TripSelectionListener listener) {
    mListeners.add(listener);
  }

  public void removeTripSelectionListener(TripSelectionListener listener) {
    mListeners.remove(listener);
  }

  public void notifyTripSelectionListeners(int position) {
    notifyTripSelectionListeners(mTrips.get(position));
  }

  private void notifyTripSelectionListeners(Trip selectedTrip) {
    for (TripSelectionListener listener : mListeners) {
      listener.onTripSelected(selectedTrip);
    }
  }
}

