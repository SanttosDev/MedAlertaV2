package backend.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import java.io.BufferedReader;
import java.io.FileReader;
import org.mockito.MockedStatic;
import org.mockito.ArgumentCaptor;
import java.util.List;
import backend.FuncoesArquivos;

class UsuarioMedicoTest {

    private Medico medico;

    @BeforeEach
    void setUp() {
        medico = new Medico(
            "Dr ray", 
            "2199999-8888", 
            "house@hospital.com", 
            "vicodin123", 
            "Plastica"
        );
    }

    @Test
    @DisplayName("Construtor deve inicializar corretamente atributos próprios e herdados de Pessoa")
    void testConstrutorEHeranca() {
        assertAll("Verificando integridade do objeto Médico",
            () -> assertEquals("Dr ray", medico.getNome()),
            () -> assertEquals("2199999-8888", medico.getTelefone()),
            () -> assertEquals("house@hospital.com", medico.getEmail()),
            () -> assertEquals("vicodin123", medico.getSenha()),
            () -> assertEquals("Plastica", medico.getEspecialidade())
        );
    }

    @Test
    @DisplayName("Setters devem atualizar o estado do objeto corretamente")
    void testSetters() {
        medico.setNome("Dr. Auzio");
        medico.setEspecialidade("Tudo");
        assertEquals("Dr. Auzio", medico.getNome(), "Nome herdado deve ser atualizado");
        assertEquals("Tudo", medico.getEspecialidade(), "Especialidade deve ser atualizada");
    }

    @Test
    @DisplayName("Polimorfismo: getParticularidade deve retornar a Especialidade")
    void testGetParticularidade() {
        Object particularidade = medico.getParticularidade();

        assertNotNull(particularidade);
        assertTrue(particularidade instanceof String);
        assertEquals("Plastica", particularidade);
    }

    @Test
    @DisplayName("Polimorfismo: setParticularidade deve atualizar a Especialidade")
    void testSetParticularidade() {
        medico.setParticularidade("Cirurgião");

        assertEquals("Cirurgião", medico.getEspecialidade());
        assertEquals("Cirurgião", medico.getParticularidade());
    }

    @Test
    @DisplayName("toString deve formatar CSV incluindo a especialidade no final")
    void testToString() {
        String resultadoEsperado = "Dr ray,2199999-8888,house@hospital.com,vicodin123,Plastica";
        
        assertEquals(resultadoEsperado, medico.toString(), 
            "O formato deve ser compatível com a gravação em arquivo (CSV)");
    }
    
    @Test
    @DisplayName("Deve verificar a constante do nome do arquivo")
    void testNomeArquivoConstante() {
        assertEquals("backend\\usuario\\RegistroMedicos.txt", Medico.nomeArquivoMedicos);
    }


    @Test
    @DisplayName("Deve resgatar médico simulando leitura de arquivo (Stub)")
    void testResgatarMedicoComSucesso() {
        String linhaDoArquivoStub = "Dr. Bernardo,2199999999,bernardo@hospital.com,senha123,Cirurgiao";

        try (MockedConstruction<FileReader> mockFileReader = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockBufferedReader = mockConstruction(BufferedReader.class,
                 (mock, context) -> {
                     when(mock.readLine()).thenReturn(linhaDoArquivoStub).thenReturn(null);
                 })) {

            Medico medicoResgatado = Medico.resgatarMedicoArquivo("bernardo@hospital.com", "senha123", false);

            assertNotNull(medicoResgatado);
            assertEquals("Dr. Bernardo", medicoResgatado.getNome());
            assertEquals("Cirurgiao", medicoResgatado.getEspecialidade());
        }
    }

    @Test
    @DisplayName("Deve retornar null se a senha estiver incorreta (Simulação)")
    void testFalhaSenhaIncorreta() {
        String linhaDoArquivoStub = "Dr. Bernardo,999,bernardo@hospital.com,senhaCerta,Geral";

        try (MockedConstruction<FileReader> mockFR = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockBR = mockConstruction(BufferedReader.class,
                 (mock, context) -> {
                     when(mock.readLine()).thenReturn(linhaDoArquivoStub).thenReturn(null);
                 })) {

            Medico resultado = Medico.resgatarMedicoArquivo("bernardo@hospital.com", "senhaErrada", false);

            assertNull(resultado, "Deve retornar null para senha incorreta");
        }
    }

    @Test
    @DisplayName("Deve chamar FuncoesArquivos para salvar os dados sem escrever no disco")
    void testSalvarDadosArquivo() {
        try (MockedStatic<FuncoesArquivos> mockedFuncoes = mockStatic(FuncoesArquivos.class)) {
            medico.salvarDadosArquivo();
            ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
            mockedFuncoes.verify(() -> 
                FuncoesArquivos.salvarListaEmArquivo(
                    eq(Medico.nomeArquivoMedicos), 
                    listCaptor.capture(), 
                    eq(true)
                )
            );

            List<String> valorCapturado = listCaptor.getValue();
            assertNotNull(valorCapturado);
            
            assertEquals(1, valorCapturado.size());
            assertEquals(medico.toString(), valorCapturado.get(0));
        }
    }
}