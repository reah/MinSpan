/* Edge.java */

package graph;
import list.*;

/**
 * An Edge is meant to be the internal representation of an edge
 */

class Edge {

  protected ListNode vertexA;
  protected ListNode vertexB;
  protected int weight;

  Edge(ListNode a, ListNode b, int weight) {
   vertexA = a;
   vertexB = b;
   this.weight = weight;
  }
  
  void setWeight(int w){
	    weight = w;
	  }
  
  int weight(){
    return weight;
  }

  void remove(){
      try {
          if(vertexA == vertexB){
            vertexA.remove();
          } else {
            vertexA.remove();
            vertexB.remove();
          }
      }
      catch(InvalidNodeException e){
          System.out.println("Removing an invalid node FAILED.");
      }
  }

}