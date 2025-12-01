package backend.integracao;

import backend.usuario.Medico;
import backend.usuario.PessoaFisica;
import backend.Agenda;
import backend.Endereco;
import backend.Pessoa;
import backend.farmacia.PessoaJuridica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;


class AgendaPessoaIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Bottom-Up: Deve integrar Medicos e Farmacias na Agenda do Usuario e persistir corretamente")
    void testIntegracaoAgendaUsuario() {
        

        Medico medico = new Medico(
            "Dr. Drauzio", 
            "1199999999", 
            "drauzio@medico.com", 
            "senhaMed", 
            "Oncologista"
        );
        

        Endereco endFarmacia = new Endereco("Rua dos Remedios", "100");
        PessoaJuridica farmacia = new PessoaJuridica(
            "Drogasil", 
            "1133334444", 
            "contato@drogasil.com", 
            "senhaFarm", 
            "CNPJ0001", 
            endFarmacia
        );


        assertEquals("Oncologista", medico.getEspecialidade());
        assertEquals("CNPJ0001", farmacia.getCnpj());


        Endereco endUsuario = new Endereco("Rua Casa", "10");
        PessoaFisica usuario = new PessoaFisica(
            "Usuario Teste", 
            "2199998888", 
            "user@teste.com", 
            "123.456.789-00", 
            "senhaUser", 
            endUsuario
        );

        usuario.adicionarContatoMedico(medico);
        usuario.addFarmaciaAosContatos(farmacia);

        assertNotNull(usuario.getContatosMedicos());
        assertNotNull(usuario.getContatosFarmacias());
        
        assertEquals(1, usuario.getContatosMedicos().getContatos().size());
        assertEquals("Dr. Drauzio", usuario.getContatosMedicos().getContatos().get(0).getNome());
        
        assertEquals(1, usuario.getContatosFarmacias().getContatos().size());
        assertEquals("Drogasil", usuario.getContatosFarmacias().getContatos().get(0).getNome());


        File arquivoTeste = tempDir.resolve("usuario_agenda_integration.bin").toFile();
        String path = arquivoTeste.getAbsolutePath();

        usuario.salvarObjetoArquivo(path, usuario);

        PessoaFisica usuarioRecuperado = (PessoaFisica) new PessoaFisica(null,null,null,null,null,null)
                .recuperarObjetoArquivo(path);

        assertNotNull(usuarioRecuperado);
        
        Agenda agendaMedicosRecuperada = usuarioRecuperado.getContatosMedicos();
        assertNotNull(agendaMedicosRecuperada);
        assertFalse(agendaMedicosRecuperada.getContatos().isEmpty());
        
        Pessoa medicoRecuperado = agendaMedicosRecuperada.getContatos().get(0);
        assertTrue(medicoRecuperado instanceof Medico);
        assertEquals("Dr. Drauzio", medicoRecuperado.getNome());
        assertEquals("Oncologista", ((Medico) medicoRecuperado).getEspecialidade());
        
        Agenda agendaFarmaciasRecuperada = usuarioRecuperado.getContatosFarmacias();
        Pessoa farmaciaRecuperada = agendaFarmaciasRecuperada.getContatos().get(0);
        assertTrue(farmaciaRecuperada instanceof PessoaJuridica);
        assertEquals("Drogasil", farmaciaRecuperada.getNome());
    }
}