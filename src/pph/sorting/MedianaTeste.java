package pph.sorting;

import java.util.ArrayList;
import java.util.List;

public class MedianaTeste<T extends Comparable<T>> extends Sorter<T> {

  public int select(List<T> list, int left, int right, int k) {

    if (left == right || right - left == 1)
      return left;

    for (int i = left; i <= right; i++) {
      if (i + 4 < right) {
        InsertionSort(list, i, i + 5);
        i = i + 5;
      }
    }

    List<T> listAux = null;
    //if (list.size() > 2) {
    listAux = new ArrayList<T>(((right - left) + 1) / 5); //((right - left) + 1) / 5
    //int index = 1;
    for (int i = 2; i <= right; i++) {
      listAux.add(list.get(i));
      i = i + 5;
      //index++;
    }
    // }
    // else {
    //   return 1;
    // }

    int x = select(listAux, 0, listAux.size() - 1, listAux.size() / 2);
    int q = partition(list, left, right, x);

    if (k == q) {
      return x;
    }
    if (k < q)
      return select(list, left, q - 1, k);

    return select(list, q + 1, right, k);
  }

  public int partition(List<T> list, int left, int right, int pivotIndex) {
    T temp;
    // escolhendo o pivot
    T pivot = list.get(pivotIndex);
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

  public void InsertionSort(List<T> list, int left, int right) {
    for (int j = left + 1; j < right; j++) {
      T orderedPairkey = list.get(j);
      int i = j - 1;
      while (i >= 0 && list.get(i).compareTo(orderedPairkey) > 0) {
        list.set(i + 1, list.get(i));
        i = i - 1;
      }
      list.set(i + 1, orderedPairkey);
    }
  }

  @Override
  public void sortAscending(T[] arValues) {
    // TODO Auto-generated method stub

  }

  @Override
  public void sortAscending(List<T> list) {
    // TODO Auto-generated method stub

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
