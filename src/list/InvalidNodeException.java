/* InvalidNodeException.java */

package list;

/**
 *  Implements an Exception that signals an attempt to use an invalid ListNode.
 */

public class InvalidNodeException extends Exception {
  /**
	 * 
	 */
	private static final long serialVersionUID = 6545402036920590662L;

protected InvalidNodeException() {
    super();
  }

  protected InvalidNodeException(String s) {
    super(s);
  }
}
