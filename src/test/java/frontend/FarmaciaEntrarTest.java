package frontend;

import backend.Endereco;
import backend.farmacia.PessoaJuridica;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static org.assertj.swing.finder.WindowFinder.findFrame;

public class FarmaciaEntrarTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private final String arquivoFarmacias = "backend/farmacia/RegistroFarmacias.txt";

    @Override
    protected void onSetUp() {
        prepararDadosDeTeste();

        EntrarFarmacia frame = GuiActionRunner.execute(EntrarFarmacia::new);
        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Test
    public void deveLogarComSucessoEAbirHome() {
        window.textBox("campoEmail").enterText("teste@farmacia.com");
        window.textBox("campoSenha").enterText("123456");
        window.button("botaoEntrar").click();

        FrameFixture homeFrame = findFrame(HomeDaFarmacia.class).using(robot());

        homeFrame.requireVisible();

        Assert.assertNotNull("A tela Home deve ser encontrada", homeFrame);

        homeFrame.close(); 
    }

    @Test
    public void deveExibirErroComCredenciaisInvalidas() {
        window.textBox("campoEmail").enterText("teste@farmacia.com");
        window.textBox("campoSenha").enterText("senhaErrada");
        window.button("botaoEntrar").click();

        window.optionPane().requireInformationMessage().requireMessage("Erro, email ou senha incorretos!");

        Assert.assertNotNull(window);
    }


    private void prepararDadosDeTeste() {
        File arquivo = new File(arquivoFarmacias);
        if (arquivo.exists()) {
            arquivo.delete();
        }
        
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }
        
        Endereco end = new Endereco("Rua Teste", "100", "Centro");
        PessoaJuridica farmaciaTeste = new PessoaJuridica(
            "Farmacia de Teste Automatizado", 
            "11999990000", 
            "teste@farmacia.com", 
            "123456", 
            "CNPJ123", 
            end
        );
        farmaciaTeste.salvarDadosArquivo();
    }
}