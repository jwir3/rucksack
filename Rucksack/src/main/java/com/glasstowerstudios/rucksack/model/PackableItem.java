package com.glasstowerstudios.rucksack.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * A physical item that can be packed in preparation for a {@link Trip}.
 */
@Table(name = "items")
public class PackableItem extends BaseModel {
  enum Status {
    NOT_PACKED,
    PACKED
  }

  @Column(name = "itemName")
  private String mItemName;

  @Column(name = "status")
  private Status mStatus;

  @Column(name = "multiplesAllowed")
  private boolean mAllowMultiples = false; // Whether or not "multiples" of this item are allowed in
                                           // a packing list.

  public PackableItem() {
  }

  public PackableItem(String aItemName) {
    mItemName = aItemName;
    mStatus = Status.NOT_PACKED;
  }

  public String getName() {
    return mItemName;
  }

  public Status getStatus() {
    return mStatus;
  }

  public boolean isPacked() {
    return getStatus() == Status.PACKED;
  }

  public boolean equals(PackableItem aOther) {
    if (mAllowMultiples) {
      return this == aOther;
    }

    return mItemName.equals(aOther.getName());
  }

  @Override
  public int hashCode() {
    if (mAllowMultiples) {
      // Use the default Object implementation if we allow multiples (i.e. instance comparison)
      return System.identityHashCode(this);
    }

    int result = 37;
    result = 37 * result + (mItemName == null ? 0 : mItemName.hashCode());
    return result;
  }
}
