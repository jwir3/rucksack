package com.glasstowerstudios.rucksack.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Trip;
import com.glasstowerstudios.rucksack.util.TemporalFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class TripRecyclerAdapter extends RecyclerView.Adapter<TripRecyclerAdapter.TripViewHolder> {
  public static class TripViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.trip_destination_name) protected TextView mDestinationName;
    @Bind(R.id.trip_start_date) protected TextView mStartDate;
    @Bind(R.id.trip_end_date) protected TextView mEndDate;

    public TripViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  private List<Trip> mTrips;

  public TripRecyclerAdapter(List<Trip> trips) {
    mTrips = trips;
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
    if (startDate != null) {
      holder.mStartDate.setText(formatter.print(startDate));
    } else {
      holder.mStartDate.setText(R.string.unknown);
    }

    if (endDate != null) {
      holder.mEndDate.setText(formatter.print(endDate));
    } else {
      holder.mEndDate.setText(R.string.unknown);
    }
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
}

