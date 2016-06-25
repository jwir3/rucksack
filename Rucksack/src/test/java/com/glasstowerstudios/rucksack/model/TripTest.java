package com.glasstowerstudios.rucksack.model;

import android.os.Build;

import com.glasstowerstudios.rucksack.BuildConfig;
import com.glasstowerstudios.rucksack.util.RucksackGsonHelper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class TripTest extends JsonDataTest<Trip> {

  private Trip mTrip;

  @Before
  public void setUp() throws Exception {
    init(Trip.class);
    mTrip = getPrimaryData();
  }

  @Test
  public void testTripDeserializedCorrectly() {
    Assert.assertEquals("Paris", mTrip.getDestinationName());

    DateTime expectedStartDateTime = new DateTime(2015, 11, 27, 13, 0, 0, 0,
                                                  DateTimeZone.forTimeZone(
                                                    TimeZone.getTimeZone("America/Chicago")));
    Assert.assertEquals(expectedStartDateTime, mTrip.getStartDate());

    DateTime expectedEndDateTime = new DateTime(2015, 11, 29, 16, 0, 0, 0,
                                                DateTimeZone.forTimeZone(TimeZone.getTimeZone("America/Chicago")));
    Assert.assertEquals(expectedEndDateTime, mTrip.getEndDate());

    Assert.assertEquals(0, mTrip.getDurationOfYears());
    Assert.assertEquals(0, mTrip.getDurationOfMonths());
    Assert.assertEquals(0, mTrip.getDurationOfWeeks());
    Assert.assertEquals(2, mTrip.getDurationOfDays());
    Assert.assertEquals(3, mTrip.getDurationOfHours());

    Assert.assertEquals("2 days, 3 hours", mTrip.getDurationString());
  }

  @Test
  public void testTripSerializedCorrectly() {
    String jsonData = RucksackGsonHelper.getGson().toJson(mTrip);
    Assert.assertEquals(getJson().replaceAll("\\s", ""), jsonData);
  }

  @Test
  public void testTripHasTwoPastimesAssociatedWithIt() {
    List<Pastime> pastimes = mTrip.getPastimes();
    assertEquals(2, pastimes.size());
    assertEquals("Work", pastimes.get(0).getName());
    assertEquals("Dining", pastimes.get(1).getName());
  }
}