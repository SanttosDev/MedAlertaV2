package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaTest {

    private Agenda agenda;

    @Mock
    private Pessoa contatoMock;
    @Mock
    private Pessoa contatoMock2;

    static class PessoaStub extends Pessoa {
        public PessoaStub(String nome, String email) {
            super(nome, "0000-0000", email, "1234"); 
        }
        @Override public Object getParticularidade() { return null; }
        @Override 
        public <T> void setParticularidade(T novaParticularidade) { 
            // Método intencionalmente vazio para fins de teste (Stub)
        }
    }

    @BeforeEach
    void setUp() {
        agenda = new Agenda();
    }

    @Test
    void testAdicionarContatoValido() {
        agenda.adicionarContato(contatoMock);
        assertEquals(1, agenda.getContatos().size());
        assertTrue(agenda.getContatos().contains(contatoMock));
    }

    @Test
    void testAdicionarContatoNulo() {
        assertThrows(IllegalArgumentException.class, () -> agenda.adicionarContato(null));
    }

    @Test
    void testAlterarNome() {
        when(contatoMock.getNome()).thenReturn("AntigoNome");
        agenda.adicionarContato(contatoMock);
        assertTrue(agenda.alterarNomeContato("AntigoNome", "NovoNome"));
        verify(contatoMock).setNome("NovoNome");
    }

    @Test
    void testAlterarTelefone() {
        when(contatoMock.getNome()).thenReturn("Maria");
        agenda.adicionarContato(contatoMock);
        assertTrue(agenda.alterarTelContato("Maria", "999"));
        verify(contatoMock).setTelefone("999");
    }

    @Test
    void testAlterarEmail() {
        when(contatoMock.getNome()).thenReturn("Carlos");
        agenda.adicionarContato(contatoMock);
        assertTrue(agenda.alterarEmailContato("Carlos", "novo@email.com"));
        verify(contatoMock).setEmail("novo@email.com");
    }

    @Test
    void testAlterarParticularidade() {
        when(contatoMock.getNome()).thenReturn("Ana");
        agenda.adicionarContato(contatoMock);
        assertTrue(agenda.alterarParticularidadeContato("Ana", "Rua X"));
        verify(contatoMock).setParticularidade("Rua X");
    }

    @Test
    void testRemoverContato() {
        lenient().when(contatoMock.getNome()).thenReturn("Maria");
        lenient().when(contatoMock2.getNome()).thenReturn("João");
        agenda.adicionarContato(contatoMock);
        agenda.adicionarContato(contatoMock2);
        
        assertTrue(agenda.removerContato("Maria"));
        assertEquals(1, agenda.getContatos().size());
    }

    @Test
    void testToStringComContatos() {
        lenient().when(contatoMock.getEmail()).thenReturn("a@teste.com");
        lenient().when(contatoMock2.getEmail()).thenReturn("b@teste.com");
        lenient().when(contatoMock.getNome()).thenReturn("A");
        lenient().when(contatoMock2.getNome()).thenReturn("B");

        agenda.adicionarContato(contatoMock);
        agenda.adicionarContato(contatoMock2);

        String res = agenda.toString();
        assertTrue(res.contains("a@teste.com") && res.contains("b@teste.com"));
    }

    @Test
    void testToStringVazio() {
        assertEquals("null", agenda.toString());
    }

    // --- TESTES DE COBERTURA AVANÇADA (MATADORES DE MUTANTES) ---

    @Test
    void testCasosDeFalhaAoNaoEncontrar() {
        // Cobre todos os retornos 'false'
        assertFalse(agenda.alterarNomeContato("X", "Y"));
        assertFalse(agenda.alterarTelContato("X", "Y"));
        assertFalse(agenda.alterarEmailContato("X", "Y"));
        assertFalse(agenda.alterarParticularidadeContato("X", "Y"));
        assertFalse(agenda.removerContato("X"));
    }

    @Test
    void testBuscarPessoaEspecifica() {
        // Este teste mata o mutante do Lambda na linha 22.
        // Se o filtro retornar sempre true, findFirst() pegaria "Abelha" ao invés de "Zebra".
        Pessoa p1 = new PessoaStub("Abelha", "a");
        Pessoa p2 = new PessoaStub("Zebra", "z");
        agenda.adicionarContato(p1);
        agenda.adicionarContato(p2);
        
        assertTrue(agenda.alterarNomeContato("Zebra", "Zebra Alterada"));
        assertEquals("Zebra Alterada", agenda.getContatos().get(1).getNome());
    }

    @Test
    void testOrdenacaoGetContatos() {
        Pessoa p1 = new PessoaStub("Zebra", "z");
        Pessoa p2 = new PessoaStub("Abelha", "a");
        agenda.adicionarContato(p1);
        agenda.adicionarContato(p2);
        
        assertEquals("Abelha", agenda.getContatos().get(0).getNome());
    }

    @Test
    void testSetContatos() {
        agenda.setContatos(null);
        assertTrue(agenda.getContatos().isEmpty());

        ArrayList<Pessoa> novaLista = new ArrayList<>();
        novaLista.add(new PessoaStub("Teste", "t"));
        agenda.setContatos(novaLista);
        assertEquals(1, agenda.getContatos().size());
    }

    // --- TESTES ESTRUTURAIS PARA STRING TO AGENDA ---

    @Test
    void testStringToAgendaVazioOuNulo() {
        Agenda a1 = Agenda.stringToAgenda(null, "s", "u", true, true);
        assertNotNull(a1);
        assertTrue(a1.getContatos().isEmpty());
        
        Agenda a2 = Agenda.stringToAgenda("", "s", "u", true, true);
        assertNotNull(a2);
        assertTrue(a2.getContatos().isEmpty());
    }

    @Test
    void testStringToAgendaComErroArquivo() {
        // Este teste é o segredo.
        // Como não temos os arquivos, o método DEVE falhar.
        // Se o mutante remover a linha que chama o arquivo, o método NÃO falha.
        // Logo, assertThrows falha e mata o mutante.
        
        assertThrows(RuntimeException.class, () -> {
            Agenda.stringToAgenda("ArquivoInexistente", "123", "usuario", true, true);
        });

        assertThrows(RuntimeException.class, () -> {
            Agenda.stringToAgenda("ArquivoInexistente", "123", "farmacia", true, true);
        });

        assertThrows(RuntimeException.class, () -> {
            Agenda.stringToAgenda("ArquivoInexistente", "123", "medico", true, true);
        });
    }
}