import backend.Endereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {



    @Test
    @DisplayName("Conversão completa para string com todos os campos preenchidos")
    void toStringCompleto() {
        Endereco e = new Endereco("Rua", "232", "Ap", "bras", "city", "sp", "Brasil", "24465300");
        String esperado = "Rua/232/Ap/bras/city/sp/Brasil/24465300";
        assertEquals(esperado, e.toString());
    }

    @Test
    @DisplayName("Conversão para string com campos nulos")
    void testToStringNulls() {
        Endereco e = new Endereco("Rua", "232");
        String esperado = "Rua/232/null/null/null/null/null/null";
        assertEquals(esperado, e.toString());
    }

    @Test
    @DisplayName("front-toString")
    void toStringFront() {
        Endereco e = new Endereco("Rua", "232", "Ap 4", "teste", "rio", "RJ", "Brasil", "24465-300");
        String esperado = "Rua,232,Ap 4,teste,rio,RJ,Brasil,24465-300";
        assertEquals(esperado, e.toStringFront());
    }

    @Test
    @DisplayName("front ignorando valores nulos")
    void toStringFrontNulls() {
        Endereco e = new Endereco("Rua", "232");
        String esperado = "Rua,232";
        assertEquals(esperado, e.toStringFront());
    }

    @Test
    @DisplayName("Endereco a partir de string")
    void stringToEnderecoCompleto() {
        String str = "Rua/232/Ap/bairro/rio/rj/Brasil/24465300";
        Endereco e = Endereco.stringToEndereco(str);
        assertEquals("Rua", e.getNomeDaRua());
        assertEquals("232", e.getNumero());
        assertEquals("Ap", e.getComplemento());
        assertEquals("bairro", e.getNomeDoBairro());
        assertEquals("rio", e.getNomeDaCidade());
        assertEquals("rj", e.getNomeDoEstado());
        assertEquals("Brasil", e.getNomeDoPais());
        assertEquals("24465300", e.getCep());
    }

    @Test
    @DisplayName("Endereco a partir de string com campos nulos")
    void stringToEnderecoComNulos() {
        String str = "Rua/232/null/null/null/null/null/null";
        Endereco e = Endereco.stringToEndereco(str);
        assertEquals("Rua", e.getNomeDaRua());
        assertEquals("232", e.getNumero());
        assertNull(e.getComplemento());
        assertNull(e.getNomeDoBairro());
        assertNull(e.getNomeDaCidade());
        assertNull(e.getNomeDoEstado());
        assertNull(e.getNomeDoPais());
        assertNull(e.getCep());
    }
}
