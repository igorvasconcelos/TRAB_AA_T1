package pph;

import java.util.List;

import library.Logger;
import library.Utils;
import pph.sorting.RandomizedSelectPivot;
import pph.utils.MedianaPair;
import pph.utils.OrderedPair;
import pph.utils.PPHBase;

public class PPH_Teste_Pior_Caso_N_Pivot extends PPHBase {

  //O nome do arquivo de input padrão(usado para testes).
  //private static final String DEFAULT_INPUT_FILE_NAME = "test/pph/pph_10000_01.dat";

  public static void main(String[] args) {
    //String inputFile;
    // Verifica se o arquivo de input foi passado como parâmetro.
    if (args.length == 1) {
      //inputFile = args[0];

    }
    else {
      // Caso nenhum arquivo tenha sido informado, testa com o arquivo criado para testes.
      //inputFile = DEFAULT_INPUT_FILE_NAME;

      // Informa que a applicação esta em modo debug.
      Logger.isDebugging = false;
    }

    PPH_Teste_Pior_Caso_N_Pivot pph = new PPH_Teste_Pior_Caso_N_Pivot();
    pph.run(Utils.getRandomValues(100000));
  }

  /**
   * Este é o método que realmente faz todo o processamento. O método run foi criado apenas para que não fosse necessário ficar usando variáveis e métodos
   * estáticos.
   * 
   * @param listNOfOrderedPairs
   */
  @Override
  public void run(List<OrderedPair> listNOfOrderedPairs) {
    try {
      listNOfOrderedPairs.set(0, new OrderedPair(10000000, 1));
      genericProcess(listNOfOrderedPairs, "Iniciado Randomized Select Pivot - Pior Caso O(n2)...");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void specificProcess(List<OrderedPair> listNOfOrderedPairs) {
    // Ordenando a lista.
    RandomizedSelectPivot<OrderedPair> sorter = new RandomizedSelectPivot<OrderedPair>();
    MedianaPair<OrderedPair> mediana = sorter.findMediana(listNOfOrderedPairs);

    // Soma a quantidade de operações feitas pela ordenação + a quantidade atual do programa principal.
    setOperations(getOperations() + sorter.getOperations());

    // Calcula a razão máxima.
    finalRatio = maximumRatio(listNOfOrderedPairs, mediana);
  }

  @Override
  protected void setInitialPair(List<OrderedPair> listNOfOrderedPairs) {
    // Este é o par(a0, b0).
    initialPair = listNOfOrderedPairs.get(0);
  }

  @Override
  protected void printResults(List<OrderedPair> listNOfOrderedPairs, float finalRatio, long iterations, float media, long finishTime) {
    //listNOfOrderedPairs.remove(0);
    super.printResults(listNOfOrderedPairs, finalRatio, iterations, media, finishTime);
  }

  /**
   * @param listNOfOrderedPairs
   * @param mediana
   * @return A razão máxima.
   */
  private float maximumRatio(List<OrderedPair> listNOfOrderedPairs, MedianaPair<OrderedPair> mediana) {
    // O R inicial é calculado pelo a0 / b0.
    float maximumRatio = initialPair.getRatio();

    // Um par ordenado usado para auxiliar.
    OrderedPair auxPair;

    for (int i = mediana.getIndex(); i < listNOfOrderedPairs.size(); i++) {
      incOperations();
      auxPair = listNOfOrderedPairs.get(i);

      if (auxPair.getRatio() > maximumRatio) {
        // Então coloca o par(ai e o bi) na lista S.
        listS.add(auxPair);

        // Atualiza o somatório.
        addSomatory(auxPair);

        // Obtém a nova razão.
        maximumRatio = getRatio();

        if (!isLemmaValid(listS, maximumRatio)) {
          // Se existir algum par(ai / bi) que não seja maior do que a
          // razão atual, este par deve ser removido do listS e uma
          // nova razão deve ser calculada.
          maximumRatio = getRatio();
        }
      }
    }
    return maximumRatio;
  }
}