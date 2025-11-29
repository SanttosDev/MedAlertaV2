package backend;

import backend.farmacia.Estoque;
import backend.farmacia.ItemEstoque;
import backend.farmacia.PessoaJuridica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FarmaciaEstoqueIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Top-Down: Farmácia deve gerenciar e persistir estoque complexo")
    void testFluxoFarmaciaEstoque() {
        // 1. ARRANGE (Topo da hierarquia)
        Endereco end = new Endereco("Rua Farmacia", "100", "Centro");
        PessoaJuridica farmacia = new PessoaJuridica("Drogasil Teste", "210000", "droga@teste.com", "123", "CNPJ123", end);
        
        // Configurando o nível de baixo (Estoque e Medicamentos)
        Estoque estoque = new Estoque();
        Medicamento med1 = new Medicamento("Aspirina", 10.0f, "Bayer");
        Medicamento med2 = new Medicamento("Rivotril", 50.0f, "Tarja Preta");
        
        estoque.addMedicamentoEstoque(med1, 100);
        estoque.addMedicamentoEstoque(med2, 50);
        
        // Integração: Associando estoque à farmácia
        farmacia.setEstoque(estoque, false); // false para não tentar salvar no path hardcoded agora

        // Definindo arquivo temporário seguro
        File arquivoTeste = tempDir.resolve("farmacia_estoque.bin").toFile();

        // 2. ACT (Persistência)
        farmacia.salvarObjetoArquivo(arquivoTeste.getAbsolutePath(), farmacia);

        // 3. ASSERT & RELOAD (Verificação)
        PessoaJuridica farmaciaRecuperada = (PessoaJuridica) new PessoaJuridica(null,null,null,null,null,null)
                .recuperarObjetoArquivo(arquivoTeste.getAbsolutePath());

        assertNotNull(farmaciaRecuperada);
        assertNotNull(farmaciaRecuperada.getEstoque());
        
        // Verificando integridade dos dados profundos (Deep Integration)
        Estoque estoqueRecuperado = farmaciaRecuperada.getEstoque();
        assertEquals(2, estoqueRecuperado.listaEstoque.size());
        
        // Validando se o objeto Medicamento sobreviveu dentro do ItemEstoque dentro do Estoque dentro da Farmacia
        ItemEstoque item1 = estoqueRecuperado.listaEstoque.get(0);
        assertEquals("Aspirina", item1.getMedicamento().getNome());
        assertEquals(100, item1.getQntMedicamento());
    }
}