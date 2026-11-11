//Vitor e Caleb
//git config --global user.email/name "nome/email"
//home/aluno/Downloads/TCA/TcaVitor/TcaVitorCaleb.java
//javac -cp "lib/*:." TcaVitorCaleb.java
import java.util.Scanner;

public class TcaVitorCaleb{

    public final static Scanner TECLADO = new Scanner(System.in);

    public static int lerInteiro(){
        return TECLADO.nextInt();
    }
    
    public static void main(String[] args) {

        int[][] tabuleiro = KeboardManager.voltarProgresso();
        
        TabuleiroManager.iniciarCronometro();

        TabuleiroManager.imprimirMatrizInteiro(tabuleiro);

        while (true) {
            tabuleiro = KeboardManager.MoverPeca(tabuleiro);
        }
    }
}