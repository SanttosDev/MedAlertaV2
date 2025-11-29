package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import backend.farmacia.PessoaJuridica;
import backend.usuario.Medico;
import backend.usuario.PessoaFisica;
import java.io.Serializable;

public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Pessoa> contatos;

    public Agenda() {
        this.contatos = new ArrayList<>();
    }

    private Optional<Pessoa> buscarPessoa(String nome) {
        return contatos.stream()
                .filter(p -> p.getNome().equals(nome))
                .findFirst();
    }

    public void adicionarContato(Pessoa contato) {
        if (contato == null) {
            throw new IllegalArgumentException("É necessário informar um contato válido");
        }
        contatos.add(contato);
    }

    public boolean alterarNomeContato(String nome, String novoNome) {
        Optional<Pessoa> p = buscarPessoa(nome);
        if (p.isPresent()) {
            p.get().setNome(novoNome);
            return true;
        }
        return false;
    }

    public boolean alterarTelContato(String nome, String novoTelefone) {
        Optional<Pessoa> p = buscarPessoa(nome);
        if (p.isPresent()) {
            p.get().setTelefone(novoTelefone);
            return true;
        }
        return false;
    }

    public <T> boolean alterarParticularidadeContato(String nome, T novaParticularidade) {
        Optional<Pessoa> p = buscarPessoa(nome);
        if (p.isPresent()) {
            p.get().setParticularidade(novaParticularidade);
            return true;
        }
        return false;
    }

    public boolean alterarEmailContato(String nome, String novoEmail) {
        Optional<Pessoa> p = buscarPessoa(nome);
        if (p.isPresent()) {
            p.get().setEmail(novoEmail);
            return true;
        }
        return false;
    }

    public boolean removerContato(String nome) {
        return contatos.removeIf(p -> p.getNome().equals(nome));
    }

    public ArrayList<Pessoa> getContatos() {
        if (!contatos.isEmpty()) {
            Collections.sort(contatos);
        }
        return contatos;
    }

    public void setContatos(ArrayList<Pessoa> novosContatos) {
        if (novosContatos == null) {
            this.contatos = new ArrayList<>();
        } else {
            this.contatos = novosContatos;
        }
    }

    @Override
    public String toString() {
        if (contatos.isEmpty()) return "null";
        
        return contatos.stream()
                .map(Pessoa::getEmail)
                .collect(Collectors.joining("/"));
    }

    public static Agenda stringToAgenda(String agendaString, String senha, String tipo, Boolean ignSenha, Boolean ignAgenda) {
        Agenda agenda = new Agenda();
        
        if (agendaString == null || agendaString.isEmpty()) {
            return agenda;
        }

        String[] nomes = agendaString.split("/");

        try {
            for (String nome : nomes) {
                if ("usuario".equals(tipo)) {
                    agenda.adicionarContato(PessoaFisica.resgatarUsuarioArquivo(nome, senha, ignSenha, ignAgenda));
                } else if ("farmacia".equals(tipo)) {
                    agenda.adicionarContato(PessoaJuridica.resgatarFarmaciaArquivo(nome, senha, ignSenha, ignAgenda));
                } else if ("medico".equals(tipo)) {
                    agenda.adicionarContato(Medico.resgatarMedicoArquivo(nome, senha, ignSenha));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar dados", e);
        }
        return agenda;
    }
}