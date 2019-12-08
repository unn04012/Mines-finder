package pt.technic.apps.minesfinder;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

    
  
public class Normal extends JFrame{
   public long score1;
   JFrame frm = new JFrame("Select Level");
     
   public void Normal() {                                    
         frm.getContentPane().add(MinesFinder.panelNormal, java.awt.BorderLayout.CENTER);
         frm.pack();         
         frm.setSize(500,300);      
         frm.setVisible(true);            
   }
   
 
    public static void main(String[] args) {
       new Normal();
    }
 
}
