package pt.technic.apps.minesfinder;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

    
  
public class RecordMenu extends JFrame{
   
   private RecordTable recordEasy;
   private RecordTable recordMedium;
   private RecordTable recordHard;
   private RecordTable recordChallenge;
   
   JFrame frm = new JFrame("RecordMenu");
   
   String recordHeader[] = {"Ranking","Easy","Medium","Hard","Challenge"};
   String contents[][] = { {"1","Name1  |   score1","Name1  |   score1","Name1  |   score1","Name1  |   score1"},
                     {"2","Name2  |   score2","Name2  |   score2","Name2  |   score2","Name1  |   score1"},
                     {"3","Name3  |   score3","Name3  |   score3","Name3  |   score3","Name1  |   score1"},
                     };
                         
   JTable table = new JTable(contents, recordHeader);      
   public RecordMenu(RecordTable recordEasy, RecordTable recordMedium, RecordTable recordHard, RecordTable recordChallenge) {
         this.recordEasy = recordEasy;
         this.recordMedium = recordMedium;
         this.recordHard = recordHard;
         this.recordChallenge = recordChallenge;
         
         setTitle("Ranking");
         JScrollPane scrollpane = new JScrollPane(table);         
          table.setFont(new Font("Ghotic", Font.BOLD, 13));          
          DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
          celAlignCenter.setHorizontalAlignment(JLabel.CENTER);   
          table.getTableHeader().setDefaultRenderer(celAlignCenter);
          table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK));
          
          for(int i = 0; i < 5; i++) {
             table.getColumnModel().getColumn(i).setCellRenderer(celAlignCenter);
          } 
          for(int i=0; i<3; i++) {
              table.setValueAt(recordEasy.getName(i)+"  |   "+recordEasy.getScore(i)/1000,i,1);
              table.setValueAt(recordMedium.getName(i)+"  |   "+recordMedium.getScore(i)/1000,i,2);
              table.setValueAt(recordHard.getName(i)+"  |   "+recordHard.getScore(i)/1000,i,3);
              table.setValueAt(recordChallenge.getChName(i)+"  |   "+recordChallenge.getChScore(i),i,4);
          }
                    
        		  
          
         frm.add(scrollpane);         
         frm.pack();         
         frm.setSize(800,300);      
         frm.setVisible(true);            
   }
   
   
   
   
 
    public static void main(String[] args) {       
    }
 
}