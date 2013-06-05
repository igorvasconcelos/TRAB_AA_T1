package tests;

import java.io.File;
import java.util.Date;

import library.Logger;
import library.UndirectedGraph;
import library.Utils;
import mst.PrimTree;

public class PrimAvlTester {
  // O nome do arquivo de input padr�o (usado para testes).
  private static final String      DEFAULT_INPUT_FILE_NAME = "data/ALUE/alue2087.stp";
  private static String            path                    = "data/ALUT/";
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
    Logger.printOntoScreen(" ********* Prim com AVL *********");
    new PrimAvlTester().run(true);
  }

  public void run(boolean batch) throws Exception {
    if (batch) {
      String fileName;
      String fileNameAndPath;
      File folder = new File(path);
      // Obt�m a lista de arquivos neste diret�rio, como o pr�prio m�todo "listFiles()" diz,
      // n�o existe uma garantia em que ordem os arquivos ser�o retornados.
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
   * Este � o m�todo que realmente faz todo o processamento. O m�todo run foi criado apenas para que n�o fosse necess�rio ficar usando vari�veis e m�todos
   * est�ticos.
   * 
   * @param listNOfOrderedPairs
   * @param title
   */
  protected void genericProcess(UndirectedGraph<Integer> graph) {
    try {
      Logger.printOntoScreen("Execução iniciada às: " + new Date());
      PrimTree primAvl = new PrimTree(graph);

      // Momento em que o algoritmo iniciou sua execu��o.
      long startTime = System.currentTimeMillis();

      // Quantidade de itera��es feitas dentro de 5 segundos.
      long iterations = 0;

      while (System.currentTimeMillis() - startTime < 5000) {
        // Em cada itera��o, � um novo processamento, ent�o a quantidade de opera��es � setada para 0.
        primAvl.generateMST();

        // Incrementa a quantidade de itera��es feitas dentro de 5 segundos.
        iterations++;
      }

      // Momento em que o algoritmo terminou sua execu��o.
      long finishTime = System.currentTimeMillis() - startTime;

      // Calcula a m�dia de tempo de cada itera��o.
      float media = (float) finishTime / iterations;

      // Imprime os resultados obtidos.
      Logger.printOntoScreen("Execução Finalizada as: " + new Date());
      Logger.printOntoScreen("Custo total da MST: " + primAvl.getCost());
      Logger.printOntoScreen("Tempo de execução médio: " + media + " milesegundos");
      Logger.printOntoScreen("Quantidade de itera��es em 5 segundos: " + iterations);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
