package com.glasstowerstudios.rucksack.model;

import com.glasstowerstudios.rucksack.R;
import com.glasstowerstudios.rucksack.RucksackApplication;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class PastimeTest extends JsonDataTest<Pastime> {

  private Pastime mPastime;

  @Before
  public void setUp() throws Exception {
    init(Pastime.class);
    mPastime = getPrimaryData();
  }

  @Test
  public void itAssociatesValidResourceNamesWithIdentifiers() {
    RucksackApplication app = (RucksackApplication) RuntimeEnvironment.application;
    assertEquals("ic_pastime_work", mPastime.getIconResourceName());
    assertEquals(R.drawable.ic_pastime_work, mPastime.getIconResourceId(app));
  }

  @Test
  public void itUsesDefaultResourceIdentifiersForInvalidNames() {
    RucksackApplication app = (RucksackApplication) RuntimeEnvironment.application;
    Pastime invalidIcon = new Pastime("Something", "ic_pastime_bahsgy");
    assertEquals(R.drawable.ic_pastime_default, invalidIcon.getIconResourceId(app));
  }
}
