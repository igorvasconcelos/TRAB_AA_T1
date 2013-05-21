package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GenericReadBigFile implements Iterable<String> {
  private BufferedReader _reader;
  private boolean        readAllLine;

  public GenericReadBigFile(String fileNameAndPath) throws Exception {
    _reader = new BufferedReader(new FileReader(fileNameAndPath));
  }

  public void Close() {
    try {
      _reader.close();
    }
    catch (Exception ex) {
    }
  }

  public boolean isReadAllLine() {
    return readAllLine;
  }

  public void setReadAllLine(boolean readAllLine) {
    this.readAllLine = readAllLine;
  }

  @Override
  public Iterator<String> iterator() {
    return new ReadBigFileIterator();
  }

  private class ReadBigFileIterator implements Iterator<String> {
    private String       currentLine;
    private List<String> listValues;

    public ReadBigFileIterator() {
      listValues = new LinkedList<String>();
      setReadAllLine(true);
    }

    @Override
    public boolean hasNext() {
      try {
        if (listValues.size() > 0) {
          // Ainda tem algum elemento para ser retornado.
          return true;
        }
        else {
          // Lê uma linha inteira.
          currentLine = _reader.readLine();
          if (currentLine != null) {
            //if (readAllLine) {
            listValues.add(currentLine);
            // }
            /*
             * else { String[] parse = currentLine.trim().split(" ");
             * 
             * for (int i = 0; i < parse.length; i++) { listValues.add(parse[i]); } }
             */
          }
        }
      }
      catch (Exception ex) {
        currentLine = null;
        ex.printStackTrace();
      }

      // Se a lista tiver tamanho 0 é porque não tem mais nada para ser retornado.
      return (listValues.size() > 0);
    }

    @Override
    public String next() {
      if (listValues.size() > 0) {
        return listValues.remove(0);
      }
      return "";
    }

    @Override
    public void remove() {
    }

  }
}