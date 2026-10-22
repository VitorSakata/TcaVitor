public class Matrizes {

    public static void imprimirVetorDouble(double[] v){
        System.out.println();
        if(v == null){// o vetor é nulo?
            System.out.println("Vetor nulo!");
            return;
        }

        for(int i=0; i< v.length; i++){
            System.out.printf("[%3d] = %f\n", i, v[i]);
        }
    }


    public static int[][] criarMatrizInteira(int linhas, int colunas, int valor){
        int[][] mat = new int[linhas][colunas];
        for(int i=0; i< linhas; i++){
            for(int j=0; j < colunas; j++){
                mat[i][j] = valor;
            }
        }
        return mat;
    }

    public static void imprimirMatrizInteiro(int[][] mat){
        int linhas, colunas;
        linhas  = mat.length;
        colunas = mat[0].length;
        System.out.printf("\nMatrix: %d x %d\n",linhas, colunas);
        // cabecalho
        System.out.printf("Linhas/Colunas\t");
        for(int j=0; j < colunas; j++){
            System.out.printf("%d\t",j);
        }
        System.out.println();
        for(int i=0; i< linhas; i++){
            System.out.printf("Linha %d:\t",i);
            for(int j=0; j < colunas; j++){
                System.out.printf("%d\t",mat[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public static double obterMediaMatriz(int[][] mat){
        int linhas, colunas;
        double soma = 0;
        linhas  = mat.length;
        colunas = mat[0].length;
        for(int i=0; i< linhas; i++){
            for(int j=0; j < colunas; j++){
                soma = soma + mat[i][j];
            }
        }
        return soma/(linhas * colunas);
    }
    public static double[] obterMediaColunas(int[][] mat){
        int linhas, colunas;
        linhas = mat.length;
        colunas = mat[0].length;
        double[] vet;
        vet = new double[colunas];

        for (int j = 0; j < colunas; j++) {
            vet[j] = 0;
            for (int i = 0; i < linhas; i++) {        
                vet[j] = vet[j] + mat[i][j];
            }
            vet[j] = vet[j] / linhas;
        }
     
        return vet;
    }
    public static double[] obterMediaLinhas(int[][] mat){
        int linhas, colunas;
        linhas = mat.length;
        colunas = mat[0].length;
        double[] vet;
        vet = new double[linhas];

            for (int i = 0; i < linhas; i++) {
              vet[i] = 0;
               for (int j = 0; j < colunas; j++) {     
                vet[i] = vet[i] + mat[i][j];
            }
            vet[i] = vet[i] / colunas;
        }
     
        return vet;
    }
    

    public static void main(String[] args) {
        int [][] matriz;
        //  Linha (i)- Coluna (j)
        matriz = new int[3][4];
        matriz[2][1] = 42;
        //System.out.println(matriz[20][1]);

        int[][] m;
        m = criarMatrizInteira(4,6, 42);
        imprimirMatrizInteiro(m);

        

    }
}