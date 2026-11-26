import java.util.Random;

public class TabuleiroManager {

    //cria a matriz com o tamanho escolhido pelo usuário, e preenche ela com os valores de 1 até o tamanho da matriz - 1, e coloca 0 na última posição;
    public static int [][] criarMatrizInteira(int tamanho){
        int valor = 1;
         
        int[][] mat = new int[tamanho][tamanho];

        //percorre a matriz e preenche ela com os valores de 1 até o tamanho da matriz - 1, e coloca 0 na última posição;
        for(int i = 0; i < tamanho; i++){
            for(int j = 0; j < tamanho; j++){
                mat[i][j] = valor++;
            }  
        }

        mat[tamanho - 1][tamanho - 1] = 0;

        return mat;
    }

    public static void jogar(){

        int[][] tabuleiro = criarMatrizInteira(Menu.tamanho);
        KeboardManager.movimentos = 0;
        embaralharTabuleiroInteiro(tabuleiro, Menu.embaralhamentos);
        iniciarCronometro();
        imprimirMatrizInteiro(tabuleiro);
        KeboardManager.voltarAoMenu = false;
        while (verificarVitoria(tabuleiro) == false && KeboardManager.voltarAoMenu == false) {
            tabuleiro = KeboardManager.MoverPeca(tabuleiro);
        }
    }

    public static void carregarJogo(){

        String texto = (SaveGameManager.lerDoArquivo("savegame.txt"));
        if(texto == null || texto.trim().isEmpty()){
            return;
        }
        int[][] tabuleiro = textoParaTabuleiro(texto);
        iniciarCronometro();
        inicioCronometro -= (long)(tempoSalvo * 1000);
        imprimirMatrizInteiro(tabuleiro);
        KeboardManager.voltarAoMenu = false;
        while (verificarVitoria(tabuleiro) == false && KeboardManager.voltarAoMenu == false) {
            tabuleiro = KeboardManager.MoverPeca(tabuleiro);
        }
    }

    //transforma o tabuleiro em texto para salvar no arquivo
    public static String tabuleiroParaTexto(int[][] tabuleiro){

        String texto = KeboardManager.movimentos + "\n" + segundosPassados() + "\n"; 

        for(int i = 0; i < tabuleiro.length; i++){
            for(int j = 0; j < tabuleiro[0].length; j++){
                texto += tabuleiro[i][j];
                if(j < tabuleiro[0].length - 1){
                    texto += " ";
                }
            }
            texto += "\n";
        }

        return texto;
    }

    //transforma o texto do arquivo de volta em tabuleiro
    public static int[][] textoParaTabuleiro(String texto){

        String[] linhas = texto.trim().split("\n");

        KeboardManager.movimentos = Integer.parseInt(linhas[0].trim());
        tempoSalvo = Double.parseDouble(linhas[1].trim());

        int tamanho = linhas.length - 2;
        int[][] tabuleiro = new int[tamanho][tamanho];

        for(int i = 0; i < tamanho; i++){
            String[] numeros = linhas[i + 2].trim().split(" ");

            for(int j = 0; j < tamanho; j++){
                tabuleiro[i][j] = Integer.parseInt(numeros[j]);
            }
        }

        return tabuleiro;
    }

    public static void imprimirLinhaSeparadora(int colunas, int largura, String esquerda, String meio, String direita) {
        System.out.print("\t\t" + esquerda);
        for (int i = 0; i < colunas; i++) {
            System.out.print("═".repeat(largura));
            if (i < colunas - 1) {
                System.out.print(meio);
            }else{
                System.out.print(direita);
            }
            
        }
        System.out.println();
    }
            
