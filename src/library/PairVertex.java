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
}
