package mst;

import newheaps.LeftistHeapV2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import library.PairVertex;
import library.UndirectedGraph;

public class RoundRobinLeftistHeap {
  private UndirectedGraph<Integer>       graph;
  private double                         cost;
  private List<RoundRobinStruct>         listRR;

  private ArrayList<PairVertex<Integer>> spanningTree;
  private UndirectedGraph<Integer>       result;
  private List<Integer>                  groups;
  private List<Integer>                  ranks;

  public class RoundRobinStruct {

    /* The Fibonacci heap we'll use to select nodes efficiently. */
    private LeftistHeapV2<PairVertex<Integer>> pq;
    private Integer                          value;

    /*
     * This Fibonacci heap hands back internal handles to the nodes it stores. This map will associate each node with its entry in the Fibonacci heap.
     */

    public RoundRobinStruct() {
      pq = new LeftistHeapV2<PairVertex<Integer>>();
    }
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

  public RoundRobinLeftistHeap(UndirectedGraph<Integer> graph) throws Exception {
    this.graph = graph;
  }

  public void generateMST() throws Exception {

    /* The graph which will hold the resulting MST. */
    this.listRR = new ArrayList<RoundRobinStruct>();
    this.spanningTree = new ArrayList<PairVertex<Integer>>();
    this.result = new UndirectedGraph<Integer>();
    this.groups = new ArrayList<Integer>(this.graph.size());
    this.ranks = new ArrayList<Integer>(this.graph.size());
    cost = 0;

    /*
     * As an edge case, if the graph is empty, just hand back the empty graph.
     */
    if (graph.isEmpty())
      throw new Exception("The graph can not be empty");

    // initialize
    makeSet();

    for (Iterator<Integer> iterator = graph.iterator(); iterator.hasNext();) {
      int node = iterator.next();
      RoundRobinStruct rrs = new RoundRobinStruct();

      // adicionando as arestas
      makeEdgesFrom(rrs.pq, node);
      rrs.value = node;
      listRR.add(rrs);
    }

    while (listRR.size() > 1) {

      RoundRobinStruct item = listRR.get(0);
      /* Grab the cheapest node we can add. */
      PairVertex<Integer> edge = getMinEdge(item, result);

      /* Add this edge to the graph. */
      result.addNode(edge.getOne());
      result.addNode(edge.getTwo());
      result.addEdge(edge.getOne(), edge.getTwo(), edge.getCost());
      //System.out.println("toAdd: " + edge.getOne() + " - endpoint: " + edge.getTwo());

      cost += edge.getCost();

      // Se quiser imprimir o caminho é só descomentar esse código
      //spanningTree.add(new PairVertex<Integer>(edge.getOne(), edge.getTwo(), edge.getCost()));

      // Procurar
      int index = find(listRR, edge.getTwo());
      if (index >= 0) {
        RoundRobinStruct itemToMerge = listRR.get(index);
        RoundRobinStruct newItem = new RoundRobinStruct();
        //newItem.pq = LeftistHeapV2.merge(item.pq, itemToMerge.pq);
        newItem.pq.insertAll(item.pq);
        newItem.pq.union(itemToMerge.pq);

        // Merge
        newItem.value = union(edge.getOne(), edge.getTwo());
        listRR.add(newItem);
        listRR.remove(item);
        listRR.remove(itemToMerge);
      }
      else { // apenas para teste em caso de erro
        System.err.println(index);
        //find(listRR, edge.getTwo());
      }
    }
  }

  private PairVertex<Integer> getMinEdge(RoundRobinStruct item, UndirectedGraph<Integer> result) {

    PairVertex<Integer> edge;
    int group;
    do {
      //edge = item.pq.dequeueMin().getValue();
      edge = item.pq.extractMinimum().getKey();

      group = find(edge.getTwo());
    } while (result.containsEdge(edge.getOne(), edge.getTwo()) || item.value.equals(group));
    return edge;
  }

  public int find(List<RoundRobinStruct> listRR, Integer endPoint) {
    int index = -1;
    //int u = find(toAdd);
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

  private void makeSet() {
    for (int i = 0; i <= this.graph.size(); i++) {
      groups.add(-1);
      ranks.add(0);
    }
  }

  private int union(Integer one, Integer two) {
    int u = find(one);
    int v = find(two);

    if (ranks.get(one).compareTo(ranks.get(two)) > 0) {
      groups.set(v, u);
      return u;
    }
    else if (ranks.get(two).compareTo(ranks.get(one)) > 0) {
      groups.set(u, v);
      return v;
    }
    else {
      groups.set(v, u);
      ranks.set(one, ranks.get(one) + 1);
    }
    return u;
  }

  private Integer find(Integer node) {
    return groups.get(node).equals(-1) ? node : find(groups.get(node));
  }

  private void makeEdgesFrom(LeftistHeapV2<PairVertex<Integer>> heap, int node) {
    for (Map.Entry<Integer, Double> entry : graph.edgesFrom(node).entrySet()) {
      PairVertex<Integer> pairVertex = new PairVertex<Integer>(node, entry.getKey(), entry.getValue());
      //heap.enqueue(pairVertex, entry.getValue());
      heap.insert(pairVertex, entry.getValue());
    }
  }
};