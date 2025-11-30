package frontend;

import backend.usuario.PessoaFisica;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.timing.Pause;
import org.junit.Test;

import java.io.File;
import static org.assertj.swing.finder.WindowFinder.findFrame;

public class FluxoSistemaPessoaCompletoTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private final String ARQUIVO_USUARIOS = "backend/usuario/RegistroUsuarios.txt";

    @Override
    protected void onSetUp() {
        // Garante um ambiente limpo
        File arquivo = new File(ARQUIVO_USUARIOS);
        if (arquivo.exists()) arquivo.delete();
        
        File pasta = arquivo.getParentFile();
        if (pasta != null) pasta.mkdirs();

        // INÍCIO
        Inicio frame = GuiActionRunner.execute(() -> new Inicio());
        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Test
    public void testePontaAPontaCompleto() {
        // ETAPA 1: CADASTRO (LoginPessoa.java é a tela de cadastro)
        window.button("botaoIrParaCadastro").click();

        FrameFixture janelaCadastro = findFrame(LoginPessoa.class).using(robot());
        
        janelaCadastro.textBox("campoNome").enterText("Usuario Teste Completo");
        janelaCadastro.textBox("campoCpf").enterText("111.222.333-44");
        janelaCadastro.textBox("campoTelefone").enterText("2199999999");
        janelaCadastro.textBox("campoEmail").enterText("teste@completo.com");
        janelaCadastro.textBox("campoSenha").enterText("123");
        
        janelaCadastro.textBox("campoRua").enterText("Rua Teste");
        janelaCadastro.textBox("campoNumero").enterText("10");
        janelaCadastro.textBox("campoComplemento").enterText("Casa");

        janelaCadastro.button("botaoFinalizarCadastro").click();
        
        Pause.pause(1000);

        // ETAPA 2: HOME (Acesso Direto Pós-Cadastro)
        
        PessoaFisica usuarioLogado = PessoaFisica.resgatarUsuarioArquivo("teste@completo.com", "123", false, false);
        
        Home frameHome = GuiActionRunner.execute(() -> {
            Home h = new Home();
            h.receber(usuarioLogado);
            return h;
        });
        FrameFixture janelaHome = new FrameFixture(robot(), frameHome);
        janelaHome.show();
        
        janelaHome.label("labelNomeUsuario").requireText("Usuario Teste Completo");

        // ETAPA 3: CADASTRO DE MEDICAMENTO
        janelaHome.button("botaoMeusRemedios").click();
        
        FrameFixture janelaRemedios = findFrame(ListaRemedios.class).using(robot());
        janelaRemedios.button("botaoNovo").click();
        
        janelaRemedios.textBox("campoNomeRemedio").enterText("Aspirina");
        janelaRemedios.textBox("campoQtd").enterText("20");
        janelaRemedios.textBox("campoDose").enterText("1");
        janelaRemedios.textBox("campoDuracao").enterText("10");
        janelaRemedios.checkBox("checkSegunda").check();
        janelaRemedios.comboBox("comboHora").selectItem("08hr");
        janelaRemedios.comboBox("comboIntervalo").selectItem(0); 

        janelaRemedios.button("botaoSalvar").click();
        
        FrameFixture janelaRemediosAtt = findFrame(ListaRemedios.class).using(robot());
        JTableFixture tabelaRem = janelaRemediosAtt.table("tabelaRemedios");
        tabelaRem.requireCellValue(TableCell.row(0).column(0), "Aspirina");
        
        janelaRemediosAtt.button("botaoVoltarRemedio").click();
        
        // ETAPA 4: CADASTRO DE MÉDICO
        FrameFixture janelaHome2 = findFrame(Home.class).using(robot());
        janelaHome2.button("botaoContatosMedicos").click();
        
        FrameFixture janelaMedicos = findFrame(ContatosMedicos.class).using(robot());
        janelaMedicos.button("botaoNovoMedico").click();
        
        janelaMedicos.textBox("campoNomeMedico").enterText("Dr. House");
        janelaMedicos.textBox("campoTelefoneMedico").enterText("9999-8888");
        janelaMedicos.textBox("campoEspecialidadeMedico").enterText("Diagnosta");
        
        janelaMedicos.button("botaoSalvarMedico").click();
        
        FrameFixture janelaMedicosAtt = findFrame(ContatosMedicos.class).using(robot());
        JTableFixture tabelaMed = janelaMedicosAtt.table("tabelaMedicos");
        tabelaMed.requireCellValue(TableCell.row(0).column(0), "Dr. House");
        tabelaMed.requireCellValue(TableCell.row(0).column(2), "Diagnosta");
        
        janelaMedicosAtt.button("botaoVoltarMedico").click();

        // ETAPA 5: CADASTRO DE FARMÁCIA
        FrameFixture janelaHome3 = findFrame(Home.class).using(robot());
        janelaHome3.button("botaoContatosFarmacias").click();
        
        FrameFixture janelaFarmacias = findFrame(ContatosFarmacias.class).using(robot());
        janelaFarmacias.button("botaoNovaFarmacia").click();
        
        janelaFarmacias.textBox("campoNomeFarmacia").enterText("Farmacia Central");
        janelaFarmacias.textBox("campoTelefoneFarmacia").enterText("3333-0000");
        janelaFarmacias.textBox("campoEnderecoFarmacia").enterText("Rua do Comercio");
        
        janelaFarmacias.button("botaoSalvarFarmacia").click();
        
        FrameFixture janelaFarmaciasAtt = findFrame(ContatosFarmacias.class).using(robot());
        JTableFixture tabelaFarm = janelaFarmaciasAtt.table("tabelaFarmacias");
        tabelaFarm.requireCellValue(TableCell.row(0).column(0), "Farmacia Central");
        
        janelaFarmaciasAtt.button("botaoVoltarFarmacia").click();
    }
}