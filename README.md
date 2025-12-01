# MedAlerta - Qualidade & Teste 25.2

O projeto **MedAlerta** está sendo revisitado e aprimorado pelo nosso grupo como parte da disciplina de Qualidade e Teste de Software. Nosso trabalho consiste em analisar o sistema legado, refatorar código, identificar dívidas técnicas e aplicar um conjunto robusto de práticas de garantia de qualidade (QA) para aumentar a confiabilidade, desempenho e manutenibilidade da aplicação.

## • Objetivos

Nosso foco não é apenas testar, mas evoluir a maturidade do software através de:

- **Plano de Testes Abrangente**: Cobertura de cenários unitários, de integração e de sistema (E2E).
- **Automação de Testes**: Uso de frameworks modernos para garantir regressão segura.
- **Análise Estática e Dinâmica**: Monitoramento via SonarQube e JaCoCo.
- **Teste de Mutação**: Validação da qualidade dos testes com PIT (Pitest).

Ao longo do desenvolvimento, estamos elaborando um plano de testes completo, criando e executando casos de teste manuais e automatizados, além de validar módulos críticos do software por meio de diferentes técnicas de teste. Também buscamos compreender profundamente o comportamento do sistema, documentar resultados e propor ajustes que contribuam para uma versão mais estável e robusta do MedAlerta.

## • Tecnologias e Ferramentas

O projeto foi atualizado para **Java 17** e utiliza o **Maven** para gerenciamento de dependências. As principais ferramentas de qualidade configuradas são:

- **JUnit 5 & 4**: Framework base para testes unitários.
- **Mockito**: Framework para criação de mocks e stubs em testes isolados.
- **AssertJ Swing**: Para testes de interface gráfica (GUI) e testes de sistema.
- **JaCoCo**: Ferramenta para análise de cobertura de código.
- **PIT (Pitest)**: Ferramenta de teste de mutação para garantir a eficácia dos testes unitários.
- **SonarQube**: Plataforma para inspeção contínua da qualidade do código.

## • Estrutura Principal do Projeto para o Trabalho

A organização dos diretórios segue o padrão Maven, separando lógica de negócio, interface e tipos de testes:

```
src
├── main
│   └── java
│       ├── backend         # Lógica de negócio (Entities, Controllers, etc.)
│       │   ├── farmacia    # Regras específicas de farmácias e estoque
│       │   ├── gerenciamento # Lógica de agendamento e notificações
│       │   └── usuario     # Modelos de Pessoa Física e Médico
│       └── frontend        # Interface Gráfica (Swing)
└── test
    └── java
        ├── backend         # Testes Unitários (foco em lógica isolada)
        ├── backend/integracao # Testes de Integração (fluxos entre módulos)
        └── frontend        # Testes de Sistema/GUI (AssertJ Swing)
```

## • Como Executar e Testar

### Pré-requisitos

- Java JDK 17
- Maven instalado e configurado nas variáveis de ambiente
- (Opcional) SonarQube rodando localmente na porta 9000

### Comandos Principais do Maven

Abaixo estão os comandos essenciais configurados no `pom.xml` para o ciclo de vida de testes:

| Comando | Descrição |
|---------|-----------|
| `mvn clean test` | Limpa a build anterior e executa apenas os Testes Unitários (Surefire). |
| `mvn clean verify` | Executa todo o ciclo: testes unitários, testes de integração e gera relatórios (JaCoCo/PIT). |
| `mvn clean install` | Compila, testa e empacota o projeto, instalando o .jar no repositório local. |
| `mvn clean verify sonar:sonar` | Envia as métricas de código e cobertura para o servidor SonarQube (requer servidor ativo). |
| `mvn org.pitest:pitest-maven:mutationCoverage` | Executa especificamente os Testes de Mutação para verificar a robustez da suíte de testes. |

### Detalhes sobre os Níveis de Teste

**Testes Unitários** (`src/test/java/backend`):
- Validam classes isoladas como Agenda, Pessoa, Cálculo de Horários.
- Utilizam Mockito para isolar dependências externas (ex: arquivos).

**Testes de Integração** (`src/test/java/backend/integracao`):
- Validam a comunicação entre objetos reais (ex: Pessoa salvando e recuperando de Arquivos, interação Farmácia <-> Estoque).
- Configurados para rodar na fase `verify` ou `integration-test`.

**Testes de Sistema/GUI** (`src/test/java/frontend`):
- Utilizam AssertJ Swing para simular um usuário clicando em botões e preenchendo campos na interface real.
- Validam fluxos completos (ex: Cadastro -> Login -> Adicionar Remédio).

## • Integrantes

- **Bernardo Mendes**
- **João Pedro Diniz**
- **Josué Santos**
- **Lucca Gomes**
- **Ramon Rabello**

## • Links Importantes❗

- [**Repositório Original**](https://github.com/repo-software-testing-courses/MedAlertaV2)
- [**Pasta com instruções para rodar o projeto**](./Instruções/)

### Primeira Entrega

- [**Plano de Testes Elaborado pelo Grupo**](https://docs.google.com/document/d/1EqaoBccHmXqlOrUYw152tLlTjNfBMR0k/edit?usp=sharing&ouid=105546231281879977225&rtpof=true&sd=true) * Atualizado na segunda entrega
- [**Apresentação**](https://drive.google.com/file/d/1ojTRBvFRxTc_fNI74LD7ogrYetsWDK17/view?usp=sharing)

### Segunda Entrega

- [**Apresentação**](https://drive.google.com/file/d/1apshw5GemAYkaHKpP_KIUCzHn-KUJmji/view?usp=sharing)


## © Créditos e Histórico

Este projeto, **MedAlerta**, foi inicialmente desenvolvido como trabalho final para a disciplina de Programação Orientada a Objetos - 2020/1 pelos integrantes:

- Leonardo Saracino
- Lucas Martello
- Valesca Moura

Agradecemos imensamente a eles por terem criado esta base sólida. A partir de agora, nosso grupo assume o projeto para a disciplina de Qualidade e Teste de Software, com o objetivo de aprimorar sua funcionalidade, robustez e confiabilidade através da implementação de práticas de garantia de qualidade e um ciclo completo de testes.