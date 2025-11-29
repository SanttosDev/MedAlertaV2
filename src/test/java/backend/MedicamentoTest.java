package backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicamentoTest {

    @Mock
    Medicamento medicamentoMock;

    @Test
    @DisplayName("Construtor completo deve inicializar todos os atributos corretamente")
    void testaConstrutorCompleto() {
        Medicamento m = new Medicamento("Dipirona", 10.5f, "1g", "Comprimido", "Dor de cabeça", false);

        assertAll("Verificando todos os atributos do construtor completo",
            () -> assertEquals("Dipirona", m.getNome()),
            () -> assertEquals(10.5f, m.getPreco()),
            () -> assertEquals("1g", m.getEspecificacoes()),
            () -> assertEquals("Comprimido", m.getTipoDoRemedio()),
            () -> assertEquals("Dor de cabeça", m.getCondicoesDeUso()),
            () -> assertFalse(m.isRestricao())
        );
    }

    @Test
    @DisplayName("Setters devem alterar o estado do objeto corretamente")
    void testaSetters() {
        Medicamento m = new Medicamento("Inicial");
        
        m.setNome("Modificado");
        m.setPreco(50.9f);
        m.setEspecificacoes("500mg");
        m.setTipoDoRemedio("Xarope");
        m.setCondicoesDeUso("Após refeição");
        m.setRestricao(true);

        assertAll("Verificando funcionamento dos Setters",
            () -> assertEquals("Modificado", m.getNome()),
            () -> assertEquals(50.9f, m.getPreco()),
            () -> assertEquals("500mg", m.getEspecificacoes()),
            () -> assertEquals("Xarope", m.getTipoDoRemedio()),
            () -> assertEquals("Após refeição", m.getCondicoesDeUso()),
            () -> assertTrue(m.isRestricao())
        );
    }

    @Test
    @DisplayName("toString deve formatar atributos separados por vírgula")
    void testaToString() {
        Medicamento m = new Medicamento("Aspirina", 10.0f, "especificacao", "Comprimido", "Dor", false);
        String esperado = "Aspirina,10.0,especificacao,Comprimido,Dor,false";
        
        assertEquals(esperado, m.toString());
    }

    @Test
    @DisplayName("toString deve tratar atributos nulos como a string 'null'")
    void testaToStringComNulos() {
        Medicamento m = new Medicamento("Dipirona"); 

        m.setEspecificacoes(null); 
        m.setTipoDoRemedio(null);
        m.setCondicoesDeUso(null);
        String esperado = "Dipirona,0.0,null,null,null,false";
        
        assertEquals(esperado, m.toString());
    }

    @Test
    @DisplayName("compareTo deve usar Mock para isolar a comparação por nome")
    void testaCompareToComMock() {
        Medicamento m1 = new Medicamento("Aspirina");
        
        // STUB
        when(medicamentoMock.getNome()).thenReturn("Zolpidem");
        assertTrue(m1.compareTo(medicamentoMock) < 0, "Aspirina deve vir antes de Zolpidem");
        Medicamento m2 = new Medicamento("Zolpidem");
        when(medicamentoMock.getNome()).thenReturn("Aspirina");
        assertTrue(m2.compareTo(medicamentoMock) > 0, "Zolpidem deve vir depois de Aspirina");
        
        verify(medicamentoMock, atLeastOnce()).getNome();
    }

    @Test
    @DisplayName("Construtor (Nome) deve inicializar padrão")
    void testaConstrutorApenasNome() {
        Medicamento m = new Medicamento("Simples");
        assertAll(
            () -> assertEquals("Simples", m.getNome()),
            () -> assertEquals(0.0f, m.getPreco())
        );
    }

    @Test
    @DisplayName("Construtor (Nome, Preco, Specs) deve inicializar corretamente")
    void testaConstrutorParcial0() {
        Medicamento m = new Medicamento("Med", 20.0f, "Specs");
        assertAll(
            () -> assertEquals("Med", m.getNome()),
            () -> assertEquals(20.0f, m.getPreco()),
            () -> assertEquals("Specs", m.getEspecificacoes()),
            () -> assertNull(m.getTipoDoRemedio())
        );
    }

    @Test
    @DisplayName("Construtor (Nome, Tipo, Condicoes) deve inicializar corretamente")
    void testaConstrutorParcial1() {
        Medicamento m = new Medicamento("Med", "Gota", "Jejum");
        assertAll(
            () -> assertEquals("Med", m.getNome()),
            () -> assertEquals("Gota", m.getTipoDoRemedio()),
            () -> assertEquals("Jejum", m.getCondicoesDeUso()),
            () -> assertEquals(0.0f, m.getPreco())
        );
    }

    @Test
    @DisplayName("Construtor (Nome, Tipo, Condicoes, Restricao) deve inicializar corretamente")
    void testaConstrutorParcial2() {
        Medicamento m = new Medicamento("Med", "Gota", "Jejum", true);
        assertTrue(m.isRestricao());
    }

    @Test
    @DisplayName("Construtor (Nome, Preco, Tipo, Condicoes, Restricao) deve inicializar corretamente")
    void testaConstrutorParcial3() {
        Medicamento m = new Medicamento("Med", 50f, "Gota", "Jejum", true);
        assertEquals(50f, m.getPreco());
        assertTrue(m.isRestricao());
    }

    @Test
    @DisplayName("Deve aceitar valores de borda (Preço Zero ou Negativo)")
    void testaValoresDeBorda() {
        Medicamento m = new Medicamento("Gratis", 0.0f, "N/A");
        assertEquals(0.0f, m.getPreco());

        m.setPreco(-10.0f);
        assertEquals(-10.0f, m.getPreco(), "Sistema aceita preço negativo atualmente");
    }
}