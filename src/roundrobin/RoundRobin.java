package roundrobin;

import heap.FibonacciHeap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import library.PairVertex;
import library.UndirectedGraph;

public class RoundRobin<T> {
  private T                              startNode;
  private UndirectedGraph<T>             graph;
  private double                         cost;
  private ArrayList<RoundRobinStruct<T>> listRR;

  public class RoundRobinStruct<T> {
    /* The Fibonacci heap we'll use to select nodes efficiently. */
    private FibonacciHeap<T>               pq;

    /*
     * This Fibonacci heap hands back internal handles to the nodes it stores. This map will associate each node with its entry in the Fibonacci heap.
     */
    private Map<T, FibonacciHeap.Entry<T>> entries;
    private UndirectedGraph<T>             result;
    private ArrayList<T>                   keys;
    private ArrayList<PairVertex<T>>       spanningTree;

    public FibonacciHeap<T> getPq() {
      return pq;
    }

    public void setPq(FibonacciHeap<T> pq) {
      this.pq = pq;
    }

    public Map<T, FibonacciHeap.Entry<T>> getEntries() {
      return entries;
    }

    public void setEntries(Map<T, FibonacciHeap.Entry<T>> entries) {
      this.entries = entries;
    }

    public RoundRobinStruct() {
      pq = new FibonacciHeap<T>();
      entries = new HashMap<T, FibonacciHeap.Entry<T>>();
      result = new UndirectedGraph<T>();
      keys = new ArrayList<T>();
      spanningTree = new ArrayList<PairVertex<T>>();
    }

    public UndirectedGraph<T> getResult() {
      return result;
    }

    public void setResult(UndirectedGraph<T> result) {
      this.result = result;
    }
  }

  /**
   * Given a node in the source graph and a set of nodes that we've visited so far, returns the minimum-cost edge from that node to some node that has been
   * visited before.
   * 
   * @param node The node that has not been considered yet.
   * @param graph The original graph whose MST is being computed.
   * @param result The resulting graph, used to check what has been visited so far.
   */
  private <T> T minCostEndpoint(T node, UndirectedGraph<T> graph, UndirectedGraph<T> result, ArrayList<PairVertex<T>> spanningTree) {
    /*
     * Track the best endpoint so far and its cost, initially null and +infinity.
     */
    T endpoint = null;
    double leastCost = Double.POSITIVE_INFINITY;

    /* Scan each node, checking whether it's a candidate. */
    for (Map.Entry<T, Double> entry : graph.edgesFrom(node).entrySet()) {
      /*
       * If the endpoint isn't in the nodes constructed so far, don't consider it.
       */
      if (!result.containsNode(entry.getKey()) /* && !spanningTree.contains(entry.getKey()) */)
        continue;

      /* If the edge costs more than what we know, skip it. */
      if (entry.getValue() >= leastCost)
        continue;

      /* Otherwise, update our guess to be this node. */
      endpoint = entry.getKey();
      leastCost = entry.getValue();
    }
    /*
     * Hand back the result. We're guaranteed to have found something, since otherwise we couldn't have dequeued this node.
     */
    return endpoint;
  }

  /**
   * Given a node in the graph, updates the priorities of adjacent nodes to take these edges into account. Due to some optimizations we make, this step takes in
   * several parameters beyond what might seem initially required. They are explained in the param section below.
   * 
   * @param node The node to explore outward from.
   * @param graph The graph whose MST is being computed, used so we can get the edges to consider.
   * @param pq The Fibonacci heap holding each endpoint.
   * @param result The result graph. We need this information so that we don't try to update information on a node that has already been considered and thus
   *          isn't in the queue.
   * @param entries A map from nodes to their corresponding heap entries. We need this so we can call decreaseKey on the correct elements.
   */
  private static <T> void addOutgoingEdges(T node, UndirectedGraph<T> graph, FibonacciHeap<T> pq, UndirectedGraph<T> result,
    Map<T, FibonacciHeap.Entry<T>> entries) {
    /* Start off by scanning over all edges emanating from our node. */
    for (Map.Entry<T, Double> arc : graph.edgesFrom(node).entrySet()) {
      /*
       * Given this arc, there are four possibilities.
       * 
       * 1. This endpoint has already been added to the graph. If so, we ignore the edge since it would form a cycle. 2. This endpoint is not in the graph and
       * has never been in the heap. Then we add it to the heap. 3. This endpoint is in the graph, but this is a better edge. Then we use decreaseKey to update
       * its priority. 4. This endpoint is in the graph, but there is a better edge to it. In that case, we similarly ignore it.
       */
      if (result.containsNode(arc.getKey()))
        continue; // Case 1

      if (!entries.containsKey(arc.getKey())) { // Case 2
        entries.put(arc.getKey(), pq.enqueue(arc.getKey(), arc.getValue()));
        //System.out.println("ADD em pq a aresta (Key; Value): " + arc.getKey() + " ; " + arc.getValue());
        //System.out.println("ADD em entries (MAP): " + arc.getKey().toString());
      }
      else if (entries.get(arc.getKey()).getPriority() > arc.getValue()) { // Case 3
        pq.decreaseKey(entries.get(arc.getKey()), arc.getValue());
      }

      // Case 4 handled implicitly by doing nothing.
    }
  }

  public T getStartNode() {
    return startNode;
  }

