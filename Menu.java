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

    // Retorna true se alguma tecla foi pressionada. NÃO espera: se nenhuma
    // tecla foi apertada, retorna false na hora e o jogo continua.
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

    // Retorna o código ASCII da tecla pressionada (ex.: 'a', 'w', 27 = ESC).
    // Se nenhuma tecla foi apertada, ESPERA até o usuário apertar uma.
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

    // Mostra "Pressione uma tecla para continuar..." e PARA o programa
    // até o jogador apertar qualquer tecla.
    //
    // Exemplo:
    //   System.out.println("GAME OVER");
    //   esperarTecla();
    public static void esperarTecla() {
        System.out.println("Pressione uma tecla para continuar...");
        obtemTeclaPressionada(); // fica esperando a tecla
    }

    // Mac/Linux: faz o terminal entregar cada tecla na hora (sem ENTER) e
    // sem mostrar a letra na tela. Quando o programa termina, volta ao normal.
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
    // ===================================================================

    public static void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void gotoXY(int linha, int coluna) {
        char escCode = 0x1B;
        System.out.print(String.format("%c[%d;%df", escCode, linha, coluna));
    }  

    // Constantes das cores básicas, para usar com mudarCor e mudarCorFundo.
    // Assim dá para escrever mudarCor(VERDE) em vez de mudarCor(2).
    // "final" quer dizer que o valor nunca muda.
    public static final int PRETO      = 0;
    public static final int VERMELHO   = 1;
    public static final int VERDE      = 2;
    public static final int AMARELO    = 3;
    public static final int AZUL       = 4;
    public static final int MAGENTA    = 5;
    public static final int CIANO      = 6;
    public static final int CINZA      = 7;
    public static final int BRANCO     = 15;
    public static final int COR_PADRAO = -1; // a cor original do terminal

    // Muda a cor do TEXTO. Tudo o que for escrito depois sai nessa cor.
    //
    // A cor é um número de 0 a 255 (são 256 cores!). Para as cores
    // básicas, use as constantes acima. Para ver todas as 256 cores e
    // escolher o número, chame a função mostrarCores().
    // Use COR_PADRAO (-1) para voltar à cor original do terminal.
    //
    // Exemplo:
    //   mudarCor(VERDE);                  // texto verde
    //   System.out.println("Você venceu!");
    //   mudarCor(208);                    // texto laranja (uma das 256 cores)
    //   System.out.println("Cuidado!");
    //   mudarCor(COR_PADRAO);             // volta ao normal
    //
    // ATENÇÃO:
    //   - Use mudarCor(COR_PADRAO) no fim do programa para o terminal não ficar colorido.
    //   - Cuidado com texto preto em terminal escuro (ou branco em terminal claro):
    //     ele fica invisível!
    //   - Só funciona rodando no terminal (não no console do Eclipse/NetBeans).
    public static void mudarCor(int cor) {
        if (cor >= 0 && cor <= 255) {
            System.out.print("\033[38;5;" + cor + "m");
        } else {
            System.out.print("\033[39m"); // cor padrão do terminal
        }
    }

    // Muda a cor do FUNDO (atrás das letras). Usa as mesmas cores
    // do mudarCor: constantes ou números de 0 a 255.
    //
    // Exemplo:
    //   mudarCorFundo(AZUL);              // fundo azul
    //   mudarCor(AMARELO);                // texto amarelo
    //   System.out.print(" PLACAR: 10 ");
    //   mudarCorFundo(COR_PADRAO);        // volta o fundo ao normal
    //   mudarCor(COR_PADRAO);             // volta o texto ao normal
    //
    // Dica: escreva espaços " " com a cor de fundo para desenhar blocos coloridos.
    public static void mudarCorFundo(int cor) {
        if (cor >= 0 && cor <= 255) {
            System.out.print("\033[48;5;" + cor + "m");
        } else {
            System.out.print("\033[49m"); // cor padrão do terminal
        }
    }   


    public static void atualizarMenu(int opcaoSelecionada, String[] opcoes) {
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
        System.out.println("\t\t║    Menu Principal   ║");
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
        System.out.println("Use W/S para navegar e ENTER para selecionar. ESC para sair.");

    }

    public static void executarAcaoMenu(int opcaoSelecionada) {
        
        limparConsole();

        switch (opcaoSelecionada) {
            case 0:
                int[][] tabuleiro = TabuleiroManager.criarMatrizInteira(4);
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
                System.out.println("\n\nConfigurações selecionado.\n\n");
                break;

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

    public static void menuManager() {

        String [] opcoes = {            
            "iniciar jogo     ",
            "carregar jogo    ",
            "configuracoes    ",
            "ajuda            ",
            "sobre            ",
        };

        int opcaoSelecionada = 0;
        atualizarMenu(  opcaoSelecionada, opcoes);
        
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
                        atualizarMenu(opcaoSelecionada, opcoes);
                        break;  

                    case 's':
                    case 'S':
                        opcaoSelecionada++;
                        if (opcaoSelecionada >= opcoes.length) {
                            opcaoSelecionada = 0;
                        }
                        atualizarMenu(opcaoSelecionada, opcoes);
                        break;

                    case 13: 
                        executarAcaoMenu(opcaoSelecionada);
                        atualizarMenu(  opcaoSelecionada, opcoes);
                        break;

                    case 27: 
                        return; 
                }
                
            }
            
        }
    }
}