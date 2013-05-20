package pph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import library.Logger;
import library.Utils;
import pph.utils.OrderedPair;

public class TestPPH {

  public static void main(String[] args) {
    // Todos os arquivos de entrada estão nestas pastas.
    String path = "test/pph/";
    // O nome do arquivo que vai ser processado.
    String fileName;
    // O caminho do arquivo e o seu nome.
    String fileNameAndPath = null;
    File folder = new File(path);
    // Obtém a lista de arquivos neste diretório, como o próprio método "listFiles()" diz,
    // não existe uma garantia em que ordem os arquivos serão retornados.
    File[] listOfFiles = folder.listFiles();

    // Instância das classes que vão ser executadas.
    PPH_O_de_N pph_O_de_N = new PPH_O_de_N();
    PPH_O_de_N_02 pph_O_de_N_02 = new PPH_O_de_N_02();
    PPH_O_N_LOG_N_QuickSort pph_O_N_LOG_N_QuickSort = new PPH_O_N_LOG_N_QuickSort();
    // PPH_O_N_LOG_N_MergeSort pph_O_N_LOG_N_MergeSort = new PPH_O_N_LOG_N_MergeSort();
    PPH_O_N_Pivot pph_O_N_Pivot = new PPH_O_N_Pivot();
    PPH_O_N2 pph_O_N2 = new PPH_O_N2();

    for (int i = 0; i < listOfFiles.length; i++) {

      if (listOfFiles[i].isFile()) {
        fileName = listOfFiles[i].getName();
        // Somente arquivos .txt e .dat serão processados.
        if (fileName.endsWith(".txt") || fileName.endsWith(".dat")) {
          try {
            fileNameAndPath = path + fileName;
            Logger.printOntoScreen(fileNameAndPath);

            // Lê cada arquivo apenas uma vez.
            List<OrderedPair> listOriginalPair = Utils.getListFromInputFile(fileNameAndPath);

            pph_O_de_N.run(new ArrayList<OrderedPair>(listOriginalPair));
            pph_O_de_N_02.run(new ArrayList<OrderedPair>(listOriginalPair));
            pph_O_N_LOG_N_QuickSort.run(new ArrayList<OrderedPair>(listOriginalPair));
            // pph_O_N_LOG_N_MergeSort.run(new ArrayList<OrderedPair>(listOriginalPair));
            pph_O_N_Pivot.run(new ArrayList<OrderedPair>(listOriginalPair));
            pph_O_N2.run(new ArrayList<OrderedPair>(listOriginalPair));
          }
          catch (Exception e) {
            Logger.printOntoScreenF("Erro ao processar o arquivo %s com a mensagem: %s", fileNameAndPath, e.getMessage());
          }
        }
      }

    }
    // Depois que todos os arquivos foram processados, exibe uma mensagem para informar que o programa terminou com sucesso!
    Logger.printOntoScreen("Todos os arquivos foram processados.");
  }
}