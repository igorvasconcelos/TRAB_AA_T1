package pph.sorting;

import java.util.List;

/**
 * @author Luciano Sampaio
 * @param <T> Uma classe qualquer.
 * 
 */
public interface ISortable<T> {

  /**
   * @param arValues The array that will be ascending sorted.
   */
  void sortAscending(T[] arValues);

  /**
   * @param list The list that will be ascending sorted.
   */
  void sortAscending(List<T> list);

  /**
   * @param arValues The array that will be descending sorted.
   */
  void sortDescending(T[] arValues);

  /**
   * @param list The list that will be descending sorted.
   */
  void sortDescending(List<T> list);
}
