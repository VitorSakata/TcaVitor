import com.sun.jna.Library;
import com.sun.jna.Native;

public class KeboardManager{

    public interface Crt extends Library {
        Crt INSTANCE = Native.load("msvcrt", Crt.class);
        int _kbhit();
        int _getch();
    }

    static boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
    static boolean tecladoPreparado = false;

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
            // se não conseguir, o teclado só vai funcionar com ENTER
        }
    }

    public static void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static int movimentos = 0;

    public static boolean voltarAoMenu = false;

    //fução de mover a peça no tabuleiro
    public static int[][] MoverPeca(int[][] tabuleiro) {

        int linhaVazia = 0;
        int colunaVazia = 0;

        //percorre o tabuleiro para encontrar a posição vazia
        for(int i = 0; i < tabuleiro.length; i++){//for q percorre as linhas
            for(int j = 0; j < tabuleiro[0].length; j++){//for q percorre as colunas
                if(tabuleiro[i][j] == 0){
                    linhaVazia = i;
                    colunaVazia = j;
                }
            }
        }

        int linhaAtual = linhaVazia;
        int colunaAtual = colunaVazia;

        //le a tecla precionada, move a pessa da posiçao correspondente e devolve o tabuleiro atualizado
        char tecla = (char)obtemTeclaPressionada();//chama o metodo q devolve om codigo(int) da tecla precionada, e o char converte esse codigo
        limparConsole();
        SoundManager.tocarSom("Sons/mover.wav", false);

        switch (tecla) {
            case 'w':
            case 'W':
                linhaAtual++;
                break;
            case 's':
            case 'S':
                linhaAtual--;
                break;
            case 'a':
            case 'A':
                colunaAtual++;
                break;
            case 'd':
            case 'D':
                colunaAtual--;
                break;
            case 27:
    
            TabuleiroManager.SalvarJogo(tabuleiro);
        }

        if((linhaAtual != linhaVazia || colunaAtual != colunaVazia) && 
            linhaAtual >= 0 && linhaAtual < tabuleiro.length &&
            colunaAtual >= 0 && colunaAtual < tabuleiro[0].length){

            tabuleiro[linhaVazia][colunaVazia] = tabuleiro[linhaAtual][colunaAtual];
            tabuleiro[linhaAtual][colunaAtual] = 0;
            movimentos++;
        } 

        TabuleiroManager.VerificarRecorde(tabuleiro);

        TabuleiroManager.imprimirMatrizInteiro(tabuleiro);
        
        return tabuleiro;
    }
}

