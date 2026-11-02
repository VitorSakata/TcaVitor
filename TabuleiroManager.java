import java.util.Random;

public class TabuleiroManager {
    
    public static int [][] criarMatrizInteira(int tamanho){
        int valor = 1;

        int[][] mat = new int[tamanho][tamanho];

        for(int i = 0; i < tamanho; i++){
            for(int j = 0; j < tamanho; j++){
                mat[i][j] = valor++;
            }
        }

        mat[tamanho - 1][tamanho - 1] = 0;

        return mat;
    }

    //imprime o tabuleiro
    public static void imprimirMatrizInteiro(int[][] mat){
        int linhas, colunas;
        linhas = mat.length;
        colunas = mat[0].length;

        System.out.printf("\n\n\n\n\n\n\n\n\n\n\n\n");

        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t\t\tTabuleiro: %d x %d\n",linhas, colunas);

        System.out.printf("\n");

        for(int i = 0; i < linhas; i++){
            System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            for(int j = 0; j < colunas; j++){
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


}
