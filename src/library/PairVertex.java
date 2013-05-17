package library;

public class PairVertex<Node> {

  private Node   one;
  private Node   two;
  private double cost;

  public PairVertex(Node one, Node two, double cost) {

    this.setOne(one);
    this.setTwo(two);

    this.setCost(cost);
  }

  public PairVertex<Node> getPair() {

    return this;
  }

  public Node getOne() {
    return one;
  }

  public void setOne(Node one) {
    this.one = one;
  }

  public Node getTwo() {
    return two;
  }

  public void setTwo(Node two) {
    this.two = two;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }
}
