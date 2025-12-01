package backend;

import backend.usuario.PessoaFisica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoPessoaFisicaIntegrationTest {

    @Test
    @DisplayName("Integração: Deve criptografar senha da PessoaFisica e autenticar com sucesso")
    void deveCriptografarEAutenticarUsuarioCorretamente() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        
        String senhaPura = "senhaSuperSegura123";
        String emailTeste = "integracao@teste.com";
        
        Endereco endereco = new Endereco("Rua da Integracao", "500", "Bloco A");
        PessoaFisica usuario = new PessoaFisica(
            "Usuario Teste Autenticacao",
            "11999998888",
            emailTeste,
            "123.123.123-99",
            senhaPura,
            endereco
        );

        // Criptografia da senha usando Autenticacao
        String hashEsperado = Autenticacao.encriptarSenha(emailTeste, senhaPura);

        // A senha criptografada deve ser diferente da original e não nula
        assertNotEquals(senhaPura, hashEsperado, "A senha deve ser criptografada e não pode ser igual à original");
        assertNotNull(hashEsperado);

        String linhaArquivoSimulada = usuario.toString(true);
        
        // Simulação da linha que seria persistida (toString com senha criptografada)
        String[] dadosLinha = linhaArquivoSimulada.split(",");
        String senhaGeradaPeloObjeto = dadosLinha[3];

        // A senha armazenada deve ser igual ao hash esperado
        assertEquals(hashEsperado, senhaGeradaPeloObjeto, "A string gerada para o arquivo deve conter a senha já criptografada");

        // Cenário 1: Login com senha correta deve autenticar
        boolean loginSucesso = Autenticacao.autenticar(usuario.getEmail(), senhaPura, senhaGeradaPeloObjeto);
        assertTrue(loginSucesso, "O usuário deve conseguir logar com a senha correta e o hash gerado");

        //Cenário 2: Login com senha incorreta não autentica
        boolean loginFalha = Autenticacao.autenticar(usuario.getEmail(), "senhaErrada", senhaGeradaPeloObjeto);
        assertFalse(loginFalha, "O usuário NÃO deve conseguir logar com senha errada");

        //Cenário 3: Login com email incorreto não autentica
        boolean loginEmailErrado = Autenticacao.autenticar("outro@email.com", senhaPura, senhaGeradaPeloObjeto);
        assertFalse(loginEmailErrado, "O hash não deve bater se o email (usado como Sal) for diferente");
    }
}