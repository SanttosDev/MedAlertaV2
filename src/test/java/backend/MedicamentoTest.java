package backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedicamentoTest {

    @Test
    void testConstrutor1() {
        Medicamento m = new Medicamento("Dipirona");
        assertEquals("Dipirona", m.getNome());
    }

    @Test
    void testConstrutor2() {
        Medicamento m = new Medicamento("Aspirina", 10.50f, "500mg");
        assertEquals("Aspirina", m.getNome());
        assertEquals(10.50f, m.getPreco(), 0.001);
        assertEquals("500mg", m.getEspecificacoes());
    }

    @Test
    void testConstrutor3() {
        Medicamento m = new Medicamento("Xarope", "Líquido", "Após almoço");
        assertEquals("Xarope", m.getNome());
        assertEquals("Líquido", m.getTipoDoRemedio());
        assertEquals("Após almoço", m.getCondicoesDeUso());
    }

    @Test
    void testConstrutor4() {
        Medicamento m = new Medicamento("Antibiotico", "Comprimido", "8 em 8 horas", true);
        assertEquals("Antibiotico", m.getNome());
        assertTrue(m.isRestricao());
    }

    @Test
    void testConstrutor5() {
        Medicamento m = new Medicamento("Vitamina C", 20.0f, "Efervescente", "Manhã", false);
        assertEquals("Vitamina C", m.getNome());
        assertEquals(20.0f, m.getPreco(), 0.001);
        assertFalse(m.isRestricao());
    }

    @Test
    void testConstrutorCompleto() {
        Medicamento m = new Medicamento("Tylenol", 15.0f, "750mg", "Comprimido", "Livre", false);
        assertEquals("Tylenol", m.getNome());
        assertEquals(15.0f, m.getPreco(), 0.001);
        assertEquals("750mg", m.getEspecificacoes());
        assertEquals("Comprimido", m.getTipoDoRemedio());
        assertEquals("Livre", m.getCondicoesDeUso());
        assertFalse(m.isRestricao());
    }

    @Test
    void testSetters() {
        Medicamento m = new Medicamento("Teste");
        
        m.setNome("NovoNome");
        assertEquals("NovoNome", m.getNome());

        m.setPreco(50.0f);
        assertEquals(50.0f, m.getPreco(), 0.001);

        m.setEspecificacoes("NovaSpec");
        assertEquals("NovaSpec", m.getEspecificacoes());

        m.setTipoDoRemedio("Injetável");
        assertEquals("Injetável", m.getTipoDoRemedio());

        m.setCondicoesDeUso("Jejum");
        assertEquals("Jejum", m.getCondicoesDeUso());

        m.setRestricao(true);
        assertTrue(m.isRestricao());
    }


    @Test
    void testToStringNullSpecFalseRestr() {
        Medicamento m = new Medicamento("Dorflex", "Comprimido", "Uso oral");
        
        String resultado = m.toString();
        
        assertTrue(resultado.contains("Dorflex"));
        assertTrue(resultado.contains("null")); 
        assertTrue(resultado.contains("false")); 
    }

    @Test
    void testToStringValidSpecTrueRestr() {
        Medicamento m = new Medicamento("Morfina", 100f, "Alta dose", "Injetável", "Hospitalar", true);
        
        String resultado = m.toString();
        
        assertTrue(resultado.contains("Alta dose")); 
        assertTrue(resultado.contains("true"));      
    }

    @Test
    void testCompareTo() {
        Medicamento m1 = new Medicamento("Aspirina");
        Medicamento m2 = new Medicamento("Dipirona");
        Medicamento m3 = new Medicamento("Aspirina");

        assertTrue(m1.compareTo(m2) < 0);
        
        assertTrue(m2.compareTo(m1) > 0);
        
        assertEquals(0, m1.compareTo(m3));
    }
}
