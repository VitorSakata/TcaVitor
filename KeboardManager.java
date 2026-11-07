import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.util.Scanner;

public class KeboardManager{

    public interface Crt extends Library {
        Crt INSTANCE = Native.load("msvcrt", Crt.class);
        int _kbhit();
        int _getch();
    }

    public interface Kernel32 extends Library {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);
        Pointer GetStdHandle(int tipo);
        boolean GetConsoleScreenBufferInfo(Pointer console, byte[] info);
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

    //pergunta se o jogador quer voltar ao jogo salvo se sim carrega o tabuleiro do arquivo se não cria um novo e embaralha;
    public static int[][] voltarProgresso() {

        String Nome_Arquivo = "savegame.txt";

        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\tDeseja retornar ao jogo salvo? (S/N)");

        //começa com null assim se o jogador não quiser carregar o jogo cai no tabuleiro novo;
        String salvo = null;

        switch(lerTecla()){
            case 's':
            case 'S':
                salvo = SaveGameManager.lerDoArquivo(Nome_Arquivo);
        }

        //se o arquivo existir transforma o texto em tabuleiro caso o contrario cria um tabuleiro novo;
        if(salvo != null){
            return TabuleiroManager.textoParaTabuleiro(salvo);
        }

        int[][] tabuleiro = TabuleiroManager.criarMatrizInteira(4);
        TabuleiroManager.embaralharTabuleiroInteiro(tabuleiro, 200);

        return tabuleiro;
    }

    public static int[][] MoverPeca(int[][] tabuleiro) {

        String Nome_Arquivo = "savegame.txt";

        int linhas = 0;
        int colunas = 0;

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

        char tecla = lerTecla();
        limparConsole();
        SoundManager.tocarSom("Sons/mover.wav", false);

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

        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\tDeseja salvar seu progresso? (S/Esc)");

        String dadosParaSalvar = String.valueOf(TabuleiroManager.tabuleiroParaTexto(tabuleiro));

        switch(lerTecla()){
            case 's':
            case 'S':

            SaveGameManager.escreverNoArquivo(Nome_Arquivo, dadosParaSalvar);
        }
        SoundManager.tocarSom("Sons/esc.wav", true);
        System.exit(0);
        }

        if (moverLinha < 0) moverLinha = 0;
        if (moverLinha > tabuleiro.length - 1) moverLinha = tabuleiro.length - 1;
        if (moverColuna < 0) moverColuna = 0;
        if (moverColuna > tabuleiro[0].length - 1) moverColuna = tabuleiro[0].length - 1;

        tabuleiro[linhas][colunas] = tabuleiro[moverLinha][moverColuna];
        tabuleiro[moverLinha][moverColuna] = 0;

        TabuleiroManager.imprimirMatrizInteiro(tabuleiro);
        
        return tabuleiro;
    }
}
