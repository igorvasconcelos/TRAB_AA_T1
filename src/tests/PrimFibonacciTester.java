package tests;

import java.util.List;

import library.Logger;
import pph.utils.OrderedPair;

public class PrimFibonacciTester {

  //O nome do arquivo de input padrão(usado para testes).
  private static final String DEFAULT_INPUT_FILE_NAME = "data/ALUE/alue2087.stp";

  public static void main(String[] args) {
    String inputFile;
    // Verifica se o arquivo de input foi passado como parâmetro.
    if (args.length == 1) {
      inputFile = args[0];

    }
    else {
      // Caso nenhum arquivo tenha sido informado, testa com o arquivo criado para testes.
      inputFile = DEFAULT_INPUT_FILE_NAME;

      // Informa que a applicação esta em modo debug.
      Logger.isDebugging = false;
    }

    new PrimFibonacciTester().run();
  }

  public void run() {

  }

  /**
   * Este é o método que realmente faz todo o processamento. O método run foi criado apenas para que não fosse necessário ficar usando variáveis e métodos
   * estáticos.
   * 
   * @param listNOfOrderedPairs
   * @param title
   */
  protected void genericProcess(List<OrderedPair> listNOfOrderedPairs, String title) {
    try {
      Logger.printOntoScreen(title);

      // Momento em que o algoritmo iniciou sua execução.
      long startTime = System.currentTimeMillis();

      // Quantidade de iterações feitas dentro de 5 segundos.
      long iterations = 0;

      while (System.currentTimeMillis() - startTime < 5000) {
        // Em cada iteração, é um novo processamento, então a quantidade de operações é setada para 0.

        // Incrementa a quantidade de iterações feitas dentro de 5 segundos.
        iterations++;
      }

      // Momento em que o algoritmo terminou sua execução.
      long finishTime = System.currentTimeMillis() - startTime;

      // Calcula a média de tempo de cada iteração.
      float media = (float) finishTime / iterations;

      // Imprime os resultados obtidos.

    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
