import com.sun.jna.Library;
import com.sun.jna.Native;

public class Menu {

    public interface Crt extends Library {
        Crt INSTANCE = Native.load("msvcrt", Crt.class);
        int _kbhit();
        int _getch();
    }

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

        }
    }
  

    public static void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void gotoXY(int linha, int coluna) {
        char escCode = 0x1B;
        System.out.print(String.format("%c[%d;%df", escCode, linha, coluna));
    }  

    public static final int PRETO      = 0;
    public static final int VERMELHO   = 1;
    public static final int VERDE      = 2;
    public static final int AMARELO    = 3;
    public static final int AZUL       = 4;
    public static final int MAGENTA    = 5;
    public static final int CIANO      = 6;
    public static final int CINZA      = 7;
    public static final int BRANCO     = 15;
    public static final int COR_PADRAO = -1; 

    public static void mudarCor(int cor) {
        if (cor >= 0 && cor <= 255) {
            System.out.print("\033[38;5;" + cor + "m");
        } else {
            System.out.print("\033[39m"); // cor padrão do terminal
        }
    }

    public static void mudarCorFundo(int cor) {
        if (cor >= 0 && cor <= 255) {
            System.out.print("\033[48;5;" + cor + "m");
        } else {
            System.out.print("\033[49m"); // cor padrão do terminal
        }
    }   


    public static void atualizarMenu(int opcaoSelecionada, String[] opcoes, String tituloMenu) {
        limparConsole();
        System.out.println("\n\n\n\n");
        mudarCor(AMARELO);
        System.out.println(AsciiArt.titulo);
        mudarCor(COR_PADRAO);
        mudarCor(MAGENTA);
        System.out.println("          ───── o quebra-cabeça dos números ─────");
        mudarCor(COR_PADRAO);
        System.out.println();
        System.out.println("\t\t╔═════════════════════╗");
        System.out.println("\t\t║    "+tituloMenu+"   ║");
        System.out.println("\t\t╠═════════════════════╣");
        for (int i = 0; i < opcoes.length; i++) {
            if (i == opcaoSelecionada) {
                System.out.print("\t\t║> ");
                mudarCor(VERDE);
                System.out.print(opcoes[i]);
                mudarCor(COR_PADRAO); // volta para a cor padrão
                System.out.println("  ║");
            } else {
                System.out.println("\t\t║  " + opcoes[i] +"  ║");
            }
        }
        System.out.println("\t\t╚═════════════════════╝");
        System.out.println();

    }

    public static void executarAcaoMenu(int opcaoSelecionada) {
        
        limparConsole();

        switch (opcaoSelecionada) {
            case 0:
                int[][] tabuleiro = TabuleiroManager.criarMatrizInteira(4);
                TabuleiroManager.embaralharTabuleiroInteiro(tabuleiro, 200);
                TabuleiroManager.iniciarCronometro();
                TabuleiroManager.imprimirMatrizInteiro(tabuleiro);
                while (true) {
                    tabuleiro = KeboardManager.MoverPeca(tabuleiro);
                }

            case 1:
                int[][] tabuleirosalvo = TabuleiroManager.textoParaTabuleiro(SaveGameManager.lerDoArquivo("savegame.txt"));
                TabuleiroManager.iniciarCronometro();
                TabuleiroManager.imprimirMatrizInteiro(tabuleirosalvo);
                while (true) {
                    tabuleirosalvo = KeboardManager.MoverPeca(tabuleirosalvo);
                }

            case 2:

                configManagers();

            case 3:
                System.out.println("\n\nAjuda selecionado.\n\n");
                break;

            case 4:
                System.out.println("\n\nSobre selecionado.\n\n");           
                break;

            default:
                System.out.println("\n\nOpção inválida.\n\n");
                break;

        }
        esperarTecla();
        limparConsole();

    }  

    public static int escolherOpcao(String[] opcoes, String tituloMenu){

        int opcaoSelecionada = 0;

        atualizarMenu(  opcaoSelecionada, opcoes, tituloMenu);
        
        while (true) {            
            if ( pressionouTecla()) {
                int ch = obtemTeclaPressionada();     
                
                switch (ch) {
                    case 'w':
                    case 'W':
                        opcaoSelecionada--;
                        if (opcaoSelecionada < 0) {
                            opcaoSelecionada = opcoes.length - 1;
                        }
                        atualizarMenu(opcaoSelecionada, opcoes, tituloMenu);
                        break;  

                    case 's':
                    case 'S':
                        opcaoSelecionada++;
                        if (opcaoSelecionada >= opcoes.length) {
                            opcaoSelecionada = 0;
                        }
                        atualizarMenu(opcaoSelecionada, opcoes, tituloMenu);
                        break;

                    case 13: 
                        executarAcaoMenu(opcaoSelecionada);
                        atualizarMenu(  opcaoSelecionada, opcoes, tituloMenu);
                        break;

                    case 27: 
                        return -1; 
                }
                
            }
            
        }
    }
    
    public static void menuManager() {
        String [] opcoes = {            
            "iniciar jogo     ",
            "carregar jogo    ",
            "configuracoes    ",
            "ajuda            ",
            "sobre            ",
        };

        escolherOpcao(opcoes, "Menu Principal");
    }

    public static void configManagers() {
        String [] opcoes = {            
            "Tamanho          ",
            "Dificuldade      ",
        };

        escolherOpcao(opcoes, "Configurações");
    }

    public static void tamanhoManager() {
        String [] opcoes = {            
            "3x3              ",
            "4x4              ",
            "5x5              ",
        };

        escolherOpcao(opcoes, "Tamanho do Tabuleiro");
    }
}