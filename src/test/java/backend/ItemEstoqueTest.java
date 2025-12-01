package backend;


import backend.farmacia.ItemEstoque;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ItemEstoqueTest {

    @Test
    void constructorAndGettersShouldWork() {
        Medicamento medicamento = Mockito.mock(Medicamento.class);
        ItemEstoque item = new ItemEstoque(medicamento, 10);

        assertSame(medicamento, item.getMedicamento());
        assertEquals(10, item.getQntMedicamento());
    }

    @Test
    void setQntMedicamentoShouldUpdateQuantity() {
        Medicamento medicamento = Mockito.mock(Medicamento.class);
        ItemEstoque item = new ItemEstoque(medicamento, 5);

        item.setQntMedicamento(20);

        assertEquals(20, item.getQntMedicamento());
    }

    @Test
    void toStringShouldConcatenateMedicamentoToStringAndQuantity() {
        Medicamento medicamento = Mockito.mock(Medicamento.class);
        Mockito.when(medicamento.toString()).thenReturn("MEDICAMENTO_CSV");

        ItemEstoque item = new ItemEstoque(medicamento, 7);

        String result = item.toString();

        assertEquals("MEDICAMENTO_CSV,7", result);
    }

    @Test
    void stringToItemEstoqueShouldParseCorrectly() {
        String csv = "Dipirona,12.5,Comprimido,Analgésico,Temperatura ambiente,false,30";

        ItemEstoque item = ItemEstoque.stringToItemEstoque(csv);

        assertNotNull(item);
        assertEquals(30, item.getQntMedicamento());
        assertNotNull(item.getMedicamento());
        assertEquals("Dipirona", item.getMedicamento().getNome());
    }
}
