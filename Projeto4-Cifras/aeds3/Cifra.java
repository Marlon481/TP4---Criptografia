package aeds3;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cifra {
    static byte[] palavraChave = "algoritm".getBytes();

    public Cifra() {

    }

    public static byte[] cifrar(byte[] mensagem) {
        byte[] palavraChaveComp = preencherPC(palavraChave, (int) mensagem.length);
        byte[] cs = cifrarSubstituicao(mensagem, palavraChaveComp);
        byte[] saida = cifrarColuna(cs, palavraChave);
        return saida;
    }

    public static byte[] decifrar(byte[] mensagem) {
        byte[] palavraChaveComp = preencherPC(palavraChave, (int) mensagem.length);
        byte[] cc = decifrarColuna(mensagem, palavraChave);
        byte[] saida = decifrarSubstituicao(cc, palavraChaveComp);
        return saida;
    }

    public static byte[] cifrarSubstituicao(byte[] mensagem, byte[] pc) {
        byte[] resultado = new byte[mensagem.length];
        for (int i = 0; i < mensagem.length; i++) {
            int baux = (mensagem[i] + pc[i]);
            if (baux > 256) {
                baux = (byte) (baux % 256);
            }

            resultado[i] = (byte) baux;
        }
        return resultado;
    }

    public static byte[] decifrarSubstituicao(byte[] mensagem, byte[] pc) {
        byte[] resultado = new byte[pc.length];

        for (int i = 0; i < mensagem.length; i++) {
            Byte baux = (byte) (mensagem[i] - pc[i]);
            if (baux < 0) {
                baux = (byte) (baux + 256);
            }
            resultado[i] = (byte) baux;
        }
        return resultado;
    }

    private static byte[] preencherPC(byte[] palavraChave, int tamanho) {
        byte[] resp = new byte[tamanho];
        int pos = 0;
        for (int i = 0; i < tamanho; i++) {
            resp[i] = palavraChave[pos];
            pos = pos + 1;
            if (pos % palavraChave.length == 0) {
                pos = 0;
            }
        }
        return resp;
    }

    // ----------------------------------------------------------------------------------------------

    public static byte[] cifrarColuna(byte[] mensagem, byte[] palavraChave) {
        byte[] pcAux = new byte[palavraChave.length];
        for (int i = 0; i < palavraChave.length; i++) {
            pcAux[i] = palavraChave[i];
        }
        int resto = mensagem.length % palavraChave.length;
        int pos = 0;
        int tam = mensagem.length / palavraChave.length;
        List<Byte> sb = new ArrayList<Byte>();
        byte[][] s = new byte[(tam + resto)][(palavraChave.length)];
        for (int i = 0; i < (tam + resto); i++) {
            for (int j = 0; j < (palavraChave.length); j++) {
                if (pos < mensagem.length) {
                    s[i][j] = mensagem[pos];
                    pos++;
                }
            }
        }
        int aux = 0;
        pos = 0;
        int max = tam + resto;
        byte[] ordem = ordenar(pcAux);
        for (int k = 0; k < pcAux.length; k++) {
            int saidaColuna = indexOf(ordem, ordem[aux]);
            for (int i = 0; i < max; i++) {
                if (s[i][saidaColuna] != 0) {
                    sb.add(s[i][saidaColuna]);
                }
            }
            pcAux[saidaColuna] = -1;
            aux++;
        }
        byte[] saida = new byte[sb.size()];
        while (pos < sb.size()) {
            saida[pos] = sb.get(pos);
            pos++;
        }
        return saida;
    }

    // ----------------------------------------------------------------------------------------------

    public static byte[] decifrarColuna(byte[] mensagem, byte[] palavraChave) {
        int resto = mensagem.length % palavraChave.length;
        int tam = mensagem.length / palavraChave.length;
        byte[] pcOrdenada = ordenar(palavraChave);
        int pos = 0;
        byte[][] sb = new byte[tam + resto][palavraChave.length];
        for (int i = 0; i < palavraChave.length; i++) {
            if (indexOf(pcOrdenada, pcOrdenada[i]) < resto) {
                for (int k = 0; k < (tam + 1); k++) {
                    sb[k][i] = mensagem[pos];
                    pos++;
                }
            } else {
                for (int k = 0; k < tam; k++) {
                    sb[k][indexOf(pcOrdenada, pcOrdenada[i])] = mensagem[pos];
                    pos++;
                }
            }
        }
        pos = 0;
        byte[] saida = new byte[mensagem.length];
        for (int i = 0; i < (tam + resto); i++) {
            for (int j = 0; j < palavraChave.length; j++) {
                if (sb[i][j] != 0) {
                    saida[pos] = (sb[i][j]);
                    pos++;
                }
            }
        }
        return saida;
    }

    // --------------------------------------------------------------------------------------------

    private static byte[] ordenar(byte[] pc) {
        byte[] pcOrdenada = new byte[pc.length];
        for (int i = 0; i < pc.length; i++) {
            pcOrdenada[i] = pc[i];
        }
        Arrays.sort(pcOrdenada);
        return pcOrdenada;
    }

    private static int indexOf(byte[] ordem, byte posDesejada) {
        byte k = 0;
        byte resp = -1;
        while (k < ordem.length) {
            if (ordem[k] == posDesejada) {
                resp = k;
                return resp;
            }
            k++;
        }
        return -1;
    }

}
