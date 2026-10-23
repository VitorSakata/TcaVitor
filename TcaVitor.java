//Vitor e Caleb
//git config --global user.email/name "nome/email"
import java.util.Scanner;

public class TcaVitor{

    public final static Scanner TECLADO = new Scanner(System.in);

    public static int lerInteiro(){
        return TECLADO.nextInt();
    }

    //função para mover a peça
    public static int[][] MoverPeca(int[][] tabuleiro){

        return tabuleiro;
    }

    //funçao que limpa o terminal
    public static void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public static void main(String[] args) {

        limparConsole();

        int[][] tabuleiro = TabuleiroManager.criarMatrizInteira(4);

        TabuleiroManager.embaralharTabuleiroInteiro(tabuleiro);

        TabuleiroManager.imprimirMatrizInteiro(tabuleiro);
    }
}
