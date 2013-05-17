package library;

public class Node<T> {

  private T      vetex;
  private double priority;

  public Node(T node, double priority) {
    this.setNode(node);
    this.setPriority(priority);
  }

  public T getVertex() {
    return vetex;
  }

  public void setNode(T node) {
    this.vetex = node;
  }

  public double getPriority() {
    return priority;
  }

  public void setPriority(double priority) {
    this.priority = priority;
  }
}
