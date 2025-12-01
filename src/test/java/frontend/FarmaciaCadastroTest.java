package frontend;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Assert; // Import standard JUnit Assert
import org.junit.Test;
import java.io.File;
import static org.assertj.swing.finder.WindowFinder.findFrame;

public class FarmaciaCadastroTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private final String arquivoFarmacias = "backend/farmacia/RegistroFarmacias.txt";

    @Override
    protected void onSetUp() {
        limparDadosDeTeste();
        LoginFarmacia frame = GuiActionRunner.execute(LoginFarmacia::new);
        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Test
    public void deveCadastrarFarmaciaComSucessoEAbirHome() {
        window.textBox("campoEmail").enterText("nova.farmacia@teste.com");
        window.textBox("campoSenha").enterText("senhaSegura123");
        window.textBox("campoNome").enterText("Farmacia Nova Vida");
        window.textBox("campoCnpj").enterText("12345678000100");
        window.textBox("campoTelefone").enterText("11987654321");

        window.textBox("campoRua").enterText("Avenida Paulista");
        window.textBox("campoNumero").enterText("1578");
        window.textBox("campoComplemento").enterText("Loja 1");

        window.button("botaoCadastrar").click();

        FrameFixture homeFrame = findFrame(HomeDaFarmacia.class).using(robot());
        
        homeFrame.requireVisible();

        Assert.assertNotNull("A tela Home deve ser encontrada", homeFrame);
        
        homeFrame.close();
    }

    @Test
    public void deveExibirErroComCamposObrigatoriosVazios() {
        window.textBox("campoEmail").enterText("incompleto@teste.com");
        window.button("botaoCadastrar").click();
        
        window.optionPane().requireInformationMessage().requireMessage("Precisa preencher todas as opções corretamente!");
        
        Assert.assertNotNull(window);
    }
    
    private void limparDadosDeTeste() {
        File arquivo = new File(arquivoFarmacias);
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }
}
