

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author portaria
 */
public class teste extends JFrame{
    
    

    
   
    
    
    
    
    JComboBox<String> j = new JComboBox<>();
    
    
    
    
    
    
    
    
    
    public static void main(String[] args) {
            
            JFrame frame = new JFrame();
            
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            
            JPanel jp = new JPanel();
            frame.add(jp);
            
            JComboBox<String> box = new JComboBox<>();
            
           
            box.setBackground(Color.yellow);
            box.addItem(" s sds  ds d sd sds d ");
            box.setAlignmentX(TOP_ALIGNMENT);
            box.setAlignmentY(LEFT_ALIGNMENT);
           
            jp.add(box);
           
            
            frame.setVisible(true);
            jp.setVisible(true);
            box.setVisible(true);

           
    }
    
}
