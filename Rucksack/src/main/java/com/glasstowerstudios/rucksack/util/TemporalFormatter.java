package com.glasstowerstudios.rucksack.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 *
 */
public class TemporalFormatter {
  public static final PeriodFormatter TRIP_DURATION_FORMATTER =
    new PeriodFormatterBuilder()
      .appendYears()
      .appendSuffix(" year", " years")
      .appendSeparator(", ")
      .appendMonths()
      .appendSuffix(" month", " months")
      .appendSeparator(", ")
      .appendWeeks()
      .appendSuffix(" week", " weeks")
      .appendSeparator(", ")
      .appendDays()
      .appendSuffix(" day", " days")
      .appendSeparator(", ")
      .appendHours()
      .appendSuffix(" hour", " hours")
      .toFormatter();

  public static final DateTimeFormatter TRIP_DATES_FORMATTER =
    new DateTimeFormatterBuilder()
      .appendMonthOfYearShortText()
      .appendDayOfMonth(1)
      .appendYear(4, 4)
      .toFormatter();
}
