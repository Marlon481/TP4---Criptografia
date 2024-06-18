package arquivos;

import java.util.ArrayList;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.HashExtensivel;
import aeds3.ListaInvertida;
import aeds3.ParIntInt;
import entidades.Livro;

public class ArquivoLivros extends Arquivo<Livro> {

  HashExtensivel<ParIsbnId> indiceIndiretoISBN;
  ArvoreBMais<ParIntInt> relLivrosDaCategoria;
  ListaInvertida listaInv;

  public ArquivoLivros() throws Exception {
    super("livros", Livro.class.getConstructor());
    indiceIndiretoISBN = new HashExtensivel<>(
        ParIsbnId.class.getConstructor(),
        4,
        "dados/livros_isbn.hash_d.db",
        "dados/livros_isbn.hash_c.db");
    relLivrosDaCategoria = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "dados/livros_categorias.btree.db");
    listaInv = new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");

  }

  @Override
  public int create(Livro obj) throws Exception {
    int id = super.create(obj);
    obj.setID(id);
    indiceIndiretoISBN.create(new ParIsbnId(obj.getIsbn(), obj.getID()));
    relLivrosDaCategoria.create(new ParIntInt(obj.getIdCategoria(), obj.getID()));
    RemoveStopWord remSW = new RemoveStopWord();
    ArrayList<String> arrTitulo = new ArrayList<>();
    arrTitulo = remSW.RemoverStopWord(obj.getTitulo());
    for(String s: arrTitulo){
      listaInv.create(s, obj.getID());
    }
    return id;
  }

  public Livro readISBN(String isbn) throws Exception {
    ParIsbnId pii = indiceIndiretoISBN.read(ParIsbnId.hashIsbn(isbn));
    if (pii == null)
      return null;
    int id = pii.getId();
    return super.read(id);
  }

  //Busca Lista de Livros por palavras chave (titulo)
  public ArrayList<Livro> readTitulo(String sPalavrasChave){
    RemoveStopWord remSW = new RemoveStopWord();
    ArrayList<Livro> listaLivros = new ArrayList<>();
    ArrayList<String> arrTitulo = new ArrayList<>();
    arrTitulo = remSW.RemoverStopWord(sPalavrasChave);
        
    ArrayList<Integer> listaID = new ArrayList<>();
    ArrayList<Integer> listaTmp = new ArrayList<>();


    for (int x = 0; x < arrTitulo.size(); x++) {
      String sAux = arrTitulo.get(x);
      try {
        listaTmp = new ArrayList<>();
        int i [] = listaInv.read(sAux);
        for(int j = 0; j < i.length; j++){
          
          listaTmp.add(i[j]);
          if(x > 0)
          listaID.retainAll(listaTmp);
          else
          listaID = listaTmp;
          
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    for(int j = 0; j < listaID.size(); j++){
      try {
        listaLivros.add(super.read(listaID.get(j)));
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    }
    return listaLivros;
  }

  @Override
  public boolean delete(int id) throws Exception {
    Livro obj = super.read(id);
    if (obj != null) {
      RemoveStopWord remSW = new RemoveStopWord();
      ArrayList<String> arrTitulo = new ArrayList<>();
      arrTitulo = remSW.RemoverStopWord(obj.getTitulo());
      for (String sAux : arrTitulo) {
        listaInv.delete(sAux, obj.getID());
      }
      if (indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(obj.getIsbn()))
          &&
          relLivrosDaCategoria.delete(new ParIntInt(obj.getIdCategoria(), obj.getID())))
        return super.delete(id);
    }
    return false;
  }

  @Override
  public boolean update(Livro novoLivro) throws Exception {
    Livro livroAntigo = super.read(novoLivro.getID());
    if (livroAntigo != null) {

      // Testa alteração do ISBN
      if (livroAntigo.getIsbn().compareTo(novoLivro.getIsbn()) != 0) {
        indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(livroAntigo.getIsbn()));
        indiceIndiretoISBN.create(new ParIsbnId(novoLivro.getIsbn(), novoLivro.getID()));
        
      }
      if(livroAntigo.getTitulo().compareTo(novoLivro.getTitulo()) != 0){
        RemoveStopWord remSW = new RemoveStopWord();
        ArrayList<String> arrTitulo = new ArrayList<>();
        arrTitulo = remSW.RemoverStopWord(livroAntigo.getTitulo());
        for(String s: arrTitulo){
          listaInv.delete(s, livroAntigo.getID());
        }
        remSW = new RemoveStopWord();
        arrTitulo = new ArrayList<>();
        arrTitulo = remSW.RemoverStopWord(novoLivro.getTitulo());
        for(String s: arrTitulo){
          listaInv.create(s, novoLivro.getID());
        }
      }

      // Testa alteração da categoria
      if (livroAntigo.getIdCategoria() != novoLivro.getIdCategoria()) {
        relLivrosDaCategoria.delete(new ParIntInt(livroAntigo.getIdCategoria(), livroAntigo.getID()));
        relLivrosDaCategoria.create(new ParIntInt(novoLivro.getIdCategoria(), novoLivro.getID()));
      }

      // Atualiza o livro
      return super.update(novoLivro);
    }
    return false;
  }
}
