package com.glasstowerstudios.rucksack.model;

import com.glasstowerstudios.rucksack.util.TemporalFormatter;

import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * A data model representing a travel experience a user can take.
 */
public class Trip {
  private String mDestinationName;
  private DateTime mStartDate;
  private int mNightLength;

  public Trip() {
    super();
  }

  public Trip(String destinationName, DateTime startDate, int aNightLength) {
    mDestinationName = destinationName;
    mStartDate = startDate;
    mNightLength = aNightLength;
  }

  public Trip(String destinationName) {
    this(destinationName, null, 0);
  }

  public String getDestinationName() {
    return mDestinationName;
  }

  public DateTime getStartDate() {
    return mStartDate;
  }

  public DateTime getEndDate() {
    return mStartDate.plusDays(mNightLength);
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
    return new Period(mStartDate, getEndDate());
  }

  @Override
  public boolean equals(Object aOther) {
    if (!(aOther instanceof Trip)) {
      return false;
    }

    Trip otherTrip = (Trip) aOther;
    return otherTrip.getDestinationName().equals(getDestinationName())
      && otherTrip.getStartDate().equals(getStartDate())
      && otherTrip.getEndDate().equals(getEndDate());
  }

  @Override
  public int hashCode() {
    int result = 38;
    result = 37 * result + (getDestinationName() == null ? 0 : getDestinationName().hashCode());
    result = 37 * result + (getStartDate() == null ? 0 : getStartDate().hashCode());
    result = 37 * result + (getEndDate() == null ? 0 : getEndDate().hashCode());

    return result;
  }
}
