package library.struct;

import heap.LeftistHeap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// Represents an entry in the priority queue for Dijkstra's algorithm.
class Path2 implements Comparable<Path2> {
  public Vertex2 dest; // w
  public double  cost; // d(w)

  public Path2(Vertex2 d, double c) {
    dest = d;
    cost = c;
  }

  @Override
  public int compareTo(Path2 rhs) {
    double otherCost = rhs.cost;

    return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
  }
}

// Represents an edge in the graph.
class Edge2 {
  public Vertex2 dest; // Second vertex in Edge
  public double  cost; // Edge cost

  public Edge2(Vertex2 d, double c) {
    dest = d;
    cost = c;
  }
}

// Represents a vertex in the graph.
class Vertex2 {
  public String      name;   // Vertex2 name
  public List<Edge2> adj;    // Adjacent vertices
  public double      dist;   // Cost
  public Vertex2     prev;   // Previous vertex on shortest path
  public int         scratch; // Extra variable used in algorithm

  public Vertex2(String nm) {
    name = nm;
    adj = new LinkedList<Edge2>();
    reset();
  }

  public void reset() {
    dist = CopyOfGraph.INFINITY;
    prev = null;
    pos = null;
    scratch = 0;
  }

  public Path2 pos; // Used for dijkstra2 (Chapter 23)

  //public AnyType                    pos2; // Used for dijkstra3 (Chapter 23)
}

// Graph class: evaluate shortest paths.
//
// CONSTRUCTION: with no parameters.
//
// ******************PUBLIC OPERATIONS**********************
// void addEdge( String v, String w, double cvw )
//                              --> Add additional edge
// void printPath( String w )   --> Print path after alg is run
// void unweighted( String s )  --> Single-source unweighted
// void dijkstra( String s )    --> Single-source weighted
// void negative( String s )    --> Single-source negative weighted
// void acyclic( String s )     --> Single-source acyclic
// ******************ERRORS*********************************
// Some error checking is performed to make sure graph is ok,
// and to make sure graph satisfies properties needed by each
// algorithm.  Exceptions are thrown if errors are detected.

public class CopyOfGraph {
  public static final double   INFINITY  = Double.MAX_VALUE;
  private Map<String, Vertex2> vertexMap = new HashMap<String, Vertex2>();

  /**
   * Add a new edge to the graph.
   */
  public void addEdge(String sourceName, String destName, double cost) {
    Vertex2 v = getVertex(sourceName);
    Vertex2 w = getVertex(destName);
    v.adj.add(new Edge2(w, cost));
  }

  /**
   * Driver routine to handle unreachables and print total cost. It calls recursive routine to print shortest path to destNode after a shortest path algorithm
   * has run.
   */
  public void printPath(String destName) {
    Vertex2 w = vertexMap.get(destName);
    if (w == null)
      throw new NoSuchElementException("Destination vertex not found");
    else if (w.dist == INFINITY)
      System.out.println(destName + " is unreachable");
    else {
      System.out.print("(Cost is: " + w.dist + ") ");
      printPath(w);
      System.out.println();
    }
  }

  /**
   * If vertexName is not present, add it to vertexMap. In either case, return the Vertex.
   */
  private Vertex2 getVertex(String vertexName) {
    Vertex2 v = vertexMap.get(vertexName);
    if (v == null) {
      v = new Vertex2(vertexName);
      vertexMap.put(vertexName, v);
    }
    return v;
  }

  /**
   * Recursive routine to print shortest path to dest after running shortest path algorithm. The path is known to exist.
   */
  private void printPath(Vertex2 dest) {
    if (dest.prev != null) {
      printPath(dest.prev);
      System.out.print(" to ");
    }
    System.out.print(dest.name);
  }

  /**
   * Initializes the vertex output info prior to running any shortest path algorithm.
   */
  private void clearAll() {
    for (Vertex2 v : vertexMap.values())
      v.reset();
  }

  /**
   * Single-source unweighted shortest-path algorithm.
   */
  public void unweighted(String startName) {
    clearAll();

    Vertex2 start = vertexMap.get(startName);
    if (start == null)
      throw new NoSuchElementException("Start vertex not found");

    Queue<Vertex2> q = new LinkedList<Vertex2>();
    q.add(start);
    start.dist = 0;

    while (!q.isEmpty()) {
      Vertex2 v = q.remove();

      for (Edge2 e : v.adj) {
        Vertex2 w = e.dest;
        if (w.dist == INFINITY) {
          w.dist = v.dist + 1;
          w.prev = v;
          q.add(w);
        }
      }
    }
  }

  /**
   * Single-source weighted shortest-path algorithm.
   */
  public void dijkstra(String startName) {
    PriorityQueue<Path2> pq = new PriorityQueue<Path2>();

    Vertex2 start = vertexMap.get(startName);
    if (start == null)
      throw new NoSuchElementException("Start vertex not found");

    clearAll();
    pq.add(new Path2(start, 0));
    start.dist = 0;

    int nodesSeen = 0;
    while (!pq.isEmpty() && nodesSeen < vertexMap.size()) {
      Path2 vrec = pq.remove();
      Vertex2 v = vrec.dest;
      if (v.scratch != 0) // already processed v
        continue;

      v.scratch = 1;
      nodesSeen++;

      for (Edge2 e : v.adj) {
        Vertex2 w = e.dest;
        double cvw = e.cost;

        if (cvw < 0)
          throw new GraphException("Graph has negative edges");

        if (w.dist > v.dist + cvw) {
          w.dist = v.dist + cvw;
          w.prev = v;
          pq.add(new Path2(w, w.dist));
        }
      }
    }
  }

