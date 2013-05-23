package library;

public class PairVertex<T> {

  private T      one;
  private T      two;
  private double cost;

  public PairVertex(T one, T two, double cost) {

    this.setOne(one);
    this.setTwo(two);

    this.setCost(cost);
  }

  public PairVertex<T> getPair() {

    return this;
  }

  public T getOne() {
    return one;
  }

  public void setOne(T one) {
    this.one = one;
  }

  public T getTwo() {
    return two;
  }

  public void setTwo(T two) {
    this.two = two;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object object) {
    if (this.one.equals(object) || this.two.equals(object))
      return true;
    /*
     * if (object instanceof PairVertex) { PairVertex<?> pv = (PairVertex<?>) object; if (pv.one.equals(this.one) || pv.two.equals(this.two)) { return true; } }
     */
    return false;
  }

}
