package backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FuncoesArquivosTest {

    @TempDir
    Path tempDir;

    @Test
    void testCriarArquivo() {
        Path arquivoPath = tempDir.resolve("teste_criacao.txt");
        String caminhoAbsoluto = arquivoPath.toAbsolutePath().toString();

        FuncoesArquivos.criarArquivo(caminhoAbsoluto);
        assertTrue(Files.exists(arquivoPath));

        FuncoesArquivos.criarArquivo(caminhoAbsoluto);
        assertTrue(Files.exists(arquivoPath));
    }

    @Test
    void testEscreverELerArquivo() throws IOException {
        Path arquivoPath = tempDir.resolve("teste_escrita.txt");
        String caminhoStr = arquivoPath.toString();

        String conteudo = "Linha de teste";
        FuncoesArquivos.escreverArquivo(caminhoStr, conteudo);

        assertTrue(Files.exists(arquivoPath));
        assertEquals(conteudo, Files.readString(arquivoPath));

        assertDoesNotThrow(() -> FuncoesArquivos.lerArquivo(caminhoStr));
    }

    @Test
    void testAppendLinhaArquivo() throws IOException {
        Path arquivoPath = tempDir.resolve("teste_append.txt");
        String caminhoStr = arquivoPath.toString();

        FuncoesArquivos.criarArquivo(caminhoStr);
        FuncoesArquivos.appendLinhaArquivo(caminhoStr, "Linha 1");
        FuncoesArquivos.appendLinhaArquivo(caminhoStr, "Linha 2");

        List<String> linhas = Files.readAllLines(arquivoPath);
        assertEquals(2, linhas.size());
        assertEquals("Linha 1", linhas.get(0));
        assertEquals("Linha 2", linhas.get(1));
    }

    @Test
    void testListaLinhasComFile() throws IOException {
        Path arquivoPath = tempDir.resolve("teste_lista_file.txt");
        Files.write(arquivoPath, Arrays.asList("A", "B", "C"));

        List<String> resultado = FuncoesArquivos.listaLinhas(arquivoPath.toFile());

        assertEquals(3, resultado.size());
        assertEquals("A", resultado.get(0));
        assertEquals("C", resultado.get(2));
    }

    @Test
    void testObterListaLinhasComString() throws IOException {
        Path arquivoPath = tempDir.resolve("teste_lista_string.txt");
        Files.write(arquivoPath, Arrays.asList("X", "Y"));

        List<String> resultado = FuncoesArquivos.obterListaLinhas(arquivoPath.toString());

        assertEquals(2, resultado.size());
        assertEquals("X", resultado.get(0));
    }

    @Test
    void testObterStringDeNullsCsv() {
        String resultado = FuncoesArquivos.obterStringDeNullsCsv(3);
        assertEquals("null,null,null", resultado);

        String resultadoUm = FuncoesArquivos.obterStringDeNullsCsv(1);
        assertEquals("null", resultadoUm);
    }

    @Test
    void testSalvarObjetoParaArquivo() throws IOException {
        FuncoesArquivos instancia = new FuncoesArquivos();
        
        Path arquivoPath = tempDir.resolve("objeto.csv");
        List<String> dados = Arrays.asList("id1", "nome", "email");

        instancia.salvarObjetoParaArquivo(dados, arquivoPath.toString());

        List<String> linhas = Files.readAllLines(arquivoPath);
        assertEquals(1, linhas.size());
        assertEquals("id1,nome,email", linhas.get(0));
    }

    @Test
    void testSalvarListaEmArquivo() throws IOException {
        List<String> dados = Arrays.asList("L1", "L2");

        Path subDir = tempDir.resolve("subdir");
        Path arquivoEmSubDir = subDir.resolve("arquivo_sub.txt");
        
        FuncoesArquivos.salvarListaEmArquivo(arquivoEmSubDir.toString(), dados, false);
        assertTrue(Files.exists(arquivoEmSubDir));
        
        FuncoesArquivos.salvarListaEmArquivo(arquivoEmSubDir.toString(), Arrays.asList("L3"), true);
        assertEquals(3, Files.readAllLines(arquivoEmSubDir).size());
    }

    @Test
    void testAlterarInfoArquivo() throws IOException {
        Path arquivoPath = tempDir.resolve("dados_alterar.csv");
        Files.write(arquivoPath, Arrays.asList(
            "Joao,25,Rio",
            "Maria,30,SP",
            "Pedro,40,BH"
        ));

        FuncoesArquivos.alterarInfoArquivo(arquivoPath.toString(), "Maria", 2, "Curitiba");

        List<String> linhas = Files.readAllLines(arquivoPath);
        assertTrue(linhas.contains("Joao,25,Rio")); 
        assertTrue(linhas.contains("Maria,30,Curitiba")); 
        assertTrue(linhas.contains("Pedro,40,BH")); 
    }

    @Test
    void testAlterarLinhaArquivo() throws IOException {
        Path arquivoPath = tempDir.resolve("linhas_alterar.csv");
        Files.write(arquivoPath, Arrays.asList(
            "ID1,DadoA",
            "ID2,DadoB"
        ));

        FuncoesArquivos.alterarLinhaArquivo(arquivoPath.toString(), "ID1", "ID1,DadoNovo");

        List<String> linhas = Files.readAllLines(arquivoPath);
        assertTrue(linhas.contains("ID1,DadoNovo"));
        assertTrue(linhas.contains("ID2,DadoB"));
    }

    @Test
    void testChecarExistenciaNomeArquivo() throws IOException {
        Path arquivoPath = tempDir.resolve("check.csv");
        Files.write(arquivoPath, Arrays.asList("Admin,123", "User,456"));

        assertTrue(FuncoesArquivos.checarExistenciaNomeArquivo(arquivoPath.toString(), "Admin"));
        assertFalse(FuncoesArquivos.checarExistenciaNomeArquivo(arquivoPath.toString(), "Guest"));
    }

    @Test
    void testLerArquivoInexistente() {
        assertDoesNotThrow(() -> 
            FuncoesArquivos.lerArquivo("caminho/que/nao/existe/arquivo.txt")
        );
    }
    
    @Test
    void testErrosDeIO() {
        String diretorioPath = tempDir.toString(); 
        
        assertDoesNotThrow(() -> FuncoesArquivos.escreverArquivo(diretorioPath, "fail"));
        assertDoesNotThrow(() -> FuncoesArquivos.appendLinhaArquivo(diretorioPath, "fail"));
        
        FuncoesArquivos instancia = new FuncoesArquivos();
        assertDoesNotThrow(() -> instancia.salvarObjetoParaArquivo(Arrays.asList("a"), diretorioPath));
    }
}