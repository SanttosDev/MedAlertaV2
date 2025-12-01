package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FuncoesArquivos {

    private static final Logger LOGGER = Logger.getLogger(FuncoesArquivos.class.getName());
    private static final String ERROR_PREFIX = "Erro: ";

    public static void criarArquivo(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        try {
            if (arquivo.createNewFile()) {
                LOGGER.info(() -> "Arquivo criado: " + arquivo.getName());
            } else {
                LOGGER.info(() -> "Arquivo já existe: " + arquivo.getName());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "ao criar arquivo " + nomeArquivo);
        }
    }

    public static void escreverArquivo(String nomeArquivo, String linha) {
        try (FileWriter escritorArquivo = new FileWriter(nomeArquivo)) {
            escritorArquivo.write(linha);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "ao escrever no arquivo " + nomeArquivo);
        }
    }

    public static void appendLinhaArquivo(String nomeArquivo, String linha) {
        try (FileWriter fw = new FileWriter(nomeArquivo, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(linha);
            bw.newLine();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "ao adicionar linha no arquivo " + nomeArquivo);
        }
    }

    public static void lerArquivo(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        try (Scanner leitorArquivo = new Scanner(arquivo)) {
            while (leitorArquivo.hasNextLine()) {
                String linha = leitorArquivo.nextLine();
    
                LOGGER.info(linha);
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "arquivo não encontrado: " + nomeArquivo);
        }
    }

    public static List<String> listaLinhas(File arquivo) {
        List<String> listaLinhas = new ArrayList<>();

        try (FileReader fr = new FileReader(arquivo);
             BufferedReader br = new BufferedReader(fr)) {

            String linha = br.readLine();

            while (linha != null) {
                listaLinhas.add(linha);
                linha = br.readLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "ao ler linhas do arquivo " + arquivo.getName());
        }
        return listaLinhas;
    }

    public static List<String> obterListaLinhas(String nomeArquivo) {
        List<String> listaLinhas = new ArrayList<>();

        try (FileReader fr = new FileReader(nomeArquivo);
             BufferedReader br = new BufferedReader(fr)) {

            String linha = br.readLine();

            while (linha != null) {
                listaLinhas.add(linha);
                linha = br.readLine();
            }
        } catch (IOException e) {

            LOGGER.log(Level.WARNING, e, () -> ERROR_PREFIX + "ao obter linhas do arquivo " + nomeArquivo);
        }

        return listaLinhas;
    }

    public static String obterStringDeNullsCsv(int qntDeNulls) {
        String[] arrayNulls = new String[qntDeNulls];

        for (int i = 0; i < qntDeNulls; i++) {
            arrayNulls[i] = "null";
        }


        return String.join(",", arrayNulls);
    }


    public void salvarObjetoParaArquivo(List<String> listaValoresAtributos, String nomeArquivo) {

        String linhaParaArquivo = String.join(",", listaValoresAtributos);

        try (FileWriter fw = new FileWriter(nomeArquivo, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(linhaParaArquivo);
            bw.newLine();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "não foi possível salvar objeto no arquivo " + nomeArquivo);
        }
    }

  
    public static void salvarListaEmArquivo(String nomeArquivo, List<String> listaLinhas, boolean append) {
        File arquivo = new File(nomeArquivo);

    
        if (arquivo.getParentFile() != null && !arquivo.getParentFile().exists()) {
            boolean dirsCriados = arquivo.getParentFile().mkdirs();
            if (!dirsCriados) {
                LOGGER.warning(() -> "Não foi possível criar diretórios para o arquivo: " + nomeArquivo);
            }
        }

        try (FileWriter fw = new FileWriter(arquivo, append);
             BufferedWriter bw = new BufferedWriter(fw)) {

            for (String linha : listaLinhas) {
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "ao escrever lista no arquivo: " + nomeArquivo);
        }
    }


    public static void alterarInfoArquivo(String nomeArquivo, String infoReferencia,
                                          int posColunaInfo, String novaInfo) {
        File arquivoAntigo = new File(nomeArquivo);
        File temp = new File("temp.txt");

        try (FileReader fr = new FileReader(arquivoAntigo);
             BufferedReader br = new BufferedReader(fr);
             FileWriter fw = new FileWriter(temp);
             BufferedWriter bw = new BufferedWriter(fw)) {

            String linha = br.readLine();

            while (linha != null) {
                String[] dadosLinha = linha.split(",");
                String nome = dadosLinha[0];

                if (nome.equals(infoReferencia)) {
                    dadosLinha[posColunaInfo] = novaInfo;
                    String novaLinha = String.join(",", dadosLinha);
                    bw.write(novaLinha);
                } else {
                    bw.write(linha);
                }

                bw.newLine();
                linha = br.readLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "não foi possível modificar o arquivo " + nomeArquivo);
            return;
        }

        try {
            Path origem = temp.toPath();
            Path destino = arquivoAntigo.toPath();
            Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "ao substituir arquivo " + nomeArquivo);
        }
    }

    public static void alterarLinhaArquivo(String nomeArquivo, String nomeProcurado, String novaLinha) {
        File arquivoAntigo = new File(nomeArquivo);
        File temp = new File("temp.txt");

        try (FileReader fr = new FileReader(arquivoAntigo);
             BufferedReader br = new BufferedReader(fr);
             FileWriter fw = new FileWriter(temp);
             BufferedWriter bw = new BufferedWriter(fw)) {

            String linha = br.readLine();

            while (linha != null) {
                String[] dadosLinha = linha.split(",");
                String nome = dadosLinha[0];

                if (nome.equals(nomeProcurado)) {
                    bw.write(novaLinha);
                } else {
                    bw.write(linha);
                }

                bw.newLine();
                linha = br.readLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "não foi possível modificar o arquivo " + nomeArquivo);
            return;
        }

        try {
            Path origem = temp.toPath();
            Path destino = arquivoAntigo.toPath();
            Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "ao substituir arquivo " + nomeArquivo);
        }
    }

    public static boolean checarExistenciaNomeArquivo(String nomeArquivo, String nomeProcurado) {
        try (FileReader fr = new FileReader(nomeArquivo);
             BufferedReader br = new BufferedReader(fr)) {

            String linha = br.readLine();

            while (linha != null) {
                String[] dadosLinha = linha.split(",");
                if (dadosLinha[0].equals(nomeProcurado)) {
                    return true;
                }
                linha = br.readLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e, () -> ERROR_PREFIX + "ao verificar existência de nome no arquivo " + nomeArquivo);
        }

        return false;
    }
}
