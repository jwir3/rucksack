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

  @Override
  public boolean equals(Object aOther) {
    if (!(aOther instanceof PackableItem)) {
      return false;
    }

    PackableItem item = (PackableItem) aOther;

    return mItemName.equals(item.getName());
  }

  @Override
  public int hashCode() {
    int result = 37;
    result = 37 * result + (mItemName == null ? 0 : mItemName.hashCode());
    return result;
  }
}
