package pt.technic.apps.minesfinder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class GameWindowNormal extends GameWindow{	
	
	public GameWindowNormal(Minefield minefield, RecordTable record) {
		super(minefield , record);		
		
		super.action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
   			 ButtonMinefield button = (ButtonMinefield) e.getSource();           
   	            int x = button.getCol();
   	            int y = button.getLine();
   	            minefield.revealGrid(x, y);
   	            if(firstThread.getState()==Thread.State.NEW)
   	            firstThread.start();
   	            updateButtonsStates();
   	            if (minefield.isGameFinished()) {
   	                if (minefield.isPlayerDefeated()) {
   	                	Sound bomb = new Sound();
   						try {
   							bomb.bombPlay();
   						} catch(Exception ex) {
   						}
   	                   firstThread.interrupt();
   	                   secondThread.interrupt();
   	                   minefield.revealMines();
   	                   updateButtonsStates();
   	                   
   	                   JOptionPane.showMessageDialog(null, "Oh, a mine broke",
   	                           "Lost!", JOptionPane.INFORMATION_MESSAGE);
   	                   setVisible(false); 
   	                    	                    
   	                } else {
   	                   firstThread.interrupt();
   	                   secondThread.interrupt();
   	                   getTimeGameDuration = System.currentTimeMillis()-minefield.returnTimeGameStarted()-startTime; 
   	                    JOptionPane.showMessageDialog(null, "Congratulations. You managed to discover all the mines in "
   	                            + (getTimeGameDuration / 1000) + " seconds",
   	                            "victory", JOptionPane.INFORMATION_MESSAGE
   	                    );                                                                                      
   	                        String name = JOptionPane.showInputDialog("Enter your name");
   	                        if(name != "")
   	                            record.setRecord(name, getTimeGameDuration);
   	                    
   	                }
   	                setVisible(false);                     
   	            }
   	        }       
	    };	    
	    createButtons();
	    restartBtn();
	    
	}
	
	 public void restartBtn() {		 
	    	restartBtn.addMouseListener(new MouseAdapter() {
	        	public void mouseClicked(MouseEvent e) {	        		
	        		setVisible(false);
	        		GameWindowNormal gameWindow = new GameWindowNormal(new Minefield(Minefield.getWidth(), 
	        				Minefield.getHeight(), Minefield.getNumMines()), MinesFinder.getRecord());
	        		gameWindow.setVisible(true);
	        	}
	        });	    
	 }
	 
}