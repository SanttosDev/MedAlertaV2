package backend;
import backend.gerenciamento.Gerenciador;
import backend.usuario.PessoaFisica;
import backend.usuario.Uso;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GerenciadorTest {

    @Test
    void deveDetectarRemedioAcabandoTest() throws Exception {
        Medicamento remedioMock = mock(Medicamento.class);
        when(remedioMock.getNome()).thenReturn("Paracetamol");

        Uso usoMock = mock(Uso.class);
        when(usoMock.getRemedio()).thenReturn(remedioMock);
        when(usoMock.getQtdDisponivel()).thenReturn(5);
        when(usoMock.getDuracaoDoTratamento()).thenReturn(5); 
        when(usoMock.getHorariosDeUso()).thenReturn(new ArrayList<>(java.util.List.of(8, 12))); 

        Method metodo = Gerenciador.class.getDeclaredMethod("verificarQtdRemedio", Uso.class);
        metodo.setAccessible(true);

        boolean resultado = (boolean) metodo.invoke(null, usoMock);


        assertTrue(resultado, "Deveria avisar que precisa comprar mais remédio!");
    }

    @Test
    void deveConfigurarPessoaTest() throws Exception {
        PessoaFisica pessoaMock = mock(PessoaFisica.class);

        Method metodoSetPessoa = Gerenciador.class.getDeclaredMethod("setPessoa", PessoaFisica.class);
        metodoSetPessoa.setAccessible(true);
        metodoSetPessoa.invoke(null, pessoaMock);

        Field campoPessoa = Gerenciador.class.getDeclaredField("pessoa");
        campoPessoa.setAccessible(true);
        PessoaFisica pessoaAtual = (PessoaFisica) campoPessoa.get(null);

        assertEquals(pessoaMock, pessoaAtual, "O campo 'pessoa' deve ser o mock configurado.");
    }

    @Test
    void naoDeveDetectarRemedioTest() throws Exception {
        Uso usoMock = mock(Uso.class);
        when(usoMock.getQtdDisponivel()).thenReturn(10); 
        when(usoMock.getDuracaoDoTratamento()).thenReturn(5);
        when(usoMock.getHorariosDeUso()).thenReturn(new ArrayList<>(java.util.List.of(8, 12))); 

        Method metodo = Gerenciador.class.getDeclaredMethod("verificarQtdRemedio", Uso.class);
        metodo.setAccessible(true);
        boolean resultado = (boolean) metodo.invoke(null, usoMock);

        assertFalse(resultado, "Não deveria avisar que precisa comprar mais remédio.");
    }

    @Test
    void deveRetornarMenorTest() throws Exception {
        Uso uso1 = mock(Uso.class);
        when(uso1.getIntervalo()).thenReturn(6); 
        Uso uso2 = mock(Uso.class);
        when(uso2.getIntervalo()).thenReturn(12);
        Uso uso3 = mock(Uso.class);
        when(uso3.getIntervalo()).thenReturn(8);

        ArrayList<Uso> listaMocks = new ArrayList<>(java.util.List.of(uso2, uso1, uso3));
        Field campoListaDeUsos = Gerenciador.class.getDeclaredField("listaDeUsos");
        campoListaDeUsos.setAccessible(true);
        campoListaDeUsos.set(null, listaMocks);

        Method metodo = Gerenciador.class.getDeclaredMethod("verificarIntervalo");
        metodo.setAccessible(true);
        int resultado = (int) metodo.invoke(null);

        assertEquals(6, resultado, "O menor intervalo deve ser 6.");

        campoListaDeUsos.set(null, new ArrayList<Uso>());
    }

    @Test
    void deveRetornar24VaziaTest() throws Exception {

        Field campoListaDeUsos = Gerenciador.class.getDeclaredField("listaDeUsos");
        campoListaDeUsos.setAccessible(true);
        campoListaDeUsos.set(null, new ArrayList<Uso>());

        Method metodo = Gerenciador.class.getDeclaredMethod("verificarIntervalo");
        metodo.setAccessible(true);
        int resultado = (int) metodo.invoke(null);

        assertEquals(24, resultado, "Deve retornar 24");
    }
}
