package pt.technic.apps.minesfinder;



import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;



/**
 *
 * @author Gabriel Massadas
 */


    
    
public class GameWindowItem extends GameWindow {      	
	private int hintCount;
	private int shieldCount;
    private JButton hintBtn;
    private JLabel hintLabel; 
    private JButton shieldBtn;
    private JLabel shieldLabel;
    
    public GameWindowItem(Minefield minefield, RecordTable record) {
    	super(minefield, record);     
    	
        hintBtn = new JButton("힌트사용");
        hintLabel = new JLabel("3");
        shieldBtn = new JButton("방패");   
        shieldLabel = new JLabel("3");        
        
        hintCount = minefield.getNumHint();
        minefield.resetNumShield();        		
        shieldCount = minefield.getNumShield();
        shieldLabel.setFont(new Font("Gothic", Font.ITALIC,15));
        hintLabel.setFont(new Font("Gothic", Font.ITALIC,15));               
                        
        btnPanel.add(hintBtn);
        btnPanel.add(hintLabel);
        btnPanel.add(shieldBtn);
        btnPanel.add(shieldLabel);
        // 힌트 버튼
        hintBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { 
               if(hintCount>0) {
                  minefield.revealMines();
                    updateButtonsStates();
                    hintLabel.setText(Integer.toString(hintCount-=1));
               }
                              
               else{
                  JOptionPane.showMessageDialog(null, "힌트를 다 쓰셨습니다.",
                           "Lost!", JOptionPane.INFORMATION_MESSAGE);
                  return;
               }
            }
            public void mouseReleased(MouseEvent e) {
                try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
                minefield.backMines();
                updateButtonsStates();
             }
         });
       
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
            if(minefield.hasMine(x,y)) {
                if(minefield.getNumShield()>0) {                      
                   minefield.useShield();
                         JOptionPane.showMessageDialog(null, "방패 사용!!",
                                    "Lost!", JOptionPane.INFORMATION_MESSAGE);
                          shieldLabel.setText(Integer.toString(minefield.getNumShield()));                     
                   }
             }
             if (minefield.isGameFinished()) {                   
                if (minefield.isPlayerDefeated()) {                   
                   Sound bomb = new Sound();
                try {
                   bomb.bombPlay();

                } catch(Exception ex) {
                   return;   
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
                   minefield.revealMines();
                   updateButtonsStates();
                   getTimeGameDuration = System.currentTimeMillis()-minefield.returnTimeGameStarted()-startTime;
                    JOptionPane.showMessageDialog(null, "Congratulations. You managed to discover all the mines in "
                            + (getTimeGameDuration / 1000) + " seconds",
                            "victory", JOptionPane.INFORMATION_MESSAGE
                    );                                                                                                                                                                                                              
                    setVisible(false); 
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
       			GameWindowItem gameWindow = new GameWindowItem(new Minefield(Minefield.getWidth(), Minefield.getHeight(), Minefield.getNumMines()), MinesFinder.getRecord());
                gameWindow.setVisible(true);
       		}catch(IllegalThreadStateException e1){
       		return;
       		}
       	}
       });
    }
}

    
   