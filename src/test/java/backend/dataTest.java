package backend;

import backend.gerenciamento.Data;
import backend.usuario.Uso;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class DataTest {

    private void setUltimaVerificacao(int value) throws Exception {
        Field field = Data.class.getDeclaredField("ultimaVerficacaoHorario");
        field.setAccessible(true);
        field.set(null, value);
    }

    @Test
    void formatarDiaDeveMapearCorretamente() {
        assertEquals(Calendar.MONDAY, Data.formatarDia("seg"));
        assertEquals(Calendar.TUESDAY, Data.formatarDia("ter"));
        assertEquals(Calendar.WEDNESDAY, Data.formatarDia("qua"));
        assertEquals(Calendar.THURSDAY, Data.formatarDia("qui"));
        assertEquals(Calendar.FRIDAY, Data.formatarDia("sex"));
        assertEquals(Calendar.SATURDAY, Data.formatarDia("sab"));
        assertEquals(Calendar.SUNDAY, Data.formatarDia("dom"));
        assertEquals(Calendar.SUNDAY, Data.formatarDia("qualquer-coisa"));
    }

    @Test
    void verificarUltimaVerificacaoDeveRetornarFalseQuandoHoraJaVerificada() throws Exception {
        setUltimaVerificacao(10);
        assertFalse(Data.verificarUltimaVerificacao(10));
    }

    @Test
    void verificarUltimaVerificacaoDeveRetornarTrueQuandoHoraNova() throws Exception {
        setUltimaVerificacao(9);
        assertTrue(Data.verificarUltimaVerificacao(10));
    }

    @Test
    void verificarDiaDeveRetornarTrueQuandoHojeEstaNaLista() {
        Calendar mockCalendar = Mockito.mock(Calendar.class);

        try (MockedStatic<Calendar> calendarStatic = Mockito.mockStatic(Calendar.class)) {
            calendarStatic.when(Calendar::getInstance).thenReturn(mockCalendar);
            Mockito.when(mockCalendar.get(Calendar.DAY_OF_WEEK)).thenReturn(Calendar.MONDAY);

            ArrayList<String> dias = new ArrayList<>();
            dias.add("seg");
            dias.add("qua");

            assertTrue(Data.verificarDia(dias));
        }
    }

    @Test
    void verificarDiaDeveRetornarFalseQuandoHojeNaoEstaNaLista() {
        Calendar mockCalendar = Mockito.mock(Calendar.class);

        try (MockedStatic<Calendar> calendarStatic = Mockito.mockStatic(Calendar.class)) {
            calendarStatic.when(Calendar::getInstance).thenReturn(mockCalendar);
            Mockito.when(mockCalendar.get(Calendar.DAY_OF_WEEK)).thenReturn(Calendar.MONDAY);

            ArrayList<String> dias = new ArrayList<>();
            dias.add("ter");
            dias.add("qua");

            assertFalse(Data.verificarDia(dias));
        }
    }

    @Test
    void verificarHoraDeveRetornarTrueQuandoHoraAtualEHoraPassadaForemIguaisEPrimeiraVerificacao() throws Exception {
        setUltimaVerificacao(9);

        Calendar mockCalendar = Mockito.mock(Calendar.class);

        try (MockedStatic<Calendar> calendarStatic = Mockito.mockStatic(Calendar.class)) {
            calendarStatic.when(Calendar::getInstance).thenReturn(mockCalendar);
            Mockito.when(mockCalendar.get(Calendar.HOUR_OF_DAY)).thenReturn(10);

            assertTrue(Data.verificarHora(10));
        }
    }

    @Test
    void verificarHoraDeveRetornarFalseQuandoHoraJaFoiVerificada() throws Exception {
        setUltimaVerificacao(10);

        Calendar mockCalendar = Mockito.mock(Calendar.class);

        try (MockedStatic<Calendar> calendarStatic = Mockito.mockStatic(Calendar.class)) {
            calendarStatic.when(Calendar::getInstance).thenReturn(mockCalendar);
            Mockito.when(mockCalendar.get(Calendar.HOUR_OF_DAY)).thenReturn(10);

            assertFalse(Data.verificarHora(10));
        }
    }

    @Test
    void verificarHoraDeveRetornarFalseQuandoHoraDiferenteDaHoraAtual() throws Exception {
        setUltimaVerificacao(0);

        Calendar mockCalendar = Mockito.mock(Calendar.class);

        try (MockedStatic<Calendar> calendarStatic = Mockito.mockStatic(Calendar.class)) {
            calendarStatic.when(Calendar::getInstance).thenReturn(mockCalendar);
            Mockito.when(mockCalendar.get(Calendar.HOUR_OF_DAY)).thenReturn(15);

            assertFalse(Data.verificarHora(10));
        }
    }


    @Test
    void ehMeiaNoiteDeveRetornarTrueQuandoHoraZero() {
        Calendar mockCalendar = Mockito.mock(Calendar.class);

        try (MockedStatic<Calendar> calendarStatic = Mockito.mockStatic(Calendar.class)) {
            calendarStatic.when(Calendar::getInstance).thenReturn(mockCalendar);
            Mockito.when(mockCalendar.get(Calendar.HOUR_OF_DAY)).thenReturn(0);

            assertTrue(Data.ehMeiaNoite());
        }
    }

    @Test
    void ehMeiaNoiteDeveRetornarFalseQuandoHoraNaoZero() {
        Calendar mockCalendar = Mockito.mock(Calendar.class);

        try (MockedStatic<Calendar> calendarStatic = Mockito.mockStatic(Calendar.class)) {
            calendarStatic.when(Calendar::getInstance).thenReturn(mockCalendar);
            Mockito.when(mockCalendar.get(Calendar.HOUR_OF_DAY)).thenReturn(13);

            assertFalse(Data.ehMeiaNoite());
        }
    }


    @Test
    void horaDoRemedioDeveRetornarTrueQuandoHoraEEDiaCorretos() throws Exception {
        setUltimaVerificacao(9); // ainda não verificou a hora 10

        Calendar mockCalendar = Mockito.mock(Calendar.class);

        try (MockedStatic<Calendar> calendarStatic = Mockito.mockStatic(Calendar.class)) {
            calendarStatic.when(Calendar::getInstance).thenReturn(mockCalendar);
            Mockito.when(mockCalendar.get(Calendar.HOUR_OF_DAY)).thenReturn(10);
            Mockito.when(mockCalendar.get(Calendar.DAY_OF_WEEK)).thenReturn(Calendar.MONDAY);

            Uso uso = Mockito.mock(Uso.class);
            ArrayList<String> dias = new ArrayList<>();
            dias.add("seg"); // segunda
            Mockito.when(uso.getHorarios()).thenReturn(dias);

            assertTrue(Data.horaDoRemedio(uso, 10));
        }
    }

    @Test
    void horaDoRemedioDeveRetornarFalseQuandoNaoEhDiaConfigurado() throws Exception {
        setUltimaVerificacao(9);

        Calendar mockCalendar = Mockito.mock(Calendar.class);

        try (MockedStatic<Calendar> calendarStatic = Mockito.mockStatic(Calendar.class)) {
            calendarStatic.when(Calendar::getInstance).thenReturn(mockCalendar);
            Mockito.when(mockCalendar.get(Calendar.HOUR_OF_DAY)).thenReturn(10);
            Mockito.when(mockCalendar.get(Calendar.DAY_OF_WEEK)).thenReturn(Calendar.MONDAY);

            Uso uso = Mockito.mock(Uso.class);
            ArrayList<String> dias = new ArrayList<>();
            dias.add("ter"); // terça
            Mockito.when(uso.getHorarios()).thenReturn(dias);

            assertFalse(Data.horaDoRemedio(uso, 10));
        }
    }
}
