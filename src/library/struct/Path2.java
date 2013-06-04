package library.struct;

// Represents an entry in the priority queue for Dijkstra's algorithm.
public class Path2 implements Comparable<Path2> {
  public int dest; // w
  public double cost; // d(w)

  public Path2(int d, double c) {
    dest = d;
    cost = c;
  }

  @Override
  public int compareTo(Path2 rhs) {
    double otherCost = rhs.cost;

    return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
  }
}
