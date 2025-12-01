package backend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FuncoesArquivosTest {

    @TempDir
    Path tempDir;

    @AfterEach
    void tearDown() {
        File tempFixo = new File("temp.txt");
        if (tempFixo.exists()) {
            tempFixo.delete();
        }
    }

    @Test
    void testCriarArquivoSucesso() {
        File arquivo = tempDir.resolve("teste_novo.txt").toFile();
        FuncoesArquivos.criarArquivo(arquivo.getAbsolutePath());
        assertTrue(arquivo.exists());
    }

    @Test
    void testCriarArquivoJaExistente() throws IOException {
        File arquivo = tempDir.resolve("existente.txt").toFile();
        arquivo.createNewFile();
        FuncoesArquivos.criarArquivo(arquivo.getAbsolutePath());
        assertTrue(arquivo.exists());
    }

    @Test
    void testCriarArquivoException() {
        File pasta = tempDir.resolve("pasta").toFile();
        pasta.mkdir();
        FuncoesArquivos.criarArquivo(pasta.getAbsolutePath());
        assertTrue(pasta.exists());
    }

    @Test
    void testEscreverELerArquivo() throws IOException {
        File arquivo = tempDir.resolve("escrita.txt").toFile();
        String path = arquivo.getAbsolutePath();

        FuncoesArquivos.escreverArquivo(path, "Linha 1\n");
        
        String conteudoLido = Files.readString(arquivo.toPath());
        assertEquals("Linha 1", conteudoLido.trim());

        FuncoesArquivos.appendLinhaArquivo(path, "Linha 2");
        
        List<String> linhas = Files.readAllLines(arquivo.toPath());
        assertEquals(2, linhas.size());
        assertEquals("Linha 1", linhas.get(0));
        assertEquals("Linha 2", linhas.get(1));
    }

    @Test
    void testEscreverException() {
        File pasta = tempDir.resolve("pasta_bloqueada").toFile();
        pasta.mkdir();
        
        FuncoesArquivos.escreverArquivo(pasta.getAbsolutePath(), "teste");
        FuncoesArquivos.appendLinhaArquivo(pasta.getAbsolutePath(), "teste");
        
        assertTrue(pasta.isDirectory());
    }

    @Test
    void testLerArquivoLog() { 
        File arquivo = tempDir.resolve("leitura_log.txt").toFile();
        FuncoesArquivos.escreverArquivo(arquivo.getAbsolutePath(), "Teste Log");
        assertDoesNotThrow(() -> FuncoesArquivos.lerArquivo(arquivo.getAbsolutePath()));
    }

    @Test
    void testLerArquivoNaoEncontrado() {
        String pathInexistente = tempDir.resolve("fantasma.txt").toAbsolutePath().toString();
        assertDoesNotThrow(() -> FuncoesArquivos.lerArquivo(pathInexistente));
    }

    @Test
    void testListaLinhasEObterLista() throws IOException {
        File arquivo = tempDir.resolve("lista.txt").toFile();
        List<String> conteudo = Arrays.asList("A", "B", "C");
        Files.write(arquivo.toPath(), conteudo);

        List<String> resultadoFile = FuncoesArquivos.listaLinhas(arquivo);
        assertEquals(3, resultadoFile.size());
        assertEquals("B", resultadoFile.get(1));

        List<String> resultadoString = FuncoesArquivos.obterListaLinhas(arquivo.getAbsolutePath());
        assertEquals(3, resultadoString.size());
        assertEquals("C", resultadoString.get(2));
    }

    @Test
    void testListasExceptions() {
        File pasta = tempDir.resolve("pasta_lista").toFile();
        pasta.mkdir();

        List<String> l1 = FuncoesArquivos.listaLinhas(pasta);
        assertTrue(l1.isEmpty());

        List<String> l2 = FuncoesArquivos.obterListaLinhas(pasta.getAbsolutePath());
        assertTrue(l2.isEmpty());
    }

    @Test
    void testObterStringDeNullsCsv() {
        assertEquals("null,null,null", FuncoesArquivos.obterStringDeNullsCsv(3));
        assertEquals("null", FuncoesArquivos.obterStringDeNullsCsv(1));
        assertEquals("", FuncoesArquivos.obterStringDeNullsCsv(0));
    }

    @Test
    void testSalvarObjetoParaArquivo() throws IOException {
        FuncoesArquivos fa = new FuncoesArquivos();
        File arquivo = tempDir.resolve("objeto.csv").toFile();
        
        List<String> dados = Arrays.asList("Nome", "10.0", "true");
        fa.salvarObjetoParaArquivo(dados, arquivo.getAbsolutePath());

        String conteudo = Files.readString(arquivo.toPath());
        assertTrue(conteudo.contains("Nome,10.0,true"));
    }
    
    @Test
    void testSalvarObjetoException() {
        FuncoesArquivos fa = new FuncoesArquivos();
        File pasta = tempDir.resolve("pasta_obj").toFile();
        pasta.mkdir();
        
        fa.salvarObjetoParaArquivo(Arrays.asList("A"), pasta.getAbsolutePath());
        assertTrue(pasta.isDirectory());
    }

    @Test
    void testSalvarListaEmArquivo() throws IOException {
        File subDirArquivo = tempDir.resolve("subdir/novo/arquivo.txt").toFile();
        List<String> linhas = Arrays.asList("L1", "L2");

        FuncoesArquivos.salvarListaEmArquivo(subDirArquivo.getAbsolutePath(), linhas, false);

        assertTrue(subDirArquivo.exists());
        List<String> lidas = Files.readAllLines(subDirArquivo.toPath());
        assertEquals(2, lidas.size());
    }

    @Test
    void testSalvarListaException() {
         File pasta = tempDir.resolve("pasta_lista_save").toFile();
         pasta.mkdir();
         FuncoesArquivos.salvarListaEmArquivo(pasta.getAbsolutePath(), Arrays.asList("A"), false);
         assertTrue(pasta.isDirectory());
    }

    @Test
    void testAlterarInfoArquivo() throws IOException {
        File arquivo = tempDir.resolve("alterar_info.csv").toFile();
        List<String> conteudo = Arrays.asList("Joao,25,SP", "Maria,30,RJ", "Pedro,22,MG");
        Files.write(arquivo.toPath(), conteudo);

        FuncoesArquivos.alterarInfoArquivo(arquivo.getAbsolutePath(), "Maria", 2, "ES");

        List<String> novasLinhas = Files.readAllLines(arquivo.toPath());
        assertEquals("Joao,25,SP", novasLinhas.get(0));
        assertEquals("Maria,30,ES", novasLinhas.get(1));
    }
    
    @Test
    void testAlterarInfoArquivoIOException() {
        String fakePath = tempDir.resolve("nao_existe.csv").toString();
        assertDoesNotThrow(() -> FuncoesArquivos.alterarInfoArquivo(fakePath, "A", 1, "B"));
    }

    @Test
    void testAlterarLinhaArquivo() throws IOException {
        File arquivo = tempDir.resolve("alterar_linha.csv").toFile();
        List<String> conteudo = Arrays.asList("A,1", "B,2", "C,3");
        Files.write(arquivo.toPath(), conteudo);

        FuncoesArquivos.alterarLinhaArquivo(arquivo.getAbsolutePath(), "B", "B,999,Novo");

        List<String> novasLinhas = Files.readAllLines(arquivo.toPath());
        assertEquals("A,1", novasLinhas.get(0));
        assertEquals("B,999,Novo", novasLinhas.get(1));
        assertEquals("C,3", novasLinhas.get(2));
    }

    @Test
    void testAlterarLinhaArquivoIOException() {
        String fakePath = tempDir.resolve("nao_existe_linha.csv").toString();
        assertDoesNotThrow(() -> FuncoesArquivos.alterarLinhaArquivo(fakePath, "A", "Nova"));
    }

    @Test
    void testChecarExistenciaNomeArquivo() throws IOException {
        File arquivo = tempDir.resolve("checar.csv").toFile();
        List<String> conteudo = Arrays.asList("Alpha,1", "Beta,2");
        Files.write(arquivo.toPath(), conteudo);

        assertTrue(FuncoesArquivos.checarExistenciaNomeArquivo(arquivo.getAbsolutePath(), "Alpha"));
        assertFalse(FuncoesArquivos.checarExistenciaNomeArquivo(arquivo.getAbsolutePath(), "Gamma"));
    }

    @Test
    void testChecarExistenciaIOException() {
        File pasta = tempDir.resolve("pasta_check").toFile();
        pasta.mkdir();
        assertFalse(FuncoesArquivos.checarExistenciaNomeArquivo(pasta.getAbsolutePath(), "A"));
    }
}