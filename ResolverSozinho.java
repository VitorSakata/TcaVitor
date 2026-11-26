public class ResolverSozinho {

    public static int[] dLinha  = {-1, 1, 0, 0};
    public static int[] dColuna = {0, 0, -1, 1};
    public static int[] oposto  = {1, 0, 3, 2}; 
    public static String[] nomes = {"vazio para CIMA", "vazio para BAIXO", "vazio para a ESQUERDA", "vazio para a DIREITA"};

    public static int[] jogadas = new int[500];
    public static int totalJogadas = 0;

    public static int manhattan(int[][] tab) {
        int tamanho = tab.length;
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                int valor = tab[i][j];
                if (valor != 0) {
                    int linhaCerta  = (valor - 1) / tamanho;
                    int colunaCerta = (valor - 1) % tamanho;
                    soma = soma + Math.abs(i - linhaCerta) + Math.abs(j - colunaCerta);
                }
            }
        }
        return soma;
    }

    public static int[][] mover(int[][] tab, int d) {
        int tamanho = tab.length;

        int linhaVazia = 0;
        int colunaVazia = 0;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (tab[i][j] == 0) {
                    linhaVazia = i;
                    colunaVazia = j;
                }
            }
        }

        int novaLinha  = linhaVazia  + dLinha[d];
        int novaColuna = colunaVazia + dColuna[d];

        if (novaLinha < 0 || novaLinha >= tamanho || novaColuna < 0 || novaColuna >= tamanho) {
            return null;
        }

        int[][] novo = new int[tamanho][tamanho];
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                novo[i][j] = tab[i][j];
            }
        }

        novo[linhaVazia][colunaVazia] = novo[novaLinha][novaColuna];
        novo[novaLinha][novaColuna] = 0;
        return novo;
    }

    public static boolean buscar(int[][] tab, int feitas, int limite, int ultima) {

        int falta = manhattan(tab);

        if (falta == 0) {
            totalJogadas = feitas;
            return true;
        }
        if (feitas + falta > limite) {
            return false;
        }
        for (int d = 0; d < 4; d++) {

            if (ultima != -1 && d == oposto[ultima]) {
                continue;
            }

            int[][] novo = mover(tab, d);

            if (novo != null) {
                jogadas[feitas] = d;  

                if (buscar(novo, feitas + 1, limite, d)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void resolver(int[][] tab) {
        int limite = manhattan(tab);
        while (buscar(tab, 0, limite, -1) == false) {
            limite++;
        }
    }

    public static void imprimir(int[][] mat) {
        int tamanho = mat.length;
        int largura = 6;

        Menu.mudarCor(Menu.AMARELO);
        TabuleiroManager.imprimirLinhaSeparadora(tamanho, largura, "╔", "╦", "╗");

        for (int i = 0; i < tamanho; i++) {
            System.out.print("\t\t║");
            for (int j = 0; j < tamanho; j++) {
                Menu.mudarCor(Menu.COR_PADRAO);
                if (mat[i][j] == 0) {
                    System.out.print(" ".repeat(largura));
                } else {
                    System.out.printf("%" + (largura - 1) + "d ", mat[i][j]);
                }
                Menu.mudarCor(Menu.AMARELO);
                System.out.print("║");
            }
            System.out.println();

            if (i < tamanho - 1) {
                TabuleiroManager.imprimirLinhaSeparadora(tamanho, largura, "╠", "╬", "╣");
            }
        }

        TabuleiroManager.imprimirLinhaSeparadora(tamanho, largura, "╚", "╩", "╝");
        Menu.mudarCor(Menu.COR_PADRAO);
    }

    public static void resolverEMostrar(int[][] tabuleiro, int esperaMs) {

        KeboardManager.limparConsole();
        System.out.printf("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("\t\tTabuleiro inicial:\n");
        imprimir(tabuleiro);

        resolver(tabuleiro);

        if (totalJogadas == 0) {
            System.out.println("O tabuleiro já está resolvido!");
            return;
        }

        int[][] tab = tabuleiro;

        for (int i = 0; i < totalJogadas; i++) {
            int d = jogadas[i];
            tab = mover(tab, d);

            KeboardManager.limparConsole();
            SoundManager.tocarSom("Sons/mover.wav", false);

            System.out.printf("\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("\t\tJogada perfeita " + (i + 1) + " de " + totalJogadas + ": " + nomes[d] + "\n");
            imprimir(tab);

            try {
                Thread.sleep(esperaMs);
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Resolvido em " + totalJogadas + " jogadas!");
    }
}
