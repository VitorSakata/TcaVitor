public class Recordes {
    public static final String ARQUIVOMOVIMENTO = "MovimentosRecorde.txt";

    public static int carregarRecordeMovimentos(){
        String textoMov = SaveGameManager.lerDoArquivo(ARQUIVOMOVIMENTO);//le o conteudo do arquivo (algo como "42") e guarda em textoMov
        
        if(textoMov == null || textoMov.trim().isEmpty()) return 0;

        return Integer.parseInt(textoMov.trim());//Remove espaços e quebras de linha das pontas (trim) e converte o texto em int (parseInt)
    }

    public static boolean salvarRecorde(int movimentos){
        int movimentosRecorde = carregarRecordeMovimentos();//pega o recorde atual e compara

        if(movimentosRecorde == 0 || movimentos < movimentosRecorde){
           SaveGameManager.escreverNoArquivo(ARQUIVOMOVIMENTO, String.valueOf(movimentos));//String.valueOf(movimentos) converte o int em texto (42 vira "42")
        
           return true;
        }
        return false;
    }

    public static final String ARQUIVOTEMPO = "recorde.txt";

    public static double carregarRecordeTempo(){
        String texto = SaveGameManager.lerDoArquivo(ARQUIVOTEMPO);
        
        if(texto == null || texto.trim().isEmpty()) return 0;

        return Double.parseDouble(texto.trim());
    }

    public static boolean salvarRecorde(double segundos){
        double recordeAtual = carregarRecordeTempo();
        if(recordeAtual == 0 || segundos < recordeAtual){
           SaveGameManager.escreverNoArquivo(ARQUIVOTEMPO, String.valueOf(segundos));
           return true;
        }
        return false;
    }
}
