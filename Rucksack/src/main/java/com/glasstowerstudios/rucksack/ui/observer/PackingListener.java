package com.glasstowerstudios.rucksack.ui.observer;

import com.glasstowerstudios.rucksack.model.PackableItem;

import java.util.List;

/**
 * An interface that can be implemented if observers want to be notified of packing events.
 */
public interface PackingListener {
  /**
   * A single {@link PackableItem} has changed its packed/unpacked status.
   *
   * @param item The {@link PackableItem} that changed state. The state within the item is the new
   *             current state of the item.
   */
  void onPackingStatusChanged(PackableItem item);

  /**
   * Multiple {@link PackableItem} have changed their packed/unpacked status.
   *
   * @param items A list of {@link PackableItem}s that changed state. The state within each of the
   *              items is the new current state of that item.
   */
  void onPackingStatusChanged(List<PackableItem> items);
}