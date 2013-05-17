package library;

import java.util.List;

/**
 * Esta é uma classe facilitadora que evita o uso de System.out.println() por todo o código. <br/>
 * Alunos: Luciano Sampaio, Igor Oliveira e Marcio Rosemberg. <br/>
 */
public class Logger {
  /**
   * Informa se a aplicação esta é modo debug ou não. <br/>
   * Caso esteja todas as chamadas aos métodos de impressão debug serão impressas na tela.
   */
  public static boolean isDebugging;

  /**
   * Imprime uma mensagem na tela.
   * 
   * @param message A mensagem que será impressa na tela.
   */
  public static void printOntoScreen(Object message) {
    printOntoScreen(message, true);
  }

  /**
   * Imprime uma mensagem na tela.
   * 
   * @param message A mensagem que será impressa na tela.
   * @param breakLine Informa se depois de imprimir a mensagem uma quebra de linha deve ser usada.
   */
  public static void printOntoScreen(Object message, boolean breakLine) {
    System.out.print(message);

    if (breakLine) {
      System.out.println();
    }
  }

  /**
   * Só imprime a mensagem na tela.
   * 
   * @param message A mensagem que será impressa na tela.
   * @param args Argumentos que serão usados no printf.
   */
  public static void printOntoScreenF(String message, Object... args) {
    System.out.printf(message, args);
  }

  /**
   * Só imprime a mensagem na tela se a aplicação estiver em modo debug.
   * 
   * @param message A mensagem que será impressa na tela.
   */
  public static void debug(Object message) {
    debug(message, true);
  }

  /**
   * Só imprime a mensagem na tela se a aplicação estiver em modo debug.
   * 
   * @param message A mensagem que será impressa na tela.
   * @param breakLine Informa se depois de imprimir a mensagem uma quebra de linha deve ser usada.
   */
  public static void debug(Object message, boolean breakLine) {
    if (isDebugging) {
      printOntoScreen(message, breakLine);
    }
  }

  /**
   * Só imprime a mensagem na tela se a aplicação estiver em modo debug.
   * 
   * @param message A mensagem que será impressa na tela.
   * @param args Argumentos que serão usados no printf.
   */
  public static void debugF(String message, Object... args) {
    if (isDebugging) {
      printOntoScreenF(message, args);
    }
  }

  /**
   * Imprime qualquer tipo de lista.
   * 
   * @param listToPrint A lista que será impressa.
   */
  public static void printList(List<?> listToPrint) {
    for (Object currentElement : listToPrint) {
      printOntoScreen(currentElement);
    }
  }
}
