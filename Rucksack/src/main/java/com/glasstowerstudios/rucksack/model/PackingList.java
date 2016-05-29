package com.glasstowerstudios.rucksack.model;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link List} of {@link PackableItem}s, with statuses (packed/not-packed). A PackingList object
 * is attached to a {@link Pastime}.
 */
public class PackingList {
  private List<PackableItem> mItems;
  private List<PackableItem.Status> mStatuses;

  // Note: this may be null (for the "extra" category of a Trip)
  private Pastime mPastime;

  public PackingList() {

  }

  public PackingList(List<PackableItem> items) {
    this(items, null);
  }

  public PackingList(List<PackableItem> items, Pastime pastime) {
    this.mPastime = pastime;
    this.mItems = items;
  }

  public PackingList(PackableItem[] items) {
    this(items, null);
  }

  public PackingList(PackableItem[] items, Pastime pastime) {
    this(Arrays.asList(items), pastime);
  }

  public void add(PackableItem item, PackableItem.Status status) {
    mItems.add(item);
    mStatuses.add(status);
  }

  public void addAll(List<PackableItem> items) {
    for (PackableItem item : items) {
      add(item, PackableItem.Status.NOT_PACKED);
    }
  }

  public PackableItem remove(PackableItem item) {
    int position = mItems.indexOf(item);
    if (position >= 0) {
      return remove(position);
    }

    return null;
  }

  public int size() {
    return mItems.size();
  }

  public PackableItem remove(int position) {
    if (position >= 0 && position < mItems.size()) {
      PackableItem item = mItems.remove(position);
      mStatuses.remove(position);

      return item;
    }

    return null;
  }

  public void clear() {
    mItems.clear();
    mStatuses.clear();
  }

  public void setStatus(int position, PackableItem.Status status) {
    if (position >= 0 && position < mItems.size()) {
      mStatuses.set(position, status);
    }
  }

  public void setPacked(PackableItem item) {
    int position = mItems.indexOf(item);
    setStatus(position, PackableItem.Status.PACKED);
  }

  public void setPacked(int position) {
    setStatus(position, PackableItem.Status.PACKED);
  }

  public void setNotPacked(PackableItem item) {
    int position = mItems.indexOf(item);
    setStatus(position, PackableItem.Status.NOT_PACKED);
  }

  public void setNotPacked(int position) {
    setStatus(position, PackableItem.Status.NOT_PACKED);
  }
}
