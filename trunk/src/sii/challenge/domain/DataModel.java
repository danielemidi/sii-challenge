package sii.challenge.domain;


import java.io.Serializable;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;

/**
 * Implementations represent a repository of information about users and their associated {@link Preference}s
 * for items. 
 */
public interface DataModel extends Refreshable, Serializable {
  
  /**
   * @return all user IDs in the model, in order
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  LongPrimitiveIterator getUserIDs() throws TasteException;
  
  /**
   * @param userID
   *          ID of user to get prefs for
   * @return user's preferences, ordered by item ID
   * @throws org.apache.mahout.cf.taste.common.NoSuchUserException
   *           if the user does not exist
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  PreferenceArray getPreferencesFromUser(long userID) throws Exception;
  
  /**
   * @param userID
   *          ID of user to get prefs for
   * @return IDs of items user expresses a preference for
   * @throws org.apache.mahout.cf.taste.common.NoSuchUserException
   *           if the user does not exist
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  FastIDSet getItemIDsFromUser(long userID) throws Exception;
  
  /**
   * @return a {@link LongPrimitiveIterator} of all item IDs in the model, in order
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  LongPrimitiveIterator getItemIDs() throws Exception;
  
  /**
   * @param itemID
   *          item ID
   * @return all existing {@link Preference}s expressed for that item, ordered by user ID, as an array
   * @throws org.apache.mahout.cf.taste.common.NoSuchItemException
   *           if the item does not exist
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  PreferenceArray getPreferencesForItem(long itemID) throws Exception;
  
  /**
   * Retrieves the preference value for a single user and item.
   * 
   * @param userID
   *          user ID to get pref value from
   * @param itemID
   *          item ID to get pref value for
   * @return preference value from the given user for the given item or null if none exists
   * @throws org.apache.mahout.cf.taste.common.NoSuchUserException
   *           if the user does not exist
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  Float getPreferenceValue(long userID, long itemID) throws Exception;

  /**
   * Retrieves the time at which a preference value from a user and item was set, if known.
   * Time is expressed in the usual way, as a number of milliseconds since the epoch.
   *
   * @param userID user ID for preference in question
   * @param itemID item ID for preference in question
   * @return time at which preference was set or null if no preference exists or its time is not known
   * @throws org.apache.mahout.cf.taste.common.NoSuchUserException if the user does not exist
   * @throws TasteException if an error occurs while accessing the data
   */
  Long getPreferenceTime(long userID, long itemID) throws Exception;
  
  /**
   * @return total number of items known to the model. This is generally the union of all items preferred by
   *         at least one user but could include more.
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  int getNumItems() throws Exception;
  
  /**
   * @return total number of users known to the model.
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  int getNumUsers() throws Exception;
  
  /**
   * @param itemIDs item IDs to check for
   * @return the number of users who have expressed a preference for all of the items
   * @throws TasteException if an error occurs while accessing the data
   * @throws IllegalArgumentException
   *  if itemIDs is null or empty. Arguments larger than 2 elements are not necessarily supported
   *  and may result in this exception
   */
  int getNumUsersWithPreferenceFor(long... itemIDs) throws Exception;
  
  /**
   * <p>
   * Sets a particular preference (item plus rating) for a user.
   * </p>
   * 
   * @param userID
   *          user to set preference for
   * @param itemID
   *          item to set preference for
   * @param value
   *          preference value
   * @throws org.apache.mahout.cf.taste.common.NoSuchItemException
   *           if the item does not exist
   * @throws org.apache.mahout.cf.taste.common.NoSuchUserException
   *           if the user does not exist
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  void setPreference(long userID, long itemID, float value) throws Exception;
  
  /**
   * <p>
   * Removes a particular preference for a user.
   * </p>
   * 
   * @param userID
   *          user from which to remove preference
   * @param itemID
   *          item to remove preference for
   * @throws org.apache.mahout.cf.taste.common.NoSuchItemException
   *           if the item does not exist
   * @throws org.apache.mahout.cf.taste.common.NoSuchUserException
   *           if the user does not exist
   * @throws TasteException
   *           if an error occurs while accessing the data
   */
  void removePreference(long userID, long itemID) throws Exception;

  /**
   * @return true iff this implementation actually stores and returns distinct preference values;
   *  that is, if it is not a 'boolean' DataModel
   */
  boolean hasPreferenceValues();

  /**
   * @return the maximum preference value that is possible in the current problem domain being evaluated. For
   * example, if the domain is movie ratings on a scale of 1 to 5, this should be 5. While a
   * {@link org.apache.mahout.cf.taste.recommender.Recommender} may estimate a preference value above 5.0, it
   * isn't "fair" to consider that the system is actually suggesting an impossible rating of, say, 5.4 stars.
   * In practice the application would cap this estimate to 5.0. Since evaluators evaluate
   * the difference between estimated and actual value, this at least prevents this effect from unfairly
   * penalizing a {@link org.apache.mahout.cf.taste.recommender.Recommender}
   */
  float getMaxPreference();

  /**
   * @see #getMaxPreference()
   */
  float getMinPreference();
  
}
			
			