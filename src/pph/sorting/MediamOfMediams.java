package pph.sorting;

import java.util.List;

import pph.utils.MedianaPair;

public class MediamOfMediams<T extends Comparable<T>> extends Sorter<T> {

  @Override
  public void sortAscending(T[] arValues) {
  }

  @Override
  public void sortAscending(List<T> list) {
    int idx = medianOfMedians(list, 0, list.size() - 1);
    select(list, 0, list.size() - 1, idx);
  }

  public MedianaPair<T> select(List<T> list, int left, int right, int k) {
    if (left == right) // If the list contains only one element
      return new MedianaPair<T>(list.get(left), left);//list.get(left); // Return that element
    // select pivotIndex between left and right

    int pivotIndex = medianOfMedians(list, left, right);

    int pivotNewIndex = partition(list, left, right, pivotIndex); //k
    //int pivotDist = pivotNewIndex - left + 1;
    // The pivot is in its final sorted position,
    // so pivotDist reflects its 1-based position if list were sorted
    if (pivotNewIndex == k)
      return new MedianaPair<T>(list.get(pivotNewIndex), pivotNewIndex);
    else if (k < pivotNewIndex)
      return select(list, left, pivotNewIndex - 1, k);
    else
      return select(list, pivotNewIndex + 1, right, pivotIndex);
  }

  public MedianaPair<T> findMediana(List<T> list, int left, int right) {
    int medianIdx = medianOfMedians(list, 0, list.size() - 1);
    return selectIterativo(list, 0, list.size() - 1, medianIdx);
  }

  public MedianaPair<T> selectIterativo(List<T> list, int left, int right, int k) {
    // select pivotIndex between left and right
    while (left != right) {
      int pivotNewIndex = partition(list, left, right, k);
      int pivotDist = pivotNewIndex - left + 1;
      // The pivot is in its final sorted position,
      // so pivotDist reflects its 1-based position if list were sorted
      if (pivotDist == k)
        return new MedianaPair<T>(list.get(pivotNewIndex), pivotNewIndex);
      else if (k < pivotDist)
        right = pivotNewIndex - 1;
      else {
        k = k - pivotDist;
        left = pivotNewIndex + 1;
      }
    }
    return new MedianaPair<T>(list.get(left), left);
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

  // returns the index of the median of medians.
  // requires a variant of select, "selectIdx" which returns the index of the
  // selected item rather than the value
  private int medianOfMedians(List<T> list, int left, int right) {
    int numMedians = (right - left) / 5;
    T temp;
    int subLeft = left;
    int subRight = 0;//-1;
    int i;
    for (i = 0; i <= numMedians; i++) {
      // get the median of the five-element subgroup
      //subLeft = subRight + 1;
      //subRight = subLeft + 4;
      subLeft = left + i * 5;
      subRight = subLeft + 4;
      if (subRight > right)
        subRight = right;

      // Ordenando a porção trabalhada
      InsertionSort(list, subLeft, subRight);
      int medianIdx = selectIdx(list, subLeft, subRight, 2);
      // alternatively, use a faster method that works on lists of size 5
      // move the median to a contiguous block at the beginning of the list
      //swap i and medianIdx
      temp = list.get(left + i); // temp = list.get(0);
      list.set(left + i, list.get(medianIdx)); // list.set(0, list.get(medianIdx));
      list.set(medianIdx, temp);
    }
    return selectIdx(list, left, left + numMedians, numMedians / 2);
  }

  private int selectIdx(List<T> list, int left, int right, int pivot) {
    while (left != right) {
      if (right - left == 1)
        return left;

      int pivotNewIndex = partition(list, left, right, pivot);
      int pivotDist = pivotNewIndex - left + 1;
      // The pivot is in its final sorted position,
      // so pivotDist reflects its 1-based position if list were sorted
      if (pivotNewIndex == pivot) {//(pivotDist == pivot) {
        return pivotNewIndex;
      }
      else if (pivot < pivotDist)
        right = pivotNewIndex - 1;
      else {
        pivot = pivot - pivotDist;
        left = pivotNewIndex + 1;
      }
    }
    return left;
  }

  public int selectIdx2(List<T> list, int left, int right, int k) {
    if (left == right) // If the list contains only one element
      return left;
    // select pivotIndex between left and right

    int pivotIndex = medianOfMedians(list, left, right);

    int pivotNewIndex = partition(list, left, right, pivotIndex); //k
    int pivotDist = pivotNewIndex - left + 1;
    // The pivot is in its final sorted position,
    // so pivotDist reflects its 1-based position if list were sorted
    if (pivotDist == k)
      return pivotNewIndex;
    else if (k < pivotDist)
      return selectIdx2(list, left, pivotNewIndex - 1, k);
    else
      return selectIdx2(list, pivotNewIndex + 1, right, k - pivotDist);
  }

  public void InsertionSort(List<T> list, int left, int right) {
    for (int j = left + 1; j <= right; j++) {
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
  public void sortDescending(T[] arValues) {
  }

  @Override
  public void sortDescending(List<T> list) {
  }
}
