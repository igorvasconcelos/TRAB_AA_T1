package library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Esta classe contém alguns métodos utils e comuns para ajudar no desenvolvimento de questão pph.<br/>
 * Alunos: Luciano Sampaio, Igor Oliveira e Marcio Rosemberg. <br/>
 */
public class Utils {

  /**
   * Obtém os vértices, arestas e os respectivos pesos
   * 
   * @param inputFile O arquivo que será usando como fonte de dados.
   * @return Obtém os valores que correspondem ao A ou ao B.
   * @throws Exception
   */
  public static List<PairVertex<Integer>> getListFromInputFile(String inputFile) throws Exception {

    Logger.printOntoScreen("Obtendo valores do arquivo de entrada...");

    // Tentar abrir o arquivo.
    ReadBigFile rbf = new ReadBigFile(inputFile);

    // Obtém o objeto que vai iterar por todas as linhas do arquivo.
    Iterator<Integer> iterator = rbf.iterator();

    // Obtém a quantidade de números contidos neste arquivo.
    // O valor encontrado é o mesmo para os elementos de A e de B, ou seja, se o valor for 10
    // temos 10 elementos para o conjunto A e 10 para o conjunto B.
    // O + 1 é porque temos que ler mais dois(1 em para cada conjunto) números que são um para A0 e um para o B0.
    int quantityOfInputValues = (iterator.hasNext()) ? iterator.next() + 1 : 0;

    // Cria uma lista temporária que vai conter os elementos lidos do arquivo.
    List<PairVertex<Integer>> listTemp = new ArrayList<PairVertex<Integer>>(quantityOfInputValues);

    // Este loop, adiciona todos os elementos de A.
    int count = 0;
    while ((iterator.hasNext()) && (count < quantityOfInputValues)) {
      int currentValue = iterator.next();
      count++;

      //listTemp.add(new PairVetex<Integer>(one, two, cost));
    }

    // Libera o arquivo.
    rbf.Close();

    return listTemp;
  }
}