package com.glasstowerstudios.rucksack.util.data;

import com.activeandroid.serializer.TypeSerializer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

/**
 * A type serializer for {@link DateTime} objects. Every {@link DateTime} object is stored as an
 * instant (long), and assumed to be in UTC.
 */
public class DateTimeSerializer extends TypeSerializer {
  @Override
  public Class<?> getDeserializedType() {
    return DateTime.class;
  }

  @Override
  public Class<?> getSerializedType() {
    return Long.class;
  }

  @Override
  public Object serialize(Object data) {
    if (data == null) {
      return null;
    }

    DateTime dateTimeObj = (DateTime)data;
    Instant instant = dateTimeObj.toInstant();
    return instant.getMillis();
  }

  @Override
  public Object deserialize(Object data) {
    if (data == null) {
      return null;
    }

    // We need the user's default time zone to convert from instants to date times.
    DateTimeZone defaultTimeZone = DateTimeZone.getDefault();
    Long timeInDb = (Long) data;
    DateTime dateTimeInUserTimezone = new DateTime(timeInDb, defaultTimeZone);

    return dateTimeInUserTimezone;
  }
}
