/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package condominio.fx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import util.ThreadMeiaNoite;
import util.Metodos;

/**
 *
 * @author portaria
 */
public class FXMLPrincipal implements Initializable {

    //painel principal
    @FXML
    private TabPane tabPane;

    //elementos do tab morador 
    @FXML
    private TextField tfPesquisarM;
    @FXML
    private TextField tfNomeM;
    @FXML
    private Button btPesquisarM;
    @FXML
    private Button btLimparM;
    @FXML
    private Button btSalvarM;
    @FXML
    private TextField tfFixoM;
    @FXML
    private TextField tfCelular1M;
    @FXML
    private TextField tfCelular2M;
    @FXML
    private TextField tfCarro1m;
    @FXML
    private TextField tfCarro2m;
    @FXML
    private TextField tfCarro3m;
    @FXML
    private TextField tfCarro4m;
    @FXML
    private TextField tfCarro5m;
    @FXML
    private TextField tfDiarista;
    @FXML
    private TextField tfComoChegar;
    @FXML
    private TextArea taAnotacoesM;
    @FXML
    private ComboBox<String> cbVisitantesM;
    @FXML
    private Tab tabMoradores;
    @FXML
    private Button btEntradaM;
    @FXML
    private TextField tfCasaM;
    @FXML
    private Button btMenos;
    @FXML
    private Button btMais;
    @FXML
    private Button btSaidaM;

    //elementos do tab serviços
    @FXML
    private Tab tabServicos;
    @FXML
    private TextField tfPrestador;
    @FXML
    private TextField tfFixoS;
    @FXML
    private TextField tfCelular1s;
    @FXML
    private TextField ftCelular2s;
    @FXML
    private TextField tfServicos;
    @FXML
    private TextArea taAnotacaoS;
    @FXML
    private Button btSalvarS;
    @FXML
    private Button btLimparS;
    @FXML
    private Button btMenosS;
    @FXML
    private TextField tfIdS;
    @FXML
    private Button btMaisS;
    @FXML
    private TextField tfPesquisarS;
    @FXML
    private Button btPesquisarS;

    // elementos do tab recado
    @FXML
    private Tab tabRecados;
    @FXML
    private TextArea taRecados;

    //elementos do tab relatorio    
    @FXML
    private Tab tabEntradaSaida;
    @FXML
    private Label dataRelatorio;
    @FXML
    private TextArea taRelatorio;
    @FXML
    private TextField pesquisarRelatorio;
    @FXML
    private Button btAvancar;
    @FXML
    private Button btVoltar;

    //add listta de visitantes, pegar de relatorio com base na id de entrada       
    ArrayList<String> visitantes = new ArrayList();

    //usado no botão < e > para aumentar ou diminuir o valor de casa
    int numeroCasa = 0;
    int idServico = 0;

    public static ThreadMeiaNoite t;

    // essa instancia para ser usada em outra classe 
    public static FXMLPrincipal RAIZ;

    Path caminho = Paths.get(Metodos.PASTAPRINCIPAL + "/Recados.txt"); //CAMIMNHO

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //criar atalho para o objeto, usar em outra classe
        RAIZ = FXMLPrincipal.this;

        // TODO
        Metodos.CriarPastas();
        criarArquivoEntradaSaida();

        //thread que vai verificar o horario para chamar o metodo meia noite
        t = new ThreadMeiaNoite("criarArquivoEntradaSaida");
        

        configurarTabMoradores();
        configurarTabRecados();
        configurarTabEntradaSaida();

    }

