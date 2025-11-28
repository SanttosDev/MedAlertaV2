package backend;
import backend.Medicamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MedicamentoTest {

    @Test
    @DisplayName("Construtor deve inicializar todos os atributos")
    void testaConstrutor(){
        Medicamento m = new Medicamento("Dipirona", 10.5f, "1g", "Comprimido", "Dor de cabeça", false);
        assertEquals("Dipirona", m.getNome());
        assertEquals(10.5f, m.getPreco());
        assertEquals("1g", m.getEspecificacoes());
        assertEquals("Comprimido", m.getTipoDoRemedio());
        assertEquals("Dor de cabeça", m.getCondicoesDeUso());
        assertFalse(m.isRestricao());
    }

    @Test
    @DisplayName("Setters devem alterar os atributos")
    void testaSetters(){
        Medicamento m = new Medicamento("Teste");

        m.setEspecificacoes("1g");
        m.setPreco(50.9f);
        m.setNome("Teste2");
        m.setTipoDoRemedio("Tipo teste");
        m.setCondicoesDeUso("Condicao teste");
        m.setRestricao(false);

        assertEquals("1g", m.getEspecificacoes());
        assertEquals(50.9f, m.getPreco());
        assertEquals("Teste2", m.getNome());
        assertEquals("Tipo teste", m.getTipoDoRemedio());
        assertEquals("Condicao teste", m.getCondicoesDeUso());
        assertFalse(m.isRestricao());
    }

    @Test
    @DisplayName("toString deve retornar os atributos formatados separados por ','")
    void testaToString(){
        Medicamento m = new Medicamento("Dipirona", 10.5f, "1g", "Comprimido", "Dor de cabeça", false);

        String esperado = "Dipirona,10.5,1g,Comprimido,Dor de cabeça,false";
        assertEquals(esperado, m.toString());
    }

    @Test
    @DisplayName("toString deve retornar os atributos formatados separados por ',' contendo null no lugar dos atributos nulos")
    void testaToStringNull(){
        Medicamento m = new Medicamento("Dipirona", 10.5f, "1g", null, null, false);

        String esperado = "Dipirona,10.5,1g,null,null,false";
        assertEquals(esperado, m.toString());
    }

    @Test
    @DisplayName("compareTo deve ordenar por nome corretamente")
    void testaCompareTo(){
        Medicamento m1 = new Medicamento("Aspirina");
        Medicamento m2 = new Medicamento("Dipirona");
        Medicamento m3 = new Medicamento("Aspirina");

        assertTrue(m1.compareTo(m2) < 0); 
        assertTrue(m2.compareTo(m1) > 0); 
        assertEquals(0, m1.compareTo(m3)); 
    }
}