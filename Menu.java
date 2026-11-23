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


    public static void atualizarMenu(int opcaoSelecionada, String[] opcoes, String tituloMenu) {

        SoundManager.tocarSom("Sons/menu.wav", false);

        limparConsole();
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        mudarCor(AMARELO);
        System.out.println(AsciiArt.titulo);
        mudarCor(COR_PADRAO);
        mudarCor(MAGENTA);
        System.out.println("\t\t          ───── o quebra-cabeça dos números ─────");
        mudarCor(COR_PADRAO);
        System.out.println();
        System.out.println("\t\t\t");
        System.out.println("\t\t\t  "+tituloMenu+"    ");
        System.out.println("\t\t\t");
        for (int i = 0; i < opcoes.length; i++) {
            if (i == opcaoSelecionada) {
                System.out.print("\t\t\t[               ] ");
                mudarCor(VERDE);
                System.out.print(opcoes[i]);
                mudarCor(COR_PADRAO); // volta para a cor padrão
                System.out.println("  ");
            } else {
                System.out.println("\t\t\t  " + opcoes[i] +"  ");
            }
        }
        System.out.println("\t");
        System.out.println();

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
                        return opcaoSelecionada;

                    case 27: 
                        return -1; 
                }
                
            }
            
        }
    }

    public static void menuManager() {
        String [] opcoes = {            
            "NOVO JOGO        ",
            "CARREGAR JOGO    ",
            "CONFIGURAÇÕES    ",
            "AJUDA            ",
            "SOBRE            ",
            "RESOLVER SOZINHO ",
        }; 

        while (true) {

            int opcaoSelecionada = escolherOpcao(opcoes, "MENU PRINCIPAL");

            switch (opcaoSelecionada) {
                case 0:
                    limparConsole();
                    TabuleiroManager.jogar();
                    break;
                case 1:
                    limparConsole();
                    TabuleiroManager.carregarJogo();
                    break;
                case 2:
                configManagers();

                case 3:
                    System.out.println("\n\nAjuda selecionado.\n\n");
                    break;

                case 4:
                    System.out.println("\n\nSobre selecionado.\n\n");           
                    break;

                case 5:
                    limparConsole();
                    int[][] tab = TabuleiroManager.criarMatrizInteira(tamanho);
                    TabuleiroManager.embaralharTabuleiroInteiro(tab, embaralhamentos);
                    ResolverSozinho.resolverEMostrar(tab, 500);
                    
                    break;

                default:
                    System.out.println("\n\nOpção inválida.\n\n");
                    break;

            }

            limparConsole();
        }
    }

    public static void configManagers() {
        String [] opcoes = {            
            "TAMANHO          ",
            "DIFICULDADE      ",
        };

        while (true) {

            int opcaoSelecionada = escolherOpcao(opcoes, "CONFIGURAÇÕES");

            switch (opcaoSelecionada) {
                case 0:
                    tamanhoManager();
                    break;
                case 1:
                    dificuldadeManager();
                    break;
                case -1:
                    return; 
                default:
                    System.out.println("\n\nOpção inválida.\n\n");
                    break;
            }
        }
    }


    public static int tamanho = 4;

    public static void tamanhoManager() {
        String [] opcoes = {            
            "3x3              ",
            "4x4              ",
            "MODO DESAFIO 5x5 ",
        };

        while (true) {

            int opcaoSelecionada = escolherOpcao(opcoes, "TAMANHO DO TABULEIRO");

            switch (opcaoSelecionada){
                case 0:
                    tamanho = 3;
                    return;
                case 1:
                    tamanho = 4;
                    return;
                case 2:
                    tamanho = 5;
                    return;
                case -1:
                    return; 
                default:
                    System.out.println("\n\nOpção inválida.\n\n");
                    break;
            }
        }
    }

    public static int embaralhamentos = 200;

    public static void dificuldadeManager() {
        String [] opcoes = {            
            "FÁCIL            ",
            "MÉDIO            ",
            "DIFÍCIL (Não recomendado no modo 5x5)         ",
        };

        while (true) {

            int opcaoSelecionada = escolherOpcao(opcoes, "DIFICULDADE DO JOGOO");

            switch (opcaoSelecionada){
                case 0:
                    embaralhamentos = 30;
                    return;
                case 1:
                    embaralhamentos = 100;
                    return;
                case 2:
                    embaralhamentos = 300;
                    return;
                case -1:
                    return; 
                default:
                    System.out.println("\n\nOpção inválida.\n\n");
                    break;
            }
        }
    }
}