    //imprime o tabuleiro
    public static void imprimirMatrizInteiro(int[][] mat){
        //Guarda a quantidade de linhas e colunas da matriz, assim a função pode imprimir qualquer tamanho de matriz, não só 4x4;
        int linhas, colunas;
        linhas = mat.length;
        colunas = mat[0].length;
        int largura = 6;
        
        double tempoRecorde = Recorde.carregarRecorde();
        int movimentosRecorde = MovimentosRecorde.carregarRecorde();

        System.out.printf("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        
        Menu.mudarCor(Menu.AMARELO);
        System.out.printf("\t\t\tTabuleiro: %d x %d    \n",linhas, colunas);
        Menu.mudarCor(Menu.COR_PADRAO);

        System.out.println();
        Menu.mudarCor(Menu.COR_PADRAO);
        System.out.printf("\t\tMovimentos: %d\t Movimentos recorde: %d\n", KeboardManager.movimentos, movimentosRecorde);
        System.out.println();
        System.out.printf("\t\tTempo: %.1fs\t Tempo recorde: %.1f\n\n", segundosPassados(), tempoRecorde);
        Menu.mudarCor(Menu.COR_PADRAO); 
        System.out.printf("\n");

        largura = 6;
        //coloca a parte de cima do tabuleiro
        Menu.mudarCor(Menu.AMARELO);
        imprimirLinhaSeparadora(colunas, largura, "╔", "╦", "╗");
        Menu.mudarCor(Menu.AMARELO);

        //percorre as linhas da matriz, e para cada linha percorre as colunas, imprimindo o valor da posição atual;
        for(int i = 0; i < linhas; i++){
            Menu.mudarCor(Menu.AMARELO);
            System.out.printf("\t\t║");
            Menu.mudarCor(Menu.COR_PADRAO);
            for(int j = 0; j < colunas; j++){

                if(mat[i][j] == 0){
                    System.out.print(" ".repeat(largura));
                    
                }else{
                    System.out.printf("%" + (largura - 1) + "d ", mat[i][j]);
                    
                }
                Menu.mudarCor(Menu.AMARELO);
                System.out.print("║");
                Menu.mudarCor(Menu.COR_PADRAO);
            }
            System.out.println();

            if(i < linhas - 1){
                Menu.mudarCor(Menu.AMARELO);
                imprimirLinhaSeparadora(colunas, largura, "╠", "╬", "╣");
                Menu.mudarCor(Menu.COR_PADRAO);
            }

        }

        //coloca a parte de baixo do tabuleiro
        Menu.mudarCor(Menu.AMARELO);
        imprimirLinhaSeparadora(colunas, largura, "╚", "╩", "╝");
        Menu.mudarCor(Menu.COR_PADRAO);
        System.out.println("\n\t\tW A S D: mova as peças adjacentes ao vazio ");
        System.out.println("\t\tESC:  Sair do jogo");
    }

        //usa a matriz ja feita e embaralha ela
    public static void embaralharTabuleiroInteiro(int[][] tabuleiro, int movimentos){
        Random random = new Random();
        int tamanho = tabuleiro.length;

        //Essa função percorre a matriz e encontra a posição do vazia;
        //essas variáveis vão guardar a posição da peça vazia, começando com -1 pq esse valor nao existe como posição na matriz;
        int linhasVazia = -1;
        int colunasVazia = -1;

        //O primeiro for é das linhas e o segundo das colunas, assim conseguimos percorrer toda a matriz;
        for(int i = 0; i < tabuleiro.length; i++){
            for(int j = 0; j < tabuleiro[0].length; j++){
                //O if verifica se a posição é vazia, se for, ele guarda a posição da peça vazia nas variáveis(i e j), caso o contrario o if;
                if(tabuleiro[i][j] == 0){
                    linhasVazia = i;
                    colunasVazia = j;
                }
            }
        }

        //Esses dois vetores guardam as direções que a peça vazia pode se mover;
        //cada posição do vetor representa uma direção, [0] = cima, [1] = baixo, [2] = esquerda, [3] = direita;
        int direçaoLinha[] = {-1, 1, 0, 0};
        int direçaoColuna[] = {0, 0, -1, 1};
        
        //Esse faz a peça vazia se mover, ele vai fazer isso a quantidade de movimentos que o usuário escolheu; 
        for(int i = 0; i < movimentos; i++){
            int direçao = random.nextInt(4);

            //Calcula onde o vizinho da peça vazia está, somando a variação da direção com a posição da atual da peça vazia;
            int novaLinhaVazia = linhasVazia + direçaoLinha[direçao];
            int novaColunaVazia = colunasVazia + direçaoColuna[direçao];

            //Verifica se a posição do vizinho da peça vazia é válida, ou seja, se ele está dentro da matriz;
            if(novaLinhaVazia >= 0 && novaLinhaVazia < tamanho && novaColunaVazia >= 0 && novaColunaVazia < tamanho){
                //Troca. A primeira linha leva o valor da peça vizinha para a posição da peça vazia, e a segunda linha coloca 0 na casa do vizinho;
                tabuleiro[linhasVazia][colunasVazia] = tabuleiro[novaLinhaVazia][novaColunaVazia];
                tabuleiro[novaLinhaVazia][novaColunaVazia] = 0;

                //Atualiza a posição da peça vazia para a nova posição;
                linhasVazia = novaLinhaVazia;
                colunasVazia = novaColunaVazia;
            }
        }
    }

        public static boolean verificarVitoria(int[][] tabuleiro){
            int tamanho = tabuleiro.length;
            int valorEsperado = 1;

            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < tamanho; j++) {

                    if(i == tamanho -1 && j == tamanho -1){
                        return tabuleiro[i][j] == 0;
                    }

                    if(tabuleiro[i][j] != valorEsperado){
                        return false;
                    }
                    
                    valorEsperado++;
                }
            }
            return true;
        
        }

    // Disponibilizado pelo professor
    // Guarda o momento em que o cronômetro começou (em milissegundos)
    static long inicioCronometro = 0;

    // tempo lido do savegame.txt (em segundos)
    static double tempoSalvo = 0;

    // Começa (ou recomeça) a contar o tempo do zero.
    public static void iniciarCronometro() {
        inicioCronometro = System.currentTimeMillis();
    }

    // Retorna quantos segundos passaram desde o iniciarCronometro().
    // O valor tem casas decimais: 1.5 quer dizer um segundo e meio.
    public static double segundosPassados() {
        long agora = System.currentTimeMillis();
        return (agora - inicioCronometro) / 1000.0;
    }

}