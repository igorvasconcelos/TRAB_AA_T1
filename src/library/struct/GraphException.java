package library.struct;

// Used to signal violations of preconditions for
// various shortest path algorithms.
public class GraphException extends RuntimeException {
  public GraphException(String name) {
    super(name);
  }
}