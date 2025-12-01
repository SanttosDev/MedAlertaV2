package backend.usuario;

import backend.Agenda;
import backend.Endereco;
import backend.FuncoesArquivos;
import backend.Medicamento;
import backend.farmacia.PessoaJuridica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PessoaFisicaTest {

    private PessoaFisica pessoa;

    @Mock
    private Endereco enderecoMock;

    @Mock
    private Uso usoMock;

    @Mock
    private Medicamento medicamentoMock;

    @Mock
    private Medico medicoMock;

    @Mock
    private PessoaJuridica farmaciaMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(enderecoMock.toString()).thenReturn("Rua Teste/123");
        when(usoMock.getRemedio()).thenReturn(medicamentoMock);
        when(medicamentoMock.getNome()).thenReturn("Dipirona");
        
        pessoa = new PessoaFisica(
            "Bernardo",
            "9999-9999",
            "Bernardo@email.com",
            "123.456.789-00",
            "senha123",
            enderecoMock
        );
    }

    @Test
    void testConstrutorAndGetters() {
        assertAll(
            () -> assertEquals("Bernardo", pessoa.getNome()),
            () -> assertEquals("9999-9999", pessoa.getTelefone()),
            () -> assertEquals("Bernardo@email.com", pessoa.getEmail()),
            () -> assertEquals("123.456.789-00", pessoa.getCpf()),
            () -> assertEquals("senha123", pessoa.getSenha()),
            () -> assertEquals(enderecoMock, pessoa.getEndereco())
        );
    }

    @Test
    void testSetters() {
        pessoa.setCpf("999.999.999-99");
        pessoa.setEndereco(null);
        
        assertEquals("999.999.999-99", pessoa.getCpf());
        assertNull(pessoa.getEndereco());
    }

    @Test
    void testSetListaUsoMedicamentosSemSalvar() {
        List<Uso> novaLista = new ArrayList<>();
        novaLista.add(usoMock);
        
        pessoa.setListaUsoMedicamentos(novaLista, false);
        
        assertEquals(novaLista, pessoa.getListaUsoMedicamentos());
    }

    @Test
    void testSetListaUsoMedicamentosComSalvar() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            List<Uso> novaLista = new ArrayList<>();
            novaLista.add(usoMock);
            
            pessoa.setListaUsoMedicamentos(novaLista, true);
            
            assertEquals(novaLista, pessoa.getListaUsoMedicamentos());
            funcoes.verify(() -> FuncoesArquivos.salvarListaEmArquivo(anyString(), anyList(), eq(false)));
        }
    }

    @Test
    void testAdicionarUsoNaListaUsoMedicamentosListaNula() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            assertNull(pessoa.getListaUsoMedicamentos());
            
            pessoa.adicionarUsoNaListaUsoMedicamentos(usoMock);
            
            assertNotNull(pessoa.getListaUsoMedicamentos());
            assertEquals(1, pessoa.getListaUsoMedicamentos().size());
            assertEquals(usoMock, pessoa.getListaUsoMedicamentos().get(0));
        }
    }

    @Test
    void testAdicionarUsoNaListaUsoMedicamentosListaExistente() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            List<Uso> lista = new ArrayList<>();
            pessoa.setListaUsoMedicamentos(lista, false);
            
            pessoa.adicionarUsoNaListaUsoMedicamentos(usoMock);
            
            assertEquals(1, pessoa.getListaUsoMedicamentos().size());
        }
    }

    @Test
    void testRemoverUsoNaListaUsoMedicamentosListaNula() {
        assertNull(pessoa.getListaUsoMedicamentos());
        assertDoesNotThrow(() -> pessoa.removerUsoNaListaUsoMedicamentos("Dipirona"));
    }

    @Test
    void testRemoverUsoNaListaUsoMedicamentosItemEncontrado() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            List<Uso> lista = new ArrayList<>();
            lista.add(usoMock);
            pessoa.setListaUsoMedicamentos(lista, false);
            
            pessoa.removerUsoNaListaUsoMedicamentos("Dipirona");
            
            assertTrue(pessoa.getListaUsoMedicamentos().isEmpty());
        }
    }

    @Test
    void testRemoverUsoNaListaUsoMedicamentosItemNaoEncontrado() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            List<Uso> lista = new ArrayList<>();
            lista.add(usoMock);
            pessoa.setListaUsoMedicamentos(lista, false);
            
            pessoa.removerUsoNaListaUsoMedicamentos("Dorflex");
            
            assertEquals(1, pessoa.getListaUsoMedicamentos().size());
        }
    }

    @Test
    void testAtualizarQntRemediosListaUsoMedicamentosListaNula() {
        assertNull(pessoa.getListaUsoMedicamentos());
        assertDoesNotThrow(() -> pessoa.atualizarQntRemediosListaUsoMedicamentos("Dipirona", 10));
    }

    @Test
    void testAtualizarQntRemediosListaUsoMedicamentosItemEncontrado() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            List<Uso> lista = new ArrayList<>();
            lista.add(usoMock);
            pessoa.setListaUsoMedicamentos(lista, false);
            
            pessoa.atualizarQntRemediosListaUsoMedicamentos("Dipirona", 50);
            
            verify(usoMock).setQtdDisponivel(50);
        }
    }

    @Test
    void testAtualizarQntRemediosListaUsoMedicamentosItemNaoEncontrado() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            List<Uso> lista = new ArrayList<>();
            lista.add(usoMock);
            pessoa.setListaUsoMedicamentos(lista, false);
            
            pessoa.atualizarQntRemediosListaUsoMedicamentos("Outro", 50);
            
            verify(usoMock, never()).setQtdDisponivel(anyInt());
        }
    }

    @Test
    void testGetUsoListaUsoMedicamentosListaNula() {
        assertNull(pessoa.getUsoListaUsoMedicamentos("Dipirona"));
    }

    @Test
    void testGetUsoListaUsoMedicamentosEncontrado() {
        List<Uso> lista = new ArrayList<>();
        lista.add(usoMock);
        pessoa.setListaUsoMedicamentos(lista, false);
        
        assertEquals(usoMock, pessoa.getUsoListaUsoMedicamentos("Dipirona"));
    }

    @Test
    void testGetUsoListaUsoMedicamentosNaoEncontrado() {
        List<Uso> lista = new ArrayList<>();
        lista.add(usoMock);
        pessoa.setListaUsoMedicamentos(lista, false);
        
        assertNull(pessoa.getUsoListaUsoMedicamentos("Outro"));
    }

    @Test
    void testContatosMedicos() {
        Agenda novaAgenda = mock(Agenda.class);
        when(novaAgenda.toString()).thenReturn("AgendaMedicos"); 
        
        pessoa.setContatosMedicos(novaAgenda);
        assertEquals(novaAgenda, pessoa.getContatosMedicos());

        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            funcoes.when(() -> FuncoesArquivos.checarExistenciaNomeArquivo(anyString(), eq(pessoa.getNome())))
                   .thenReturn(true);

            pessoa.setContatosMedicos(novaAgenda, true);

            funcoes.verify(() -> FuncoesArquivos.alterarLinhaArquivo(
                eq(PessoaFisica.getNomeArquivoUsuarios()), 
                eq(pessoa.getNome()),                      
                anyString()                                
            ));

            funcoes.verify(() -> FuncoesArquivos.appendLinhaArquivo(anyString(), anyString()), never());
            funcoes.verify(() -> FuncoesArquivos.salvarListaEmArquivo(anyString(), anyList(), anyBoolean()), never());
        }
    }

    @Test
    void testContatosFarmacias() {
        Agenda novaAgenda = mock(Agenda.class);
        when(novaAgenda.toString()).thenReturn("AgendaFarmacias");
        
        pessoa.setContatosFarmacias(novaAgenda);
        assertEquals(novaAgenda, pessoa.getContatosFarmacias());
        
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            funcoes.when(() -> FuncoesArquivos.checarExistenciaNomeArquivo(anyString(), eq(pessoa.getNome())))
                   .thenReturn(false);

            pessoa.setContatosFarmacias(novaAgenda, true);
            funcoes.verify(() -> FuncoesArquivos.appendLinhaArquivo(
                eq(PessoaFisica.getNomeArquivoUsuarios()), 
                anyString()
            ));

            funcoes.verify(() -> FuncoesArquivos.alterarLinhaArquivo(anyString(), anyString(), anyString()), never());
        }
    }

    @Test
    void testParticularidade() {
        assertEquals(enderecoMock, pessoa.getParticularidade());
        
        Endereco novoEnd = mock(Endereco.class);
        pessoa.setParticularidade(novoEnd);
        assertEquals(novoEnd, pessoa.getEndereco());
    }

    @Test
    void testAddFarmaciaAosContatosNullAgenda() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            pessoa.setContatosFarmacias(null);
            pessoa.addFarmaciaAosContatos(farmaciaMock);
            
            assertNotNull(pessoa.getContatosFarmacias());
            assertEquals(1, pessoa.getContatosFarmacias().getContatos().size());
        }
    }

    @Test
    void testAddFarmaciaAosContatosExistingAgenda() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            pessoa.addFarmaciaAosContatos(farmaciaMock);
            
            assertNotNull(pessoa.getContatosFarmacias());
            assertEquals(1, pessoa.getContatosFarmacias().getContatos().size());
        }
    }

    @Test
    void testRemoverContatoFarmacia() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            when(farmaciaMock.getNome()).thenReturn("Farmacia1");
            pessoa.addFarmaciaAosContatos(farmaciaMock);
            
            pessoa.removerContatoFarmacia("Farmacia1");
            
            assertTrue(pessoa.getContatosFarmacias().getContatos().isEmpty());
        }
    }

    @Test
    void testRemoverContatoFarmaciaNullAgenda() {
        pessoa.setContatosFarmacias(null);
        assertDoesNotThrow(() -> pessoa.removerContatoFarmacia("Farmacia1"));
    }

    @Test
    void testRemoverContatoFarmaciaNaoEncontrado() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            when(farmaciaMock.getNome()).thenReturn("Farmacia1");
            pessoa.addFarmaciaAosContatos(farmaciaMock);
            
            pessoa.removerContatoFarmacia("Outra");
            
            assertEquals(1, pessoa.getContatosFarmacias().getContatos().size());
        }
    }

    @Test
    void testAdicionarContatoMedicoNullAgenda() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            pessoa.setContatosMedicos(null);
            pessoa.adicionarContatoMedico(medicoMock);
            
            assertNotNull(pessoa.getContatosMedicos());
            assertEquals(1, pessoa.getContatosMedicos().getContatos().size());
        }
    }

    @Test
    void testAdicionarContatoMedicoExistingAgenda() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            pessoa.adicionarContatoMedico(medicoMock);
            
            assertNotNull(pessoa.getContatosMedicos());
            assertEquals(1, pessoa.getContatosMedicos().getContatos().size());
        }
    }

    @Test
    void testRemoverContatoMedico() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            when(medicoMock.getNome()).thenReturn("Dr. Auzio");
            pessoa.adicionarContatoMedico(medicoMock);
            
            pessoa.removerContatoMedico("Dr. Auzio");
            
            assertTrue(pessoa.getContatosMedicos().getContatos().isEmpty());
        }
    }

    @Test
    void testRemoverContatoMedicoNullAgenda() {
        pessoa.setContatosMedicos(null);
        assertDoesNotThrow(() -> pessoa.removerContatoMedico("Dr. Auzio"));
    }

    @Test
    void testRemoverContatoMedicoNaoEncontrado() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            when(medicoMock.getNome()).thenReturn("Dr. Auzio");
            pessoa.adicionarContatoMedico(medicoMock);
            
            pessoa.removerContatoMedico("Dr. Rey");
            
            assertEquals(1, pessoa.getContatosMedicos().getContatos().size());
        }
    }

    @Test
    void testGetNomeArquivoUsuarios() {
        assertEquals("backend\\usuario\\RegistroUsuarios.txt", PessoaFisica.getNomeArquivoUsuarios());
    }

    @Test
    void testGetNomeArquivoUsos() {
        String expected = "backend\\usuario\\arquivosUsosUsuarios\\Uso123.456.789-00.txt";
        assertEquals(expected, pessoa.getNomeArquivoUsos());
    }

    @Test
    void testToStringCompleto() {
        List<Uso> lista = new ArrayList<>();
        pessoa.setListaUsoMedicamentos(lista, false);
        
        String res = pessoa.toString();
        
        assertTrue(res.contains("Bernardo"));
        assertTrue(res.contains("123.456.789-00"));
        assertTrue(res.contains("Uso123.456.789-00.txt"));
    }

    @Test
    void testToStringNullFields() {
        pessoa.setListaUsoMedicamentos(null, false);
        pessoa.setContatosMedicos(null);
        pessoa.setContatosFarmacias(null);
        
        String res = pessoa.toString();
        
        assertTrue(res.contains("null")); 
    }

    @Test
    void testToStringComEncriptacao() {
        String res = pessoa.toString(true);
        assertNotEquals("senha123", res.split(",")[3]);
    }

    @Test
    void testSalvarDadosArquivoNovoUsuario() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            funcoes.when(() -> FuncoesArquivos.checarExistenciaNomeArquivo(anyString(), anyString()))
                   .thenReturn(false);
            
            pessoa.salvarDadosArquivo();
            
            funcoes.verify(() -> FuncoesArquivos.appendLinhaArquivo(anyString(), anyString()));
            funcoes.verify(() -> FuncoesArquivos.alterarLinhaArquivo(anyString(), anyString(), anyString()), never());
        }
    }

    @Test
    void testSalvarDadosArquivoUsuarioExistente() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class)) {
            funcoes.when(() -> FuncoesArquivos.checarExistenciaNomeArquivo(anyString(), anyString()))
                   .thenReturn(true);
            
            pessoa.salvarDadosArquivo();
            
            funcoes.verify(() -> FuncoesArquivos.alterarLinhaArquivo(anyString(), anyString(), anyString()));
            funcoes.verify(() -> FuncoesArquivos.appendLinhaArquivo(anyString(), anyString()), never());
        }
    }

    @Test
    void testResgatarListaUsoMedicamentosArquivo() {
        try (MockedStatic<FuncoesArquivos> funcoes = mockStatic(FuncoesArquivos.class);
             MockedStatic<Uso> usoStatic = mockStatic(Uso.class)) {
            
            List<String> linhas = new ArrayList<>();
            linhas.add("linha1");
            
            funcoes.when(() -> FuncoesArquivos.obterListaLinhas(anyString())).thenReturn(linhas);
            usoStatic.when(() -> Uso.stringToUso("linha1")).thenReturn(usoMock);
            
            List<Uso> resultado = PessoaFisica.resgatarListaUsoMedicamentosArquivo("arquivo.txt");
            
            assertEquals(1, resultado.size());
            assertEquals(usoMock, resultado.get(0));
        }
    }

    @Test
    void testResgatarUsuarioArquivoSucesso() {
        String linhaCsv = "Bernardo,9999,Bernardo@email.com,senha123,123.456.789-00,EndStr,null,null,null";
        
        try (MockedConstruction<FileReader> mockFR = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockBR = mockConstruction(BufferedReader.class,
                 (mock, context) -> {
                     when(mock.readLine()).thenReturn(linhaCsv).thenReturn(null);
                 });
             MockedStatic<Endereco> endStatic = mockStatic(Endereco.class)) {
             
            endStatic.when(() -> Endereco.stringToEndereco("EndStr")).thenReturn(enderecoMock);

            PessoaFisica res = PessoaFisica.resgatarUsuarioArquivo("Bernardo@email.com", "senha123", false, true);
            
            assertNotNull(res);
            assertEquals("Bernardo", res.getNome());
        }
    }

    @Test
    void testResgatarUsuarioArquivoSenhaIncorreta() {
        String linhaCsv = "Bernardo,9999,Bernardo@email.com,senha123,123.456.789-00,EndStr,null,null,null";
        
        try (MockedConstruction<FileReader> mockFR = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockBR = mockConstruction(BufferedReader.class,
                 (mock, context) -> {
                     when(mock.readLine()).thenReturn(linhaCsv).thenReturn(null);
                 })) {

            PessoaFisica res = PessoaFisica.resgatarUsuarioArquivo("Bernardo@email.com", "senhaErrada", false, true);
            
            assertNull(res);
        }
    }

    @Test
    void testResgatarUsuarioArquivoEmailIncorreto() {
        String linhaCsv = "Bernardo,9999,outro@email.com,senha123,123.456.789-00,EndStr,null,null,null";
        
        try (MockedConstruction<FileReader> mockFR = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockBR = mockConstruction(BufferedReader.class,
                 (mock, context) -> {
                     when(mock.readLine()).thenReturn(linhaCsv).thenReturn(null);
                 })) {

            PessoaFisica res = PessoaFisica.resgatarUsuarioArquivo("Bernardo@email.com", "senha123", false, true);
            
            assertNull(res);
        }
    }

    @Test
    void testResgatarUsuarioArquivoExcecao() {
        try (MockedConstruction<FileReader> mockFR = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockBR = mockConstruction(BufferedReader.class,
                 (mock, context) -> {
                     when(mock.readLine()).thenThrow(new IOException());
                 })) {

            PessoaFisica res = PessoaFisica.resgatarUsuarioArquivo("Bernardo@email.com", "senha123", false, true);
            
            assertNull(res);
        }
    }

    @Test
    void testBancoDeDadosStubs() {
        assertTrue(pessoa.estabelecerConexaoBD("path"));
        assertNull(pessoa.recuperarObjetoBancoDeDados("path"));
        assertDoesNotThrow(() -> pessoa.salvarObejtoBancoDeDados(new Object()));
    }
}