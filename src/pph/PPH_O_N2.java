package pph;

import java.util.List;

import library.Logger;
import pph.sorting.SelectionSort;
import pph.utils.OrderedPair;
import pph.utils.PPHBase;

public class PPH_O_N2 extends PPHBase {

  // O nome do arquivo de input padrão(usado para testes).
  private static final String DEFAULT_INPUT_FILE_NAME = "test/pph/pph_100000_01.dat";

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

    PPH_O_N2 pph = new PPH_O_N2();
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
      genericProcess(listNOfOrderedPairs, "Iniciado SelectionSort - O(n2)...");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void specificProcess(List<OrderedPair> listNOfOrderedPairs) {
    // Ordenando a lista.
    SelectionSort<OrderedPair> sorter = new SelectionSort<OrderedPair>();
    sorter.sortAscending(listNOfOrderedPairs);

    // Soma a quantidade de operações feitas pela ordenação + a quantidade atual do programa principal.
    setOperations(getOperations() + sorter.getOperations());

    // Calcula a razão máxima.
    finalRatio = maximumRatio(listNOfOrderedPairs);
  }

  /**
   * @param listNOfOrderedPairs
   * @return A razão máxima.
   */
  private float maximumRatio(List<OrderedPair> listNOfOrderedPairs) {
    // O R inicial é calculado pelo a0 / b0.
    float maximumRatio = initialPair.getRatio();

    OrderedPair auxPair;
    for (int i = listNOfOrderedPairs.size() - 1; i >= 0; i--) {
      incOperations();
      auxPair = listNOfOrderedPairs.get(i);

      if (auxPair.getRatio() > maximumRatio) {
        // Então coloca o par(ai e o bi) na lista S.
        listS.add(auxPair);

        // Atualiza o R(razão).
        addSomatory(auxPair);

        // Obtém a nova razão.
        maximumRatio = getRatio();
      }
      else {
        break;
      }
    }
    return maximumRatio;
  }
}