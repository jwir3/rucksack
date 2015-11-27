package com.glasstowerstudios.rucksack.model;

import com.glasstowerstudios.rucksack.util.DataStub;
import com.glasstowerstudios.rucksack.util.RucksackGsonHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TripTest {

  private String mJson;
  private Trip mTrip;

  @Before
  public void setUp() throws Exception {

    mJson = DataStub.readFile("trip.json");
    Assert.assertNotNull(mJson);

    mTrip = RucksackGsonHelper.getGson().fromJson(mJson, Trip.class);
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testTripDeserializedCorrectly() {
    Assert.assertEquals("Paris", mTrip.getDestinationName());
  }

  @Test
  public void testTripSerializedCorrectly() {
    String jsonData = RucksackGsonHelper.getGson().toJson(mTrip);
    Assert.assertEquals(mJson.replaceAll("\\s", ""), jsonData);
  }
}