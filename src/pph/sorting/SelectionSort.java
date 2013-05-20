package pph.sorting;

import java.util.Arrays;
import java.util.List;

/**
 * This class sorts an array using the Selection Sort algorithm.
 * 
 * @author Luciano Sampaio
 * @param <T>
 * 
 */
public class SelectionSort<T extends Comparable<T>> extends Sorter<T> {

  @Override
  public void sortAscending(List<T> list) {
    // Desta forma ficou genérico.
    @SuppressWarnings("unchecked")
    T[] arValues = (T[]) java.lang.reflect.Array.newInstance(list.get(0).getClass(), list.size());
    for (int i = 0; i < list.size(); i++) {
      arValues[i] = list.get(i);
    }

    sortAscending(arValues);

    list.clear();
    list.addAll(Arrays.asList(arValues));
  }

  /**
   * @param arValues The array that will be sorted.
   */
  @Override
  public void sortAscending(T[] arValues) {
    int positionSmallestValue;

    for (int i = 0; i < arValues.length; i++) {
      // It gets the value of the array in the i position.
      positionSmallestValue = i;

      for (int j = (i + 1); j < arValues.length; j++) {
        this.incOperations();
        if ((arValues[positionSmallestValue].compareTo(arValues[j])) > 0) {
          // It gets the new smallest value.
          positionSmallestValue = j;
        }
      }
      // Put the smallest value at the position i.
      swap(arValues, i, positionSmallestValue);
    }
  }

  @Override
  public void sortDescending(T[] arValues) {
  }

  @Override
  public void sortDescending(List<T> list) {
  }

}