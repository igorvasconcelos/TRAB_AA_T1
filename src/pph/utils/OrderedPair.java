package pph.utils;

/**
 * Esta classe contém os elementos Ai, Bi e o calculo da sua razão R = (Ai/Bi).<br/>
 * Alunos: Luciano Sampaio, Igor Oliveira e Marcio Rosemberg. <br/>
 */
public class OrderedPair implements Comparable<OrderedPair> {

  private int     a;
  private int     b;
  private float   ratio;
  private boolean isChanged;

  public OrderedPair(int a) {
    this(a, 0);
  }

  public OrderedPair(int a, int b) {
    this.a = a;
    this.b = b;
    isChanged = true;
  }

  public int getA() {
    return a;
  }

  public int getB() {
    return b;
  }

  public void setB(int b) {
    this.b = b;
    isChanged = true;
  }

  public float getRatio() {
    // Se a razão já foi calculada e nenhum dos valores a e b foi alterado, 
    // então para ganhar alguns milisegundos nós não recalculamos a razão mas retornamos 
    // a que já tinha sido calculada anteriormente.
    if (isChanged) {
      isChanged = false;
      ratio = (float) getA() / getB();
    }
    return ratio;
  }

  @Override
  public String toString() {
    return String.format("[(%3d,%3d) - %f]", getA(), getB(), getRatio());
  }

  @Override
  public int compareTo(OrderedPair obj) {
    float divisaoThis = this.getRatio();
    float divisaoObj = obj.getRatio();
    if (divisaoThis < divisaoObj) {
      return -1;
    }
    else if (divisaoThis == divisaoObj) {
      return 0;
    }
    else
      return 1;
  }
}
