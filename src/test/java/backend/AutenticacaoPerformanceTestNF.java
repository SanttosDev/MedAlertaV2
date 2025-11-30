package backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoPerformanceTestNF {


    @Test
    @DisplayName("Teste de Latência: A criptografia deve ser executada em menos de 100ms")
    void testeDePerformanceUnitaria() {
        String email = "usuario@teste.com";
        String senha = "minhaSenhaSuperLongaParaForcarOProcessador123456";

        assertTimeout(Duration.ofMillis(100), () -> {
            Autenticacao.encriptarSenha(email, senha);
        }, "A criptografia está muito lenta! Isso vai travar a interface do usuário.");
    }


   @Test
    @DisplayName("Teste de Carga: O sistema deve aguentar 5.000 criptografias em menos de 1 segundo")
    void testeDeEstresseDeCarga() {
        String email = "stress@teste.com";
        String senha = "senha";

        long inicio = System.currentTimeMillis();

        for (int i = 0; i < 5000; i++) {
        
            int index = i; 
            assertDoesNotThrow(() -> {
                Autenticacao.encriptarSenha(email, senha + index); 
            });
        }

        long fim = System.currentTimeMillis();
        long tempoTotal = fim - inicio;

        System.out.println("Tempo total para 5.000 operacoes: " + tempoTotal + "ms");

        assertTrue(tempoTotal < 1000, 
            "O sistema falhou no teste de carga. Lento demais para alto volume de dados.");
    }
}
