import org.junit.jupiter.api.BeforeEach;

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

}
