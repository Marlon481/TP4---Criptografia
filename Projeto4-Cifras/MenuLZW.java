import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import aeds3.LZW;

public class MenuLZW {

  private static Scanner console = new Scanner(System.in);

  public MenuLZW() throws IOException {
    menu();
    return;
  }

  // ---------------------
  // MENU_LZW
  // ---------------------
  public void menu() throws IOException {

    // Mostra o menu
    int opcao;
    do {
      System.out.println("\n> Início > LZW");
      System.out.println("\n1) Compactar arquivos");
      System.out.println("2) Descompactar arquivos");
      System.out.println("\n0) Retornar ao menu anterior");

      System.out.print("\nOpção: ");
      try {
        opcao = Integer.valueOf(console.nextLine());
      } catch (NumberFormatException e) {
        opcao = -1;
      }

      // Seleciona a operação
      switch (opcao) {
        case 1:
          try {
            compactarArquivos();
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        case 2:
          try {
            descompactarArquivos();
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          break;
        case 0:
          break;
        default:
          System.out.println("Opção inválida");
          break;
      }
    } while (opcao != 0);
    System.out.println("opca0 0");
    return;
  }

  // ---------------------------------------------------------------------------------------
  public void compactarArquivos() throws IOException {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    String versaoBackup = dtf.format(LocalDateTime.now()).toString();

    // Cria diretorio caso ainda não exista nenhum
    File dirDest1 = new File("backup");
    File dirDest2 = new File("backup2");
    if (!dirDest1.exists())
      dirDest1.mkdir();
    if (!dirDest2.exists())
      dirDest2.mkdir();
    dirDest1 = new File("backup/" + versaoBackup);
    dirDest1.mkdir();
    dirDest2 = new File("backup2/" + versaoBackup);
    dirDest2.mkdir();
    File dirOrig = new File("dados");
    File[] listaFiles = dirOrig.listFiles();
    try {
      metodo1_compactar(listaFiles, dirDest1);
      metodo2_compactar(listaFiles, versaoBackup);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  // -----------------------------------------------------------------------------

  public void descompactarArquivos() throws IOException {
    File dirOrig = new File("backup");
    if (!dirOrig.exists()) {
      System.out.println("Não existem backup dos arquivos.");
      return;
    }
    File[] listaFiles = dirOrig.listFiles();
    int j = 0;
    File[] listaInv = new File[listaFiles.length];
    for (int i = listaFiles.length - 1; i >= 0; i--, j++) {
      listaInv[j] = listaFiles[i];
      System.out.println((j + 1) + ") " + listaInv[j].getName());
    }
    System.out.println("Selecione a opção a ser recuperada: ");

    int op = Integer.valueOf(console.nextLine()) - 1;
    if (op < 0 || op >= listaFiles.length) {
      System.out.println("Valor inválido");
      return;
    }

    System.out.println("opcao selecionada: " + listaInv[op].getName());
    File[] listaArq = listaInv[op].listFiles();

    try {
      metodo1_descompactar(listaArq, listaInv[op].getName());
      metodo2_descompactar(listaArq, listaInv[op].getName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // --------------------------------------------------------------------------------
  public static void metodo1_compactar(File[] f, File destino) throws Exception {
    // Cria diretorio caso ainda não exista nenhum

    File[] listaArq = f;
    for (int i = 0; i < listaArq.length; i++) {
      byte[] vByte = new byte[(int) listaArq[i].length()];
      RandomAccessFile raf = new RandomAccessFile("dados/" + f[i].getName(), "rwd");
      int j = 0;
      while (raf.getFilePointer() < raf.length()) {
        vByte[j] = raf.readByte();
        j++;
      }

      byte[] cod = LZW.codifica(vByte);
      RandomAccessFile arq = new RandomAccessFile(destino + "/" + f[i].getName(), "rwd");
      arq.write(cod);
    }
    System.out.println("\nTaxa de compressão entre o arquivo original e o backup gerado(LZW aplicado com a leitura integral do arquivo):\n");
    File[] fdest = new File("" + destino).listFiles();  
    File[] forig = new File("dados/").listFiles();
      for (int i = 0; i < forig.length; i++) {
        float r1 = forig[i].length();
        float r2 = fdest[i].length();
        float result = r2 / r1;
        System.out.println(fdest[i].getName() + " -> " + result);
      }

  }

  // ------------------------------------------------------------------------------------------
  // METODO 2 -> NAO SE SABE O TAMANHO DO ARQUIVO A SER COMPACTADO
  public static void metodo2_compactar(File[] f, String vers) throws Exception {
    // Cria diretorio caso ainda não exista nenhum
    File dir = new File("backup2/");
    if (!dir.exists())
      dir.mkdir();
    dir.mkdir();
    File[] listaArq = f;
    for (int aux = 0; aux < listaArq.length; aux++) {

      RandomAccessFile raf = new RandomAccessFile("dados/" + listaArq[aux].getName(), "rwd");
      RandomAccessFile rafSaida = new RandomAccessFile(dir + "/" + vers + "/" + listaArq[aux].getName(), "rwd");
      while (raf.getFilePointer() < raf.length()) {
        ArrayList<Byte> arr = new ArrayList<>();
        int i = 0;
        while (i < 30 && raf.getFilePointer() < raf.length()) {
          byte b = raf.readByte();
          arr.add(b);
          i++;
        }
        if (arr.size() > 0) {
          byte[] vetorB = new byte[arr.size()];
          for (int x = 0; x < vetorB.length; x++) {
            vetorB[x] = arr.get(x);
          }
          byte[] cod = new byte[vetorB.length];
          cod = LZW.codifica(vetorB);
          rafSaida.write(cod);
        }
      }
    }
    System.out.println("\nTaxa de compressão entre o arquivo original e o backup gerado(LZW aplicado com a leitura integral do arquivo):\n");
    File[] fdest = new File("backup2/" + vers).listFiles();
    File[] forig = new File("dados/").listFiles();
      for (int i = 0; i < forig.length; i++) {
        float r1 = forig[i].length();
        float r2 = fdest[i].length();
        float result = r2 / r1;
        System.out.println(fdest[i].getName() + " -> " + result);
      }
  }

  // ---------------------------------------------------------------------------------------------

  public static void metodo1_descompactar(File[] f, String origem) throws Exception {
    File tmp = new File("dados");
    if (!tmp.exists())
      tmp.mkdir();
    File[] listaArq = f;
    for (int i = 0; i < listaArq.length; i++) {
      byte[] vByte = new byte[(int) listaArq[i].length()];
      System.out.println(listaArq[i].getName() + "-> " + vByte.length);
      RandomAccessFile raf = new RandomAccessFile("backup/" + origem + "/" + listaArq[i].getName(), "rwd");
      raf.seek(0);
      int j = 0;
      while (raf.getFilePointer() < raf.length()) {
        vByte[j] = raf.readByte();
        j++;
      }
      byte[] dec = LZW.decodifica(vByte);
      raf = new RandomAccessFile("dados/" + listaArq[i].getName(), "rwd");
      raf.write(dec);
    }
  }

  // ----------------------------------------------------------------------------------------

  public static void metodo2_descompactar(File[] f, String origem) throws Exception {
    File tmp = new File("dados2");
    if (!tmp.exists())
      tmp.mkdir();

    File[] listaArq = f;
    for (int i = 0; i < listaArq.length; i++) {
      RandomAccessFile raf = new RandomAccessFile("backup2/" + origem + "/" + listaArq[i].getName(), "rwd");
      System.out.println(listaArq[i].getName() + ": " + raf.length());
      RandomAccessFile rafDestino = new RandomAccessFile("dados2/" + listaArq[i].getName(), "rwd"); // destino
      ArrayList<Byte> arr = new ArrayList<>();
      while (raf.getFilePointer() < raf.length()) {
        byte b = raf.readByte();
        arr.add(b);
        RandomAccessFile rTmp = new RandomAccessFile("backup2/" + origem + "/" + listaArq[i].getName(), "rwd");
        rTmp.seek(raf.getFilePointer());
        if ((rTmp.getFilePointer() + 3) < rTmp.length() && rTmp.readByte() == 0 && rTmp.readByte() == 0
            && rTmp.readByte() == 0) {
          byte[] vetorB = new byte[arr.size()];
          for (int x = 0; x < vetorB.length; x++) {
            vetorB[x] = arr.get(x);
          }

          byte[] cod = new byte[vetorB.length];
          cod = LZW.decodifica(vetorB);
          rafDestino.write(cod);
          arr = new ArrayList<>();
        }
      }

      if (arr.size() != 0) {
        byte[] vetorB = new byte[arr.size()];
        for (int x = 0; x < vetorB.length; x++) {
          vetorB[x] = arr.get(x);
        }

        byte[] cod = new byte[vetorB.length];
        cod = LZW.decodifica(vetorB);
        rafDestino.write(cod);
      }
    }

    // fazendo a comparação entre as duas compactações
    System.out
        .println("Proporção entre as duas compactações(arquivo comprimido em partes / arquivo comprimido inteiro)");
    File[] fb1 = new File("backup/" + origem).listFiles();
    File[] fb2 = new File("backup2/" + origem).listFiles();
    for (int i = 0; i < fb1.length; i++) {
      float r1 = fb1[i].length();
      float r2 = fb2[i].length();
      float result = r2 / r1;
      System.out.println(fb1[i].getName() + " -> " + result);
    }


    

  }

}
