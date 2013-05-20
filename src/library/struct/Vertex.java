package library.struct;

import heap.PairingHeap;

// Represents a vertex in the graph.
public class Vertex {
  public String     name;   // Vertex name
  public List<Edge> adj;    // Adjacent vertices
  public double     dist;   // Cost
  public Vertex     prev;   // Previous vertex on shortest path
  public int        scratch; // Extra variable used in algorithm

  public Vertex(String nm) {
    name = nm;
    adj = new LinkedList<Edge>();
    reset();
  }

  public void reset() {
    dist = Graph.INFINITY;
    prev = null;
    pairingHeapEntry = null;
    scratch = 0;
  }

  public PairingHeap.Position<Path> pairingHeapEntry; // Used for dijkstra2 (Chapter 23)

  public Path                       leftistEntry;    // Used for dijkstra2 (Chapter 23)
}