import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}
