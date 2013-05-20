package pph.sorting;

import java.util.List;

import pph.utils.MedianaPair;

public class RandomizedSelect<T extends Comparable<T>> extends Sorter<T> {

  /**
   * Método que retorna a Mediana, ou seja, retorna o par ordenando e sua posição na lista
   * 
   * @param list
   * @return Método que retorna a Mediana.
   */
  public MedianaPair<T> findMediana(List<T> list) {
    return sort(list, 0, list.size() - 1, list.size() / 2);
  }

  /**
   * Método que recursivamente re-organiza a lista de forma que do pivot à esquerda todos os elementos são menores e à direita todos são maiores. Não há
   * ordenação no lado esquerdo e direito.
   * 
   * @param list
   * @param left
   * @param right
   * @param pivot
   * @return Ordena.
   */
  public MedianaPair<T> sort(List<T> list, int left, int right, int pivot) {
    // Incrementado as iterações para facilitar a mensuração da complexidade final
    this.incOperations();
    if (left == right)
      return new MedianaPair<T>(list.get(left), left);

    // Seleciona um pivot aleatório entre o índice inferior e superio da lista
    int pivotNewIndex = RandomizedPartition(list, left, right);
    int pivotDist = pivotNewIndex - left + 1;
    if (pivot == pivotDist)
      return new MedianaPair<T>(list.get(pivotNewIndex), pivotNewIndex);
    else if (pivot < pivotDist)// Procurando apenas no lado esquerdo da lista
      return sort(list, left, pivotNewIndex - 1, pivot);
    else {// Procurando apenas no lado direito da lista
      return sort(list, pivotNewIndex + 1, right, pivot - pivotDist);
    }
  }

  /**
   * Método retorna uma posição aleatória entre o limite inferior e superior.
   * 
   * @param list
   * @param left
   * @param right
   * @return Método retorna uma posição aleatória entre o limite inferior e superior.
   */
  private int RandomizedPartition(List<T> list, int left, int right) {
    // Escolhendo aleatoreamente um pivot entre "left" e "right"
    int index = new java.util.Random().nextInt(right + 1 - left) + left;
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
   * @return Método que efetivamente separa a lista em 2.
   */
  private int partition(List<T> list, int left, int right) {
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

  @Override
  public void sortAscending(T[] arValues) {
  }

  @Override
  public void sortAscending(List<T> list) {
    sort(list, 0, list.size() - 1, list.size() / 2);
  }

  @Override
  public void sortDescending(T[] arValues) {
  }

  @Override
  public void sortDescending(List<T> list) {
  }
}
