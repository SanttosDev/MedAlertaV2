package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaTest {

    private Agenda agenda;

    @Mock
    private Pessoa contatoMock;

    @Mock
    private Pessoa contatoMock2;

    @BeforeEach
    void setUp() {
        agenda = new Agenda();
    }

    @Test
    @DisplayName("Deve adicionar um contato válido com sucesso")
    void testAdicionarContatoValido() {
        agenda.adicionarContato(contatoMock);

        assertEquals(1, agenda.getContatos().size());
        assertTrue(agenda.getContatos().contains(contatoMock));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar contato nulo")
    void testAdicionarContatoNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            agenda.adicionarContato(null);
        });

        assertEquals("É necessário informar um contato válido", exception.getMessage());
    }

    @Test
    @DisplayName("Deve alterar nome do contato")
    void testAlterarNome() {
        when(contatoMock.getNome()).thenReturn("AntigoNome");
        agenda.adicionarContato(contatoMock);

        boolean resultado = agenda.alterarNomeContato("AntigoNome", "NovoNome");

        assertTrue(resultado);
        verify(contatoMock).setNome("NovoNome");
    }

    @Test
    @DisplayName("Deve alterar telefone buscando pelo nome")
    void testAlterarTelefone() {
        when(contatoMock.getNome()).thenReturn("Maria");
        agenda.adicionarContato(contatoMock);

        boolean resultado = agenda.alterarTelContato("Maria", "9999-8888");

        assertTrue(resultado);
        verify(contatoMock).setTelefone("9999-8888");
    }

    @Test
    @DisplayName("Não deve alterar telefone de contato inexistente")
    void testAlterarTelefoneInexistente() {
        when(contatoMock.getNome()).thenReturn("João");
        agenda.adicionarContato(contatoMock);

        boolean resultado = agenda.alterarTelContato("Maria", "9999-8888");

        assertFalse(resultado);
        verify(contatoMock, never()).setTelefone(anyString());
    }

    @Test
    @DisplayName("Deve alterar email buscando pelo nome")
    void testAlterarEmail() {
        when(contatoMock.getNome()).thenReturn("Carlos");
        agenda.adicionarContato(contatoMock);

        boolean resultado = agenda.alterarEmailContato("Carlos", "carlos@novo.com");

        assertTrue(resultado);
        verify(contatoMock).setEmail("carlos@novo.com");
    }

    @Test
    @DisplayName("Deve alterar particularidade (Genérico)")
    void testAlterarParticularidade() {
        when(contatoMock.getNome()).thenReturn("Ana");
        agenda.adicionarContato(contatoMock);

        String novaParticularidade = "Nova Rua 123";
        
        boolean resultado = agenda.alterarParticularidadeContato("Ana", novaParticularidade);

        assertTrue(resultado);
        verify(contatoMock).setParticularidade(novaParticularidade);
    }

    @Test
    @DisplayName("Deve remover contato existente")
    void testRemoverContato() {
        lenient().when(contatoMock.getNome()).thenReturn("Maria");
        lenient().when(contatoMock2.getNome()).thenReturn("João");

        agenda.adicionarContato(contatoMock);
        agenda.adicionarContato(contatoMock2);

        boolean resultado = agenda.removerContato("Maria");

        assertTrue(resultado);
        assertEquals(1, agenda.getContatos().size());
        
        assertEquals(contatoMock2, agenda.getContatos().get(0));
    }

    @Test
    @DisplayName("Não deve remover contato inexistente")
    void testRemoverContatoInexistente() {
        when(contatoMock.getNome()).thenReturn("Pedro");
        agenda.adicionarContato(contatoMock);

        boolean resultado = agenda.removerContato("Mariana");

        assertFalse(resultado);
        assertEquals(1, agenda.getContatos().size());
    }

    @Test
    @DisplayName("Deve formatar toString corretamente com emails")
    void testToStringComContatos() {
        lenient().when(contatoMock.getEmail()).thenReturn("a@teste.com");
        lenient().when(contatoMock2.getEmail()).thenReturn("b@teste.com");
        
        lenient().when(contatoMock.getNome()).thenReturn("A");
        lenient().when(contatoMock2.getNome()).thenReturn("B");

        agenda.adicionarContato(contatoMock);
        agenda.adicionarContato(contatoMock2);

        String resultado = agenda.toString();

        assertNotNull(resultado);
        assertTrue(resultado.contains("a@teste.com"));
        assertTrue(resultado.contains("b@teste.com"));
        assertTrue(resultado.contains("/"));
    }

    @Test
    @DisplayName("ToString deve retornar string 'null' se vazia")
    void testToStringVazio() {
        assertEquals("null", agenda.toString());
    }
    
    @Test
    @DisplayName("Deve obter lista de contatos")
    void testGetContatos() {
        agenda.adicionarContato(contatoMock);
        ArrayList<Pessoa> lista = agenda.getContatos();
        assertEquals(1, lista.size());
        assertTrue(lista.contains(contatoMock));
    }
}