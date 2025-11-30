package backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoSecurityTest {

    @Test
    @DisplayName("A Criptografia deve mudar se o email (sal) mudar, garantindo segurança.")
    void testaCriptografiaComDiferentesEmails() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String senha = "minhaSenha";
        
        String email1 = "ramon@email.com";
        String hash1 = Autenticacao.encriptarSenha(email1, senha);
        
        String email2 = "ramon2@email.com";
        String hash2 = Autenticacao.encriptarSenha(email2, senha);
        
        String hash3 = Autenticacao.encriptarSenha(email1, senha);
        
        System.out.println("Hash 1 (Email 1): " + hash1);
        System.out.println("Hash 2 (Email 2): " + hash2);

        assertNotEquals(hash1, hash2, "O hash deve mudar se o email/sal mudar.");
        
        assertEquals(hash1, hash3, "O hash deve ser determinístico.");
        
        assertEquals(64, hash1.length(), "O tamanho do hash deve ser 64.");
    }
    

    @Test
    @DisplayName("Deve autenticar corretamente se o hash gerado bater com o hash salvo.")
    void testaAutenticacaoCorreta() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String email = "teste@autenticacao.com";
        String senha = "senhaSegura123";

        String senhaArquivo = Autenticacao.encriptarSenha(email, senha);
        

        assertTrue(Autenticacao.autenticar(email, senha, senhaArquivo), 
                   "Deve autenticar com sucesso para as credenciais corretas.");
        
        assertFalse(Autenticacao.autenticar(email, "senhaIncorreta", senhaArquivo),
                    "Não deve autenticar com senha incorreta.");
    }
}