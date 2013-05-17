package tests;

import library.Node;
import library.PairVertex;
import library.UndirectedGraph;
import mst.Prim;

public class Execute {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub

    UndirectedGraph<Integer> intGraph = new UndirectedGraph<Integer>();
    UndirectedGraph<String> stringGraph = new UndirectedGraph<String>();
    UndirectedGraph<Node<String>> stringGraph2 = new UndirectedGraph<Node<String>>();
/*
 * intGraph.addNode(1); intGraph.addNode(2); intGraph.addNode(3); intGraph.addNode(4); intGraph.addNode(5); intGraph.addNode(6); intGraph.addNode(7);
 * intGraph.addNode(8); intGraph.addNode(9); intGraph.addEdge(1, 2, 4); intGraph.addEdge(2, 1, 4); intGraph.addEdge(1, 3, 8);
 * 
 * intGraph.addEdge(2, 4, 8); intGraph.addEdge(2, 3, 11);
 * 
 * intGraph.addEdge(3, 5, 7); intGraph.addEdge(3, 6, 1);
 * 
 * intGraph.addEdge(4, 5, 2); intGraph.addEdge(4, 7, 7); intGraph.addEdge(4, 8, 4);
 * 
 * intGraph.addEdge(5, 6, 6);
 * 
 * intGraph.addEdge(6, 8, 2);
 * 
 * intGraph.addEdge(7, 8, 14); intGraph.addEdge(7, 9, 9);
 * 
 * intGraph.addEdge(8, 9, 10);
 * 
 * stringGraph.addNode("A"); stringGraph.addNode("B"); stringGraph.addNode("C"); stringGraph.addNode("D");
 * 
 * stringGraph.addEdge("A", "B", 2); stringGraph.addEdge("A", "C", 8);
 * 
 * stringGraph.addEdge("B", "D", 1); stringGraph.addEdge("C", "D", 3);
 */

    Node<String> nodeA = new Node<String>("A", Double.POSITIVE_INFINITY);
    stringGraph2.addNode(nodeA);
    Node<String> nodeB = new Node<String>("B", Double.POSITIVE_INFINITY);
    stringGraph2.addNode(nodeB);
    Node<String> nodeC = new Node<String>("C", Double.POSITIVE_INFINITY);
    stringGraph2.addNode(nodeC);
    Node<String> nodeD = new Node<String>("D", Double.POSITIVE_INFINITY);
    stringGraph2.addNode(nodeD);
    Node<String> nodeE = new Node<String>("E", Double.POSITIVE_INFINITY);
    stringGraph2.addNode(nodeE);

    stringGraph2.addEdge(nodeA, nodeB, 8);
    stringGraph2.addEdge(nodeA, nodeC, 5);

    stringGraph2.addEdge(nodeB, nodeC, 2);
    stringGraph2.addEdge(nodeB, nodeD, 15);

    stringGraph2.addEdge(nodeC, nodeD, 10);
    stringGraph2.addEdge(nodeC, nodeE, 5);

    stringGraph2.addEdge(nodeD, nodeE, 20);

    //UndirectedGraph<Integer> result = mst.Prim.mst(graph);
/*
 * System.out.println("***** Testes sem Lazy ******"); mst.Prim<Integer> teste = new Prim<Integer>(intGraph); teste.generateMST(false);
 * System.out.println("Nó inicial: " + teste.getStartNode()); for (PairVetex<Integer> node : teste.getSpanningTree()) { System.out.println(node.getOne() + " - "
 * + node.getTwo() + " : " + node.getCost()); } System.out.println("Custo da MST:" + teste.getCost());
 * 
 * System.out.println(); mst.Prim<String> teste2 = new Prim<String>(stringGraph); teste2.generateMST(false); System.out.println("Nó inicial: " +
 * teste2.getStartNode()); for (PairVetex<String> node : teste2.getSpanningTree()) { System.out.println(node.getOne() + " - " + node.getTwo() + " : " +
 * node.getCost()); } System.out.println("Custo da MST:" + teste2.getCost());
 */
    //System.out.println("***** Testes com Lazy ******");
    //mst.Prim<String> teste3 = new Prim<String>(stringGraph2, true);
    //teste3.generateMST(false);
    //System.out.println("Nó inicial: " + teste3.getStartNode().getVertex());
    //for (PairVertex<Node<String>> parVetex : teste3.getSpanningTree()) {
    //  System.out.println(parVetex.getOne().getVertex() + " - " + parVetex.getTwo().getVertex() + " : " + parVetex.getCost());
    // }
    //System.out.println("Custo da MST:" + teste3.getCost());

    System.out.println("***** Testes sem Lazy ******");
    System.out.println();
    mst.Prim<String> teste3 = new Prim<String>(stringGraph2, false);
    teste3.generateMST(false);
    System.out.println("Nó inicial: " + teste3.getStartNode().getVertex());
    for (PairVertex<Node<String>> parVetex : teste3.getSpanningTree()) {
      System.out.println(parVetex.getOne().getVertex() + " - " + parVetex.getTwo().getVertex() + " : " + parVetex.getCost());
    }
    System.out.println("Custo da MST:" + teste3.getCost());

    //teste2.generateMST(true);

    //Node n = new Node(1, 2);
    //n.g
    // Utils.getListFromInputFile("data/ALUE/alue2087.stp");
  }
}
