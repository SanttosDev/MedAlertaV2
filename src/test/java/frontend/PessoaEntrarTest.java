package frontend;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class PessoaEntrarTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;

    @Override
    protected void onSetUp() {
        EntrarPessoa frame = GuiActionRunner.execute(() -> new EntrarPessoa());
        
        window = new FrameFixture(robot(), frame);
        window.show(); 
    }

    @Test
    public void deveExibirErroComCredenciaisInvalidas() {
        window.textBox("loginEmail").enterText("Testedefalha@teste.com");

        window.textBox("loginSenha").enterText("senhaerrada");

        window.button("botaoEntrar").click();

        window.optionPane().requireInformationMessage().requireMessage("Erro, email ou senha incorretos!");

        var dialog = window.optionPane();
        dialog.requireInformationMessage().requireMessage("Erro, email ou senha incorretos!");
        assertNotNull(dialog);
    }
}