package backend;

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

class SistemaIntegrationTest {

    // O JUnit cria esta pasta temporária automaticamente e a deleta ao final
    @TempDir
    Path diretorioTemporario;

    @Test
    @DisplayName("Integração: Fluxo completo de Criar Usuário -> Adicionar Uso -> Salvar -> Recuperar")
    void testCicloCompletoDePersistencia() {
        // --- 1. CONFIGURAÇÃO  ---
        
        // Define caminhos de arquivo dentro da pasta temporária segura
        File arquivoUsuario = diretorioTemporario.resolve("usuario_teste.bin").toFile();
        String caminhoArquivo = arquivoUsuario.getAbsolutePath();

        // Cria componentes reais (Sem Mocks!)
        Endereco enderecoReal = new Endereco("Rua Teste", "123", "Apto 1", "Centro", "Cidade", "ES", "Brasil", "29000-000");
        PessoaFisica usuarioOriginal = new PessoaFisica(
            "Maria Integração", 
            "2799999999", 
            "maria@teste.com", 
            "123.456.789-00", 
            "senhaForte", 
            enderecoReal
        );

        // Cria e configura um Uso de Medicamento real
        Medicamento remedio = new Medicamento("Dipirona", 15.50f, "500mg", "Comprimido", "Se dor", false);
        ArrayList<String> dias = new ArrayList<>();
        dias.add("seg");
        dias.add("qua");
        
        // Uso: Remedio, dose, dias, duração(dias), qtdDisponivel, horaInicio, intervalo
        Uso usoOriginal = new Uso(remedio, 1, dias, 7, 20, 8, 8); 
        usoOriginal.calcularHorariosDeUso();

        usuarioOriginal.adicionarUsoNaListaUsoMedicamentos(usoOriginal);

        // --- 2. AÇÃO 
        
        usuarioOriginal.salvarObjetoArquivo(caminhoArquivo, usuarioOriginal);

        // --- 3. VERIFICAÇÃO (ASSERT) - Parte 1: Arquivo ---
        
        assertTrue(arquivoUsuario.exists(), "O arquivo deveria ter sido criado no disco");
        assertTrue(arquivoUsuario.length() > 0, "O arquivo não deve estar vazio");

        // --- 4. RECUPERAÇÃO (RELOAD) ---
        Object objetoRecuperado = new PessoaFisica(null, null, null, null, null, null)
                                    .recuperarObjetoArquivo(caminhoArquivo);

        // --- 5. VERIFICAÇÃO FINAL (ASSERT) - Parte 2: Integridade ---
        
        assertNotNull(objetoRecuperado, "A recuperação não deve retornar nulo");
        assertTrue(objetoRecuperado instanceof PessoaFisica, "O objeto recuperado deve ser do tipo PessoaFisica");

        PessoaFisica usuarioRecuperado = (PessoaFisica) objetoRecuperado;

        assertEquals("Maria Integração", usuarioRecuperado.getNome());
        assertEquals("123.456.789-00", usuarioRecuperado.getCpf());
        
        List<Uso> listaUsos = usuarioRecuperado.getListaUsoMedicamentos();
        assertNotNull(listaUsos);
        assertFalse(listaUsos.isEmpty(), "A lista de medicamentos não deve estar vazia");
        
        Uso usoRecuperado = listaUsos.get(0);
        assertEquals("Dipirona", usoRecuperado.getRemedio().getNome());
        assertEquals(20, usoRecuperado.getQtdDisponivel());
        
        // Valida lógica de negócio preservada (Horários calculados)
        assertNotNull(usoRecuperado.getHorarios());
        assertTrue(usoRecuperado.getHorarios().contains("seg"));
    }
}
