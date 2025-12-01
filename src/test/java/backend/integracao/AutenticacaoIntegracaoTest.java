package backend.integracao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import backend.Autenticacao;

import static org.junit.jupiter.api.Assertions.*;


class AutenticacaoIntegracaoTest {

    @Test
    @DisplayName("Deve garantir que uma senha cadastrada permita o login corretamente")
    void testeFluxoCompletoDeLogin() throws Exception {
        String emailUsuario = "teste@facil.com";
        String senhaDigitadaPeloUsuario = "senhaSuperSecreta123";
        String senhaIncorreta = "senhaErrada";

        String senhaSalvaNoBancoDeDados = Autenticacao.encriptarSenha(emailUsuario, senhaDigitadaPeloUsuario);

        assertNotNull(senhaSalvaNoBancoDeDados, "A senha gerada não pode ser nula");
        assertNotEquals(senhaDigitadaPeloUsuario, senhaSalvaNoBancoDeDados, 
            "ERRO DE SEGURANÇA: A senha salva não pode ser texto puro, deve estar criptografada!");

        boolean loginSucesso = Autenticacao.autenticar(emailUsuario, senhaDigitadaPeloUsuario, senhaSalvaNoBancoDeDados);

        assertTrue(loginSucesso, "O login DEVERIA funcionar com a senha correta.");

        boolean loginFalha = Autenticacao.autenticar(emailUsuario, senhaIncorreta, senhaSalvaNoBancoDeDados);

        assertFalse(loginFalha, "O login NÃO deveria funcionar com a senha errada.");
    }
}