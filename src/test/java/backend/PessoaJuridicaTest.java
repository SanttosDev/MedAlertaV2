package backend;

import backend.farmacia.Estoque;
import backend.farmacia.PessoaJuridica;
import backend.usuario.PessoaFisica;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.nio.file.Path;
import org.junit.jupiter.api.io.TempDir;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PessoaJuridicaTest {

    private PessoaJuridica novaPessoaJuridicaBasica() {
        Endereco endereco = mock(Endereco.class);
        return new PessoaJuridica(
                "Farmacia Teste",
                "123456789",
                "farmacia@teste.com",
                "senha123",
                "CNPJ123",
                endereco
        );
    }

    @TempDir
    Path tempDir;


    @Test
    void construtorDeveInicializarCamposBasicos() {
        Endereco endereco = mock(Endereco.class);

        PessoaJuridica pj = new PessoaJuridica(
                "Farmacia A",
                "99999999",
                "a@farmacia.com",
                "senha",
                "CNPJ-ABC",
                endereco
        );

        assertEquals("Farmacia A", pj.getNome());
        assertEquals("99999999", pj.getTelefone());
        assertEquals("a@farmacia.com", pj.getEmail());
        assertEquals("CNPJ-ABC", pj.getCnpj());
        assertEquals(endereco, pj.getEndereco());
        assertNull(pj.getEstoque());
        assertNull(pj.getContatosClientes());
    }


    @Test
    void setCnpjSemModificarArquivoNaoDeveChamarFuncoesArquivos() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();

        try (MockedStatic<FuncoesArquivos> funcoesMock =
                     Mockito.mockStatic(FuncoesArquivos.class)) {

            pj.setCnpj("NOVO-CNPJ", false);

            assertEquals("NOVO-CNPJ", pj.getCnpj());
            funcoesMock.verifyNoInteractions();
        }
    }

    @Test
    void setCnpjComModificarArquivoDeveChamarFuncoesArquivos() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();

        try (MockedStatic<FuncoesArquivos> funcoesMock =
                     Mockito.mockStatic(FuncoesArquivos.class)) {

            pj.setCnpj("NOVO-CNPJ", true);

            assertEquals("NOVO-CNPJ", pj.getCnpj());

            funcoesMock.verify(() ->
                    FuncoesArquivos.alterarInfoArquivo(
                            PessoaJuridica.nomeArquivoFarmacias,
                            pj.getNome(),
                            4,
                            "NOVO-CNPJ"
                    )
            );
        }
    }


    @Test
    void setEnderecoSemModificarArquivoAtualizaApenasMemoria() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();
        Endereco novoEndereco = mock(Endereco.class);

        try (MockedStatic<FuncoesArquivos> funcoesMock =
                     Mockito.mockStatic(FuncoesArquivos.class)) {

            pj.setEndereco(novoEndereco, false);

            assertEquals(novoEndereco, pj.getEndereco());
            funcoesMock.verifyNoInteractions();
        }
    }

    @Test
    void setEnderecoComModificarArquivoDeveChamarFuncoesArquivos() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();
        Endereco novoEndereco = mock(Endereco.class);
        when(novoEndereco.toString()).thenReturn("ENDERECO_MOCK");

        try (MockedStatic<FuncoesArquivos> funcoesMock =
                     Mockito.mockStatic(FuncoesArquivos.class)) {

            pj.setEndereco(novoEndereco, true);

            assertEquals(novoEndereco, pj.getEndereco());

            funcoesMock.verify(() ->
                    FuncoesArquivos.alterarInfoArquivo(
                            PessoaJuridica.nomeArquivoFarmacias,
                            pj.getNome(),
                            5,
                            "ENDERECO_MOCK"
                    )
            );
        }
    }


    @Test
    void setEstoqueSemModificarArquivoNaoDevePersistir() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();
        Estoque estoque = mock(Estoque.class);

        PessoaJuridica spy = Mockito.spy(pj);

        doNothing().when(spy).salvarEstoqueArquivo();

        spy.setEstoque(estoque, false);

        assertEquals(estoque, spy.getEstoque());
        verify(spy, never()).salvarEstoqueArquivo();
    }

    @Test
    void setEstoqueComModificarArquivoDeveChamarSalvarEstoqueArquivo() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();
        Estoque estoque = mock(Estoque.class);

        PessoaJuridica spy = Mockito.spy(pj);
        doNothing().when(spy).salvarEstoqueArquivo();

        spy.setEstoque(estoque, true);

        assertEquals(estoque, spy.getEstoque());
        verify(spy, times(1)).salvarEstoqueArquivo();
    }

    @Test
    void setContatosClientesComModificarArquivoDeveChamarFuncoesArquivos() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();
        Agenda agenda = mock(Agenda.class);
        when(agenda.toString()).thenReturn("AGENDA_MOCK");

        try (MockedStatic<FuncoesArquivos> funcoesMock =
                     Mockito.mockStatic(FuncoesArquivos.class)) {

            pj.setContatosClientes(agenda, true);

            assertEquals(agenda, pj.getContatosClientes());

            funcoesMock.verify(() ->
                    FuncoesArquivos.alterarInfoArquivo(
                            PessoaJuridica.nomeArquivoFarmacias,
                            pj.getNome(),
                            7,
                            "AGENDA_MOCK"
                    )
            );
        }
    }

    @Test
    void addUsuarioAosContatosQuandoJaExisteAgendaDeveAdicionarContatoEAtualizarArquivo() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();

        Agenda agenda = mock(Agenda.class);
        when(agenda.toString()).thenReturn("AGENDA_MOCK");
        pj.setContatosClientes(agenda, false); 

        PessoaFisica usuario = mock(PessoaFisica.class);

        try (MockedStatic<FuncoesArquivos> funcoesMock =
                     Mockito.mockStatic(FuncoesArquivos.class)) {

            pj.addUsuarioAosContatos(usuario);

            verify(agenda, times(1)).adicionarContato(usuario);

            funcoesMock.verify(() ->
                    FuncoesArquivos.alterarInfoArquivo(
                            PessoaJuridica.nomeArquivoFarmacias,
                            pj.getNome(),
                            7,
                            "AGENDA_MOCK"
                    )
            );
        }
    }


    @Test
    void getParticularidadeDeveRetornarEndereco() {
        Endereco endereco = mock(Endereco.class);
        PessoaJuridica pj = new PessoaJuridica(
                "Farmacia",
                "1111",
                "email@x.com",
                "senha",
                "CNPJ",
                endereco
        );

        assertSame(endereco, pj.getParticularidade());
    }

    @Test
    void setParticularidadeDeveAtualizarEndereco() {
        Endereco enderecoInicial = mock(Endereco.class);
        PessoaJuridica pj = new PessoaJuridica(
                "Farmacia",
                "1111",
                "email@x.com",
                "senha",
                "CNPJ",
                enderecoInicial
        );

        Endereco novoEndereco = mock(Endereco.class);

        pj.setParticularidade(novoEndereco);

        assertSame(novoEndereco, pj.getEndereco());
    }


    @Test
    void getNomeArquivoEstoqueDeveMontarCaminhoCorreto() {
        Endereco endereco = mock(Endereco.class);
        PessoaJuridica pj = new PessoaJuridica(
                "Farmacia",
                "1111",
                "email@x.com",
                "senha",
                "CNPJ-XYZ",
                endereco
        );

        String esperado = "backend\\farmacia\\estoquesFarmacias\\EstoqueCNPJ-XYZ.txt";
        assertEquals(esperado, pj.getNomeArquivoEstoque());
    }


    @Test
    void toStringDeveAdicionarCnpjEnderecoNomeArquivoEstoqueEAgendaAoFinal() {
        Endereco endereco = mock(Endereco.class);
        when(endereco.toString()).thenReturn("ENDERECO_MOCK");

        PessoaJuridica pj = new PessoaJuridica(
                "Farmacia T",
                "9999",
                "farm@t.com",
                "senha",
                "CNPJ-TST",
                endereco
        );

        Estoque estoque = mock(Estoque.class);
        pj.setEstoque(estoque, false);

        Agenda agenda = mock(Agenda.class);
        when(agenda.toString()).thenReturn("AGENDA_MOCK");
        pj.setContatosClientes(agenda, false);

        String result = pj.toString();
        String[] parts = result.split(",");

        assertTrue(parts.length >= 8, "Esperava ao menos 8 campos separados por vírgula");

        int len = parts.length;
        assertEquals("CNPJ-TST", parts[len - 4]);
        assertEquals("ENDERECO_MOCK", parts[len - 3]);
        assertEquals(pj.getNomeArquivoEstoque(), parts[len - 2]);
        assertEquals("AGENDA_MOCK", parts[len - 1]);
    }

    @Test
    void toStringDeveUsarNullParaCamposNaoPreenchidos() {
        Endereco endereco = mock(Endereco.class);
        when(endereco.toString()).thenReturn("ENDERECO_INICIAL");

        PessoaJuridica pj = new PessoaJuridica(
                "Farmacia N",
                "8888",
                "farm@n.com",
                "senha",
                "CNPJ-NULL",
                endereco
        );

        pj.setEndereco(null, false);
        pj.setEstoque(null, false);
        pj.setContatosClientes(null, false);

        String result = pj.toString();
        String[] parts = result.split(",");

        int len = parts.length;
        assertEquals("CNPJ-NULL", parts[len - 4]);
        assertEquals("null", parts[len - 3]); // endereco
        assertEquals("null", parts[len - 2]); // estoque
        assertEquals("null", parts[len - 1]); // agenda
    }
    @Test
    void adicionarMedicamentoEstoqueDeveCriarEstoqueSeForNulo() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();

        PessoaJuridica spyPj = spy(pj);
        doNothing().when(spyPj).salvarEstoqueArquivo(); 
        
        assertNull(spyPj.getEstoque());

        Medicamento med = new Medicamento("Dipirona", 5.0f, "500mg");


        spyPj.adicionarMedicamentoEstoque(med, 10);


        assertNotNull(spyPj.getEstoque());
        assertEquals(1, spyPj.getEstoque().listaEstoque.size());
        assertEquals("Dipirona", spyPj.getEstoque().listaEstoque.get(0).getMedicamento().getNome());

        verify(spyPj, times(1)).setEstoque(any(Estoque.class), eq(true));
    }
    @Test
    void retirarMedicamentoEstoqueNaoDeveQuebrarSeEstoqueForNulo() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();
        assertNull(pj.getEstoque());

        assertDoesNotThrow(() -> pj.retirarMedicamentoEstoque("Qualquer"));
    }

    @Test
    void atualizarQntMedicamentoEstoqueDeveAdicionarSeNaoExistir() {

        PessoaJuridica pj = novaPessoaJuridicaBasica();
        PessoaJuridica spyPj = spy(pj);
        doNothing().when(spyPj).salvarEstoqueArquivo();

        Medicamento med = new Medicamento("Novalgina");

        spyPj.atualizarQntMedicamentoEstoque(med, 20);

        assertNotNull(spyPj.getEstoque());
        assertEquals(20, spyPj.getEstoque().listaEstoque.get(0).getQntMedicamento());
    }

    @Test
    void atualizarQntMedicamentoEstoqueDeveAlterarQuantidadeSeJaExistir() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();
        PessoaJuridica spyPj = spy(pj);
        doNothing().when(spyPj).salvarEstoqueArquivo();

        Medicamento med = new Medicamento("Novalgina");
        spyPj.adicionarMedicamentoEstoque(med, 10);

        spyPj.atualizarQntMedicamentoEstoque(med, 50);

        assertEquals(50, spyPj.getEstoque().listaEstoque.get(0).getQntMedicamento());
    }
    @Test
    void salvarDadosArquivoDeveChamarFuncoesArquivosComListaCorreta() {
        PessoaJuridica pj = novaPessoaJuridicaBasica();
        
        try (MockedStatic<FuncoesArquivos> funcoesMock = Mockito.mockStatic(FuncoesArquivos.class)) {
            pj.salvarDadosArquivo();

            funcoesMock.verify(() -> FuncoesArquivos.salvarListaEmArquivo(
                eq(PessoaJuridica.nomeArquivoFarmacias), 
                argThat(lista -> {

                    return lista.size() == 1 && lista.get(0).contains(pj.getCnpj());
                }), 
                eq(true)
            ));
        }
    }

    
}
