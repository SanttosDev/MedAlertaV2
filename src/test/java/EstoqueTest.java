import backend.Medicamento;
import backend.farmacia.Estoque;
import backend.farmacia.ItemEstoque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EstoqueTest {

    private Estoque estoque;
    private Medicamento medicamento1;
    private Medicamento medicamento2;

    @BeforeEach
    void setUp() {
        estoque = new Estoque();
        medicamento1 = new Medicamento("Paracetamol", "500mg", "Genérico");
        medicamento2 = new Medicamento("Ibuprofeno", "400mg", "Genérico");
    }

    @Test
    void testAdicionarMedicamentoPorObjeto() {
        ItemEstoque item = new ItemEstoque(medicamento1, 10);
        estoque.addMedicamentoEstoque(item);

        assertEquals(1, estoque.listaEstoque.size());
        assertEquals("Paracetamol", estoque.listaEstoque.get(0).getMedicamento().getNome());
        assertEquals(10, estoque.listaEstoque.get(0).getQntMedicamento());
    }

    @Test
    void testAdicionarMedicamentoPorParametros() {
        estoque.addMedicamentoEstoque(medicamento1, 20);

        assertEquals(1, estoque.listaEstoque.size());
        assertEquals("Paracetamol", estoque.listaEstoque.get(0).getMedicamento().getNome());
        assertEquals(20, estoque.listaEstoque.get(0).getQntMedicamento());
    }

    @Test
    void testRemoverMedicamento() {
        ItemEstoque item = new ItemEstoque(medicamento1, 15);
        estoque.addMedicamentoEstoque(item);

        estoque.removerMedicamentoEstoque(item);

        assertEquals(0, estoque.listaEstoque.size());
    }

    @Test
    void testAtualizarQuantidadeMedicamentoExistente() {
        estoque.addMedicamentoEstoque(medicamento1, 30);

        estoque.atualizarQntMedicamento(medicamento1, 50);

        assertEquals(50, estoque.listaEstoque.get(0).getQntMedicamento());
    }

    @Test
    void testAtualizarQuantidadeMedicamentoInexistente() {
        estoque.addMedicamentoEstoque(medicamento1, 30);

        estoque.atualizarQntMedicamento(medicamento2, 40);

        // Medicamento2 não existe, então não deve alterar nada
        assertEquals(30, estoque.listaEstoque.get(0).getQntMedicamento());
    }

}
