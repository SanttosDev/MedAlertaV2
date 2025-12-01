package backend.integracao;

import backend.Endereco;
import backend.Medicamento;
import backend.usuario.PessoaFisica;
import backend.usuario.Uso;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PacienteTratamentoIntegrationTest {
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Integração: Fluxo completo de Criar Usuário -> Adicionar Uso -> Salvar -> Recuperar")
    void testCicloCompletoDePersistencia() {
        File arquivoUsuario = tempDir.resolve("usuario_teste.bin").toFile();
        String caminhoArquivo = arquivoUsuario.getAbsolutePath();

        Endereco enderecoReal = new Endereco("Rua Teste", "123", "Apto 1", "Ingá", "Cidade", "ES", "Brasil", "29000-000");
        PessoaFisica usuarioOriginal = new PessoaFisica(
            "Bernardo Mendes", 
            "2199999999", 
            "bernardo@teste.com", 
            "123.456.789-00", 
            "senhaForte", 
            enderecoReal
        );

        Medicamento remedio = new Medicamento("Dipirona", 15.50f, "500mg", "Comprimido", "Se dor de cabeça", false);
        ArrayList<String> dias = new ArrayList<>();
        dias.add("seg");
        dias.add("qua");
        
        Uso usoOriginal = new Uso(remedio, 1, dias, 7, 20, 8, 8);
        usoOriginal.calcularHorariosDeUso();

        usuarioOriginal.adicionarUsoNaListaUsoMedicamentos(usoOriginal);
        usuarioOriginal.salvarObjetoArquivo(caminhoArquivo, usuarioOriginal);

        assertTrue(arquivoUsuario.exists(), "O arquivo deveria ter sido criado no disco");
        assertTrue(arquivoUsuario.length() > 0, "O arquivo não deve estar vazio");

        Object objetoRecuperado = new PessoaFisica(null, null, null, null, null, null).recuperarObjetoArquivo(caminhoArquivo);
        
        assertNotNull(objetoRecuperado, "A recuperação não deve retornar nulo");
        assertTrue(objetoRecuperado instanceof PessoaFisica, "O objeto recuperado deve ser do tipo PessoaFisica");

        PessoaFisica usuarioRecuperado = (PessoaFisica) objetoRecuperado;

        assertEquals("Bernardo Mendes", usuarioRecuperado.getNome());
        assertEquals("123.456.789-00", usuarioRecuperado.getCpf());
        
        List<Uso> listaUsos = usuarioRecuperado.getListaUsoMedicamentos();
        assertNotNull(listaUsos);
        assertFalse(listaUsos.isEmpty(), "A lista de medicamentos não deve estar vazia");
        
        Uso usoRecuperado = listaUsos.get(0);
        assertEquals("Dipirona", usoRecuperado.getRemedio().getNome());
        assertEquals(20, usoRecuperado.getQtdDisponivel());
        
        assertNotNull(usoRecuperado.getHorarios());
        assertTrue(usoRecuperado.getHorarios().contains("seg"));
    }
}