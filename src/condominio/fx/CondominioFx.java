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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.ThreadMeiaNoite;

/**
 *
 * @author portaria
 */
public class CondominioFx extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLPrincipal.fxml"));
        
        Scene scene = new Scene(root);
        Image icon = new Image(getClass().getResourceAsStream("/imagens/icon/casa.png"));        
        stage.getIcons().add(icon);
        stage.setTitle("Cadastros");
        
        stage.setScene(scene);
        
        
        
        stage.show();
    }
    
    
    @Override
    public void stop() throws Exception{
         
        //ThreadMeiaNoite.currentThread().notifyAll();
        ThreadMeiaNoite.EXECUTAR = false;
        
         
         System.out.println("stop foi usado ; " + ThreadMeiaNoite.currentThread().isAlive());
         super.stop();
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
