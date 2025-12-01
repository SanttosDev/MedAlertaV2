package backend.usuario;

import backend.Medicamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsoTest {

    private Uso uso;

    @Mock
    private Medicamento medicamentoMock;

    private ArrayList<String> diasSemana;

    @BeforeEach
    void setUp() {
        diasSemana = new ArrayList<>(Arrays.asList("seg", "qua", "sex"));
        lenient().when(medicamentoMock.getNome()).thenReturn("Dipirona");

        uso = new Uso(medicamentoMock, 1, diasSemana, 7, 20, 8, 8);
    }

    @Test
    @DisplayName("Construtor completo deve inicializar todos atributos")
    void testConstrutorCompleto() {
        assertEquals(medicamentoMock, uso.getRemedio());
        assertEquals(1, uso.getDose());
        assertEquals(diasSemana, uso.getHorarios());
        assertEquals(7, uso.getDuracaoDoTratamento());
        assertEquals(20, uso.getQtdDisponivel());
        assertEquals(8, uso.getHorarioDeInicio());
        assertEquals(8, uso.getIntervalo());
    }

    @Test
    @DisplayName("Construtor simplificado deve inicializar atributos e deixar intervalo/hora padrão (0)")
    void testConstrutorSimplificado() {
        Uso usoSimples = new Uso(medicamentoMock, 2, diasSemana, 5, 10);
        
        assertEquals(2, usoSimples.getDose());
        assertEquals(5, usoSimples.getDuracaoDoTratamento());
        assertEquals(10, usoSimples.getQtdDisponivel());
        assertEquals(0, usoSimples.getHorarioDeInicio());
        assertEquals(0, usoSimples.getIntervalo());
    }

    @Test
    @DisplayName("Setters devem atualizar valores válidos")
    void testSettersValidos() {
        Medicamento novoMed = mock(Medicamento.class);
        uso.setRemedio(novoMed);
        uso.setHorarioDeInicio(10);
        uso.setIntervalo(12);
    
        assertDoesNotThrow(() -> uso.setDose(5));
        assertDoesNotThrow(() -> uso.setTipoDoRemedio("Xarope"));
        assertDoesNotThrow(() -> uso.setDuracaoDoTratamento(10));
        assertDoesNotThrow(() -> uso.setQtdDisponivel(50));
        
        ArrayList<String> novosDias = new ArrayList<>(List.of("dom"));
        uso.setHorarios(novosDias);

        assertAll(
            () -> assertEquals(novoMed, uso.getRemedio()),
            () -> assertEquals(10, uso.getHorarioDeInicio()),
            () -> assertEquals(12, uso.getIntervalo()),
            () -> assertEquals(5, uso.getDose()),
            () -> assertEquals("Xarope", uso.getTipoDoRemedio()),
            () -> assertEquals(10, uso.getDuracaoDoTratamento()),
            () -> assertEquals(50, uso.getQtdDisponivel()),
            () -> assertEquals(novosDias, uso.getHorarios())
        );
    }

    @Test
    @DisplayName("setDose deve lançar exceção para valores negativos")
    void testSetDoseInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            uso.setDose(-1);
        });
        assertEquals("Não é possível setar número negativo para dose.", exception.getMessage());
    }

    @Test
    @DisplayName("setDuracaoDoTratamento deve lançar exceção para valores negativos")
    void testSetDuracaoInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            uso.setDuracaoDoTratamento(-5);
        });
        assertEquals("Não é possível setar número negativo para duração do Tratamento.", exception.getMessage());
    }

    @Test
    @DisplayName("setQtdDisponivel deve lançar exceção para valores negativos")
    void testSetQtdInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            uso.setQtdDisponivel(-10);
        });
        assertEquals("Não é possível setar quantidade negativa de um medicamento.", exception.getMessage());
    }

    @Test
    @DisplayName("setTipoDoRemedio deve lançar exceção se string for vazia")
    void testSetTipoVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            uso.setTipoDoRemedio("");
        });
        assertEquals("É necessário informar tipo do remédio.", exception.getMessage());
    }

    @Test
    @DisplayName("horariosToString deve unir lista com barras")
    void testHorariosToString() {
        String resultado = uso.horariosToString();
        assertEquals("seg/qua/sex", resultado);
    }

    @Test
    @DisplayName("horariosStringToList deve separar string por barras")
    void testHorariosStringToList() {
        String entrada = "seg/qua/sex";
        ArrayList<String> lista = Uso.horariosStringToList(entrada);
        
        assertEquals(3, lista.size());
        assertEquals("seg", lista.get(0));
        assertEquals("qua", lista.get(1));
        assertEquals("sex", lista.get(2));
    }

    @Test
    @DisplayName("toString deve gerar CSV correto")
    void testToStringCSV() {
        String esperado = "Dipirona,1,seg/qua/sex,7,20,8,8";
        assertEquals(esperado, uso.toString());
    }

    @Test
    @DisplayName("stringToUso deve recriar objeto a partir do CSV")
    void testStringToUso() {
        String linhaCSV = "Paracetamol,2,seg/ter,10,30,8,12";
        
        Uso usoRecuperado = Uso.stringToUso(linhaCSV);

        assertAll(
            () -> assertEquals("Paracetamol", usoRecuperado.getRemedio().getNome()),
            () -> assertEquals(2, usoRecuperado.getDose()),
            () -> assertEquals(2, usoRecuperado.getHorarios().size()),
            () -> assertEquals("seg", usoRecuperado.getHorarios().get(0)),
            () -> assertEquals(10, usoRecuperado.getDuracaoDoTratamento()),
            () -> assertEquals(30, usoRecuperado.getQtdDisponivel()),
            () -> assertEquals(8, usoRecuperado.getHorarioDeInicio()),
            () -> assertEquals(12, usoRecuperado.getIntervalo())
        );
    }

    @Test
    @DisplayName("calcularHorariosDeUso: Intervalo > 0 (Branch Else)")
    void testCalcularHorariosComIntervalo() {
        
        uso.setHorarioDeInicio(8);
        uso.setIntervalo(8);
        
        uso.calcularHorariosDeUso();
        
        ArrayList<Integer> horariosCalculados = uso.getHorariosDeUso();
        assertEquals(3, horariosCalculados.size());
        assertTrue(horariosCalculados.contains(8));
        assertTrue(horariosCalculados.contains(16));
        assertTrue(horariosCalculados.contains(24)); 
    }

    @Test
    @DisplayName("calcularHorariosDeUso: Intervalo = 0 (Branch If)")
    void testCalcularHorariosSemIntervalo() {
        uso.setHorarioDeInicio(9);
        uso.setIntervalo(0);
        
        uso.calcularHorariosDeUso();
        
        ArrayList<Integer> horariosCalculados = uso.getHorariosDeUso();
        assertEquals(1, horariosCalculados.size(), "Deveria ter apenas 1 horário");
        assertEquals(9, horariosCalculados.get(0));
    }
    
    @Test
    @DisplayName("calcularHorariosDeUso: Intervalo pequeno gera muitos horários")
    void testCalcularHorariosIntervaloPequeno() {
        uso.setHorarioDeInicio(0);
        uso.setIntervalo(4);
        
        uso.calcularHorariosDeUso();
        
        List<Integer> lista = uso.getHorariosDeUso();
        assertEquals(6, lista.size());
        assertEquals(0, lista.get(0));
        assertEquals(20, lista.get(5));
    }
}