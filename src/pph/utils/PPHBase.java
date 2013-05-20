package pph.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import library.Base;
import library.Logger;
import library.Utils;

public abstract class PPHBase extends Base {

  private float               ratio;
  private boolean             isChanged;

  // A lista S* que vai conter os valores que validam o lemma.
  protected List<OrderedPair> listS;

  // A razão que deve ser calculada e apresentada no final.
  protected float             finalRatio;

  // Este é o par(a0, b0).
  protected OrderedPair       initialPair;

  // Guardamos o somatório de A e B para evitar reprocessamento desta informação.
  protected long              somatoryA;
  protected long              somatoryB;

  /**
   * O método run foi criado apenas para que não fosse necessário ficar usando variáveis e métodos estáticos.
   * 
   * @param inputFile
   */
  public void run(String inputFile) {
    try {
      // Carrega a lista de pares ordenados na memória.
      List<OrderedPair> listNOfOrderedPairs = Utils.getListFromInputFile(inputFile);

      // Este é o método que realmente faz todo o processamento.
      run(listNOfOrderedPairs);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public abstract void run(List<OrderedPair> listNOfOrderedPairs);

  protected void resetSomatory() {
    somatoryA = initialPair.getA();
    somatoryB = initialPair.getB();
    isChanged = true;
  }

  /**
   * @param auxPair
   */
  protected void addSomatory(OrderedPair auxPair) {
    somatoryA += auxPair.getA();
    somatoryB += auxPair.getB();
    isChanged = true;
  }

  /**
   * @param auxPair
   */
  protected void subtractSomatory(OrderedPair auxPair) {
    somatoryA -= auxPair.getA();
    somatoryB -= auxPair.getB();
    isChanged = true;
  }

  /**
   * Calcula a razão.
   * 
   * @return O resultado da divisão do somatoryA por somatoryB.
   */
  protected float getRatio() {
    // Se a razão já foi calculada e nenhum dos valores a e b foi alterado, 
    // então para ganhar alguns milisegundos nós não recalculamos a razão mas retornamos 
    // a que já tinha sido calculada anteriormente.
    if (isChanged) {
      isChanged = false;
      ratio = (float) somatoryA / somatoryB;
    }

    return ratio;
  }

  /**
   * Método que garante que todos os algoritmos vão imprimir da mesma forma.
   * 
   * @param listNOfOrderedPairs
   * @param finalRatio
   * @param iterations
   * @param media
   * @param finishTime
   */
  protected void printResults(List<OrderedPair> listNOfOrderedPairs, float finalRatio, long iterations, float media, long finishTime) {
    //Log.printList(listS);
    Logger.printOntoScreen("Tamanho do N: " + formatString(listNOfOrderedPairs.size()));
    Logger.printOntoScreenF("Conjunto S* com %s elementos: \n", formatString(listS.size()));
    Logger.printOntoScreenF("Razão final: %f\n", finalRatio);
    Logger.printOntoScreen("Operações: " + getStrOperations());
    Logger.printOntoScreen("Iterações realizadas em 5 segundos: " + formatString(iterations));
    Logger.printOntoScreenF("Tempo de execução Médio: %f\n", media);
    Logger.printOntoScreenF("Tempo de execução Total: %d\n\n", finishTime);
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

      // Cria um metodo recebe par inicial.
      setInitialPair(listNOfOrderedPairs);

      // Quantidade de iterações feitas dentro de 5 segundos.
      long iterations = 0;

      while (System.currentTimeMillis() - startTime < 5000) {
        // Em cada iteração, é um novo processamento, então a quantidade de operações é setada para 0.
        setOperations(0);

        // Seta o somatório de A e B para 0.
        resetSomatory();

        // Zerando as variáveis iniciais.
        listS = new LinkedList<OrderedPair>();

        // Calcula a razão máxima, cada classe(complexidade) faz de uma maneira.
        // A cada iteração precisamos criar uma nova lista, porque se não depois de ordenar a primeira vez, 
        // todas as próximas vezes já vai pegar uma lista ordenada.
        specificProcess(new ArrayList<OrderedPair>(listNOfOrderedPairs));

        // Incrementa a quantidade de iterações feitas dentro de 5 segundos.
        iterations++;
      }
      // Como informa na questão o par ordenado (a0, b0) sempre estará em S*.
      listS.add(0, initialPair);

      // Momento em que o algoritmo terminou sua execução.
      long finishTime = System.currentTimeMillis() - startTime;

      // Calcula a média de tempo de cada iteração.
      float media = (float) finishTime / iterations;

      // Imprime os resultados obtidos.
      printResults(listNOfOrderedPairs, finalRatio, iterations, media, finishTime);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Recebe o par ordenado inicial(A0 e B0).
   * 
   * @param listNOfOrderedPairs
   */
  protected void setInitialPair(List<OrderedPair> listNOfOrderedPairs) {
    // Este é o par(a0, b0).
    initialPair = listNOfOrderedPairs.get(0);
    // Remove o par(a0, b0) da lista N de pares ordenados
    listNOfOrderedPairs.remove(0);
  }

  /**
   * @param listS
   * @param maximumRatio
   * @return Retorna true se existiu algum par ordenado na lista S que não era verdade em relação ao Lemma.
   */
  protected boolean isLemmaValid(List<OrderedPair> listS, float maximumRatio) {
    boolean valid = true;

    List<OrderedPair> listAux = new LinkedList<OrderedPair>();

    OrderedPair auxPair;
    for (Iterator<OrderedPair> iterator = listS.iterator(); iterator.hasNext();) {
      auxPair = iterator.next();
      incOperations();

      // Se o ratio for menor, então o par ordenado deve ser removido.
      if (auxPair.getRatio() < maximumRatio) {
        valid = false;

        // Tenho que remover o par ordenado na posição i.
        subtractSomatory(auxPair);

        // Adiciona o item para ser removido depois que o loop terminar. 
        // Remover de uma só vez é mais rápido.
        listAux.add(auxPair);
      }
    }

    // Remove os itens do conjunto S*.
    listS.removeAll(listAux);

    return valid;
  }

  protected abstract void specificProcess(List<OrderedPair> listNOfOrderedPairs);

}