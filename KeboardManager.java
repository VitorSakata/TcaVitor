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

    public static int[][] MoverPeca(int[][] tabuleiro) {

        

        String Nome_Arquivo = "savegame.txt";

        int linhaVazia = 0;
        int colunaVazia = 0;

        for(int i = 0; i < tabuleiro.length; i++){
            for(int j = 0; j < tabuleiro[0].length; j++){
                if(tabuleiro[i][j] == 0){
                    linhaVazia = i;
                    colunaVazia = j;
                }
            }
        }

        int moverLinha = linhaVazia;
        int moverColuna = colunaVazia;

        char tecla = (char)obtemTeclaPressionada();
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

        System.out.println("Deseja salvar seu progresso? (S/Esc)");

        String dadosParaSalvar = (TabuleiroManager.tabuleiroParaTexto(tabuleiro));

        switch((char)obtemTeclaPressionada()){
            case 's':
            case 'S':

            SaveGameManager.escreverNoArquivo(Nome_Arquivo, dadosParaSalvar);
        }

        limparConsole();
        Menu.mudarCor(Menu.AMARELO);
        System.out.println(AsciiArt.msgAdeus);
        Menu.mudarCor(Menu.COR_PADRAO);
        SoundManager.tocarSom("Sons/esc.wav", true);
        voltarAoMenu = true;   
        return tabuleiro;
        }

        if(moverLinha >= 0 && moverLinha < tabuleiro.length &&
            moverColuna >= 0 && moverColuna < tabuleiro[0].length){

            tabuleiro[linhaVazia][colunaVazia] = tabuleiro[moverLinha][moverColuna];
            tabuleiro[moverLinha][moverColuna] = 0;
            movimentos++;
        }

        if(TabuleiroManager.verificarVitoria(tabuleiro) == true){
            
            double tempo = TabuleiroManager.segundosPassados();
            boolean recordeTempo = Recorde.salvarRecorde(tempo);

            boolean recordeMovimentos = MovimentosRecorde.salvarRecorde(movimentos);
            
            if(recordeTempo){
                Menu.mudarCor(Menu.VERDE);
                System.out.println("\n\n\n\n\n\n\t\tNovo recorde! Tempo: " + tempo + " segundos\n");
                Menu.mudarCor(Menu.COR_PADRAO);
            } else {
                System.out.println("\n\n\n\n\n\n\t\tTempo: " + tempo + " segundos\n");
            }

            if(recordeMovimentos){
                Menu.mudarCor(Menu.VERDE);
                System.out.println("\t\tNovo recorde! Movimentos: " + movimentos);
                Menu.mudarCor(Menu.COR_PADRAO);
            } else {
                System.out.println("\t\tMovimentos:" + movimentos);
            }

            if(Menu.embaralhamentos == 300 && Menu.tamanho == 5){
                System.out.println(AsciiArt.msgVitoria2);
                SoundManager.tocarSom("Sons/vitoria.wav", true);
            }else{
                Menu.mudarCor(Menu.AMARELO);
                System.out.println(AsciiArt.msgVitoria1);
                Menu.mudarCor(Menu.COR_PADRAO);
                SoundManager.tocarSom("Sons/vitoria.wav", true);
            }

            
        }

        TabuleiroManager.imprimirMatrizInteiro(tabuleiro);
        
        return tabuleiro;
    }
}
