//Vitor e Caleb
//git config --global user.email/name "nome/email"
import java.util.Scanner;

public class TcaVitorCaleb{

    public final static Scanner TECLADO = new Scanner(System.in);

    public static int lerInteiro(){
        return TECLADO.nextInt();
    }
    
    public static void main(String[] args) {

        //chamado antes do while, assim só pergunta uma vez no começo do jogo;
        int[][] tabuleiro = KeboardManager.voltarProgresso();

        TabuleiroManager.imprimirMatrizInteiro(tabuleiro);

        while (true) {
            tabuleiro = KeboardManager.MoverPeca(tabuleiro);
        }
    }
}