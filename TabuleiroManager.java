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

        System.out.printf("\n\t\t\t\t\t\t\t\t\tTabuleiro: %d x %d\n",linhas, colunas);

        System.out.printf("\n");

        for(int i = 0; i < linhas; i++){
            System.out.printf("\t\t\t\t\t\t\t\t\t");
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
    public static int embaralharTabuleiroInteiro(int[][] tabuleiro){
        return 0;
    }

    
}
