package com.glasstowerstudios.rucksack.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class PackItemTest extends JsonDataTest<PackItem> {
  private PackItem mItem;

  @Before
  public void setUp() throws Exception {
    init(PackItem.class);
    mItem = getData();
  }

  @Test
  public void testPackItemSetupSuccessfully() {
    assertEquals("Toothbrush", mItem.getName());
  }
}
