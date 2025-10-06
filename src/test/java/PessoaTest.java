import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import backend.Endereco;
import backend.Pessoa;
import backend.usuario.PessoaFisica;

public class PessoaTest {
    
     private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        Endereco end = new Endereco("Rua A", "10", "Casa", "Bairro", "Cidade", "UF", "Brasil", "00000-000");
        
        pessoa = new PessoaFisica("João", "1234", "joao@teste.com", "123.456.789-00", "senha123", end);
    }

    @Test
    void testGettersESettersBasicos() {
        assertEquals("João", pessoa.getNome());
        assertEquals("1234", pessoa.getTelefone());
        assertEquals("joao@teste.com", pessoa.getEmail());
        assertEquals("senha123", pessoa.getSenha());

        pessoa.setNome("João Silva");
        pessoa.setTelefone("9999");
        pessoa.setEmail("novo@teste.com");
        pessoa.setSenha("novaSenha");

        assertEquals("João Silva", pessoa.getNome());
        assertEquals("9999", pessoa.getTelefone());
        assertEquals("novo@teste.com", pessoa.getEmail());
        assertEquals("novaSenha", pessoa.getSenha());
    }

     @Test
    void testParticularidadeGetterSetter() {
        Endereco novo = new Endereco("Rua B", "20");
        assertNotNull(pessoa.getParticularidade());
        pessoa.setParticularidade(novo);
        Endereco atual = (Endereco) pessoa.getParticularidade();
        assertEquals("Rua B", atual.getNomeDaRua());
        assertEquals("20",  atual.getNumero());
    }

     @Test
    void testCompareToOrdenaPorNome() {
        Endereco e = new Endereco("X", "1");
        Pessoa a = new PessoaFisica("Ana", "1111", "ana@ex.com", "111.111.111-11", "s1", e);
        Pessoa b = new PessoaFisica("Bruno", "2222", "bruno@ex.com", "222.222.222-22", "s2", e);
        Pessoa c = new PessoaFisica("Carlos", "3333", "carlos@ex.com", "333.333.333-33", "s3", e);

        assertTrue(b.compareTo(a) > 0);   // "Bruno" > "Ana"
        assertTrue(b.compareTo(c) < 0);   // "Bruno" < "Carlos"
        Pessoa b2 = new PessoaFisica("Bruno", "0000", "x@y.z", "000.000.000-00", "s", e);
        assertEquals(0, b.compareTo(b2));
    }

}
