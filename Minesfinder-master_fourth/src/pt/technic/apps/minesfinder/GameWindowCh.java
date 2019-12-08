package pt.technic.apps.minesfinder;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.*;

/**
 *
 * @author Gabriel Massadas
 */

public class GameWindowCh extends GameWindow  {	
    private JLabel clearMap;
    private JLabel clearMapCount;
    private JLabel space; 
    private RecordTable recordChallenge;
     
    public GameWindowCh(Minefield minefield, RecordTable record) {
        super(minefield, record);        
        space = new JLabel("         ");
        clearMap= new JLabel("클리어 수 : ");
        clearMapCount = new JLabel(Integer.toString(Minefield.getMapCnt()));             
                        
        btnPanel.add(space);
        btnPanel.add(clearMap);
        btnPanel.add(clearMapCount);               
        clearMapCount.setFont(new Font("Gothic", Font.ITALIC,15));
                                                                                                           
                           
        super.action = new ActionListener() {
        @Override
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
					JOptionPane.showMessageDialog(null, "Congratulations. You cleared "
							+ (minefield.getMapCnt()) + " maps",
							"Game over", JOptionPane.INFORMATION_MESSAGE
							);															
						String chName = JOptionPane.showInputDialog("Enter your name");
						if(chName != "") {
							record.setChallengeRecord(chName, minefield.getMapCnt());
							minefield.resetMapCnt();
					
					}
						minefield.resetMapCnt();					
					setVisible(false);
				} else {
					setVisible(false);
					clearMapCount.setText(Integer.toString(Minefield.getMapCnt()));
					Minefield.incMapCnt();
					GameWindowCh gameWindow = new GameWindowCh(new Minefield(4, 4, 3), MinesFinder.getRecord());
			        gameWindow.setVisible(true);
				}
			}
		}
	};       
	restartBtn();
	createButtons();  
    }
    
    public void restartBtn() {
    restartBtn.addMouseListener(new MouseAdapter() {
       	public void mouseClicked(MouseEvent e) {
       		try {
       			setVisible(false);
       			GameWindowCh gameWindow = new GameWindowCh(new Minefield(4, 4, 3), recordChallenge);
                gameWindow.setVisible(true);
       		}catch(IllegalThreadStateException e1){
       		return;
       		}
       	}
       });
    }
    public RecordTable record() {
    	return recordChallenge;
    }
 
}