package tests;

import library.PairVertex;
import library.UndirectedGraph;

public class ExecuteTree {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    UndirectedGraph<String> stringGraph = new UndirectedGraph<String>();
    UndirectedGraph<Integer> intGraph = new UndirectedGraph<Integer>();
    
    intGraph.addNode(1);
    intGraph.addNode(2);
    intGraph.addNode(3);
    intGraph.addNode(4);
    intGraph.addNode(5);
    
    intGraph.addEdge(1, 2, 1);
    intGraph.addEdge(1, 3, 2);
    intGraph.addEdge(2, 3, 3);
    intGraph.addEdge(3, 4, 1);
    intGraph.addEdge(2, 4, 3);
    intGraph.addEdge(4, 5, 2);
    intGraph.addEdge(3, 5, 4);
    
    stringGraph.addNode("A");
    stringGraph.addNode("B");
    stringGraph.addNode("C");
    stringGraph.addNode("D");
    stringGraph.addNode("E");
    stringGraph.addNode("F");
    stringGraph.addNode("G");

    stringGraph.addEdge("A", "B", 8);
    stringGraph.addEdge("A", "C", 5);

    stringGraph.addEdge("B", "C", 10);
    stringGraph.addEdge("B", "D", 2);
    stringGraph.addEdge("B", "E", 18);
    
    stringGraph.addEdge("C", "D", 3);
    stringGraph.addEdge("C", "F", 16);
    
    stringGraph.addEdge("D", "E", 12);
    stringGraph.addEdge("D", "F", 30);
    stringGraph.addEdge("D", "G", 14);
    
    stringGraph.addEdge("E", "G", 4);
    
    stringGraph.addEdge("F", "G", 26);

    
    System.out.println(" ********** Prim ********");

    System.out.println();
    mst.Prim<Integer> teste3 = new mst.Prim<Integer>(intGraph);
    teste3.generateMST();
    System.out.println("Nó inicial: " + teste3.getStartNode());
    for (PairVertex<Integer> parVetex : teste3.getSpanningTree()) {
      System.out.println(parVetex.getOne() + " - " + parVetex.getTwo() + " : " + parVetex.getCost());
    }
    System.out.println("Custo da MST:" + teste3.getCost());
    
    System.out.println();
    mst.Prim<String> teste2 = new mst.Prim<String>(stringGraph);
    teste2.generateMST();
    System.out.println("Nó inicial: " + teste2.getStartNode());
    for (PairVertex<String> parVetex : teste2.getSpanningTree()) {
      System.out.println(parVetex.getOne() + " - " + parVetex.getTwo() + " : " + parVetex.getCost());
    }
    System.out.println("Custo da MST:" + teste2.getCost());

    System.out.println();
    mst.PrimTree<Integer> teste4 = new mst.PrimTree<Integer>(intGraph);
    teste4.generateMST();
    System.out.println("Nó inicial: " + teste4.getStartNode());
    for (PairVertex<Integer> parVetex : teste4.getSpanningTree()) {
      System.out.println(parVetex.getOne() + " - " + parVetex.getTwo() + " : " + parVetex.getCost());
    }
    System.out.println("Custo da MST:" + teste4.getCost());
    
	System.out.println();
    mst.PrimTree<String> teste = new mst.PrimTree<String>(stringGraph);
    teste.generateMST();
    System.out.println("Nó inicial: " + teste.getStartNode());
    for (PairVertex<String> parVetex : teste.getSpanningTree()) {
      System.out.println(parVetex.getOne() + " - " + parVetex.getTwo() + " : " + parVetex.getCost());
    }
    System.out.println("Custo da MST:" + teste.getCost());
    
    System.out.println(" ********** Round Robin ********");
  }
}