  public double getCost() {

    if (cost == 0) {
      for (PairVertex<T> pair : listRR.get(0).spanningTree) {
        cost += pair.getCost();
      }
    }
    return cost;
  }

  public void PrintSpanningTree() {
    for (PairVertex<T> pair : listRR.get(0).spanningTree) {
      System.out.println(pair.getOne() + " - " + pair.getTwo() + " : " + pair.getCost());
    }
  }

  public RoundRobin(UndirectedGraph<T> graph) throws Exception {
    this.graph = graph;
    //this.spanningTree = new ArrayList<PairVertex<T>>();
  }

  public void generateMST() throws Exception {

    /* The graph which will hold the resulting MST. */
    //UndirectedGraph<T> result = new UndirectedGraph<T>();

    listRR = new ArrayList<RoundRobinStruct<T>>(graph.size());

    for (Iterator<T> iterator = graph.iterator(); iterator.hasNext();) {
      T node = iterator.next();
      RoundRobinStruct<T> rrs = new RoundRobinStruct<T>();

      // adicionando os vértices
      rrs.keys.add(node);
      rrs.pq.enqueue(node, Double.POSITIVE_INFINITY);
      rrs.result.addNode(node);
      // adicionando as arestas para esse vértice
      addOutgoingEdges(node, graph, rrs.pq, rrs.result, rrs.entries);
      // adicioando na "fila"
      listRR.add(rrs);
    }

    /*
     * As an edge case, if the graph is empty, just hand back the empty graph.
     */
    if (graph.isEmpty())
      throw new Exception("The graph can not be empty");

    while (listRR.size() > 1) {
      RoundRobinStruct<T> item = listRR.get(0);
      /* Grab the cheapest node we can add. */
      T toAdd = item.pq.dequeueMin().getValue();
      /*
       * Determine which edge we should pick to add to the MST. We'll do this by getting the endpoint of the edge leaving the current node that's of minimum
       * cost and that enters the visited edges.
       */
      T endpoint = minCostEndpoint(toAdd, graph, item.result, item.spanningTree);
      /* Add this edge to the graph. */
      item.result.addNode(toAdd);
      //item.result.addNode(endpoint);
      double edgeCost = graph.edgeCost(toAdd, endpoint);
      item.result.addEdge(toAdd, endpoint, edgeCost);
      item.spanningTree.add(new PairVertex<T>(toAdd, endpoint, edgeCost));

      // Procurar
      int index = find(listRR, toAdd);
      RoundRobinStruct<T> itemToMerge = listRR.get(index);
      RoundRobinStruct<T> newItem = new RoundRobinStruct<T>();
      newItem.pq = FibonacciHeap.merge(item.pq, itemToMerge.pq);
      newItem.entries = mergeEntries(item.entries, itemToMerge.entries);
      mergeResult(newItem.result, item.result, itemToMerge.result);
      newItem.keys.addAll(item.keys);
      newItem.keys.addAll(itemToMerge.keys);
      newItem.spanningTree.addAll(item.spanningTree);
      newItem.spanningTree.addAll(itemToMerge.spanningTree);
      listRR.add(newItem);
      listRR.remove(item);
      listRR.remove(itemToMerge);
    }

    /*
     * for (PairVertex<T> pair : listRR.get(0).spanningTree) { System.out.println(pair.getOne() + " - " + pair.getTwo() + " : " + pair.getCost()); cost +=
     * pair.getCost(); } System.out.println("Custo: " + cost);
     */
  }

  public int find(List<RoundRobinStruct<T>> listRR, T node) {
    for (int i = 1; i < listRR.size(); i++) {
      for (T key : listRR.get(i).keys) {
        if (key.equals(node)) {
          return i;
        }
      }
      //if (listRR.get(i).pq.find(node))
    }
    return -1;
  }

  public boolean findSpanningTree(T node, ArrayList<PairVertex<?>> spanningTree) {
    for (PairVertex<?> pairVertex : spanningTree) {
      if (pairVertex.getOne().equals(node) || pairVertex.getTwo().equals(node))
        return true;
    }
    return false;
  }

  public Map<T, FibonacciHeap.Entry<T>> mergeEntries(Map<T, FibonacciHeap.Entry<T>> one, Map<T, FibonacciHeap.Entry<T>> two) {
    Map<T, FibonacciHeap.Entry<T>> three = new HashMap<T, FibonacciHeap.Entry<T>>();
    three.putAll(one);
    three.putAll(two);

    return three;
  }

  public void mergeResult(UndirectedGraph<T> toMerge, UndirectedGraph<T> one, UndirectedGraph<T> two) {

    for (T t : one) {
      toMerge.addNode(t);
    }

    for (T t : two) {
      toMerge.addNode(t);
    }

    for (Iterator<T> iterator = one.iterator(); iterator.hasNext();) {
      T node = iterator.next();
      for (Map.Entry<T, Double> arc : graph.edgesFrom(node).entrySet()) {
        if (toMerge.containsNode(arc.getKey()))
          toMerge.addEdge(arc.getKey(), node, arc.getValue());
      }
    }

    for (Iterator<T> iterator = two.iterator(); iterator.hasNext();) {
      T node = iterator.next();
      for (Map.Entry<T, Double> arc : graph.edgesFrom(node).entrySet()) {
        if (toMerge.containsNode(arc.getKey()))
          toMerge.addEdge(arc.getKey(), node, arc.getValue());
      }
    }
  }
};