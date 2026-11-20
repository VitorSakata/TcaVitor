public class Recorde {
    public static final String ARQUIVO = "recorde.txt";

    public static double carregarRecorde(){
        String texto = SaveGameManager.lerDoArquivo(ARQUIVO);
        
        if(texto == null || texto.trim().isEmpty()) return -1;

        return Double.parseDouble(texto.trim());
    }

    public static boolean salvarRecorde(double segundos){
        double recordeAtual = carregarRecorde();
        if(recordeAtual == -1 || segundos < recordeAtual){
           SaveGameManager.escreverNoArquivo(ARQUIVO, String.valueOf(segundos));
           return true;
        }
        return false;
    }
}
