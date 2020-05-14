/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package condominio.fx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author portaria
 */
public class EntradaFx extends Application {
    
    @Override
    public void start(Stage entradaStage) throws IOException {
       Parent root2 = FXMLLoader.load(getClass().getResource("FXMLEntrada.fxml"));
        
        Scene scene2 = new Scene(root2);
        entradaStage.setScene(scene2);
        entradaStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
