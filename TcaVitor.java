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
    
    public static void main(String[] args) {

        int[][] tabuleiro = TabuleiroManager.criarMatrizInteira(4);

        TabuleiroManager.embaralharTabuleiroInteiro(tabuleiro, 200);

        TabuleiroManager.imprimirMatrizInteiro(tabuleiro);

        while (true) {
            tabuleiro = KeboardManager.MoverPeca(tabuleiro);
        }
    }
}