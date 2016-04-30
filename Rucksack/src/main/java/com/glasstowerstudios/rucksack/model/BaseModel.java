package com.glasstowerstudios.rucksack.model;

import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * A base class that all data models should extend.
 */
public class BaseModel extends Model {

  public BaseModel() {
    super();
  }

  /**
   * Find an existing {@link BaseModel} by its database id.
   *
   * @param type The {@link Class} type of the object to retrieve. Must derive from {@link BaseModel}.
   * @param id The id to search for in the database.
   *
   * @return An object of type type, if one was found having the given id; null, otherwise.
   */
  public static <T extends BaseModel> T findById(Class<T> type, long id) {
    TableInfo tableInfo = new TableInfo(type);
    return new Select().from(type).where(tableInfo.getIdName() + " = ?", id).executeSingle();
  }

  /**
   * Retrieve a {@link List} of all objects in the database of a specific type.
   *
   * @param type The type of model to retrieve. Must be a {@link Class} that derives from
   *        {@link BaseModel}.
   *
   * @return A {@link List} of all objects of type type in the database.
   */
  public static <T extends BaseModel> List<T> getAll(Class<T> type) {
    return new Select().from(type).execute();
  }
}