  /**
   * Single-source weighted shortest-path algorithm using pairing heaps.
   */
  public void dijkstra2(String startName) {
    LeftistHeap<Path2> pq = new LeftistHeap<Path2>();

    Vertex2 start = vertexMap.get(startName);
    if (start == null)
      throw new NoSuchElementException("Start vertex not found");

    clearAll();
    pq.insert(new Path2(start, 0));
    start.pos = pq.findMin();
    start.dist = 0;

    while (!pq.isEmpty()) {
      Path2 vrec = pq.deleteMin();
      Vertex2 v = vrec.dest;

      for (Edge2 e : v.adj) {
        Vertex2 w = e.dest;
        double cvw = e.cost;

        if (cvw < 0)
          throw new GraphException("Graph has negative edges");

        if (w.dist > v.dist + cvw) {
          w.dist = v.dist + cvw;
          w.prev = v;

          Path2 newVal = new Path2(w, w.dist);
          if (w.pos == null) {
            pq.insert(newVal);
            w.pos = pq.findMin();
          }
          //else
          //  pq. decreaseKey(w.pos, newVal);
        }
      }
    }
  }

  /**
   * Single-source negative-weighted shortest-path algorithm.
   */
  public void negative(String startName) {
    clearAll();

    Vertex2 start = vertexMap.get(startName);
    if (start == null)
      throw new NoSuchElementException("Start vertex not found");

    Queue<Vertex2> q = new LinkedList<Vertex2>();
    q.add(start);
    start.dist = 0;
    start.scratch++;

    while (!q.isEmpty()) {
      Vertex2 v = q.remove();
      if (v.scratch++ > 2 * vertexMap.size())
        throw new GraphException("Negative cycle detected");

      for (Edge2 e : v.adj) {
        Vertex2 w = e.dest;
        double cvw = e.cost;

        if (w.dist > v.dist + cvw) {
          w.dist = v.dist + cvw;
          w.prev = v;
          // Enqueue only if not already on the queue
          if (w.scratch++ % 2 == 0)
            q.add(w);
          else
            w.scratch--; // undo the enqueue increment    
        }
      }
    }
  }

  /**
   * Single-source negative-weighted acyclic-graph shortest-path algorithm.
   */
  public void acyclic(String startName) {
    Vertex2 start = vertexMap.get(startName);
    if (start == null)
      throw new NoSuchElementException("Start vertex not found");

    clearAll();
    Queue<Vertex2> q = new LinkedList<Vertex2>();
    start.dist = 0;

    // Compute the indegrees
    Collection<Vertex2> vertexSet = vertexMap.values();
    for (Vertex2 v : vertexSet)
      for (Edge2 e : v.adj)
        e.dest.scratch++;

    // Enqueue vertices of indegree zero
    for (Vertex2 v : vertexSet)
      if (v.scratch == 0)
        q.add(v);

    int iterations;
    for (iterations = 0; !q.isEmpty(); iterations++) {
      Vertex2 v = q.remove();

      for (Edge2 e : v.adj) {
        Vertex2 w = e.dest;
        double cvw = e.cost;

        if (--w.scratch == 0)
          q.add(w);

        if (v.dist == INFINITY)
          continue;

        if (w.dist > v.dist + cvw) {
          w.dist = v.dist + cvw;
          w.prev = v;
        }
      }
    }

    if (iterations != vertexMap.size())
      throw new GraphException("Graph has a cycle!");
  }

  /**
   * Process a request; return false if end of file.
   */
  public static boolean processRequest(BufferedReader in, CopyOfGraph g) {
    String startName = null;
    String destName = null;
    String alg = null;

    try {
      System.out.print("Enter start node:");
      if ((startName = in.readLine()) == null)
        return false;
      System.out.print("Enter destination node:");
      if ((destName = in.readLine()) == null)
        return false;
      System.out.print(" Enter algorithm (u, d, n, a ): ");
      if ((alg = in.readLine()) == null)
        return false;

      if (alg.equals("u"))
        g.unweighted(startName);
      else if (alg.equals("d")) {
        g.dijkstra(startName);
        g.printPath(destName);
        g.dijkstra2(startName);
      }
      else if (alg.equals("n"))
        g.negative(startName);
      else if (alg.equals("a"))
        g.acyclic(startName);

      g.printPath(destName);
    }
    catch (IOException e) {
      System.err.println(e);
    }
    catch (NoSuchElementException e) {
      System.err.println(e);
    }
    catch (GraphException e) {
      System.err.println(e);
    }
    return true;
  }

  /**
   * A main routine that: 1. Reads a file containing edges (supplied as a command-line parameter); 2. Forms the graph; 3. Repeatedly prompts for two vertices
   * and runs the shortest path algorithm. The data file is a sequence of lines of the format source destination.
   */
  public static void main(String[] args) {
    CopyOfGraph g = new CopyOfGraph();
    try {
      FileReader fin = new FileReader("data/weiss/graph1.txt");
      BufferedReader graphFile = new BufferedReader(fin);

      // Read the edges and insert
      String line;
      while ((line = graphFile.readLine()) != null) {
        StringTokenizer st = new StringTokenizer(line);

        try {
          if (st.countTokens() != 3) {
            System.err.println("Skipping ill-formatted line " + line);
            continue;
          }
          String source = st.nextToken();
          String dest = st.nextToken();
          int cost = Integer.parseInt(st.nextToken());
          g.addEdge(source, dest, cost);
        }
        catch (NumberFormatException e) {
          System.err.println("Skipping ill-formatted line " + line);
        }
      }
    }
    catch (IOException e) {
      System.err.println(e);
    }

    System.out.println("File read...");
    System.out.println(g.vertexMap.size() + " vertices");

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    while (processRequest(in, g))
      ;
  }
}