public class MovimentosRecorde {
    public static final String ARQUIVO = "MovimentosRecorde.txt";

    public static int carregarRecorde(){
        String textoMov = SaveGameManager.lerDoArquivo(ARQUIVO);
        
        if(textoMov == null || textoMov.trim().isEmpty()) return -1;

        return Integer.parseInt(textoMov.trim());
    }

    public static boolean salvarRecorde(int movimentos){
        int movimentosRecorde = carregarRecorde();
        if(movimentosRecorde == -1 || movimentos < movimentosRecorde){
           SaveGameManager.escreverNoArquivo(ARQUIVO, String.valueOf(movimentos));
           return true;
        }
        return false;
    }
}
