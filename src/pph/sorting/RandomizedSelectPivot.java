package pph.sorting;

import java.util.List;

import pph.utils.MedianaPair;
import pph.utils.OrderedPair;

public class RandomizedSelectPivot<T extends Comparable<T>> extends Sorter<T> {

  // Para evitar colocar numeros literais no código.
  private int         somaA = 0;
  private int         somaB = 0;
  // Par Inicial
  private OrderedPair parInicial;

  /**
   * Método que retorna a Mediana, ou seja, retorna o par ordenando e sua posição na lista.
   * 
   * @param list
   * @return Método que retorna a Mediana, ou seja, retorna o par ordenando e sua posição na lista.
   */
  public MedianaPair<T> findMediana(List<T> list) {
    // Armazena o par inicial e o remove da lista
    this.parInicial = (OrderedPair) list.get(0);
    //list.remove(0);
    return sort(list, 1, list.size() - 1, list.size() / 2);
  }

  /**
   * Método que recursivamente re-organiza a lista de forma que do pivot à esquerda todos os elementos são menores e à direita todos são maiores. Não há
   * ordenação no lado esquerdo e direito.
   * 
   * @param list
   * @param parInicial
   * @param left
   * @param right
   * @param pivot
   * @return
   */
  @SuppressWarnings({ "javadoc", "hiding" })
  public <T extends Comparable<T>> MedianaPair<T> sort(List<T> list, int left, int right, int pivot) {
    // Incrementado as iterações para facilitar a mensuração da complexidade final
    while (left != right) {
      this.incOperations();

      // Seleciona um pivot com base no somatório das razões entre o índice inferior e superio da lista
      int pivotNewIndex = PivotPartition(list, left, right);
      int pivotDist = pivotNewIndex - left + 1;
      if (pivot == pivotDist)
        return new MedianaPair<T>(list.get(pivotNewIndex), pivotNewIndex);
      else if (pivot < pivotDist)// Procurando apenas no lado esquerdo da lista
        //return sort(list, left, pivotNewIndex - 1, pivot);
        right = pivotNewIndex - 1;
      else {// Procurando apenas no lado direito da lista
        //return sort(list, pivotNewIndex + 1, right, pivot - pivotDist);
        pivot = pivot - pivotDist;
        left = pivotNewIndex + 1;
      }
    }
    return new MedianaPair<T>(list.get(left), left);
  }

  /**
   * Método retorna uma posição aleatória entre o limite inferior e superior.
   * 
   * @param list
   * @param left
   * @param right
   * @return Método retorna uma posição aleatória entre o limite inferior e superior
   */
  @SuppressWarnings("hiding")
  private <T extends Comparable<T>> int PivotPartition(List<T> list, int left, int right) {
    // Escolhendo aleatoreamente um pivot entre "left" e "right"
    int index = calcularPivot(list, left, right);
    //swap
    T aux = list.get(index);
    list.set(index, list.get(right));
    list.set(right, aux);

    //Particionando  a lista
    return partition(list, left, right);
  }

  /**
   * Método que efetivamente separa a lista em 2. O menores que o pivot ficam à esquerda e todos maiores ficam à direita
   * 
   * @param list
   * @param left
   * @param right
   * @return
   */
  @SuppressWarnings({ "hiding", "javadoc" })
  private <T extends Comparable<T>> int partition(List<T> list, int left, int right) {
    T temp;
    // escolhendo o pivot
    T pivot = list.get(right);
    int i = left - 1;
    for (int j = left; j <= right - 1; j++) {
      // Incrementado as iterações para facilitar a mensuração da complexidade final
      this.incOperations();
      if (list.get(j).compareTo(pivot) <= 0) {// Se menor precisa trocar de posição
        i = i + 1;
        // Swap entre os elementos da posição i e j
        temp = list.get(j);
        list.set(j, list.get(i));
        list.set(i, temp);
      }
    }
    //Swap entre os elementos da posição i+1  e right (Final da lista) 
    temp = list.get(right);
    list.set(right, list.get(i + 1));
    list.set(i + 1, temp);
    // Retorna a posição do pivot. À esquerda os menores e à direita os maiores mas sem uma ordenação em cada lado
    return i + 1;
  }

  @SuppressWarnings("hiding")
  private <T extends Comparable<T>> int calcularPivot(List<T> list, int left, int right) {

    // Calculando a razão máxima para o intervalo
    float maximumRatio = parInicial.getRatio();
    for (int i = left; i < right; i++) {
      OrderedPair auxlPar = (OrderedPair) list.get(i);
      if (auxlPar.getRatio() > maximumRatio) {
        SomaRazao(auxlPar);
        maximumRatio = calcularRazao();
      }
    }
    // Há casos que a razão é maior que o intervalo trabalhado. Então retorna a posição do (a0, b0)
    if (maximumRatio > right)
      return list.indexOf(parInicial);

    return (int) maximumRatio;
  }

  private void SomaRazao(OrderedPair auxlPar) {
    this.somaA += auxlPar.getA();
    this.somaB += auxlPar.getB();
  }

  private float calcularRazao() {
    return (float) (this.parInicial.getA()) / (this.parInicial.getB());
  }

  @Override
  public void sortAscending(T[] arValues) {
    // TODO Auto-generated method stub
  }

  @Override
  public void sortAscending(List<T> list) {
    // Armazena o par inicial e o remove da lista
    this.parInicial = (OrderedPair) list.get(0);
    sort(list, 0, list.size() - 1, list.size() / 2);
  }

  @Override
  public void sortDescending(T[] arValues) {
    // TODO Auto-generated method stub

  }

  @Override
  public void sortDescending(List<T> list) {
    // TODO Auto-generated method stub

  }
}
