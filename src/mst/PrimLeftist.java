package mst;

import heap.LeftistHeap;

import java.io.IOException;
import java.util.ArrayList;

import library.PairVertex;
import library.Utils;
import library.struct.Edge;
import library.struct.GraphException;
import library.struct.MstGraph;
import library.struct.NoSuchElementException;
import library.struct.Path;
import library.struct.Vertex;

public class PrimLeftist {

  private Vertex                        start;
  public static final double            INFINITY = Double.MAX_VALUE;
  private MstGraph                      graph;
  private ArrayList<PairVertex<String>> spanningTree;
  /* The graph which will hold the resulting MST. */
  ArrayList                             result   = new ArrayList();

  public PrimLeftist(MstGraph graph) {
    this.graph = graph;
  }

  /**
   * Single-source weighted shortest-path algorithm using pairing heaps.
   */
  public void generateMst() {
    LeftistHeap<Path> pq = new LeftistHeap<Path>();

    if (!graph.getVertexMap().values().iterator().hasNext())
      throw new NoSuchElementException("Start vertex not found");

    start = graph.getVertexMap().values().iterator().next(); //vertexMap.get("0"); // TODO:

    clearAll();
    pq.insert(new Path(start, 0));
    //System.err.println("Colocou " + start.name + " no Heap com peso " + start.dist);
    start.leftistEntry = pq.findMin();
    start.dist = 0;

    int nodesSeen = 0;
    while (!pq.isEmpty() && nodesSeen < this.graph.getVertexMap().size()) {
      Path vrec = pq.deleteMin();
      //System.err.println("Removeu " + vrec.dest.name + " no Heap");

      Vertex v = vrec.dest;

      if (v.scratch != 0) // already processed v
        continue;

      v.scratch = 1;
      nodesSeen++;
      result.add(v);

      for (Edge e : v.adj) {
        Vertex w = e.dest;
        double cvw = e.cost;

        if (cvw < 0)
          throw new GraphException("Graph has negative edges");

        if (w.dist > cvw && !result.contains(w)) {
          //System.out.println("Antes : " + v.name + " - " + w.name + " com peso: " + w.dist);
          w.dist = cvw;
          w.prev = v;

          //System.out.println("Depois: " + v.name + " - " + w.name + " com peso: " + w.dist);

          Path newVal = new Path(w, w.dist);

          pq.insert(newVal);
          //System.err.println("Inseriu " + newVal.dest.name + " no Heap com peso " + newVal.cost);
          w.leftistEntry = pq.findMin();
        }
      }
    }
    printPath();
  }

  /**
   * Initializes the vertex output info prior to running any shortest path algorithm.
   */
  private void clearAll() {
    for (Vertex v : graph.getVertexMap().values())
      v.reset();
  }

  public void printPath() {
    Vertex w = start;
    /*
     * if (w == null) throw new NoSuchElementException("Destination vertex not found"); else if (w.dist == INFINITY) System.out.println(w.name +
     * " is unreachable"); else { System.out.print("(Cost is: " + w.dist + ") "); printPath(w); System.out.println(); }
     */

    int cost = 0;
    for (Vertex v : this.graph.getVertexMap().values()) {
      if (v.prev != null) {
        System.out.println(v.prev.name + " - " + v.name + " : " + v.dist);
        cost += v.dist;
      }
    }
    System.out.println("Custo total: " + cost);
  }

  /**
   * Recursive routine to print shortest path to dest after running shortest path algorithm. The path is known to exist.
   */
  private void printPath(Vertex dest) {
    if (dest.prev != null) {
      printPath(dest.prev);
      System.out.print(" to ");
    }
    System.out.print(dest.name);
  }

  /**
   * A main routine that: 1. Reads a file containing edges (supplied as a command-line parameter); 2. Forms the graph; 3. Repeatedly prompts for two vertices
   * and runs the shortest path algorithm. The data file is a sequence of lines of the format source destination.
   * 
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    MstGraph g = Utils.getGraphFromInputFile("data/ALUE/alue2087.stp");
    PrimLeftist prim = new PrimLeftist(g);

    processRequest(prim);
  }

  /**
   * Process a request; return false if end of file.
   */
  public static boolean processRequest(PrimLeftist prim) throws IOException {

    try {
      prim.generateMst();
    }
    catch (NoSuchElementException e) {
      System.err.println(e);
    }
    catch (GraphException e) {
      System.err.println(e);
    }
    return true;
  }

  public ArrayList<PairVertex<String>> getSpanningTree() {
    return spanningTree;
  }

  public void setSpanningTree(ArrayList<PairVertex<String>> spanningTree) {
    this.spanningTree = spanningTree;
  }
}
