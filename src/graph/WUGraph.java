/* WUGraph.java */

package graph;

import list.*;
import dict.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

	private HashTableChained vTable, eTable;
	private int nEdges;
	private DList vList;

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph(){
	vTable = new HashTableChained();
	eTable = new HashTableChained();
	vList = new DList();
  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount(){
	return vList.length();
  }
	
  /**
   * edgeCount() returns the number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount(){
	return nEdges;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */


  public Object[] getVertices(){
    Object[] vertexList = new Object[vertexCount()];

    int i = 0;
    ListNode current = vList.front();

    try{
        Vertex tempVert;
        while (current.isValidNode()) {
            tempVert = (Vertex)current.item();
            vertexList[i] = tempVert.key();
            current = current.next();
            i++;
        }
    } catch(InvalidNodeException e) {
      System.out.println("Error in getVertices()");
    }

    return vertexList;
  }		
	

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.  The
   * vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex){
	if (vTable.find(vertex) == null){
    	Vertex newVert = new Vertex(vertex);
    	vList.insertFront(newVert);
   		vTable.insert(vertex,vList.front());
    }
  }
	

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */


  public void removeVertex(Object vertex){
    Entry entry = vTable.find(vertex);
    if(entry != null){
      ListNode node = (ListNode)entry.value();
      try{
        Vertex tempVert = (Vertex)(node.item());
        clearEdges(tempVert);
		vTable.remove(vertex);
        node.remove();
      } catch (InvalidNodeException e){
        System.out.println("Error in removeVertex()");
      }
    }
  }


  void clearEdges(Vertex v){
    ListNode edgeNode = v.myEdges.front();
    try{
      Vertex tempVert;
      while(edgeNode.isValidNode()){
        tempVert = (Vertex)edgeNode.item();
        edgeNode = edgeNode.next();
        removeEdge(v.key(),tempVert.key());
      }
    } catch(InvalidNodeException e) {
      System.out.println("Error in clearEdges()");
    }
  }
			

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex){
	return vTable.find(vertex) !=  null;
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex){
    Entry entry = vTable.find(vertex);
    if(entry != null){
      ListNode node = (ListNode)entry.value();
      try {
        Vertex vert = (Vertex)node.item();
        return vert.degree();
      } catch (InvalidNodeException e) {
        System.out.println("Error in degree()");
      }
    }
    return 0;
  }
  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex){
    int degree = degree(vertex);
    if (degree == 0){
        return null;
    }

    Neighbors neighbors = new Neighbors();
    neighbors.neighborList = new Object[degree];
    neighbors.weightList = new int[degree];
    
    try {
    
        Entry entry = vTable.find(vertex);
        ListNode node = (ListNode)entry.value();
        Vertex vert = (Vertex)node.item();
    
        int i = 0;
        Vertex tempVert;
        node = vert.myEdges.front();
              
        while (node.isValidNode()){
            tempVert = (Vertex)node.item();
            neighbors.weightList[i] = weight(vertex, tempVert.key());
            neighbors.neighborList[i] = tempVert.key();              
            node = node.next();
            i++;
        }
   }
   catch (InvalidNodeException e) {
        System.out.println("Error in getNeighbors.");
   }

   return neighbors;
 }	

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the edge is already
   * contained in the graph, the weight is updated to reflect the new value.
   * Self-edges (where u == v) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight){
    if (!isVertex(u) || !isVertex(v)){
      return;
    }
    Entry tempEntry;
    VertexPair pair = new VertexPair(u, v);
    if (isEdge(u, v)){
      tempEntry = eTable.find(pair);
      Edge currEdge = (Edge)tempEntry.value();
      currEdge.setWeight(weight);
    } else {
      tempEntry = vTable.find(u);
      ListNode a = (ListNode)tempEntry.value();
      tempEntry = vTable.find(v);
      ListNode b = (ListNode)tempEntry.value();
      try{
          List edgeListA = ((Vertex)a.item()).myEdges;
          List edgeListB = ((Vertex)b.item()).myEdges;
          edgeListA.insertFront(b.item());
          if(a != b){
            edgeListB.insertFront(a.item());
          }
          Edge newEdge = new Edge(edgeListA.front(), edgeListB.front(), weight);
          eTable.insert(pair, newEdge);
		  nEdges++;

      }
      catch (InvalidNodeException e) {
          System.out.println("Error with addEdge");
      }
    }
      }
  
  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
   public void removeEdge(Object u, Object v){
    VertexPair pair = new VertexPair(u, v);
    if (!isVertex(u) || !isVertex(v) || !isEdge(u, v)){
      return;
    }
    Entry tempEntry = eTable.find(pair);
    Edge currEdge = (Edge)tempEntry.value();
    currEdge.remove();
	eTable.remove(pair);
    nEdges--;
  }
  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v){
		VertexPair pair = new VertexPair(u, v);
		return eTable.find(pair) !=  null;
  } 

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but
   * also more annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v){
	if (isEdge(u, v)){
		VertexPair pair = new VertexPair(u, v);
		Edge currEdge = (Edge)(eTable.find(pair).value());
		return currEdge.weight();
	}
	return 0;
  }

 }
