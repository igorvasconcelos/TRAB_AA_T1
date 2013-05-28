package mst;

import heap.FibonacciHeap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import library.PairVertex;
import library.UndirectedGraph;

public class RoundRobinFibonacciV2<T> {
  private UndirectedGraph<T>        graph;
  private double                    cost;
  private List<RoundRobinStruct<T>> listRR;

  private ArrayList<PairVertex<T>>  spanningTree;
  private UndirectedGraph<T>        result;

  public class RoundRobinStruct<T> {

    /* The Fibonacci heap we'll use to select nodes efficiently. */
    private FibonacciHeap<T>               pq;

    /*
     * This Fibonacci heap hands back internal handles to the nodes it stores. This map will associate each node with its entry in the Fibonacci heap.
     */
    private Map<T, FibonacciHeap.Entry<T>> entries;
    //private UndirectedGraph<T>             result;
    private ArrayList<T>                   keys;

    //private ArrayList<PairVertex<T>>       spanningTree;

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
      //result = new UndirectedGraph<T>();
      keys = new ArrayList<T>();
      //spanningTree = new ArrayList<PairVertex<T>>();
    }

    //public UndirectedGraph<T> getResult() {
    //  return result;
    // }

    //public void setResult(UndirectedGraph<T> result) {
    //  this.result = result;
    // }
  }

  /**
   * Given a node in the source graph and a set of nodes that we've visited so far, returns the minimum-cost edge from that node to some node that has been
   * visited before.
   * 
   * @param toAdd
   * 
   * @param keys Group Keys
   * @param graph The original graph whose MST is being computed.
   * @param result The resulting graph, used to check what has been visited so far.
   * @return pair
   */
  private PairVertex<T> minCostEndpoint(T toAdd, ArrayList<T> keys, UndirectedGraph<T> graph, UndirectedGraph<T> result) {
    /*
     * Track the best endpoint so far and its cost, initially null and +infinity.
     */
    T endpoint = null;
    double leastCost = Double.POSITIVE_INFINITY;

    for (int i = 0; i < keys.size(); i++) {
      /* Scan each node, checking whether it's a candidate. */
      for (Map.Entry<T, Double> entry : graph.edgesFrom(keys.get(i)).entrySet()) {

        if (result.containsEdge(entry.getKey(), keys.get(i)))
          continue;

        if (keys.get(i) != entry.getKey()) {
          /* If the edge costs more than what we know, skip it. */
          if (entry.getValue() >= leastCost)
            continue;

          toAdd = keys.get(i);
          endpoint = entry.getKey();
          leastCost = entry.getValue();
        }
      }
    }
    /*
     * Hand back the result. We're guaranteed to have found something, since otherwise we couldn't have dequeued this node.
     */
    PairVertex<T> p = new PairVertex<T>(toAdd, endpoint, leastCost);
    return p;
  }

  public double getCost() {

    if (cost == 0) {
      for (PairVertex<T> pair : spanningTree) {
        cost += pair.getCost();
      }
    }
    return cost;
  }

  public void PrintSpanningTree() {
    for (PairVertex<T> pair : spanningTree) {
      System.out.println(pair.getOne() + " - " + pair.getTwo() + " : " + pair.getCost());
    }
  }

  public RoundRobinFibonacciV2(UndirectedGraph<T> graph) throws Exception {
    this.graph = graph;
    //this.spanningTree = new ArrayList<PairVertex<T>>();
    //this.result = new UndirectedGraph<T>();
  }

  public void generateMST() throws Exception {

    /* The graph which will hold the resulting MST. */
    //UndirectedGraph<T> result = new UndirectedGraph<T>();

    this.listRR = new ArrayList<RoundRobinStruct<T>>();
    this.spanningTree = new ArrayList<PairVertex<T>>();
    this.result = new UndirectedGraph<T>();

    for (Iterator<T> iterator = graph.iterator(); iterator.hasNext();) {
      T node = iterator.next();
      RoundRobinStruct<T> rrs = new RoundRobinStruct<T>();

      // adicionando os vértices
      rrs.keys.add(node);
      rrs.pq.enqueue(node, Double.POSITIVE_INFINITY);
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
      T aux = toAdd;

      PairVertex<T> pair = minCostEndpoint(toAdd, item.keys, graph, result);
      T endpoint = pair.getOne();
      toAdd = pair.getTwo();
      /* Add this edge to the graph. */
      result.addNode(toAdd);
      result.addNode(endpoint);
      //System.out.println("toAdd: " + toAdd + " - endpoint: " + endpoint);

      double edgeCost = graph.edgeCost(toAdd, endpoint);
      result.addEdge(toAdd, endpoint, edgeCost);

      spanningTree.add(new PairVertex<T>(toAdd, endpoint, edgeCost));

      // Procurar
      int index = find(listRR, item.keys, toAdd, endpoint);
      if (index < 0) {
        System.out.println("ops...");
        System.out.println(aux);
        minCostEndpoint(aux, item.keys, graph, result);
      }
      if (index >= 0) {
        RoundRobinStruct<T> itemToMerge = listRR.get(index);
        RoundRobinStruct<T> newItem = new RoundRobinStruct<T>();
        newItem.pq = FibonacciHeap.merge(item.pq, itemToMerge.pq);

        newItem.keys.addAll(item.keys);
        newItem.keys.addAll(itemToMerge.keys);

        // Merge
        listRR.add(newItem);
        listRR.remove(item);
        listRR.remove(itemToMerge);
      }
    }
  }

  public int find(List<RoundRobinStruct<T>> listRR, ArrayList<T> keys, T node, T endPoint) {
    int index = -1;
    for (int i = 1; i < listRR.size(); i++) {
      if (listRR.get(i).keys.contains(node) || listRR.get(i).keys.contains(endPoint))
        index = i;
    }
    return index;
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