//********************tab moradores*****************************************
    public void configurarTabMoradores() {
        tabMoradores.setOnSelectionChanged((event) -> {
            preencherJCombobox();
        });

        tfCasaM.setText(Integer.toString(0));

        //ao clicar com o mouse apagar texto 
        tfCasaM.setOnMouseClicked((event) -> {
            tfCasaM.setText("");
        });

        //mostrar dados da pesquisa ao apertar enter
        tfPesquisarM.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == ENTER) {
                mostrar();
            }
        });

        btPesquisarM.setOnAction((event) -> {
            if (!tfPesquisarM.getText().equals("")) {
                mostrar();
            }

        });

        btSaidaM.setOnMouseClicked((MouseEvent event) -> {
            //verificar se existe um arquivo com os dados 
            criarArquivoEntradaSaida();
            Icon icoLeft = new ImageIcon(getClass().getResource("/imagens/icon/mouse_esquerdo.png"));
            Icon icoRigh = new ImageIcon(getClass().getResource("/imagens/icon/mouse_direito.png"));

            String visitaNoCondominio = cbVisitantesM.getValue();
            switch (event.getButton()) {
                case PRIMARY:
                    // ao clicar
                    if (!visitaNoCondominio.equals("não há visitantes")) {
                        // retorna 0 para sim 1 para não e 2 para cancelar
                        int resposta = JOptionPane.showConfirmDialog(null, "adicionar hora de saida para esse visitante, " + visitaNoCondominio + " ?", "horario do sistema", 1, JOptionPane.ERROR_MESSAGE, icoLeft);
                        if (resposta == 0) {
                            addSaida();
                            preencherJCombobox();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "não há visitantes para registrar saida!");
                    }
                    break;

                case SECONDARY: {
                    // pegar hora do clique em saida mais o item da casa

                    if (!visitaNoCondominio.equals("não há visitantes")) {
                        // retorna 0 para sim 1 para não e 2 para cancelar

                        int resposta = JOptionPane.showConfirmDialog(null, "adicionar hora de saida para esse visitante, " + visitaNoCondominio + " ?", "gerar horario", 1, JOptionPane.ERROR_MESSAGE, icoRigh);
                        if (resposta == 0) {
                            String respostaSaida = JOptionPane.showInputDialog("qual foi o horario de saida?");
                            if (respostaSaida != null) {
                                addSaida(respostaSaida);
                                preencherJCombobox();
                            } else {
                                JOptionPane.showMessageDialog(null, "não foi feito alteração! horario não informado");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "não há visitantes para registrar saida!");
                    }
                    break;
                }
                default:
                    JOptionPane.showMessageDialog(null, "clic botão esquerdo para adicionar horario do sistema\ne botão direito para adicionar o horario informado");
                    break;

            }

        });

        btEntradaM.setOnAction((ActionEvent event) -> {
            try {

                //criar o arquivo caso não exista
                criarArquivoEntradaSaida();

                //cria o painel principal
                Stage s1 = new Stage();

                Parent root = FXMLLoader.load(getClass().getResource("FXMLEntrada.fxml"));

                Image icon = new Image(getClass().getResourceAsStream("/imagens/icon/casa.png"));
                s1.getIcons().add(icon);
                s1.setTitle("Cadastros");
                Scene scene = new Scene(root);

                s1.setScene(scene);
                s1.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        tfCasaM.setOnKeyPressed((event) -> {
            voltarAvancarTecladoMorador(event);

            if (event.getCode() == ENTER) {

                addMostrar(criarSplit("casa" + tfCasaM.getText()));
                numeroCasa = Integer.parseInt(tfCasaM.getText());
            }
        });

        //colocar no combobox as entradas se houver
        preencherJCombobox();

    }

    public void configurarTabRecados() {
        tabRecados.setOnSelectionChanged((event) -> {

            if (tabRecados.isSelected()) {
                LerRecados();
            }
            if (!tabRecados.isSelected()) {
                exibirRecados();
            }

        });
    }

    public void configurarTabEntradaSaida() {
        tabEntradaSaida.setOnSelectionChanged((event) -> {
            //colocar data atual
            dataRelatorio.setText(Metodos.mudaDatas(Metodos.DataHora("data")).replace(".", "/"));

            //gerar o arquivo do relatorio atual
            criarArquivoEntradaSaida();

            //coloca o texto na area de texto
            taRelatorio.setText(Metodos.lerString("Relatorio/" + Metodos.mudaDatas(Metodos.DataHora("data")) + ".txt"));

        });
    }

    @FXML
    public void btAvancarMoradores() {
        numeroCasa++;
        try {
            //usado para fazer o numero reiniciar toda vez que chega a ultima casa
            if (numeroCasa > Files.list(Metodos.CASAS).count()) {
                numeroCasa = 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        //define como qual digito deve ter ao iniciar
        tfCasaM.setText(Integer.toString(numeroCasa));

        botoesTrocar();

    }

    @FXML
    public void btVoltarMoradores() {
        numeroCasa--;
        if (numeroCasa >= 0) {
            tfCasaM.setText(Integer.toString(numeroCasa));
            botoesTrocar();
        } else {
            tfCasaM.setText("0");
            numeroCasa = 0;
        }
    }

    public void voltarAvancarTecladoMorador(KeyEvent e) {

        if (e.getCode() == LEFT) {
            btVoltarMoradores();
        } else if (e.getCode() == RIGHT) {
            btAvancarMoradores();
        }

    }

    //usado quando os botoes < > são clicados
    public void botoesTrocar() {
        String valor = tfCasaM.getText();
        if (valor.equals("0")) {
            limpar();
        } else if (Metodos.lerString("Casas/casa" + valor + ".txt") == null) {
            tfCasaM.setText("0");
            limpar();
        } else {
            addMostrar(criarSplit("casa" + valor));

        }
    }

    /**
     * metodo usado no metodo editarCasa para adicionar arquivos com os dados
     *
     * @param arquivo nome do arquivo
     * @param path pasta de arquivos
     * @param path2 pasta de arquivos
     */
    public void salvarArquivo(Path path, Path path2) {

        try {

            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(tfCasaM.getText().replace(";", ":") + ";");
            arrayList.add(tfNomeM.getText().replace(";", ":") + ";");
            arrayList.add(tfFixoM.getText().replace(";", ":") + ";");
            arrayList.add(tfCelular1M.getText().replace(";", ":") + ";");
            arrayList.add(tfCelular2M.getText().replace(";", ":") + ";");
            arrayList.add(tfCarro1m.getText().replace(";", ":") + ";");
            arrayList.add(tfCarro2m.getText().replace(";", ":") + ";");
            arrayList.add(tfCarro3m.getText().replace(";", ":") + ";");
            arrayList.add(tfCarro4m.getText().replace(";", ":") + ";");
            arrayList.add(tfCarro5m.getText().replace(";", ":") + ";");
            arrayList.add(tfDiarista.getText().replace(";", ":") + ";");
            arrayList.add(tfComoChegar.getText().replace(";", ":") + ";");
            arrayList.add(taAnotacoesM.getText().replace(";", ":"));

            //escreve no arquivo
            Files.write(path, arrayList, Charset.defaultCharset());
            JOptionPane.showMessageDialog(null, "Alteraçoes realizadas.");

            ArrayList<String> dadosApp = new ArrayList<>();
            dadosApp.add(tfCasaM.getText());
            dadosApp.add(tfNomeM.getText());
            dadosApp.add(tfFixoM.getText());
            dadosApp.add(tfCelular1M.getText());
            dadosApp.add(tfCelular2M.getText());
            dadosApp.add(tfCarro1m.getText());
            dadosApp.add(tfCarro2m.getText());
            dadosApp.add(tfCarro3m.getText());
            dadosApp.add(tfCarro4m.getText());
            dadosApp.add(tfCarro5m.getText());
            dadosApp.add(tfDiarista.getText());
            dadosApp.add(tfComoChegar.getText());
            dadosApp.add(taAnotacoesM.getText());
            Files.write(path2, dadosApp, Charset.defaultCharset());

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "não foi possivel fazer alteraçoes");

        }
    }

    /**
     * edita os arquivos de texto
     */
    @FXML
    public void editarCasa() {
        String perg2;
        String pegarNumeroCasa = tfCasaM.getText();
        //verifica se os campos não estão vazios
        if (!pegarNumeroCasa.equals("0")) {
            int perg1 = JOptionPane.showConfirmDialog(null, "Continuar vai alterar os dados! quer continuar?");
            if (perg1 == 0) {
                String arquivo = "casa" + pegarNumeroCasa + ".txt";
                //essas duas proximas strings foram feitas pq o metodo pede dois paths e os paths de variaveis reference apenas a pasta e não ao arquivo
                String pastaCasa = Metodos.CASAS + FileSystems.getDefault().getSeparator() + arquivo;
                String pastaDadosApp = Metodos.PASTADADOSAPP + FileSystems.getDefault().getSeparator() + "casa_" + pegarNumeroCasa;

                //aqui é escrito um novo aquivo 
                salvarArquivo(Paths.get(pastaCasa), Paths.get(pastaDadosApp));
            } else if (perg1 == 1 | perg1 == 2 | perg1 == -1) {
                JOptionPane.showMessageDialog(null, "cancelado! não foi feito alterações!");
            }

        }
    }

    //usado para colocar o foco na TextField que contem a pesquisa
    public void mudarFoco(int index) {
        switch (index) {
            case 1:
                tfCasaM.requestFocus();
                break;
            case 2:
                tfNomeM.requestFocus();
                break;
            case 3:
                tfFixoM.requestFocus();
                break;
            case 4:
                tfCelular1M.requestFocus();
                break;
            case 5:
                tfCelular2M.requestFocus();
                break;
            case 6:
                tfCarro1m.requestFocus();
                break;
            case 7:
                tfCarro2m.requestFocus();
                break;
            case 8:
                tfCarro3m.requestFocus();
                break;
            case 9:
                tfCarro4m.requestFocus();
                break;
            case 10:
                tfCarro5m.requestFocus();
                break;
            case 11:
                tfDiarista.requestFocus();
                break;
            case 12:
                tfComoChegar.requestFocus();
                break;
            case 13:
                taAnotacoesM.requestFocus();
                break;
            default:
                tfCasaM.requestFocus();
                break;
        }

    }

    /**
     * mostra o conteudo dos arquivos nos campos correspondentes
     */
    public void mostrar() {

        int local = 1;
        try {
            boolean terminar = false;
            int n1 = 0;
            int n2 = 0;
            String resposta;

            //verificar se o foco ja foi alterado
            //preencher o array com o nome de cada casa que contiver a String pesquisada
            //*adiciona ao array todos os arquivos que contem a pesquisa********
            ArrayList<String> array = new ArrayList<>();
            do {
                n1++;
                String casa = "casa" + Integer.toString(n1);
                String campoPesquisar = tfPesquisarM.getText().trim();

                //verifica os arquivos para ver quais tem o digitado n campoPesquisa
                if (!campoPesquisar.isEmpty()
                        && Metodos.lerString("Casas/" + casa + ".txt")
                                .contains(campoPesquisar)) {

                    array.add(casa);

                    n2 = n1;
                }

                if (n1 == Files.list(Metodos.CASAS).count()) {
                    terminar = true;
                }
            } while (!terminar);
            ///*****************************************************************

            //entra aqui quando tem mais de um arquivo
            if (array.size() > 1) {
                boolean sair = true;
                //repete a pergunta até ser informado o numero da casa correto
                do {
                    resposta = JOptionPane.showInputDialog("foi encontrado " + tfPesquisarM.getText()
                            + " nas casas\n" + array.toString() + "\ndigite  o numero da casa desejada");

                    if (resposta.matches("\\D+")) {
                        JOptionPane.showMessageDialog(null, "por favor digite apenas numero!");
                    }
                    if (resposta.matches("\\d{1,2}")) {
                        if (array.contains("casa" + resposta)) {
                            Scanner sc = new Scanner(Metodos.lerString("Casas/casa" + resposta + ".txt"));
                            int controle = 0;

                            //***verifica a posição que a palavra pesquisada esta no arquivo  
                            do {

                                controle++;
                                String linha = sc.nextLine();
                                if (linha.contains(tfPesquisarM.getText())) {
                                    local = controle;
                                    break;
                                }
                            } while (sc.hasNextLine());
                            //**************************************************************

                            addMostrar(criarSplit("casa" + resposta));
                            tfCasaM.setText(resposta);
                            numeroCasa = Integer.parseInt(resposta);
                            mudarFoco(local);
                            sair = false;
                        } else {
                            JOptionPane.showMessageDialog(null, "o numero informado não corresponde as casas que contem sua busca");
                        }
                    } else if (resposta.matches("\\d{3,}") || resposta.equals("")) {
                        JOptionPane.showMessageDialog(null, "não foi informado numero ou\no numero informado não corresponde as casas que contem sua busca");
                    }
                } while (sair);
            } else if (array.size() == 1) {

                //faz leitura do arquivo 
                Scanner sc = new Scanner(Metodos.lerString("Casas/" + array.get(0) + ".txt"));
                int controle = 0;
                //***verifica a posição que a palavra pesquisada esta no arquivo  
                do {
                    controle++;
                    String linha = sc.nextLine();
                    if (linha.contains(tfPesquisarM.getText())) {
                        local = controle;
                        break;
                    }
                } while (sc.hasNextLine());
                //**************************************************************

                tfPesquisarM.setText("");
                addMostrar(criarSplit(array.get(0)));
                tfCasaM.setText(Integer.toString(n2));
                numeroCasa = n2;
                mudarFoco(local);

            } else if (array.isEmpty() && !tfCasaM.getText().equals("0")) {
                JOptionPane.showMessageDialog(null, "arquivo não encontrado");
            } else {
                JOptionPane.showMessageDialog(null, "arquivo não encontrado");
            }
        } catch (IOException ex) {
            System.out.println("deu merda no metodo mostrar");
        }

    }

    /**
     * limpa todos os campos do programa
     */
    @FXML
    public void limpar() {
        tfNomeM.setText("");
        tfFixoM.setText("");
        tfCelular1M.setText("");
        tfCelular2M.setText("");
        tfCarro1m.setText("");
        tfCarro2m.setText("");
        tfCarro3m.setText("");
        tfCarro4m.setText("");
        tfCarro5m.setText("");
        tfPesquisarM.setText("");
        tfDiarista.setText("");
        tfComoChegar.setText("");
        taAnotacoesM.setText("");
        tfCasaM.setText("0");
        numeroCasa = 0;
        preencherJCombobox();

    }

    //em construção, metodo para alterar as informaçoes passadas na hr de adicionar visitantes
    public void editarJComboBox() {
        /**
         * se casa nome e placa for igual a item selecionado em jcombobox
         * alterar essas linhas no arquivo
         */

    }

    //edita o arquivo de relatorio add o horario de saida 
    public void addSaida() {
        //retorna arquivo inteiro 
        Scanner sc = new Scanner(Metodos.lerString("Relatorio/" + Metodos.mudaDatas(Metodos.DataHora("data")) + ".txt"));
        // recebe uma array contendo todos os dados do visitante 
        ArrayList visitanteTodos = new ArrayList();
        //recebe as linhas q formam os dados do visitante 
        ArrayList leitor = new ArrayList();
        //recebe cada linha do arquivo
        String linha;

        do {
            linha = sc.nextLine();
            leitor.add(linha);
            //remover espaços ou linhas em branco
            if (leitor.contains("")) {
                leitor.remove("");
            }
            if (leitor.size() == 5) {
                visitanteTodos.add(leitor.toString());
                leitor.clear();
            }
        } while (sc.hasNextLine());

        if (!visitanteTodos.isEmpty()) {

            //jb resultado do que esta escrito no combobox
            String[] jb = cbVisitantesM.getValue().split(",");
            //recebe as informaçoes do arquivo ja modificadas 
            ArrayList<String> arraySplit = new ArrayList<>();
            /*primeiro laço confere todas as strings dentro de visitanteTodos 
            *segundo laço confere todas as palavras dentro da string pega em visitanteTodos 
             */
            int n = -1; // controle do while
            int n2; // index de splitVisitantes
            int n3; // controle do while interno
            int n4 = -1; //controle do while
            int n5 = 4; //controla a remoção de item saida vazio
            do {
                n++;
                n2 = -1;
                n3 = -1;
                String[] splitVisitantes = visitanteTodos.get(n).toString().split(",");
                do {
                    n2++;
                    n3++;
                    arraySplit.add(splitVisitantes[n2].replace("[", "").replace("]", "").trim());
                } while (n3 < splitVisitantes.length - 1);
            } while (n < visitanteTodos.size() - 1);

            do {
                n4++;
                if (n4 > 0) {
                    n5 = n5 + 5;
                }
                if (n4 < visitanteTodos.size()) {

                    String[] visitanteEmLeitura = visitanteTodos.get(n4).toString().replace("[", "").replace("]", "").trim().split(",");

                    if (visitanteEmLeitura[0].equals(jb[0]) && visitanteEmLeitura[1].equals(jb[1]) && visitanteEmLeitura[2].equals(jb[2])
                            && visitanteEmLeitura[4].equals(" Saida:")) {

                        String saida = "Saida: " + Metodos.DataHora("hora");
                        arraySplit.remove(n5);
                        arraySplit.add(n5, saida);

                        for (int i = 0; i < arraySplit.size(); i++) {

                            if (arraySplit.get(i).contains("Saida")) {
                                i++;
                                arraySplit.add(i, "");
                            }
                        }

                        try {
                            Files.write(Metodos.relatorio(), arraySplit, Charset.defaultCharset());
                            preencherJCombobox();
                            break;

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "não foi possivel registrar os dados");
                        }
                    }
                }
            } while (n4 < visitanteTodos.size());
        }
    }

    //edita o arquivo de relatorio add o horario de saida informado pelo usuario
    public void addSaida(String horaSaida) {
        //retorna arquivo inteiro 
        Scanner sc = new Scanner(Metodos.lerString("Relatorio/" + Metodos.mudaDatas(Metodos.DataHora("data")) + ".txt"));
        // recebe uma array contendo todos os dados do visitante 
        ArrayList visitanteTodos = new ArrayList();
        //recebe as linhas q formam os dados do visitante 
        ArrayList leitor = new ArrayList();
        //recebe cada linha do arquivo
        String linha;

        do {
            linha = sc.nextLine();
            leitor.add(linha);
            //remover espaços ou linhas em branco
            if (leitor.contains("")) {
                leitor.remove("");
            }
            if (leitor.size() == 5) {
                visitanteTodos.add(leitor.toString());
                leitor.clear();
            }
        } while (sc.hasNextLine());

        if (!visitanteTodos.isEmpty()) {

            //jb resultado do que esta escrito no jcombobox
            String[] jb = cbVisitantesM.getValue().split(",");
            //recebe as informaçoes do arquivo ja modificadas 
            ArrayList<String> arraySplit = new ArrayList<>();
            /*primeiro laço confere todas as strings dentro de visitanteTodos 
            *segundo laço confere todas as palavras dentro da string pega em visitanteTodos 
             */
            int n = -1; // controle do while
            int n2; // index de splitVisitantes
            int n3; // controle do while interno
            int n4 = -1; //controle do while
            int n5 = 4; //controla a remoção de item saida vazio
            do {
                n++;
                n2 = -1;
                n3 = -1;
                String[] splitVisitantes = visitanteTodos.get(n).toString().split(",");
                do {
                    n2++;
                    n3++;
                    arraySplit.add(splitVisitantes[n2].replace("[", "").replace("]", "").trim());
                } while (n3 < splitVisitantes.length - 1);
            } while (n < visitanteTodos.size() - 1);

            do {
                n4++;
                if (n4 > 0) {
                    n5 = n5 + 5;
                }

                if (n4 < visitanteTodos.size()) {

                    String[] visitanteEmLeitura = visitanteTodos.get(n4).toString().replace("[", "").replace("]", "").trim().split(",");
                    if (visitanteEmLeitura[0].equals(jb[0]) && visitanteEmLeitura[1].equals(jb[1]) && visitanteEmLeitura[2].equals(jb[2])
                            && visitanteEmLeitura[4].equals(" Saida:")) {

                        String saida = "Saida: " + horaSaida;
                        arraySplit.remove(n5);
                        arraySplit.add(n5, saida);

                        for (int i = 0; i < arraySplit.size(); i++) {

                            if (arraySplit.get(i).contains("Saida")) {
                                i++;
                                arraySplit.add(i, "");
                            }
                        }

                        try {
                            Files.write(Metodos.relatorio(), arraySplit, Charset.defaultCharset());
                            preencherJCombobox();
                            break;

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "não foi possivel registrar os dados");
                        }
                    }

                }

            } while (n4 < visitanteTodos.size());

        }

    }

    //usado para fazer leitura do arquivo do dia anterior apos mudar a data a meia noite 
    public ArrayList<String> criarMeiaNoite() {
        //ler arquivo do dia anterior, verificar se falta add saida e add ao arquivo do dia atual
        String[] lbl = Metodos.DataHora("data").split("/");
        int d = Integer.parseInt(lbl[0]);
        int m = Integer.parseInt(lbl[1]);
        int a = Integer.parseInt(lbl[2]);

        StringBuilder dataAnterior = new StringBuilder();
        if (d > 1 && d <= Metodos.verificaMes(m).length(false)) {
            dataAnterior = dataAnterior.append(d - 1).append(".").append(m).append(".").append(a);
        } else if (d == 1) {
            if (m > 1 && m <= 12) {
                m--;
                dataAnterior = dataAnterior.append(d = Metodos.verificaMes(m).length(false)).append(".").append(m).append(".").append(a);
            } else if (m == 1 && d == 1) {
                dataAnterior = dataAnterior.append(d = 31).append(".").append(m = 12).append(".").append(a - 1);
            }
        }
        // Path relatorioF = Paths.get(Metodos.pastaDados + FileSystems.getDefault().getSeparator() + dataAnterior + ".txt");

        String casa = null;
        String nome = null;
        String entrada = null;
        String saida = null;
        String placa = null;
        visitantes.clear();
        // cbVisitantesM.getSelectionModel().clearSelection();
        //cbVisitantesM.getItems().clear();

        ArrayList<String> visitaOntem = new ArrayList();

        String diaDeEntrada = new String(dataAnterior);
        String arquivo = Metodos.lerString("Relatorio/" + dataAnterior + ".txt");

        //ler arquivo linha por linha
        if (arquivo != null) {
            Scanner sc = new Scanner(arquivo);
            if (!arquivo.isEmpty()) {
                int n = 0;
                do {
                    String linha = sc.nextLine();
                    if (linha.contains("Casa:")) {
                        casa = linha;
                    } else if (linha.contains("Nome:")) {
                        nome = linha;
                    } else if (linha.contains("Placa")) {
                        placa = linha;
                    } else if (linha.contains("Entrada:")) {
                        entrada = linha;
                    } else if (linha.contains("Saida:")) {
                        saida = linha;
                    }

                    if (casa != null && nome != null && placa != null && entrada != null && saida != null) {
                        n++;
                        if (saida.equals("Saida:")) {
                            visitaOntem.add(casa);
                            visitaOntem.add(nome);
                            visitaOntem.add(placa);
                            if (!entrada.contains(" dia:")) {
                                visitaOntem.add(entrada + " dia: " + diaDeEntrada.replace(".", "/"));
                            } else {
                                visitaOntem.add(entrada);
                            }
                            visitaOntem.add(saida);
                            visitaOntem.add("");
                            casa = null;
                            nome = null;
                            placa = null;
                            entrada = null;
                            saida = null;
                        }

                    }
                } while (sc.hasNextLine());

                if (!visitaOntem.isEmpty()) {
                    return visitaOntem;
                }
            }
        }

        return null;
    }

    /**
     * add os itens de jCombobox
     */
    public final void preencherJCombobox() {
        String casa = null;
        String nome = null;
        String entrada = null;
        String saida = null;
        String placa = null;
        String visita;
        visitantes.clear();

        //limpara a lista para não duplicar entradas
        // cbVisitantesM.getItems().removeAll(visitantes);
        String data = Metodos.mudaDatas(Metodos.DataHora("data"));
        String arquivo = Metodos.lerString("Relatorio/" + data + ".txt");

        //ler arquivo linha por linha
        if (arquivo != null) {
            Scanner sc = new Scanner(arquivo);
            if (!arquivo.isEmpty()) {
                int n = 0;
                do {
                    String linha = sc.nextLine();
                    if (linha.contains("Casa:")) {
                        casa = linha;
                    } else if (linha.contains("Nome:")) {
                        nome = linha;
                    } else if (linha.contains("Placa")) {
                        placa = linha;
                    } else if (linha.contains("Entrada:")) {
                        entrada = linha;
                    } else if (linha.contains("Saida:")) {
                        saida = linha;
                    }

                    if (casa != null && nome != null && entrada != null && saida != null) {
                        n++;
                        if (saida.equals("Saida:")) {
                            visita = casa + ", " + nome + ", " + placa;
                            visitantes.add(visita);
                            casa = null;
                            nome = null;
                            entrada = null;
                            saida = null;
                        }

                    }
                } while (sc.hasNextLine());
            }
        }

        if (visitantes.isEmpty()) {
            cbVisitantesM.setPromptText("sem entradas");
            cbVisitantesM.getItems().clear();
        } else {
            cbVisitantesM.setPromptText("entradas registradas");
            cbVisitantesM.getItems().clear();
            cbVisitantesM.getItems().addAll(visitantes);

//            int n = -1;
//            do {
//                n++;
//                cbVisitantesM.getItems().addAll(visitantes);          
//               
//            } while (n < visitantes.size() - 1);
        }

    }

    /**
     * faz a leitura do arquivo esse metodo é usado no metodo mostrar
     *
     * @param arquivo é o nome do arquivo q deve ser lido
     * @return retorna um array de String com as informaçoes lidas no arquivo
     */
    public String[] criarSplit(String arquivo) {
        String[] split = Metodos.lerString("Casas/" + arquivo + ".txt").split(";"); // CRIA UM ARRAY DE STRING USANDO O ; PARA DIVIDIR AS STRINGS 
        return split;

    }

    /**
     * metodo que add arrays de dados no metodo mostrar
     *
     * @param split corresponde ao array de strings criado no medodo mostrar
     */
    public void addMostrar(String[] split) {

        tfNomeM.setText(split[1].trim());
        tfFixoM.setText(split[2].trim());
        tfCelular1M.setText(split[3].trim());
        tfCelular2M.setText(split[4].trim());
        tfCarro1m.setText(split[5].trim());
        tfCarro2m.setText(split[6].trim());
        tfCarro3m.setText(split[7].trim());
        tfCarro4m.setText(split[8].trim());
        tfCarro5m.setText(split[9].trim());
        tfDiarista.setText(split[10].trim());
        tfComoChegar.setText(split[11].trim());
        taAnotacoesM.setText(split[12].trim());
    }

    //colocar nome com maiusculas 
    public String inicialMaiuscula(String string) {
        ArrayList nome = new ArrayList();
        String[] split = string.split("\\s");
        for (String texto : split) {
            String textoMod = texto.toUpperCase().charAt(0) + texto.substring(1, texto.length());
            nome.add(textoMod);
        }
        return nome.toString().replace("[", "").replace("]", "").replaceAll(",", "");
    }

//***************************tab recados ***********************************
    //escreve em um arquivo txt na pasta condominio/dados/recados.txt
    private void exibirRecados() {
        try {
            //pega o texto de JTAreaRecados
            byte[] bytestxt = taRecados.getText().getBytes(Charset.defaultCharset());
            ArrayList<String> txtRecados = new ArrayList<>();
            txtRecados.add(new String(bytestxt).trim());
            //escreve em um arquivo de txt
            Files.write(caminho, txtRecados, Charset.defaultCharset());

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro de leitura do arquivo ou arquivo nao existe");
        }

    }

    //verifica se existe o arquivo e le o conteudo
    private void LerRecados() {
        try {
            if (Files.notExists(caminho)) {
                Files.createFile(caminho);
            } else {
                byte[] bytes = Files.readAllBytes(caminho); // ARRAY DE BYTE QUE RECEBE OS DADOS DO ARQUIVO DE TXT
                String txtbytes = new String(bytes); // PASSA O ARRAY DE BYTE PARA STRING
                if (!txtbytes.isEmpty()) {
                    taRecados.setText(txtbytes);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro de leitura do arquivo ou arquivo nao existe");
        }

    }

//*********************** fim tab  recados *********************************
//*********************************relatorio***********************************   
    //criar arquivo do relatorio que recebe as entradas
    public void criarArquivoEntradaSaida() {
        try {
            if (Files.notExists(Metodos.relatorio())) {
                Files.createFile(Metodos.relatorio());
                if (criarMeiaNoite() != null) {
                    Files.write(Metodos.relatorio(), criarMeiaNoite(), Charset.defaultCharset());
                    preencherJCombobox();
                } else {
                    preencherJCombobox();
                }
            }
        } catch (IOException ex) {
            System.out.println("erro ao gravar arquivo meia noite");

        }
    }

    //verificar se existe registros da visita 
    @FXML
    public void pesquisarVisita() {
        try {
            int numeroDeArquivos = (int) Files.list(Metodos.PASTARELATORIO).count();
            ArrayList arquivosRelatorio = new ArrayList();
            int index = 0;
            while (numeroDeArquivos != 0) {
                numeroDeArquivos--;
                Object[] toArray = Files.list(Metodos.PASTARELATORIO).toArray();
                Object name = toArray[index];
                index++;
                Path arquivo = Paths.get(name.toString());

                if (Metodos.lerString("Relatorio/" + arquivo.getFileName()).contains(pesquisarRelatorio.getText())) {
                    arquivosRelatorio.add(arquivo.getFileName().toString().replace(".txt", ""));
                }

            }

            if (!arquivosRelatorio.isEmpty()) {

                JPanel panel = new JPanel();
                JComboBox jcombo = new JComboBox();
                for (int i = 0; arquivosRelatorio.size() > i; i++) {
                    jcombo.addItem(arquivosRelatorio.get(i));
                }
                JLabel label = new JLabel("Escolha a data que quer ver!");
                panel.add(label);
                panel.add(jcombo);
                String[] options = new String[]{"OK"};
                int option = JOptionPane.showOptionDialog(null, panel, "Registro de entradas",
                        JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION,
                        null, options, null);
                if (option == 0) {
                    taRelatorio.setText(Metodos.lerString("Relatorio/" + jcombo.getSelectedItem().toString() + ".txt"));
                    dataRelatorio.setText(jcombo.getSelectedItem().toString().replace(".", "/"));
                }
            } else {
                JOptionPane.showMessageDialog(null, "não encontrado!");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "erro!");
        }

    }

    public boolean validaData(String data) {
        try {
            Date dataF = null;
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setLenient(false);
            dataF = format.parse(data);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    @FXML
    public void voltarDiaRelatorio() {
        // voltar um dia

        String[] lbl = dataRelatorio.getText().split("/");
        int d = Integer.parseInt(lbl[0]);
        int m = Integer.parseInt(lbl[1]);
        int a = Integer.parseInt(lbl[2]);
        StringBuilder dataAnterior = new StringBuilder();

        btAvancar.setVisible(true);

        if (d > 1 && d <= Metodos.verificaMes(m).length(false)) {
            dataAnterior.append(d - 1).append("/").append(m).append("/").append(a);
            dataRelatorio.setText(dataAnterior.toString());
            taRelatorio.setText(Metodos.lerString("Relatorio/" + dataAnterior.toString().replace("/", ".") + ".txt"));
        } else if (d == 1) {
            if (m > 1 && m <= 12) {
                m--;
                dataAnterior.append(d = Metodos.verificaMes(m).length(false)).append("/").append(m).append("/").append(a);
                dataRelatorio.setText(dataAnterior.toString());
                taRelatorio.setText(Metodos.lerString("Relatorio/" + dataAnterior.toString().replace("/", ".") + ".txt"));
            } else if (m == 1 && d == 1) {
                dataAnterior.append(d = 31).append("/").append(m = 12).append("/").append(a - 1);
                dataRelatorio.setText(dataAnterior.toString());
                taRelatorio.setText(Metodos.lerString("Relatorio/" + dataAnterior.toString().replace("/", ".") + ".txt"));
            }
        }
    }

    @FXML
    public void avancarDiaRelatorio() {
        // avancar um dia
        //essa data vem de lblData 
        String[] lbl = dataRelatorio.getText().split("/");
        int d = Integer.parseInt(lbl[0]);
        int m = Integer.parseInt(lbl[1]);
        int a = Integer.parseInt(lbl[2]);
        //essa data vem do lblData
        String stringlblData = Integer.toString(d) + "." + Integer.toString(m) + "." + Integer.toString(a);
        // esse string sera atualizada nas condicionais 
        StringBuilder dataProxima = new StringBuilder();

        //se a data atual do sistema não for igual a stringlblData
        if (!Metodos.mudaDatas(Metodos.DataHora("data")).equals(stringlblData)) {
            //se dia for maior ou igual a 1 e se for menor que o tamanho do mes
            if (d >= 1 && d < Metodos.verificaMes(m).length(false)) {
                dataProxima.append(d + 1).append("/").append(m).append("/").append(a);
                dataRelatorio.setText(dataProxima.toString());
                taRelatorio.setText(Metodos.lerString("Relatorio/" + dataProxima.toString().replace("/", ".") + ".txt"));

                //se dia = tamanho do mes 
            } else if (d == Metodos.verificaMes(m).length(false)) {
                if (m < 12) {
                    dataProxima.append(d = 1).append("/").append(m + 1).append("/").append(a);
                    dataRelatorio.setText(dataProxima.toString());
                    taRelatorio.setText(Metodos.lerString("Relatorio/" + dataProxima.toString().replace("/", ".") + ".txt"));
                } else if (m == 12 && d == Metodos.verificaMes(m).length(false)) {
                    dataProxima.append(d = 1).append("/").append(m = 1).append("/").append(a + 1);
                    dataRelatorio.setText(dataProxima.toString());
                    taRelatorio.setText(Metodos.lerString("Relatorio/" + dataProxima.toString().replace("/", ".") + ".txt"));
                }
            }
        } else {
            btAvancar.setVisible(false);

        }
    }

    @FXML
    public void escolherDataRelatorio() {
        boolean controle = true;
        do {
            String dataResposta = JOptionPane.showInputDialog("informe uma data");

            if (dataResposta != null) {

                if (validaData(dataResposta)) {
                    dataRelatorio.setText(Metodos.mudaDatas(dataResposta).replace(".", "/"));
                    taRelatorio.setText(Metodos.lerString("Relatorio/" + Metodos.mudaDatas(dataResposta) + ".txt"));
                    if (!Metodos.DataHora("data").equals(Metodos.mudaDatas(dataResposta))) {
                        btAvancar.setVisible(true);
                    } else {
                        btAvancar.setVisible(false);
                    }
                    controle = false;
                } else {
                    JOptionPane.showMessageDialog(null, "informe uma data ex: " + Metodos.DataHora("data"));
                }

            } else {
                controle = false;
            }

        } while (controle);

    }

// ************************fim relatorio*************************************
//************************ tab servicos ****************************************
    @FXML
    public void botaoAvancarServico() {
        idServico++;
        try {
            //usado para fazer o numero reiniciar toda vez que chega a ultima casa
            if (idServico > Files.list(Metodos.SERVICOS).count()) {
                idServico = 0;
                LimparServicos();
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        //define com qual digito deve  iniciar
        tfIdS.setText(Integer.toString(idServico));

        botoesTrocarServico();
    }

    @FXML
    public void botaoVoltarServico() {
        idServico--;
        if (idServico > 0) {
            tfIdS.setText(Integer.toString(idServico));
            botoesTrocarServico();
        } else {
            tfIdS.setText("0");
            idServico = 0;
            LimparServicos();
        }
    }

    //usado quando os botoes < > são clicados
    public void botoesTrocarServico() {
        String valor = tfIdS.getText();
        if (valor.equals("0")) {
            limpar();
        } else if (Metodos.lerString("Servicos/servicos" + valor + ".txt") == null) {
            tfIdS.setText("0");
            LimparServicos();
        } else {
            addMostrarServicos(lerSplit("servicos" + valor));

        }
    }

    @FXML
    public void pesquisarComEnter() {
        String digitado;
        if (!tfIdS.getText().equals("")) {
            digitado = tfPesquisarS.getText();
            LimparServicos();
            tfPesquisarS.setText(digitado);
            MostrarServicos();
        } else {
            if (!tfPesquisarS.getText().equals("") | !tfIdS.getText().equals("")) {
                MostrarServicos();
            }
        }
    }

    @FXML
    public void botaoPesquisarServicos() {
        if (!tfPesquisarS.getText().equals("") | !tfPesquisarS.getText().equals("")) {
            MostrarServicos();
        }

    }

    /**
     * faz a leitura do arquivo esse metodo é usado no metodo mostrar
     *
     * @param arquivo é o nome do arquivo q deve ser lido
     * @return retorna um array de String com as informaçoes lidas no arquivo
     */
    public String[] lerSplit(String arquivo) {
        String[] split = Metodos.lerString("Servicos/" + arquivo + ".txt").split(";"); // CRIA UM ARRAY DE STRING USANDO O ; PARA DIVIDIR AS STRINGS 
        return split;

    }

    /**
     * metodo que add arrays de dados no metodo mostrar
     *
     * @param split corresponde ao array de strings criado no medodo mostrar
     */
    public void addMostrarServicos(String[] split) {
        tfIdS.setText(split[0].trim());
        tfPrestador.setText(split[1].trim());
        tfFixoS.setText(split[2].trim());
        tfCelular1s.setText(split[3].trim());
        ftCelular2s.setText(split[4].trim());
        tfServicos.setText(split[5].trim());
        taAnotacaoS.setText(split[6].trim());

    }

    /**
     * mostra o conteudo dos arquivos nos campos correspondentes
     */
    public void MostrarServicos() {
        //MUDA OS CAMPOS DO APP PARA OS ENCONTRADOS NO ARQUIVO DE TEXTO
        boolean terminar = false;
        int n1 = 0;

        do {
            n1++;
            String servicos = "servicos" + Integer.toString(n1) + ".txt";
            if (Files.exists(Paths.get(Metodos.SERVICOS
                    + FileSystems.getDefault().getSeparator()
                    + servicos))) {

                if (Metodos.lerString("Servicos/" + servicos)
                        .contains(tfPesquisarS.getText()
                                .trim()) && tfIdS.getText()
                                .equals("") | tfIdS.getText()
                        .equals(Integer.toString(n1))) {
                    addMostrarServicos(lerSplit("servicos" + n1));
                    terminar = true;
                }

            }

        } while (terminar == false);
    }

    /**
     * limpa todos os campos do programa
     */
    @FXML
    public void LimparServicos() {
        tfIdS.setText("");
        tfPrestador.setText("");
        tfFixoS.setText("");
        tfCelular1s.setText("");
        ftCelular2s.setText("");
        tfPesquisarS.setText("");
        tfServicos.setText("");
        taAnotacaoS.setText("");

    }

    /**
     * metodo usado no metodo editarCasa para adicionar arrays com os dados
     *
     * @param servico corresponde ao nome de cada casa e deve ser usado no
     * metodo editarCasa com o nome de cada casa
     */
    public void gerarArquivoServico(String servico) {

        try {
            Path path = Paths.get(Metodos.SERVICOS + FileSystems.getDefault().getSeparator() + servico + ".txt");

            ArrayList<String> arc1 = new ArrayList<>();
            arc1.add(tfIdS.getText() + ";");
            arc1.add(tfPrestador.getText() + ";");
            arc1.add(tfFixoS.getText() + ";");
            arc1.add(tfCelular1s.getText() + ";");
            arc1.add(ftCelular2s.getText() + ";");
            arc1.add(tfServicos.getText() + ";");
            arc1.add(taAnotacaoS.getText());

            Files.write(path, arc1, Charset.defaultCharset());
            JOptionPane.showMessageDialog(null, "Alteraçoes realizadas.");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "não foi possivel fazer alteraçoes");

        }
    }

    /**
     * edita os arquivos de texto
     */
    @FXML
    public void editarServico() {
        String perg2;

        int perg1 = JOptionPane.showConfirmDialog(null, "Continuar vai alterar os dados! quer continuar?");
        if (perg1 == 0) {

            gerarArquivoServico("servicos" + tfIdS.getText());

        } else if (perg1 == 1 | perg1 == 2 | perg1 == -1) {
            perg1 = JOptionPane.CLOSED_OPTION;
        }
    }

    /**
     *
     */
    @FXML
    public void excluirServico() {
        try {

            //local do arquivo
            String arquivo = Metodos.SERVICOS + FileSystems.getDefault().getSeparator() + "servicos" + tfIdS.getText() + ".txt";

            int pergunta = JOptionPane.showConfirmDialog(null, "quer realmente excluir os dados?");
            if (pergunta == 0) {
                //apaga o arquivo
                Files.deleteIfExists(Paths.get(arquivo));

                //usado no nome do arquivo 
                int n = 0;

                // mudar o nome dos arquivos e o numero de referencia dentro do arquivo 
                do {
                    //gera um nome em cada entrada no loop
                    String arquivoServico = Metodos.SERVICOS + FileSystems.getDefault().getSeparator() + "servicos" + (n + 1) + ".txt";
                    //lista quantos arquivos tem na pasta
                    Object[] toArray = Files.list(Metodos.SERVICOS).toArray();
                    //renomeia o arquivo existente com o nome gerado no arquivoServico 
                    new File(toArray[n].toString()).renameTo(new File(arquivoServico));
                    //muda o numero dentro do arquivo
                    String[] lerSplit = lerSplit("servicos" + (n + 1));
                    ArrayList<String> a = new ArrayList();
                    a.add(lerSplit[0].trim() + ";");
                    a.add(lerSplit[1].trim() + ";");
                    a.add(lerSplit[2].trim() + ";");
                    a.add(lerSplit[3].trim() + ";");
                    a.add(lerSplit[4].trim() + ";");
                    a.add(lerSplit[5].trim() + ";");
                    a.add(lerSplit[6].trim());

                    a.remove(0);
                    a.add(0, Integer.toString(n + 1) + ";");

                    //salvar novo arquivo com alteração 
                    Files.write(Paths.get(arquivoServico), a, Charset.defaultCharset());
                    //gerarArquivoServico("servicos" + Integer.toString(n+1) );

                    n++;

                    //falta renomear o id dentro do arquivo 
                } while (n < Files.list(Metodos.SERVICOS).count());

                JOptionPane.showMessageDialog(null, "Alteraçoes realizadas.");

                LimparServicos();
            } else {

            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipal.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    //********************* fim tab servicos **************************************  
}
