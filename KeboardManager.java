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

    //le a tecla precionada, move a pessa da posiçao correspondente e devolve o tabuleiro atualizado
    public static int[][] MoverPeca(int[][] tabuleiro) {

        

        String Nome_Arquivo = "savegame.txt";

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

        //posicao da peça q vai mover , começa com o valor do vazioe o switch move 1 casa conforme a tecla
        int moverLinha = linhaVazia;
        int moverColuna = colunaVazia;

        char tecla = (char)obtemTeclaPressionada();//chama o metodo q devolve om codigo(int) da tecla precionada, e o char converte esse codigo
        limparConsole();
        SoundManager.tocarSom("Sons/mover.wav", false);

        switch (tecla) {
    case 'w':
    case 'W':
        moverLinha++;//pega a peça abaixo do vazio
        break;
    case 's':
    case 'S':
        moverLinha--;//pega a peça acima do vazio
        break;
    case 'a':
    case 'A':
        moverColuna++;//pega a peça à direita do vazio
        break;
    case 'd':
    case 'D':
        moverColuna--;//pega a peça da esquerda
        break;
    case 27:

        System.out.println("Deseja salvar seu progresso? (S/Esc)");

        String dadosParaSalvar = (TabuleiroManager.tabuleiroParaTexto(tabuleiro));//converte a matriz do tabuleiro em String para o arquivo receber
        

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
        System.exit(0);
        }

        //valida a posiçao de troca, so é realizada se a posiçao do alvo estiver dentro do tabuleiro
        if(moverLinha >= 0 && moverLinha < tabuleiro.length &&
            moverColuna >= 0 && moverColuna < tabuleiro[0].length){

            tabuleiro[linhaVazia][colunaVazia] = tabuleiro[moverLinha][moverColuna];//posião vazia recebe o valor da peça q vai mover//as duas casas ficam com o mesmo valor temporariamente
            tabuleiro[moverLinha][moverColuna] = 0;//corrige o valor da peça q vai mover, agora ela fica com o valor 0, q é a posição vazia
            movimentos++;
        } 

        //verifica se o tabuleiro esta resolvido
        if(TabuleiroManager.verificarVitoria(tabuleiro)){
            

            double tempo = TabuleiroManager.segundosPassados();//Pede ao TabuleiroManager quantos segundos se passaram desde o início da partida
            boolean recordeTempo = Recorde.salvarRecorde(tempo);//Entrega o tempo para a classe Recorde. Ela compara com o melhor tempo guardado e, se este for melhor, salva. Devolve true se bateu o recorde e false se não.

            boolean recordeMovimentos = MovimentosRecorde.salvarRecorde(movimentos);//entrega os movimentos para a classe MovimentosRecorde . Ela compara com o melhor tempo guardado e, se este for melhor, salva. Devolve true se bateu o recorde e false se não.
            
            if(recordeTempo){
                Menu.mudarCor(Menu.VERDE);
                System.out.println("\n\n\n\\n\n\n\t\tNovo recorde! Tempo: " + tempo + " segundos\n");
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

            //escolhe a mensagem de vitoria conforme o tamanho do tabuleiro e a quantidade de embaralhamentos
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
