package library.struct;

// Represents an edge in the graph.
public class Edge {
  public Vertex dest; // Second vertex in Edge
  public double cost; // Edge cost

  public Edge(Vertex d, double c) {
    dest = d;
    cost = c;
  }
}
