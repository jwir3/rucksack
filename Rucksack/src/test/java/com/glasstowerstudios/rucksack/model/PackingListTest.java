package com.glasstowerstudios.rucksack.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class PackingListTest extends JsonDataTest<PackingList> {
  private PackingList mList;

  @Before
  public void setUp() throws Exception {
    init(PackingList.class);
    mList = getPrimaryData();
  }

  @Test
  public void testPackingListCreated() throws Exception {
    // We should have 2 items, a Toothbrush and Clothes
    assertEquals(2, mList.size());

    List<String> itemNames = new ArrayList<>();
    itemNames.add("Toothbrush");
    itemNames.add("Clothes");

    for (int i = 0; i < mList.size(); i++) {
      PackableItem nextItem = mList.get(i);
      assertTrue(itemNames.contains(nextItem.getName()));
    }
  }

  @Test
  public void itShouldMergeTwoPackingListsByCreatingAUnion() throws Exception {
    PackingList secondaryList = getSecondaryData();
    assertNotNull(secondaryList);

    PackingList mergedList = secondaryList.merge(mList);

    // There should be four items in the list.
    assertEquals(4, mergedList.size());
  }
}
