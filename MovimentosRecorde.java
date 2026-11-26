public class MovimentosRecorde {
    public static final String ARQUIVO = "MovimentosRecorde.txt";

    public static int carregarRecorde(){
        String textoMov = SaveGameManager.lerDoArquivo(ARQUIVO);//le o conteudo do arquivo (algo como "42") e guarda em textoMov
        
        if(textoMov == null || textoMov.trim().isEmpty()) return 0;

        return Integer.parseInt(textoMov.trim());//Remove espaços e quebras de linha das pontas (trim) e converte o texto em int (parseInt)
    }

    public static boolean salvarRecorde(int movimentos){
        int movimentosRecorde = carregarRecorde();//pega o recorde atual e compara

        if(movimentosRecorde == 0 || movimentos < movimentosRecorde){
           SaveGameManager.escreverNoArquivo(ARQUIVO, String.valueOf(movimentos));//String.valueOf(movimentos) converte o int em texto (42 vira "42")
        
           return true;
        }
        return false;
    }
}
