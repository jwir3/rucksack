package com.glasstowerstudios.rucksack.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * A physical item that can be packed in preparation for a {@link Trip}.
 */
@Table(name = "items")
public class PackItem extends BaseModel {

  @Column(name = "itemName")
  private String mItemName;

  public PackItem() {
  }

  public PackItem(String aItemName) {
    mItemName = aItemName;
  }

  public String getName() {
    return mItemName;
  }
}
