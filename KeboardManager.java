import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

    // retorna {colunas, linhas} da janela do terminal
    public static int[] tamanhoTerminal() {
        try {
            if (windows) {
                byte[] info = new byte[22];
                Pointer console = Kernel32.INSTANCE.GetStdHandle(-11); // -11: saída do console
                if (Kernel32.INSTANCE.GetConsoleScreenBufferInfo(console, info)) {
                    ByteBuffer dados = ByteBuffer.wrap(info).order(ByteOrder.LITTLE_ENDIAN);
                    int colunas = dados.getShort(14) - dados.getShort(10) + 1;
                    int linhas = dados.getShort(16) - dados.getShort(12) + 1;
                    return new int[]{colunas, linhas};
                }
            } else {
                Process stty = new ProcessBuilder("sh", "-c", "stty size < /dev/tty").start();
                String[] tamanho = new String(stty.getInputStream().readAllBytes()).trim().split(" ");
                return new int[]{Integer.parseInt(tamanho[1]), Integer.parseInt(tamanho[0])};
            }
        } catch (Exception e) {
            // se não conseguir medir, usa o tamanho padrão abaixo
        }
        return new int[]{80, 24};
    }

    // limpa a tela e imprime os blocos no meio dela, com uma linha em branco entre eles
    public static void imprimirCentralizado(String... blocos) {
        int[] tamanho = tamanhoTerminal();

        int totalLinhas = blocos.length - 1;
        for (String bloco : blocos) {
            totalLinhas += bloco.split("\n").length;
        }

        limparConsole();

        for (int i = 0; i < (tamanho[1] - totalLinhas) / 2; i++) {
            System.out.println();
        }

        for (int b = 0; b < blocos.length; b++) {
            String[] linhas = blocos[b].split("\n");

            int maiorLinha = 0;
            for (String linha : linhas) {
                maiorLinha = Math.max(maiorLinha, linha.length());
            }

            String margem = " ".repeat(Math.max(0, (tamanho[0] - maiorLinha) / 2));
            for (String linha : linhas) {
                System.out.println(margem + linha);
            }

            if (b < blocos.length - 1) {
                System.out.println();
            }
        }
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

        String tchau = "█████  ███  █   █  ███  █   █\n" +
                        "  █   █     █   █ █   █ █   █\n" +
                        "  █   █     █████ █████ █   █\n" +
                        "  █   █     █   █ █   █ █   █  █\n" +
                        "  █    ███  █   █ █   █  ███  █";
        String covarde = " ███   ███  █   █  ███  ████  ████  █████ █\n" +
                        "█     █   █ █   █ █   █ █   █ █   █ █     █\n" +
                        "█     █   █ █   █ █████ ████  █   █ ████  █\n" +
                        "█     █   █  █ █  █   █ █  █  █   █ █\n" +
                        " ███   ███    █   █   █ █   █ ████  █████ █";
        String desenho = "                  :.:++=-----=+=:::\n" +
                        "              -=--:................:-=-\n" +
                        "          .---:........................:=:.\n" +
                        "        ---:....................:::::::::--=:\n" +
                        "      :-:.............:.-====:.:::::::::.::--=:\n" +
                        "     +-.........::::::::+%#.::::::::::--::::----:                  .:+*:\n" +
                        "   .-:...:::::::+*%%+::::::::::::::-:-**-==-------            .--::::....--\n" +
                        "  .+:::::::::::#=:::::::::::::::::--##%#=:::------+  .=+=++.=---:::::.....:-\n" +
                        "  =-::::::::::-::::::::::::-------.:+###+---:------+-:::::::=---:::::::...::\n" +
                        " +---.:::--=-:::::::---------+::...-####+--------=-::::::::::+---:::::::::--\n" +
                        ":---:::--=-::--------==-:-:...... +%%%%%*-::----=-::::::...::=-=--::::::--.\n" +
                        "---------=%%+:.................=#%######+-:::-------:....:::=-:-:=+=-=+.\n" +
                        ":------:::+%%%#=..........:+#%%%#######*-::::::-=-----====-::::::::---:-\n" +
                        ":------::::+%%%%%%%%%%#####%%%######%%#+:::::::-------:::::::::::::::.--\n" +
                        ":------:::::=%%%%%%%%%####%%%%###%%%%%+:::::--:-=----:...::::::-:::.:=+\n" +
                        "--------:::::=#%%%%%%%####%%%#*==++##=:::::-----:=----=:::::::::::-=--:\n" +
                        ":-------:::-=--=-::-=%###+=+++*=++*+-::::::---::::=-----:-++++-:-:--::=\n" +
                        " =--------=::--::::::+%*===+++++++-::::::::---::::-=----:...::::::::--\n" +
                        "  =-----:=:::::::::==+==--+++*+-:---:::::::::::::::-=.+=-...::::::-=\n" +
                        "  .=-----:::::::::---::::---------::::::::::::::::--:     --:::-.\n" +
                        "   :=----::::+:..::=:::::-+::::::::::::::::::::::---\n" +
                        "  .----::+:::::::::::::--:::::::::::::::::::::::-=:\n" +
                        "   .-:--:::::::::::::::-=:::::::::::::::::::::::-+.\n" +
                        "   .:...--::::::::::::--:::::::::::::::::::::--:\n" +
                        "         +=:::::::..:+::::::::::::::::::::---\n" +
                        "           .=--::...:=::::::::::::::::::--=.\n" +
                        "              .===+=--::::::::::::------.                         .\n" +
                        "                   .:.=+=------=+-.:.             . . ...  .   ......  .\n" +
                        "                                                  .   .... .. . . . .  ... .";
        imprimirCentralizado(tchau, covarde, desenho);
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
