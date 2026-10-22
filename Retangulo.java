import java.util.Scanner;

public class Retangulo {

    public final static Scanner TECLADO = new Scanner(System.in);

    public static int LerValorInteiro(){
        return  TECLADO.nextInt();
    }

    public static void imprimirRetanfulo(int linhas, int colunas){

        System.out.println("\n\n\n");
        for(int linha = 0; linha < linhas; linha++){
            for(int coluna = 0; coluna < colunas; coluna++){
                System.out.printf("%d ", (colunas * linha) + coluna);
                
            }
            System.out.println();
        }
        System.out.println("\n\n\n");
    }

    
    public static void main(String[] args) {   
        
        imprimirRetanfulo(4, 4);
    }
}