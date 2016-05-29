package com.glasstowerstudios.rucksack.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class PackingListTest extends JsonDataTest<PackingList> {
  private PackingList mList;

  @Before
  public void setUp() throws Exception {
    init(PackingList.class);
    mList = getData();
  }

  @Test
  public void testPackingListCreated() throws Exception {
    // We should have 2 items, a Toothbrush and Clothes
    assertEquals(2, mList.size());
  }
}
