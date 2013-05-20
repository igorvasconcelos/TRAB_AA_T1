/**
 * Esta o algoritmo que resolve a 1ª questão do T1 de PAA - PUC-Rio 2012.2. <br/>
 * Alunos: Luciano Sampaio, Igor Oliveira e Marcio Rosemberg. <br/>
 */
package pph;

import java.util.List;

import library.Logger;
import pph.sorting.MedianaTeste;
import pph.utils.MedianaPair;
import pph.utils.OrderedPair;
import pph.utils.PPHBase;

public class CopyOfPPH_O_de_N_Teste extends PPHBase {

  // O nome do arquivo de input padrão(usado para testes).
  private static final String DEFAULT_INPUT_FILE_NAME = "test/pph/pph_10_01.dat";

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

    CopyOfPPH_O_de_N_Teste pph = new CopyOfPPH_O_de_N_Teste();
    pph.run(inputFile);
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
      genericProcess(listNOfOrderedPairs, "Iniciado Randomized Select - O(n)...");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void specificProcess(List<OrderedPair> listNOfOrderedPairs) {
    // Ordenando a lista.
    MedianaTeste<OrderedPair> sorter = new MedianaTeste<OrderedPair>();
    int idx = sorter.select(listNOfOrderedPairs, 0, listNOfOrderedPairs.size() - 1, listNOfOrderedPairs.size() / 2);

    //MedianaPair<OrderedPair> mediana = sorter.findMediana(listNOfOrderedPairs);

    // Soma a quantidade de operações feitas pela ordenação + a quantidade atual do programa principal.
    setOperations(getOperations() + sorter.getOperations());

    // Calcula a razão máxima.
    //finalRatio = maximumRatio(listNOfOrderedPairs, mediana);

    finalRatio = maximumRatio(listNOfOrderedPairs, idx);
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

  private float maximumRatio(List<OrderedPair> listNOfOrderedPairs, int idx) {
    // O R inicial é calculado pelo a0 / b0.
    float maximumRatio = initialPair.getRatio();

    // Um par ordenado usado para auxiliar.
    OrderedPair auxPair;

    for (int i = idx; i < listNOfOrderedPairs.size(); i++) {
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