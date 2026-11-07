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

    //transforma o tabuleiro em texto para salvar no arquivo
    public static String tabuleiroParaTexto(int[][] tabuleiro){

        String texto = "";

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

        int tamanho = linhas.length;
        int[][] tabuleiro = new int[tamanho][tamanho];

        for(int i = 0; i < tamanho; i++){
            String[] numeros = linhas[i].trim().split(" ");

            for(int j = 0; j < tamanho; j++){
                tabuleiro[i][j] = Integer.parseInt(numeros[j]);
            }
        }

        return tabuleiro;
    }

    //imprime o tabuleiro
    public static void imprimirMatrizInteiro(int[][] mat){
        //Guarda a quantidade de linhas e colunas da matriz, assim a função pode imprimir qualquer tamanho de matriz, não só 4x4;
        int linhas, colunas;
        linhas = mat.length;
        colunas = mat[0].length;

        System.out.printf("\n\n\n\n\n\n\n\n\n\n\n\n");

        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t\t\tTabuleiro: %d x %d\n",linhas, colunas);

        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t\t\tMovimentos: %d\n", movimentos);

        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t\t\tTempo: %.1f segundos\n", TabuleiroManager.segundosPassados());

        System.out.printf("\n");

        //percorre as linhas da matriz, e para cada linha percorre as colunas, imprimindo o valor da posição atual;
        for(int i = 0; i < linhas; i++){
            System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            for(int j = 0; j < colunas; j++){

                //verifica se a posição atual é a peça vazia, se for, imprime um espaço em branco, caso contrario imprime o valor da posição;
                if(mat[i][j] == 0){
                    System.out.print("\t");
                }else{
                System.out.printf("%2d\t", mat[i][j]);
                }
            }
            System.out.println("\n");
        }
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

    public static int movimentos = 0;


    // ===================================================================
    // Disponibilizado pelo professor
    // Guarda o momento em que o cronômetro começou (em milissegundos)
    static long inicioCronometro = 0;

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
