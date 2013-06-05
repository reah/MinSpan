/* Kruskal.java */

import graph.*;
import set.*;
import dict.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   */
  public static WUGraph minSpanTree(WUGraph g){
    WUGraph minSpanningTree = new WUGraph();
    Object edges[][] = new Object[2*g.edgeCount()][3];
    HashTableChained hashedVertices = new HashTableChained(g.vertexCount());
    DisjointSets sets = new DisjointSets(g.vertexCount());
    Neighbors neighbors;
    int edgeIndex = 0;
    int vertMap = 0;
    for(Object vertex : g.getVertices()){
      minSpanningTree.addVertex(vertex);
      hashedVertices.insert(vertex, vertMap++);
      neighbors = g.getNeighbors(vertex);
      for(int i = 0; i<neighbors.neighborList.length; i++){
        edges[edgeIndex][0] = neighbors.weightList[i];
        edges[edgeIndex][1] = vertex;
        edges[edgeIndex][2] = neighbors.neighborList[i];
        edgeIndex++;
      }
    }
    Object cleanEdges[][] = new Object[edgeIndex][3];
    for(int i = 0; i<cleanEdges.length; i++){
    	cleanEdges[i]=edges[i];
    	}
    edges = mergesort(cleanEdges);    int vert1;
    int vert2;
    for (Object[] edge : edges){
      vert1 = (Integer)hashedVertices.find(edge[1]).value();
      vert2 = (Integer)hashedVertices.find(edge[2]).value();
      vert1 = sets.find(vert1);
      vert2 = sets.find(vert2);
      if(vert1 != vert2){
        sets.union(vert1,vert2);
        minSpanningTree.addEdge(edge[1],edge[2],(Integer)edge[0]);
      }
    }
    return minSpanningTree;
  }

  private static Object[][] mergesort(Object elements[][]){
    if(elements.length<2){
      return elements;
    }
    Object[][] left, right;
    int mid = elements.length/2;
    left = new Object[mid][];
    for(int i = 0; i<mid; i++){
      left[i] = elements[i];
    }
    right = new Object[elements.length-mid][];
    for(int i = mid; i<elements.length; i++){
      right[i-mid] = elements[i];
    }
    left = mergesort(left);
    right = mergesort(right);
    return merge(left,right);
  }

  private static Object[][] merge(Object left[][], Object right[][]){
    Object[][] sorted = new Object[left.length+right.length][];
    int r = 0, l = 0, s = 0;
    while(l < left.length && r < right.length){
      if((left[l][0] != null && right[r][0] != null) && (Integer)left[l][0] < (Integer)right[r][0]){
        sorted[s++] = left[l++];
      } else {
        sorted[s++] = right[r++];
      }
    }
    while(l < left.length){
      sorted[s++] = left[l++];
    }
    while(r < right.length){
      sorted[s++] = right[r++];
    }
    return sorted;
  }

  public static void main(String[] args) {
    System.out.println("Testing Mergesort");
    Object[][] test = {{1},{49},{84},{3},{70},{23},{0},{25},{93},{14},{62},{34},{100},{43},{79},{51},{12}};
    System.out.print("[ ");
    for(int i = 0; i<test.length; i++){
      System.out.print(test[i][0]+" ");
    }
    System.out.println("]");
    Object[][] sorted = mergesort(test);
    System.out.print("[ ");
    for(int i = 0; i<sorted.length; i++){
      System.out.print(sorted[i][0]+" ");
    }
    System.out.println("]");
  }

}
