/* HashTableChained.java */

package dict;
import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
  public static final double MAX_LOAD_FACTOR = 0.6;
  public static final int DEFAULTESTIMATE = 75;
  protected int numOfEntries;
  protected List table[];
  protected int numOfBuckets;

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    numOfBuckets = sizeEstimate * 2;
    table = new DList[numOfBuckets];
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    this(DEFAULTESTIMATE);
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
	  return ((Math.abs(code)+761)%2147483647)%numOfBuckets;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return numOfEntries;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    return numOfEntries==0;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    if((double)numOfEntries/numOfBuckets > MAX_LOAD_FACTOR){
      increaseTableSize();
    }
    int index = compFunction(key.hashCode());
    if (table[index]==null){
      table[index] = new DList();
    }
    Entry newEntry = new Entry();
    newEntry.key = key;
    newEntry.value = value;
    table[index].insertFront(newEntry);
    numOfEntries++;
    return newEntry;
  }

  /**
   *  Double the table size and re-hash all existing entries.
   **/

  private void increaseTableSize(){
    numOfBuckets = numOfBuckets * 2;
    List oldTable[] = table;
    makeEmpty();
    ListNode node;
    Entry entry;
    for(int i = 0; i < oldTable.length; i++){
      if (oldTable[i]!=null){
        node = oldTable[i].front();
        try{
          while(node.isValidNode()){
            entry = (Entry)node.item();
            insert(entry.key(),entry.value());
            node = node.next();
          }
        } catch (InvalidNodeException e) {
          System.err.println("Exception thrown in increaseTableSize()");
        }
      }
    }
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
    int index = compFunction(key.hashCode());
    if (table[index]!=null){
      List list = table[index];
      ListNode node = list.front();
      try{
        while(node.isValidNode()){
          Entry entry = (Entry)node.item();
          if(entry.key().equals(key)){
            return entry;
          }
          node = node.next();
        }
      } catch (InvalidNodeException e) {
        System.err.println("Exception thrown in find()");
      }
    }
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    int index = compFunction(key.hashCode());
    if (table[index]!=null){
      List list = table[index];
      ListNode node = list.front();
      try{
        while(node.isValidNode()){
          Entry entry = (Entry)node.item();
          if(entry.key().equals(key)){
            node.remove();
            numOfEntries--;
            return entry;
          }
          node = node.next();
        }
      } catch (InvalidNodeException e) {
        System.err.println("Exception thrown in remove()");
      }
    }
    return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */

  public void makeEmpty() {
    table = new DList[numOfBuckets];
    numOfEntries = 0;
  }

  /**
  *   FOR TESTING ONLY: Print a histogram of this hashtable
  *   @param printDebug the boolean indicating whether to print debug data
  *   @return an int expressing the total number of collisions
  */ 
  public int histogram(boolean printDebug){
    int maxCollisions = 0;
    int numCollisions = 0;
    int size = 10;
    int hist[] = new int[size];
    //System.out.print("[");
    for(int i = 0; i<numOfBuckets; i++){
      if(table[i]!=null){
        //System.out.print(i+" ");
        if(maxCollisions < table[i].length()-1){
          maxCollisions = table[i].length()-1;
        }
        hist[table[i].length()-1]++;
        numCollisions+=table[i].length()-1;
      } else {
        //System.out.print(" - ");
      }
    }
    //System.out.println("]");
    if(printDebug){
      System.out.println("Number of entries: " + numOfEntries);
      System.out.println("Number of buckets: " + numOfBuckets);
      for(int i = 0; i<size; i++){
        System.out.println(i + " collisions " + hist[i] + " time(s)");
      }
      System.out.println("Max collisions per hash = " + maxCollisions);
      double expected = (double)numOfEntries - (double)numOfBuckets + (double)numOfBuckets * Math.pow((double)1 - (double)1/(double)numOfBuckets,(double)numOfEntries);
      System.out.println("Expected number of collisions: " + expected);
      System.out.println("Actual number of collisions: " + numCollisions);
      System.out.println("Load factor: " + (double)numOfEntries/numOfBuckets);
    }
    return numCollisions;
  }

}
