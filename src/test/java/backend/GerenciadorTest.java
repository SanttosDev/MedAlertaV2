package backend;

import backend.gerenciamento.Gerenciador;
import backend.usuario.PessoaFisica;
import backend.usuario.Uso;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GerenciadorTest {

    PessoaFisica pessoaMock;

    @BeforeEach 
    void setup() throws Exception {

        pessoaMock = mock(PessoaFisica.class);
        when(pessoaMock.getNomeArquivoUsos()).thenReturn("arquivo_test.txt"); 

        Method metodoSetPessoa = Gerenciador.class.getDeclaredMethod("setPessoa", PessoaFisica.class);
        metodoSetPessoa.setAccessible(true);
        metodoSetPessoa.invoke(null, pessoaMock);
    }
    

    @Test
    void deveDetectarRemedioAcabandoTest() throws Exception {
        Medicamento remedioMock = mock(Medicamento.class);
        when(remedioMock.getNome()).thenReturn("Paracetamol");

        Uso usoMock = mock(Uso.class);
        when(usoMock.getRemedio()).thenReturn(remedioMock);
        when(usoMock.getQtdDisponivel()).thenReturn(5);
        when(usoMock.getDuracaoDoTratamento()).thenReturn(5); 
        when(usoMock.getHorariosDeUso()).thenReturn(new ArrayList<>(List.of(8, 12))); 

        Method metodo = Gerenciador.class.getDeclaredMethod("verificarQtdRemedio", Uso.class);
        metodo.setAccessible(true);
        boolean resultado = (boolean) metodo.invoke(null, usoMock);

        assertTrue(resultado, "Deveria avisar que precisa comprar mais remédio!");
    }

    @Test
    void naoDeveDetectarRemedioTest() throws Exception {
        Uso usoMock = mock(Uso.class);

        when(usoMock.getQtdDisponivel()).thenReturn(10); 
        when(usoMock.getDuracaoDoTratamento()).thenReturn(5);
        when(usoMock.getHorariosDeUso()).thenReturn(new ArrayList<>(List.of(8, 12))); 

        Method metodo = Gerenciador.class.getDeclaredMethod("verificarQtdRemedio", Uso.class);
        metodo.setAccessible(true);
        boolean resultado = (boolean) metodo.invoke(null, usoMock);

        assertFalse(resultado, "Não deveria avisar que precisa comprar mais remédio se a quantidade é suficiente.");
    }


    @Test
    void deveRetornarMenorIntervaloTest() throws Exception {
        Uso uso1 = mock(Uso.class);
        when(uso1.getIntervalo()).thenReturn(6); 
        Uso uso2 = mock(Uso.class);
        when(uso2.getIntervalo()).thenReturn(12);
        Uso uso3 = mock(Uso.class);
        when(uso3.getIntervalo()).thenReturn(8);

        ArrayList<Uso> listaMocks = new ArrayList<>(List.of(uso2, uso1, uso3));
        
        Field campoListaDeUsos = Gerenciador.class.getDeclaredField("listaDeUsos");
        campoListaDeUsos.setAccessible(true);
        campoListaDeUsos.set(null, listaMocks);

        Method metodo = Gerenciador.class.getDeclaredMethod("verificarIntervaloDoGerenciador");
        metodo.setAccessible(true);
        int resultado = (int) metodo.invoke(null);

        assertEquals(6, resultado, "O menor intervalo deve ser 6.");

        campoListaDeUsos.set(null, new ArrayList<Uso>());
    }

    @Test
    void deveRetornar24VaziaTest() throws Exception {

        Field campoListaDeUsos = Gerenciador.class.getDeclaredField("listaDeUsos");
        campoListaDeUsos.setAccessible(true);
        campoListaDeUsos.set(null, new ArrayList<Uso>()); // Lista vazia

        Method metodo = Gerenciador.class.getDeclaredMethod("verificarIntervaloDoGerenciador");
        metodo.setAccessible(true);
        int resultado = (int) metodo.invoke(null);

        assertEquals(24, resultado, "Deve retornar 24 (padrão) quando a lista de usos está vazia.");
    }
    
    @Test
    void atualizaArquivoSeDuracaoNaoZeroTest() throws Exception {

        Uso usoMock = mock(Uso.class);
        when(usoMock.getDuracaoDoTratamento()).thenReturn(5); 
        when(usoMock.toString()).thenReturn("uso_string_atualizada");

        Medicamento remedioMock = mock(Medicamento.class);
        when(remedioMock.getNome()).thenReturn("RemedioA");
        when(usoMock.getRemedio()).thenReturn(remedioMock);

        try (MockedStatic<FuncoesArquivos> funcoesArquivosMock = mockStatic(FuncoesArquivos.class)) {
            
            Method metodo = Gerenciador.class.getDeclaredMethod("atualizarDuracaoDeUso", Uso.class);
            metodo.setAccessible(true);
            metodo.invoke(null, usoMock);


            verify(usoMock).setDuracaoDoTratamento(4);

 
            funcoesArquivosMock.verify(() -> FuncoesArquivos.alterarLinhaArquivo(
                eq("arquivo_test.txt"),
                eq("RemedioA"), 
                eq("uso_string_atualizada")
            ));
        }
    }

    @Test
    void naoAtualizaArquivoSeDuracaoZeroTest() throws Exception {
        Uso usoMock = mock(Uso.class);
        when(usoMock.getDuracaoDoTratamento()).thenReturn(0); 

        try (MockedStatic<FuncoesArquivos> funcoesArquivosMock = mockStatic(FuncoesArquivos.class)) {
            
            Method metodo = Gerenciador.class.getDeclaredMethod("atualizarDuracaoDeUso", Uso.class);
            metodo.setAccessible(true);
            metodo.invoke(null, usoMock);

            verify(usoMock, never()).setDuracaoDoTratamento(anyInt());

            funcoesArquivosMock.verify(() -> FuncoesArquivos.alterarLinhaArquivo(anyString(), anyString(), anyString()), never());
        }
    }

    @Test
    void deveChamarRemoveUsoNaPessoaTest() throws Exception {
        Uso usoMock = mock(Uso.class);
        Medicamento remedioMock = mock(Medicamento.class);
        when(remedioMock.getNome()).thenReturn("RemedioB");
        when(usoMock.getRemedio()).thenReturn(remedioMock);

        Method metodo = Gerenciador.class.getDeclaredMethod("excluirUso", Uso.class);
        metodo.setAccessible(true);
        metodo.invoke(null, usoMock);

        verify(pessoaMock).removerUsoNaListaUsoMedicamentos("RemedioB");
    }
}