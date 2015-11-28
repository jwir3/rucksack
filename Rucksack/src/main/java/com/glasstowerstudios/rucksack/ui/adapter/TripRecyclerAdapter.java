package com.glasstowerstudios.rucksack.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.model.Trip;

import java.util.List;

/**
 *
 */
public class TripRecyclerAdapter extends RecyclerView.Adapter<TripRecyclerAdapter.TripViewHolder> {
  public static class TripViewHolder extends RecyclerView.ViewHolder {
    private TextView mDestinationName;

    public TripViewHolder(TextView destinationName) {
      super(destinationName);
      mDestinationName = destinationName;
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
    TextView v = (TextView)LayoutInflater.from(parent.getContext())
                                         .inflate(R.layout.trip_list_item, parent, false);
    // set the view's size, margins, paddings and layout parameters

    TripViewHolder vh = new TripViewHolder(v);
    return vh;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(TripRecyclerAdapter.TripViewHolder holder, int position) {
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    holder.mDestinationName.setText(mTrips.get(position).getDestinationName());
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

