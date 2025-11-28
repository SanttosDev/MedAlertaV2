package backend;
import backend.gerenciamento.Gerenciador;
import backend.usuario.Uso;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GerenciadorTest {

    @Test
    void deveDetectarRemedioAcabandoQuandoQtdInsuficiente() throws Exception {
        // mock do medicamento
        Medicamento remedioMock = mock(Medicamento.class);
        when(remedioMock.getNome()).thenReturn("Paracetamol");

        // criação do Uso com valores controlados
        Uso usoMock = mock(Uso.class);
        when(usoMock.getRemedio()).thenReturn(remedioMock);
        when(usoMock.getQtdDisponivel()).thenReturn(5); // só tem 5 comprimidos
        when(usoMock.getDuracaoDoTratamento()).thenReturn(5); // tratamento dura 5 dias
        when(usoMock.getHorariosDeUso()).thenReturn(new ArrayList<>(java.util.List.of(8, 12))); // 2x ao dia

        // acesso ao método privado
        Method metodo = Gerenciador.class.getDeclaredMethod("verificarQtdRemedio", Uso.class);
        metodo.setAccessible(true);

        // execução
        boolean resultado = (boolean) metodo.invoke(null, usoMock);

        // validação
        assertTrue(resultado, "Deveria avisar que precisa comprar mais remédio!");
    }
}
