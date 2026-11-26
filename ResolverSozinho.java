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
<<<<<<< HEAD
                if (valor == 0) continue;                // o vazio não conta e é pulado

                int linhaCerta  = (valor - 1) / tamanho;  // onde o número deveria estar
                int colunaCerta = (valor - 1) % tamanho;  // onde o número deveria estar

                soma += Math.abs(i - linhaCerta) + Math.abs(j - colunaCerta); //Soma a distância (linhas + colunas) entre onde a peça está e onde deveria estar.
            }
        }
        return soma;//devolve a soma, se for 0, todas as peça estao no lugar certo e o tabuleiro está resolvido.
    }

    // Copia a matriz (cada vizinho precisa ser um tabuleiro separado).
    static int[][] copiar(int[][] tab) {
        int[][] copia = new int[tab.length][tab.length];//cria uma matriz nova vazia com o mesmo tamanho da matriz original
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab.length; j++) {
                copia[i][j] = tab[i][j];//copia o valor da matriz original para a nova matriz
            }
        }
        return copia;
    }

    // Devolve as teclas que resolvem o tabuleiro
    // ou null se não encontrar solução.
    public static String resolver(int[][] inicial) {

        int tamanho = inicial.length;

        // fila de prioridade = sempre entrega o nó de menor f(g + h)
        PriorityQueue<No> abertos = new PriorityQueue<>((a, b) -> a.f() - b.f());

        // tabuleiros já explorados (guardados como texto)
        HashSet<String> visitados = new HashSet<>();

        //coloca o no inicial na fila de abertos, com g=0, sem pai e sem movimento
        abertos.add(new No(copiar(inicial), 0, null, ' '));

        // laço principal
        //Repete enquanto houver nós para explorar. Se a fila esvaziar, não há solução.
        while (!abertos.isEmpty()) {

            No atual = abertos.poll();                // pega o no de menor f

            if (atual.h == 0) {                       //resolvido? Se estiver, monta o caminho de volta e devolve
                return montarCaminho(atual);          
            }

            String chave = Arrays.deepToString(atual.tabuleiro);//converte o tabuleiro atual em texto, serve de identidade do estado
            if (visitados.contains(chave)) {          //já viu esse estado? Se sim, pula. Se não, marca como visitado e continua.
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
                //calcula para onde o vazio se moveria
                int novaLinha  = linhaVazia  + dLinha[d];
                int novaColuna = colunaVazia + dColuna[d];

                //verifica se a nova posição está dentro do tabuleiro. Se sim, cria o vizinho e adiciona na fila de abertos.
                if (novaLinha >= 0 && novaLinha < tamanho &&
                    novaColuna >= 0 && novaColuna < tamanho) {

                    int[][] vizinho = copiar(atual.tabuleiro);   //cria uma copia e não mexe no original
                    vizinho[linhaVazia][colunaVazia] = vizinho[novaLinha][novaColuna];//move a peça para o lugar do vazio
                    vizinho[novaLinha][novaColuna] = 0;//coloca o vazio no lugar da peça

                    //se o vizinho n ainfa foi visitado entra na fila g = atual.g + 1 (mais um movimento),pai = atual, movimento = tecla[d] (a tecla usada).
                    if (!visitados.contains(Arrays.deepToString(vizinho))) {
                        abertos.add(new No(vizinho, atual.g + 1, atual, tecla[d]));
                    }
                }
            }
        }

        //Só chega aqui se a fila esvaziou sem achar solução. Isso acontece em tabuleiros de paridade impossível.
        return null; // não tem solução
    }

    // volta de pai em pai juntando os movimentos
    //a cada passo, coloca a tecla na frente da string. Como estamos andando de trás para frente, isso deixa as teclas na ordem certa.
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
    // //Pega as dimensões e define a largura de cada célula (6 caracteres).
    static void imprimir(int[][] mat) {

        int linhas, colunas;
        linhas = mat.length;
        colunas = mat[0].length;
        int largura = 6;
        

        

        Menu.mudarCor(Menu.AMARELO);
        TabuleiroManager.imprimirLinhaSeparadora(colunas, largura, "╔", "╦", "╗");
        Menu.mudarCor(Menu.AMARELO);

        for(int i = 0; i < linhas; i++){
            Menu.mudarCor(Menu.AMARELO);
            System.out.printf("\t\t║");
            Menu.mudarCor(Menu.COR_PADRAO);

            // Para cada célula:
            for(int j = 0; j < colunas; j++){

                if(mat[i][j] == 0){// se for o vazio, imprime 6 espaços;
                    System.out.print(" ".repeat(largura));
                    
                }else{// senão, imprime o número alinhado à direita em 5 colunas, mais 1 espaço (total 6). O formato vira "%5d "
                    System.out.printf("%" + (largura - 1) + "d ", mat[i][j]);
                    
                }
                Menu.mudarCor(Menu.AMARELO);
                System.out.print("║");
                Menu.mudarCor(Menu.COR_PADRAO);
            }
            System.out.println();

            if(i < linhas - 1){
                Menu.mudarCor(Menu.AMARELO);
                TabuleiroManager.imprimirLinhaSeparadora(colunas, largura, "╠", "╬", "╣");
                Menu.mudarCor(Menu.COR_PADRAO);
            }

        }

        Menu.mudarCor(Menu.AMARELO);
        TabuleiroManager.imprimirLinhaSeparadora(colunas, largura, "╚", "╩", "╝");
        Menu.mudarCor(Menu.COR_PADRAO);
    }

    // Traduz a letra para um texto que dá para usar no metodo resolverEMostrar.
    // Se não for W, S nem A, assume D.
    static String nomeDoMovimento(char letra) {
        if (letra == 'W') return "vazio para CIMA";
        if (letra == 'S') return "vazio para BAIXO";
        if (letra == 'A') return "vazio para a ESQUERDA";
        return "vazio para a DIREITA";
    }

    static void moverVazio(int[][] tab, char letra) {
=======
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
>>>>>>> 28d71fef654abb44afc60b0e7cea5ead56d2c2d2
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

<<<<<<< HEAD
        int d = 0;//o índice da direção escolhida. Começa em 0
        for (int k = 0; k < 4; k++) {
            if (tecla[k] == letra) d = k;
        }

=======
>>>>>>> 28d71fef654abb44afc60b0e7cea5ead56d2c2d2
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
