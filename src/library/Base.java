package library;

import java.text.DecimalFormat;

public abstract class Base {
  /**
   * A quantidade de operações que foi necessária para que o resultado esperado fosse encontrado.
   */
  private static long _operations;

  /**
   * @return A quantidade de operações que foi necessária para que o resultado esperado fosse encontrado.
   */
  public static long getOperations() {
    return _operations;
  }

  /**
   * @return A quantidade de operações que foi necessária para que o resultado esperado fosse encontrado.
   */
  public static String getStrOperations() {
    return formatString(getOperations());
  }

  /**
   * Atribui uma valor à quantidade de operações que foi necessária para que o resultado esperado fosse encontrado.
   * 
   * @param operations
   */
  public void setOperations(long operations) {
    _operations = operations;
  }

  /**
   * Incrementa em 1 a quantidade de operações que foi necessária para que o resultado esperado fosse encontrado.
   */
  public void incOperations() {
    setOperations(getOperations() + 1);
  }

  /**
   * @param obj
   * @return Formata um objeto no formato ###,###.
   */
  public static String formatString(Object obj) {
    DecimalFormat myFormatter = new DecimalFormat("###,###");
    return myFormatter.format(obj);
  }

}