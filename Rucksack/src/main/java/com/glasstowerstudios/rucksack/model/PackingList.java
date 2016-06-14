package com.glasstowerstudios.rucksack.model;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A {@link List} of {@link PackableItem}s, with statuses (packed/not-packed). A PackingList object
 * may be attached to a {@link Pastime}.
 */
public class PackingList {
  List<PackableItem> mItems = new LinkedList<>();

  // Note: this may be null (for the "extra" category of a Trip)
  private Pastime mPastime;

  // Default constructor provided for Gson
  public PackingList() {

  }

  public PackingList(List<PackableItem> items) {
    this(items, null);
  }

  public PackingList(List<PackableItem> items, Pastime pastime) {
    this.mPastime = pastime;
    this.addAll(items);
  }

  public PackingList(PackableItem[] items) {
    this(items, null);
  }

  public PackingList(PackableItem[] items, Pastime pastime) {
    this(Arrays.asList(items), pastime);
  }

  public void add(int location, PackableItem object) {
    mItems.add(location, object);
  }

  public boolean add(PackableItem object) {
    return mItems.add(object);
  }

  public boolean addAll(int location, Collection<? extends PackableItem> collection) {
    return mItems.addAll(location, collection);
  }

  public boolean addAll(Collection<? extends PackableItem> collection) {
    return mItems.addAll(collection);
  }

  public void clear() {
    mItems.clear();
  }

  public boolean contains(Object object) {
    return mItems.contains(object);
  }

  public boolean containsAll(Collection<?> collection) {
    return mItems.containsAll(collection);
  }

  public PackableItem get(int location) {
    return mItems.get(location);
  }

  public int indexOf(Object object) {
    return mItems.indexOf(object);
  }

  public boolean isEmpty() {
    return mItems.isEmpty();
  }

  @NonNull
  public Iterator<PackableItem> iterator() {
    return mItems.iterator();
  }

  public int lastIndexOf(Object object) {
    return mItems.lastIndexOf(object);
  }

  public ListIterator<PackableItem> listIterator() {
    return mItems.listIterator();
  }

  @NonNull
  public ListIterator<PackableItem> listIterator(int location) {
    return mItems.listIterator(location);
  }

  public PackableItem remove(int location) {
    return mItems.remove(location);
  }

  public boolean remove(Object object) {
    return mItems.remove(object);
  }

  public boolean removeAll(Collection<?> collection) {
    return mItems.removeAll(collection);
  }

  public boolean retainAll(Collection<?> collection) {
    return mItems.retainAll(collection);
  }

  public PackableItem set(int location, PackableItem object) {
    return mItems.set(location, object);
  }

  public int size() {
    return mItems.size();
  }

  @NonNull
  public List<PackableItem> subList(int start, int end) {
    return mItems.subList(start, end);
  }

  @NonNull
  public Object[] toArray() {
    return mItems.toArray();
  }

  @NonNull
  public <T> T[] toArray(T[] array) {
    return mItems.toArray(array);
  }

  public PackingList merge(PackingList aOther) {
    List<PackableItem> mergedList = new LinkedList<>();
    mergedList.addAll(mItems);

    for (int i = 0; i < aOther.size(); i++) {
      PackableItem nextItem = aOther.get(i);
      if (!mergedList.contains(nextItem)) {
        mergedList.add(nextItem);
      }
    }

    return new PackingList(mergedList);
  }

  public void addItems(List<PackableItem> aItems) {
    List<PackableItem> copiedList = new LinkedList<>(aItems);

    // Deep-copy the list
    for (PackableItem item : aItems) {
      copiedList.add(new PackableItem(item.getName()));
    }

    for (PackableItem item : copiedList) {
      if (!mItems.contains(item)) {
        mItems.add(item);
      }
    }
  }
}
