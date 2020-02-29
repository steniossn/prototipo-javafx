/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backup;

import condominio.fx.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import util.Variaveis;

/**
 * FXML Controller class
 *
 * @author portaria
 */
public class FXMLEntradaController implements Initializable {

    @FXML
    private TextField tfEntrada;

    @FXML
    private TextField tfCasa;

    @FXML
    private Button btnOk;

    @FXML
    private Label ID;
        
    @FXML
    private CheckBox cbUber;
    
    @FXML
    private TextField tfMotorista;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField tfPlaca;

    @FXML
    private TextField tfNome;
    
    //***************variaveis****************
    
    int iD = 0;
    

    public FXMLEntradaController() {
    }
    //int count = -1;
    //String letra;
    //public static ArrayList<String> digitado = new ArrayList();
   
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       
        
        //coloca hora do sistema em entrada assim que a janela é aberta
        tfEntrada.setText(Variaveis.DataHora("hora"));
        
        // ao apertar ok pegar o nome informado e acionar metodo 
        btnOk.setOnAction((ActionEvent event) -> {
            if (cbUber.isSelected()) {
                tfNome.setText(inicialMaiuscula(tfNome.getText()));
                visitaDeUber();
            } else {
                tfNome.setText(inicialMaiuscula(tfNome.getText()));
                entrada();
            }

            tfCasa.requestFocus();
            
            //fechar a tela
            Stage stage = (Stage) btnOk.getScene().getWindow(); //pegar tela atual
            stage.close(); //fechar
        });

        //ao apertar botão de cancelar fechar a janela
        btnCancelar.setOnAction((event) -> {
            FXMLDocumentController.RAIZ.preencherJCombobox();
            //finalizar janela
            Stage stage = (Stage) btnCancelar.getScene().getWindow(); //pegar tela atual
            stage.close(); //fechar
            
        });

        //ao apertar enter trocar o foco 
        tfCasa.setOnAction((event) -> {
            tfNome.requestFocus();
        });

        //ao apertar enter trocar o foco 
        tfNome.setOnAction((event) -> {
            tfPlaca.requestFocus();
        });

        //ao apertar enter trocar o foco 
        tfNome.setOnKeyPressed((event) -> {
            String texto = tfNome.getText();

            if (event.getCode() == KeyCode.ENTER & !texto.isEmpty()) {
                tfNome.setText(inicialMaiuscula(texto));
            }
        });

        
        tfPlaca.setOnAction((event) -> {
            //salva e fecha
            entrada();
             //finalizar janela
            Stage stage = (Stage) btnCancelar.getScene().getWindow(); //pegar tela atual
            stage.close(); //fechar
            
        });


        tfPlaca.setOnKeyTyped((event) -> {
            String letra = event.getCharacter();
            String letrasPlaca = "\\D{3}";
            String padrao = "\\D{3}-\\d{4}";

            String texto = tfPlaca.getText();
            if (texto.matches(letrasPlaca)) {
                tfPlaca.setText(texto.toUpperCase(Locale.ROOT) + "-");
               //coloca o caracter na ultima posição
                tfPlaca.end();
            }
            if (texto.matches(padrao)) {
                event.consume();
            }

        });
    }



    //capiturar os dados e salvar em txt
    public void entrada() {
        try {

            byte[] bytes = Files.readAllBytes(Variaveis.relatorio());
            String relatorio = new String(bytes);

            ArrayList<String> entrada = new ArrayList<>();
            entrada.add(0, relatorio);
            entrada.add(1, "Casa: " + tfCasa.getText());
            entrada.add(2, "Nome: " + tfNome.getText());
            entrada.add(3, "Placa: " + tfPlaca.getText().toUpperCase(Locale.ROOT));
            entrada.add(4, "Entrada: " + tfEntrada.getText());
            entrada.add(5, "Saida:");
            Files.write(Variaveis.relatorio(), entrada, Charset.defaultCharset());
            FXMLDocumentController.RAIZ.preencherJCombobox();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "não foi possivel registrar os dados");
        }

    }

    //add o visitante que veio de uber 
    public void visitaDeUber() {
        try {

            byte[] bytes = Files.readAllBytes(Variaveis.relatorio());
            String relatorio = new String(bytes);

            ArrayList<String> entrada = new ArrayList<>();
            entrada.add(0, relatorio);
            entrada.add(1, "Casa: " + tfCasa.getText());
            entrada.add(2, "Nome: Uber " + tfMotorista.getText());
            entrada.add(3, "Placa: " + tfPlaca.getText().toUpperCase(Locale.ROOT));
            entrada.add(4, "Entrada: " + tfEntrada.getText());
            entrada.add(5, "Saida:");
            entrada.add(6, "");
            entrada.add(7, "Casa: " + tfCasa.getText());
            entrada.add(8, "Nome: " + tfNome.getText() + " veio de uber");
            entrada.add(9, "Placa: " + " esta sem carro");
            entrada.add(10, "Entrada: " + tfEntrada.getText());
            entrada.add(11, "Saida:");

            Files.write(Variaveis.relatorio(), entrada, Charset.defaultCharset());
            FXMLDocumentController.RAIZ.preencherJCombobox();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "não foi possivel registrar os dados");
        }
    }

    public String inicialMaiuscula(String string) {
        ArrayList nome = new ArrayList();
        String[] split = string.split("\\s");
        for (String texto : split) {
            String textoMod = texto.toUpperCase().charAt(0) + texto.substring(1, texto.length());
            nome.add(textoMod);
        }
        return nome.toString().replace("[", "").replace("]", "").replaceAll(",", "");
    }
    
    
    
}
