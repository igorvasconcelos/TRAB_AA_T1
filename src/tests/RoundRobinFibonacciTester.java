package tests;

import java.io.File;
import java.util.Date;

import library.Logger;
import library.UndirectedGraph;
import library.Utils;
import mst.RoundRobinFibonacciV2;

public class RoundRobinFibonacciTester {

  //O nome do arquivo de input padrão(usado para testes).
  private static final String      DEFAULT_INPUT_FILE_NAME = "data/ALUE/alue2105.stp";
  private static String            path                    = "data/DMXA/";
  private static String            inputFile               = "";
  private UndirectedGraph<Integer> graph;

  public static void main(String[] args) throws Exception {

    // Verifica se o arquivo de input foi passado como parâmetro.
    if (args.length == 1) {
      inputFile = args[0];
    }
    else if (args.length == 2) {
      path = args[1];
    }
    else {
      // Caso nenhum arquivo tenha sido informado, testa com o arquivo criado para testes.
      inputFile = DEFAULT_INPUT_FILE_NAME;
      // Informa que a applicação esta em modo debug.
      Logger.isDebugging = false;
    }
    Logger.printOntoScreen(" ********* Round Robin *********");
    new RoundRobinFibonacciTester().run(args.length == 2); //args.length == 2
  }

  public void run(boolean batch) throws Exception {
    if (batch) {
      String fileName;
      String fileNameAndPath;
      File folder = new File(path);
      // Obtém a lista de arquivos neste diretório, como o próprio método "listFiles()" diz,
      // não existe uma garantia em que ordem os arquivos serão retornados.
      File[] listOfFiles = folder.listFiles();
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          fileName = listOfFiles[i].getName();
          fileNameAndPath = path + fileName;
          Logger.printOntoScreen("***********************************************");

          Logger.printOntoScreen("Lendo Arquivo: " + fileName);
          graph = new UndirectedGraph<Integer>();
          Utils.getUndirectedFromInputFile(graph, fileNameAndPath);

          Logger.printOntoScreen("Grafo Montado.");
          genericProcess(graph);
        }
      }
    }
    else {
      graph = new UndirectedGraph<Integer>();
      Utils.getUndirectedFromInputFile(graph, inputFile);
      genericProcess(graph);
    }
  }

  /**
   * Este é o método que realmente faz todo o processamento. O método run foi criado apenas para que não fosse necessário ficar usando variáveis e métodos
   * estáticos.
   * 
   * @param graph
   */
  protected void genericProcess(UndirectedGraph<Integer> graph) {
    try {

      Logger.printOntoScreen("Execução iniciada às: " + new Date());
      RoundRobinFibonacciV2<Integer> primFibonacci = new RoundRobinFibonacciV2<Integer>(graph);
      //for (Integer node : graph) {
      // System.err.println("Partindo de " + node);
      // for (Entry<Integer, Double> entry : graph.edgesFrom(node).entrySet()) {
      //  System.out.println("      - chega em: " + entry.getKey() + " com peso: " + entry.getValue());
      // }
      // }

      // Momento em que o algoritmo iniciou sua execução.
      long startTime = System.currentTimeMillis();

      // Quantidade de iterações feitas dentro de 5 segundos.
      long iterations = 0;

      while (System.currentTimeMillis() - startTime < 5000) {
        // Em cada iteração, é um novo processamento, então a quantidade de operações é setada para 0.
        primFibonacci.generateMST();

        // Incrementa a quantidade de iterações feitas dentro de 5 segundos.
        iterations++;
      }

      // Momento em que o algoritmo terminou sua execução.
      //long end = System.currentTimeMillis();
      long finishTime = System.currentTimeMillis() - startTime;

      // Calcula a média de tempo de cada iteração.
      float media = (float) finishTime / iterations;

      // Imprime os resultados obtidos.
      //System.err.println("Fim em mile: " + finishTime);
      Logger.printOntoScreen("Execução Finalizada as: " + new Date());
      Logger.printOntoScreen("Custo total da MST: " + primFibonacci.getCost());
      Logger.printOntoScreen("Tempo de execução médio: " + media + " milisegundos");
      Logger.printOntoScreen("Quantidade de iterações em 5 segundos: " + iterations);

      //primFibonacci.PrintSpanningTree();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}