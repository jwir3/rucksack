package com.glasstowerstudios.rucksack.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.glasstowerstudios.rucksack.util.TemporalFormatter;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.List;

/**
 * A data model representing a travel experience a user can take.
 */
@Table(name = "trips")
public class Trip extends BaseModel {

  @Column(name = "destination_name")
  private String mDestinationName;

  @Column(name = "start_date")
  private DateTime mStartDate;

  @Column(name = "end_date")
  private DateTime mEndDate;

  public Trip() {
  }

  public Trip(String destinationName, DateTime startDate, DateTime endDate) {
    mDestinationName = destinationName;
    mStartDate = startDate;
    mEndDate = endDate;
  }

  public Trip(String destinationName) {
    this(destinationName, null, null);
  }

  public String getDestinationName() {
    return mDestinationName;
  }

  public DateTime getStartDate() {
    return mStartDate;
  }

  public DateTime getEndDate() {
    return mEndDate;
  }

  public int getDurationOfYears() {
    return getDuration().getYears();
  }

  public int getDurationOfMonths() {
    return getDuration().getMonths();
  }

  public int getDurationOfWeeks() {
    return getDuration().getWeeks();
  }

  public int getDurationOfDays() {
    return getDuration().getDays();
  }

  public int getDurationOfHours() {
    return getDuration().getHours();
  }

  public String getDurationString() {
    StringBuffer buffer = new StringBuffer();
    TemporalFormatter.TRIP_DURATION_FORMATTER.printTo(buffer, getDuration());
    return buffer.toString();
  }

  private Period getDuration() {
    return new Period(mStartDate, mEndDate);
  }

  /**
   * Convenience method for retrieving all {@link Trip} objects in the database.
   *  Equivalent to {@link BaseModel#getAll(Trip.class)}.
   *
   * @return A {@link List} of all {@link Trip} objects in the database.
   */
  public static List<Trip> getAll() {
    return BaseModel.getAll(Trip.class);
  }
}
