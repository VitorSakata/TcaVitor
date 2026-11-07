import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveGameManager {
    
    /**
     * MÉTODO 1: ESCREVER (GRAVAR) no arquivo
     * Escreve uma string no arquivo, substituindo o conteúdo anterior.     
     */
    public static void escreverNoArquivo(String nomeArquivo, String conteudo) {
        
        // tentando abrir o arquivo para escrita
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            
            // Grava o conteúdo no arquivo
            writer.write(conteudo);
            
            System.out.println("Sucesso: Dados salvos em " + nomeArquivo);
            
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo '" + nomeArquivo + "': " + e.getMessage());
        }
    }

    /**
     * MÉTODO 2: LER (CARREGAR) o arquivo
     * Lê todo o conteúdo do arquivo e o retorna como uma única string.
     */
    public static String lerDoArquivo(String nomeArquivo) {
        StringBuilder conteudo = new StringBuilder();
        
        // tentando abrir o arquivo para leitura
        try (FileReader reader = new FileReader(nomeArquivo);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            String linha;
            
            // Lê cada linha do arquivo até chegar ao fim (null)
            while (true) {
                // Tenta ler a próxima linha
                linha = bufferedReader.readLine();
                
                // Testa a condição de PARADA
                if (linha == null) {
                    // Se for nulo, chegamos ao final do arquivo, então saímos do loop.
                    break;
                }
                
                // Se a linha não for nula, processamos
                conteudo.append(linha).append("\n");
            }
            
            System.out.println("Sucesso: Dados lidos de " + nomeArquivo);
            return conteudo.toString();

        } catch (IOException e) {
            // Se o arquivo não existir, retorna a mensagem de erro e uma string vazia/padrão
            System.err.println("Erro ao ler o arquivo '" + nomeArquivo + "' (ou arquivo não existe): " + e.getMessage());
            return null;
        }
    }
}