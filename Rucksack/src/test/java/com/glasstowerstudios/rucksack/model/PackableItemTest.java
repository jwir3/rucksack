package com.glasstowerstudios.rucksack.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 *
 */
public class PackableItemTest extends JsonDataTest<PackableItem> {
  private PackableItem mItem;

  @Before
  public void setUp() throws Exception {
    init(PackableItem.class);
    mItem = getPrimaryData();
  }

  @Test
  public void testPackItemSetupSuccessfully() {
    assertEquals("Toothbrush", mItem.getName());
//    assertEquals(PackableItem.Status.NOT_PACKED, mItem.getStatus());
    assertFalse(mItem.isPacked());
  }
}
