package com.glasstowerstudios.rucksack.model;

/**
 * A physical item that can be packed in preparation for a {@link Trip}.
 */
public class PackableItem {
  enum Status {
    NOT_PACKED,
    PACKED
  }

  private String mItemName;
  private Status mStatus;
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
//    if (mAllowMultiples) {
//      return this == aOther;
//    }

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
