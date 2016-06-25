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

  public void setPacked(boolean aPacked) {
    if (aPacked) {
      mStatus = Status.PACKED;
    } else {
      mStatus = Status.NOT_PACKED;
    }
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

  public int compareIncludingPacking(PackableItem another) {
    if (equals(another)) {
      return 0;
    }

    if (isPacked() && !another.isPacked()) {
      return 1;
    } else if (!isPacked() && another.isPacked()) {
      return -1;
    }

    return getName().compareTo(another.getName());
  }

  public int compareExcludingPacking(PackableItem another) {
    if (equals(another)) {
      return 0;
    }

    return getName().compareTo(another.getName());
  }
}
