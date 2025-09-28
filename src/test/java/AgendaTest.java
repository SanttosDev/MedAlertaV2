import backend.Agenda;
import backend.Endereco;
import backend.usuario.PessoaFisica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AgendaTest {

    private Agenda agenda;
    private PessoaFisica contato;

    @BeforeEach
    void setUp() {
        agenda = new Agenda();
        Endereco endereco = new Endereco("Avenida Central", "500", "Casa 1", "Jardim", "Rio de Janeiro", "RJ", "Brasil", "20000-000");
        contato = new PessoaFisica("Maria", "9876", "maria@teste.com", "987.654.321-00", "senha456", endereco);
    }

    @Test
    void testAdicionarContatoValido() {
        agenda.adicionarContato(contato);
        assertEquals(1, agenda.getContatos().size());
        assertEquals("Maria", agenda.getContatos().get(0).getNome());
    }

    @Test
    void testAdicionarContatoInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> agenda.adicionarContato(null));
        assertEquals("É necessário informar um contato válido", exception.getMessage());
    }

    @Test
    void testAlterarTelefoneExistente() {
        agenda.adicionarContato(contato);
        boolean resultado = agenda.alterarTelContato("Maria", "5555");
        assertTrue(resultado);
        assertEquals("5555", contato.getTelefone());
    }

    @Test
    void testAlterarTelefoneInexistente() {
        boolean resultado = agenda.alterarTelContato("Ana", "4444");
        assertFalse(resultado);
    }

    @Test
    void testAlterarEmail() {
        agenda.adicionarContato(contato);
        boolean resultado = agenda.alterarEmailContato("Maria", "novoemail@teste.com");
        assertTrue(resultado);
        assertEquals("novoemail@teste.com", contato.getEmail());
    }

    @Test
    void testAlterarParticularidade() {
        agenda.adicionarContato(contato);
        Endereco novoEndereco = new Endereco("Rua das Flores", "789");
        boolean resultado = agenda.alterarParticularidadeContato("Maria", novoEndereco);
        assertTrue(resultado);
        assertEquals("Rua das Flores", ((Endereco) contato.getParticularidade()).getNomeDaRua());
    }

    @Test
    void testRemoverContatoExistente() {
        agenda.adicionarContato(contato);
        boolean resultado = agenda.removerContato("Maria");
        assertTrue(resultado);
        assertEquals(0, agenda.getContatos().size());
    }

    @Test
    void testRemoverContatoInexistente() {
        boolean resultado = agenda.removerContato("Ana");
        assertFalse(resultado);
    }

    @Test
    void testStringComContato() {
        agenda.adicionarContato(contato);
        assertEquals("maria@teste.com", agenda.toString());
    }

    @Test
    void testStringVazio() {
        assertEquals("null", agenda.toString());
    }
}
