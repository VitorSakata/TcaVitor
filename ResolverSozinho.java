import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

public class ResolverSozinho {

    static class No {
        int[][] tabuleiro;  // como o tabuleiro está
        int g;              // movimentos feitos desde o início
        int h;              // estimativa do que falta (Manhattan)
        No pai;             // de qual tabuleiro viemos
        char movimento;     // para onde o vazio andou para chegar aqui (W, S, A, D)

        No(int[][] tabuleiro, int g, No pai, char movimento){
            this.tabuleiro = tabuleiro;
            this.g = g;
            this.h = manhattan(tabuleiro);
            this.pai = pai;
            this.movimento = movimento;
        }

        int f() {
            return g + h;
        }
    }

    // As 4 direções do vazio: cima, baixo, esquerda, direita
    static int[] dLinha  = {-1, 1, 0, 0};
    static int[] dColuna = {0, 0, -1, 1};
    static char[] tecla  = {'W', 'S', 'A', 'D'};

    // Soma, para cada peça, quantas casas ela está longe do lugar certo.
    static int manhattan(int[][] tab) {
        int tamanho = tab.length;
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                int valor = tab[i][j];
                if (valor == 0) continue;                // o vazio não conta

                int linhaCerta  = (valor - 1) / tamanho;  // onde o número deveria estar
                int colunaCerta = (valor - 1) % tamanho;

                soma += Math.abs(i - linhaCerta) + Math.abs(j - colunaCerta);
            }
        }
        return soma;
    }

    // Copia a matriz (cada vizinho precisa ser um tabuleiro separado).
    static int[][] copiar(int[][] tab) {
        int[][] copia = new int[tab.length][tab.length];
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab.length; j++) {
                copia[i][j] = tab[i][j];
            }
        }
        return copia;
    }

    // Devolve as teclas que resolvem o tabuleiro
    // ou null se não encontrar solução.
    public static String resolver(int[][] inicial) {

        int tamanho = inicial.length;

        // fila de prioridade = sempre entrega o nó de menor f
        PriorityQueue<No> abertos = new PriorityQueue<>((a, b) -> a.f() - b.f());

        // tabuleiros já explorados (guardados como texto)
        HashSet<String> visitados = new HashSet<>();

        abertos.add(new No(copiar(inicial), 0, null, ' '));

        // laço principal
        while (!abertos.isEmpty()) {

            No atual = abertos.poll();                // pega o de menor f

            if (atual.h == 0) {                       //resolvido?
                return montarCaminho(atual);          
            }

            String chave = Arrays.deepToString(atual.tabuleiro);
            if (visitados.contains(chave)) {          //já vi esse? pula
                continue;
            }
            visitados.add(chave);

            // acha onde está o vazio
            int linhaVazia = 0, colunaVazia = 0;
            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < tamanho; j++) {
                    if (atual.tabuleiro[i][j] == 0) {
                        linhaVazia = i;
                        colunaVazia = j;
                    }
                }
            }

            //  gera os vizinhos
            for (int d = 0; d < 4; d++) {
                int novaLinha  = linhaVazia  + dLinha[d];
                int novaColuna = colunaVazia + dColuna[d];

                if (novaLinha >= 0 && novaLinha < tamanho &&
                    novaColuna >= 0 && novaColuna < tamanho) {

                    int[][] vizinho = copiar(atual.tabuleiro);   // não mexe no original
                    vizinho[linhaVazia][colunaVazia] = vizinho[novaLinha][novaColuna];
                    vizinho[novaLinha][novaColuna] = 0;

                    if (!visitados.contains(Arrays.deepToString(vizinho))) {
                        abertos.add(new No(vizinho, atual.g + 1, atual, tecla[d]));
                    }
                }
            }
        }

        return null; // não tem solução
    }

    // volta de pai em pai juntando os movimentos
    static String montarCaminho(No fim) {
        String caminho = "";
        No n = fim;
        while (n.pai != null) {
            caminho = n.movimento + caminho;  // coloca na frente: estamos voltando
            n = n.pai;
        }
        return caminho;
    }

    // Imprime o tabuleiro (o vazio aparece como espaço).
    static void imprimir(int[][] mat) {
        for(int i = 0; i < mat.length; i++){

            System.out.printf("\n");
            for(int j = 0; j < mat.length; j++){


                if(mat[i][j] == 0){
                    System.out.print("\t");
                }else{
                    System.out.printf("%2d\t", mat[i][j]);
                }
            }
            System.out.println("\n");
        }
    }

    // Traduz a letra para um texto que dá para usar no metodo resolverEMostrar.
    static String nomeDoMovimento(char letra) {
        if (letra == 'W') return "vazio para CIMA";
        if (letra == 'S') return "vazio para BAIXO";
        if (letra == 'A') return "vazio para a ESQUERDA";
        return "vazio para a DIREITA";
    }

    static void moverVazio(int[][] tab, char letra) {
        int tamanho = tab.length;

        int linhaVazia = 0, colunaVazia = 0;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (tab[i][j] == 0) {
                    linhaVazia = i;
                    colunaVazia = j;
                }
            }
        }

        int d = 0;
        for (int k = 0; k < 4; k++) {
            if (tecla[k] == letra) d = k;
        }

        int novaLinha  = linhaVazia  + dLinha[d];
        int novaColuna = colunaVazia + dColuna[d];

        tab[linhaVazia][colunaVazia] = tab[novaLinha][novaColuna];
        tab[novaLinha][novaColuna] = 0;
    }

    public static void resolverEMostrar(int[][] tabuleiro, int esperaMs) {

        KeboardManager.limparConsole();

        System.out.println("Tabuleiro inicial:");
        imprimir(tabuleiro);

        String caminho = resolver(tabuleiro);

        if (caminho == null) {
            System.out.println("Este tabuleiro não tem solução.");
            return;
        }

        if (caminho.length() == 0) {
            System.out.println("O tabuleiro já está resolvido!");
            return;
        }

        int[][] tab = copiar(tabuleiro);

        for (int passo = 0; passo < caminho.length(); passo++) {
            KeboardManager.limparConsole();
            SoundManager.tocarSom("Sons/mover.wav", false);
            
            char letra = caminho.charAt(passo);
            moverVazio(tab, letra);

            System.out.println("Jogada perfeita " + (passo + 1) + " de " + caminho.length() + ": " + nomeDoMovimento(letra));
            imprimir(tab);

            try {
                Thread.sleep(esperaMs);
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Resolvido em " + caminho.length() + " jogadas!");
    }

}