package pph.utils;

public class MedianaPair<T extends Comparable<T>> {
  private T   orderedPair;
  private int index;

  public MedianaPair(T orderedPair, int index) {
    this.orderedPair = orderedPair;
    this.index = index;
  }

  public T getOrderedPair() {
    return orderedPair;
  }

  public void setOrderedPair(T orderedPair) {
    this.orderedPair = orderedPair;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }
}
