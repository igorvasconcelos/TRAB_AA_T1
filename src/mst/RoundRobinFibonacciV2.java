package mst;

import heap.FibonacciHeap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import library.PairVertex;
import library.UndirectedGraph;

public class RoundRobinFibonacciV2 {
  private UndirectedGraph<Integer>       graph;
  private double                         cost;
  private List<RoundRobinStruct>         listRR;

  private ArrayList<PairVertex<Integer>> spanningTree;
  private UndirectedGraph<Integer>       result;
  private List<Integer>                  groups;

  public class RoundRobinStruct {

    /* The Fibonacci heap we'll use to select nodes efficiently. */
    private FibonacciHeap<Integer> pq;
    private Integer                value;

    /*
     * This Fibonacci heap hands back internal handles to the nodes it stores. This map will associate each node with its entry in the Fibonacci heap.
     */
    //private Map<T, FibonacciHeap.Entry<T>> entries;
    //private UndirectedGraph<T>             result;
    //private ArrayList<T>                   keys;

    //private ArrayList<PairVertex<T>>       spanningTree;

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public FibonacciHeap<Integer> getPq() {
      return pq;
    }

    public void setPq(FibonacciHeap<Integer> pq) {
      this.pq = pq;
    }

    public RoundRobinStruct() {
      pq = new FibonacciHeap<Integer>();
    }
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
  private PairVertex<Integer> minCostEndpoint(Integer toAdd, UndirectedGraph<Integer> graph, UndirectedGraph<Integer> result) {
    /*
     * Track the best endpoint so far and its cost, initially null and +infinity.
     */
    int endpoint = -1;
    double leastCost = Double.POSITIVE_INFINITY;

    int groupId = find(toAdd);

    // Montando os grupos do componente
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 1; i < groups.size(); i++) {
      if (find(i).equals(groupId))
        list.add(i);
    }
    int temp = toAdd;
    /* Scan each node, checking whether it's a candidate. */
    for (int i = 0; i < list.size(); i++) {

      for (Map.Entry<Integer, Double> entry : graph.edgesFrom(list.get(i)).entrySet()) {
        if (result.containsEdge(entry.getKey(), list.get(i)))
          continue;

        // Testa se estão no mesmo grupo
        if (find(entry.getKey()) == find(list.get(i))) {
          continue;
        }

        // Testa se o valor é maior, se for não atualizará 
        if (entry.getValue() >= leastCost)
          continue;

        //toAdd = keys.get(i);
        temp = list.get(i);
        endpoint = entry.getKey();
        leastCost = entry.getValue();
      }
    }
    /*
     * Hand back the result. We're guaranteed to have found something, since otherwise we couldn't have dequeued this node.
     */
    PairVertex<Integer> p = new PairVertex<Integer>(temp, endpoint, leastCost);
    return p;
  }

  public double getCost() {

    if (cost == 0) {
      for (PairVertex<Integer> pair : spanningTree) {
        cost += pair.getCost();
      }
    }
    return cost;
  }

  public void PrintSpanningTree() {
    for (PairVertex<Integer> pair : spanningTree) {
      System.out.println(pair.getOne() + " - " + pair.getTwo() + " : " + pair.getCost());
    }
  }

  public RoundRobinFibonacciV2(UndirectedGraph<Integer> graph) throws Exception {
    this.graph = graph;
  }

  public void generateMST() throws Exception {

    /* The graph which will hold the resulting MST. */
    this.listRR = new ArrayList<RoundRobinStruct>();
    this.spanningTree = new ArrayList<PairVertex<Integer>>();
    this.result = new UndirectedGraph<Integer>();
    this.groups = new ArrayList<Integer>();

    makeSet();

    for (Iterator<Integer> iterator = graph.iterator(); iterator.hasNext();) {
      int node = iterator.next();
      RoundRobinStruct rrs = new RoundRobinStruct();

      // adicionando os vértices
      rrs.pq.enqueue(node, Double.POSITIVE_INFINITY);
      rrs.value = node;
      listRR.add(rrs);
    }

    /*
     * As an edge case, if the graph is empty, just hand back the empty graph.
     */
    if (graph.isEmpty())
      throw new Exception("The graph can not be empty");

    while (listRR.size() > 1) {
      RoundRobinStruct item = listRR.get(0);
      /* Grab the cheapest node we can add. */
      int toAdd = item.pq.dequeueMin().getValue();

      //if (toAdd == 133)
      //  System.out.println("ops;;");

      PairVertex<Integer> pair = minCostEndpoint(toAdd, graph, result);
      toAdd = pair.getOne();
      Integer endpoint = pair.getTwo();
      /* Add this edge to the graph. */
      result.addNode(toAdd);
      result.addNode(endpoint);
      System.out.println("toAdd: " + toAdd + " - endpoint: " + endpoint);

      double edgeCost = graph.edgeCost(toAdd, endpoint);
      result.addEdge(toAdd, endpoint, edgeCost);

      spanningTree.add(new PairVertex<Integer>(toAdd, endpoint, edgeCost));

      // Procurar
      int index = find(listRR, toAdd, endpoint);
      if (index >= 0) {
        RoundRobinStruct itemToMerge = listRR.get(index);
        RoundRobinStruct newItem = new RoundRobinStruct();
        newItem.pq = FibonacciHeap.merge(item.pq, itemToMerge.pq);
        newItem.value = itemToMerge.value;

        // Merge
        union(item.value, itemToMerge.value);
        listRR.add(newItem);
        listRR.remove(item);
        listRR.remove(itemToMerge);
      }
    }
  }

  public int find(List<RoundRobinStruct> listRR, Integer node, Integer endPoint) {
    int index = -1;
    //int u = find(Integer.parseInt(node.toString()));
    int v = find(endPoint);

    //System.out.println(u);
    //System.out.println(v);

    for (int i = 1; i < listRR.size(); i++) {
      if (listRR.get(i).value.equals(v)) {
        index = i;
        break;
      }
    }
    return index;
  }

  public boolean findSpanningTree(Integer node, ArrayList<PairVertex<?>> spanningTree) {
    for (PairVertex<?> pairVertex : spanningTree) {
      if (pairVertex.getOne().equals(node) || pairVertex.getTwo().equals(node))
        return true;
    }
    return false;
  }

  private void makeSet() {
    for (int i = 0; i <= this.graph.size(); i++) {
      groups.add(-1);
    }
  }

  private void union(Integer one, Integer two) {
    int u = find(one);
    int v = find(two);
    groups.set(u, v);
  }

  private Integer find(Integer node) {
    return groups.get(node).equals(-1) ? node : find(groups.get(node));
  }
};