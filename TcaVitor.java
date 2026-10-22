//Vitor e Caleb
import java.util.Scanner;
import java.util.Random;

public class TcaVitor{

    public final static Scanner TECLADO = new Scanner(System.in);

    public static int lerInteiro(){
        return TECLADO.nextInt();
    }

    //criar um tabuleiro ja resolvido para que embaralhemos depois
    public static int [][] criarMatrizInteira(int tamanho){
        int valor = 1;

        int[][] mat = new int[tamanho][tamanho];

        for(int i = 0; i < tamanho; i++){
            for(int j = 0; j < tamanho; j++){
                mat[i][j] = valor++;
            }
        }

        mat[tamanho - 1][tamanho - 1] = 0;

        return mat;
    }

    //imprime o tabuleiro
    public static void imprimirMatrizInteiro(int[][] mat){
        int linhas, colunas;
        linhas = mat.length;
        colunas = mat[0].length;

        System.out.printf("\n\t\t\t\t\t\t\t\t\tTabuleiro: %d x %d\n",linhas, colunas);

        System.out.printf("\n");

        for(int i = 0; i < linhas; i++){
            System.out.printf("\t\t\t\t\t\t\t\t\t");
            for(int j = 0; j < colunas; j++){
                if(mat[i][j] == 0){
                    System.out.print("\t");
                }else{
                System.out.printf("%2d\t", mat[i][j]);
                }
            }
            System.out.println("\n");
        }
    }

    //usa a matriz ja feita e embaralha ela
    public static int EmbaralharTabuleiro(int[][] tabuleiro){
        return 0;
    }

    public static int MoverPeca(int[][] tabuleiro){

        int i;
        int j;
        int auxiliar;

        System.out.println("qual peça deseja mover? Diga a linha e a coluna (0-3)");
        i = lerInteiro();
        j = lerInteiro();

        if(tabuleiro[][])

        return 0;
    }
    
    public static void main(String[] args) {
        
        int [][] tabuleiro;

        tabuleiro = criarMatrizInteira(4);

        imprimirMatrizInteiro(tabuleiro);

        System.out.println("   __ _____                            _           \n" + //
                        "  / /|___  |  _ __ ___  ___  ___ _ __ | |__   __ _ \n" + //
                        " | '_ \\ / /  | '__/ _ \\/ __|/ _ \\ '_ \\| '_ \\ / _` |\n" + //
                        " | (_) / /   | | |  __/\\__ \\  __/ | | | | | | (_| |\n" + //
                        "  \\___/_/    |_|  \\___||___/\\___|_| |_|_| |_|\\__,_|\n" + //
                        "                                                   ");
    }
}
