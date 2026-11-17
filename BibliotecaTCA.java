// =====================================================================
// BIBLIOTECA DO TCA - CORES DAS LETRAS
//
// No seu código, chame as funções com "BibliotecaTCA." na frente:
//     BibliotecaTCA.mudarCor(BibliotecaTCA.VERDE);
// =====================================================================
public class BibliotecaTCA {

    // Constantes das cores básicas, para usar com mudarCor.
    // Assim dá para escrever mudarCor(VERDE) em vez de mudarCor(2).
    // "final" quer dizer que o valor nunca muda.
    public static final int PRETO      = 0;
    public static final int VERMELHO   = 1;
    public static final int VERDE      = 2;
    public static final int AMARELO    = 3;
    public static final int AZUL       = 4;
    public static final int MAGENTA    = 5;
    public static final int CIANO      = 6;
    public static final int CINZA      = 7;
    public static final int BRANCO     = 15;
    public static final int COR_PADRAO = -1; // a cor original do terminal

    // Muda a cor do TEXTO. Tudo o que for escrito depois sai nessa cor.
    //
    // A cor é um número de 0 a 255 (são 256 cores!). Para as cores
    // básicas, use as constantes acima. Para ver todas as 256 cores e
    // escolher o número, chame a função mostrarCores().
    // Use COR_PADRAO (-1) para voltar à cor original do terminal.
    //
    // Exemplo:
    //   mudarCor(VERDE);                  // texto verde
    //   System.out.println("Você venceu!");
    //   mudarCor(208);                    // texto laranja (uma das 256 cores)
    //   System.out.println("Cuidado!");
    //   mudarCor(COR_PADRAO);             // volta ao normal
    //
    // ATENÇÃO:
    //   - Use mudarCor(COR_PADRAO) no fim do programa para o terminal não ficar colorido.
    //   - Cuidado com texto preto em terminal escuro (ou branco em terminal claro):
    //     ele fica invisível!
    public static void mudarCor(int cor) {
        if (cor >= 0 && cor <= 255) {
            System.out.print("\033[38;5;" + cor + "m");
        } else {
            System.out.print("\033[39m"); // cor padrão do terminal
        }
    }

    // Mostra na tela as 256 cores, cada número escrito na sua cor.
    // Use para escolher o número da cor que você quer no seu jogo.
    //   0 a 15    : cores básicas
    //   16 a 231  : cores variadas
    //   232 a 255 : tons de cinza, do mais escuro ao mais claro
    public static void mostrarCores() {
        System.out.println("Cores de 0 a 255:");
        for (int i = 0; i < 256; i++) {
            mudarCor(i);
            System.out.printf("Cor %-3d ", i);
            if (i % 8 == 7) {
                System.out.println(); // pula a linha a cada 8 cores
            }
        }
        mudarCor(COR_PADRAO);
    }
}
