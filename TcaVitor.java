//Vitor e Caleb
//git config --global user.email/name "nome/email"
import java.util.Scanner;
import java.util.Random;

public class TcaVitor{

    public final static Scanner TECLADO = new Scanner(System.in);

    public static int lerInteiro(){
        return TECLADO.nextInt();
    }

    public static char lerCaractere(){
        char valor;
        // pega o primeiro caractere digitado
        valor = TECLADO.next().charAt(0);
        return valor;
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

    //função para mover a peça
    public static int[][] MoverPeca(int[][] tabuleiro){

        char movimento;
        int linha0;
        int coluna0;
        int auxiliar;
        int n;
        int m;

        for(int i = 0; i < tabuleiro.length; i++){
            for(int j = 0; j < tabuleiro[0].length; j++){
                if(tabuleiro[i][j] == 0){
                    linha0 = n;
                    coluna0 = m;
                }
            }
        }

        System.out.println("qual peça deseja mover? Diga a direção(W A S D");
        movimento = lerCaractere();

        switch(movimento){
            case 'w': 
            case 'W':
                tabuleiro[linha0 + 1][coluna0] = tabuleiro[n][m];
                break;
            default:
                break;
        }

        return tabuleiro;
    }
    
    public static void main(String[] args) {
        
        int [][] tabuleiro;

        tabuleiro = criarMatrizInteira(4);

        imprimirMatrizInteiro(tabuleiro);
    }
}
