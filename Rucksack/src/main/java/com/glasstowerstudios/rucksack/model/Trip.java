package com.glasstowerstudios.rucksack.model;

import com.glasstowerstudios.rucksack.util.TemporalFormatter;

import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * A data model representing a travel experience a user can take.
 */
public class Trip extends BaseModel {
  private String mDestinationName;
  private DateTime mStartDate;
  private DateTime mEndDate;

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
}
