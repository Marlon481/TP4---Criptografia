package arquivos;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class RemoveStopWord{


public ArrayList<String> RemoverStopWord(String s) {
    
    ArrayList<String> arrayStopW = new ArrayList<>();
    s = s.toLowerCase();
    StringBuilder s1 = new StringBuilder(s);
    
    while(s.contains("ã") || s.contains("õ") || s.contains("á") || s.contains("é") || s.contains("í") || s.contains("ó") || s.contains("ú")  || s.contains("â") || s.contains("ô") || s.contains("ê")){
      if(s.contains("ã")){
        s1.setCharAt(s.indexOf('ã'), 'a');
        s = s1.toString();
      }
      if(s.contains("õ")){
        s1.setCharAt(s.indexOf('õ'), 'o');
        s = s1.toString();
      }
      if(s.contains("á")){
        s1.setCharAt(s.indexOf('á'), 'a');
        s = s1.toString();
      }
      if(s.contains("é")){
        s1.setCharAt(s.indexOf('é'), 'e');
        s = s1.toString();
      }
      if(s.contains("í")){
        s1.setCharAt(s.indexOf('í'), 'i');
        s = s1.toString();
      }
      if(s.contains("ó")){
        s1.setCharAt(s.indexOf('ó'), 'o');
        s = s1.toString();
      }
      if(s.contains("ú")){
        s1.setCharAt(s.indexOf('ú'), 'u');
        s = s1.toString();
      }
      if(s.contains("ô")){
        s1.setCharAt(s.indexOf('ô'), 'o');
        s = s1.toString();
      }
      if(s.contains("â")){
        s1.setCharAt(s.indexOf('â'), 'a');
        s = s1.toString();
      }
      if(s.contains("ê")){
        s1.setCharAt(s.indexOf('ê'), 'e');
        s = s1.toString();
      }
      if(s.contains("ç")){
        s1.setCharAt(s.indexOf('ç'), 'c');
        s = s1.toString();
      }
      
  }
    
    String[] arrayTitulo = s.split(" ");
    //System.out.println(s);
    

    for (String sAux : arrayTitulo) {
      arrayStopW.add(sAux);

    }
    try {
      ArrayList<String> arrSw = new ArrayList<>();
      File arq = new File("arquivos/stpowords.txt");
      Scanner sc = new Scanner(arq);
      while (sc.hasNextLine()) {
        arrSw.add(sc.nextLine());

      }
      sc.close();
      for (String sAux : arrSw) {
        if (arrayStopW.contains(sAux))
          arrayStopW.remove(sAux);
      }
     // System.out.println(arrayStopW);
    } catch (Exception e) {
      System.out.println("Erro no tratamento de stopWords...");
    }
    return arrayStopW;
  }
}
