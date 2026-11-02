import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.Scanner;

public class KeboardManager{

    public interface Crt extends Library {
        Crt INSTANCE = Native.load("msvcrt", Crt.class);
        int _kbhit();
        int _getch();
    }

    public final static Scanner TECLADO = new Scanner(System.in);

    static boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
    static boolean tecladoPreparado = false;

    public static boolean pressionouTecla() {
        if (windows) {
            return Crt.INSTANCE._kbhit() != 0;
        }
        prepararTecladoMacLinux();
        try {
            return System.in.available() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static int obtemTeclaPressionada() {
        if (windows) {
            return Crt.INSTANCE._getch();
        }
        prepararTecladoMacLinux();
        try {
            return System.in.read();
        } catch (Exception e) {
            return -1;
        }
    }

    public static void esperarTecla() {
        System.out.println("Pressione uma tecla para continuar...");
        obtemTeclaPressionada(); // fica esperando a tecla
    }

    static void prepararTecladoMacLinux() {
        if (tecladoPreparado) {
            return;
        }
        tecladoPreparado = true;
        executarStty("-icanon -echo -icrnl min 1"); // -icrnl: ENTER vira 13, igual ao Windows
        Runtime.getRuntime().addShutdownHook(new Thread(() -> executarStty("sane")));
    }

    static void executarStty(String opcoes) {
        try {
            new ProcessBuilder("sh", "-c", "stty " + opcoes + " < /dev/tty").start().waitFor();
        } catch (Exception e) {
            // se não conseguir, o teclado só vai funcionar com ENTER
        }
    }

    public static void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static char lerTecla(){
        return (char)obtemTeclaPressionada();
    }

    public static void esperar(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
        }
    }

    public static void desenharTela(int x, int y, int largura, int altura) {
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                if (i == y && j == x) {
                    System.out.print('H');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
        System.out.println("Use A, D, W, S para mover o personagem. Pressione ESC para sair.");
    }

    public static int[][] MoverPeca(int[][] tabuleiro) {

        int linhas = -1;
        int colunas = -1;

        for(int i = 0; i < tabuleiro.length; i++){
            for(int j = 0; j < tabuleiro[0].length; j++){
                if(tabuleiro[i][j] == 0){
                    linhas = i;
                    colunas = j;
                }
            }
        }

        int moverLinha = linhas;
        int moverColuna = colunas;

        System.out.println("mova a peça(w, a, s, d): ");

        char tecla = lerTecla();
        limparConsole();

        switch (tecla) {
    case 'w':
    case 'W':
        moverLinha++;
        break;
    case 's':
    case 'S':
        moverLinha--;
        break;
    case 'a':
    case 'A':
        moverColuna++;
        break;
    case 'd':
    case 'D':
        moverColuna--;
        break;
    case 27:
        System.exit(0);
        }

        if (moverLinha >= 0 && moverLinha < tabuleiro.length
            && moverColuna >= 0 && moverColuna < tabuleiro[0].length) {
        tabuleiro[linhas][colunas] = tabuleiro[moverLinha][moverColuna];
        tabuleiro[moverLinha][moverColuna] = 0;
        }

        System.out.println("Tabuleiro atualizado:");
        TabuleiroManager.imprimirMatrizInteiro(tabuleiro);
        
        return tabuleiro;
    }
}
