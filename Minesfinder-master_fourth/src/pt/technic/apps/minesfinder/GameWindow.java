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


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Gabriel Massadas
 */


public  class GameWindow extends javax.swing.JFrame {
	private long pauseCurrentTime;
	protected long startTime;  
    protected ButtonMinefield[][] buttons;
    protected Minefield minefield;
    protected RecordTable record;   
    private JLabel time;
    private JLabel timerLabel;
    private JLabel restart;
    private JLabel mine;
    protected int mineCount;
   
    private JButton pauseBtn;
    private JButton startBtn;
    protected JButton restartBtn;
    protected JPanel btnPanel;
    protected Thread firstThread;
    protected Thread secondThread;
    protected long getTimeGameDuration;
    protected long getTimeGameStarted;
    private JLabel minecount;
    protected ActionListener action;

    protected JPanel panel;
        /**
     * Creates new form GameWindow
     */
    public GameWindow() {
        initComponents();        
    }    
    public GameWindow(Minefield minefield, RecordTable record) {       
       
       initComponents();       
       
       mineCount = minefield.getNumMines();                  
       minecount= new JLabel(Integer.toString(mineCount)); 
       minecount.setFont(new Font("Gothic", Font.ITALIC,15));
       panel = new JPanel(new GridLayout(minefield.getWidth(), minefield.getHeight()));                  
       this.minefield = minefield;
       this.record = record;
       
       time = new JLabel("시간");
       timerLabel = new JLabel("0");
       restart = new JLabel("재시작");
       mine = new JLabel("지뢰");
       
       
       pauseBtn = new JButton("pause");
       startBtn = new JButton("start");
       restartBtn = new JButton("restart");
       btnPanel = new JPanel(new FlowLayout());      
       
       btnPanel.add(time);
       btnPanel.add(timerLabel);
       btnPanel.add(pauseBtn);
       btnPanel.add(startBtn);
       btnPanel.add(restartBtn);
       btnPanel.add(mine);
       btnPanel.add(minecount);
       getTimeGameDuration = minefield.returnTimeGameDuration();
       getTimeGameStarted = minefield.returnTimeGameStarted();
        
        
       btnPanel.add(time);
       btnPanel.add(timerLabel);
       btnPanel.add(pauseBtn);
       btnPanel.add(startBtn);
       btnPanel.add(restartBtn);
       btnPanel.add(mine);
       btnPanel.add(minecount);
       
       timerLabel.setFont(new Font("Gothic", Font.ITALIC,15));
                                   
                                                               
          TimeRunnable runnable = new TimeRunnable(timerLabel);
          firstThread = new Thread(runnable);
          secondThread = new Thread(runnable);          
                                                                                    
          buttons = new ButtonMinefield[minefield.getWidth()][minefield.getHeight()];
           
            pauseBtn.addMouseListener(new MouseAdapter() { 
                 @Override                            
                 public void mouseClicked(MouseEvent e) {                 
                       firstThread.interrupt();                         
                       pauseCurrentTime = System.currentTimeMillis(); 
                       JOptionPane.showMessageDialog(null, "일시정지는 한 번만 됩니다.",
                                "Lost!", JOptionPane.INFORMATION_MESSAGE);
                       for (int x = 0; x < minefield.getWidth(); x++) {
                           for (int y = 0; y < minefield.getHeight(); y++) {                            
                               buttons[x][y].setEnabled(false);                                            
                           }
                       }
                 }          
             }); 
            startBtn.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent e) {                
                  try {
                     secondThread.start(); 
                     startTime = System.currentTimeMillis()-pauseCurrentTime;
                  }catch(IllegalThreadStateException e1) {                      
                         return;
                      }
                  for (int x = 0; x < minefield.getWidth(); x++) {
                      for (int y = 0; y < minefield.getHeight(); y++) {
                   	   buttons[x][y].setEnabled(true);
                      }
                  }

               }
            });
            
            
        getContentPane().setLayout(new BorderLayout());
    }
 // Create buttons for the player
    public void createButtons() {
    	 for (int x = 0; x < minefield.getWidth(); x++) {
             for (int y = 0; y < minefield.getHeight(); y++) {
                 buttons[x][y] = new ButtonMinefield(x, y);
                 buttons[x][y].addActionListener(action);
                 buttons[x][y].addMouseListener(mouseListener);
                 buttons[x][y].addKeyListener(keyListener);
                 panel.add(buttons[x][y]);
                 getContentPane().add(panel);                
             }
         }
         buttons[0][0].requestFocus();
         getContentPane().add(btnPanel,BorderLayout.NORTH);
    }    
    
    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mousePressed(MouseEvent e) { 
           
           if (e.getButton() == MouseEvent.BUTTON3) {
                ButtonMinefield botao = (ButtonMinefield) e.getSource();
                int x = botao.getCol();
                int y = botao.getLine();
                if (minefield.getGridState(x, y) == minefield.COVERED) {
                    minefield.setMineMarked(x, y);  
                    
                    if(mineCount <=0) {
                      JOptionPane.showMessageDialog(null, "지뢰가 부족합니다",
                                "Lost!", JOptionPane.INFORMATION_MESSAGE);   
                      minecount.setText(Integer.toString(mineCount+=1));
                      minefield.setMineCovered(x, y);
                      updateButtonsStates();
                      }
                    minecount.setText(Integer.toString(mineCount-=1));                        
                } else if (minefield.getGridState(x,
                        y) == minefield.MARKED) {
                    minefield.setMineQuestion(x, y);
                    minecount.setText(Integer.toString(mineCount+=1));
                } else if (minefield.getGridState(x,
                        y) == minefield.QUESTION) {
                    minefield.setMineCovered(x, y);                        
                }
                updateButtonsStates();
            }
           if (SwingUtilities.isLeftMouseButton(e) && SwingUtilities.isRightMouseButton(e)) {
               // 좌우클릭 동시에 하면
               ButtonMinefield botao = (ButtonMinefield) e.getSource();
               int x = botao.getCol();
               int y = botao.getLine();
               if (minefield.getGridState(x, y) == minefield.countMARKEDAround(x, y)) {
                  minefield.revealGridNeighbors(x, y);
                  // 누른 곳의 주변지뢰개수가 주변 체크한 지뢰개수가 같으면 다른거 다 열어줌
               }
            }
        }

        @Override
        public void mouseClicked(MouseEvent me) {
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }
    };
    KeyListener keyListener = new KeyListener() {
        @Override
        public void keyPressed(KeyEvent e) {
            ButtonMinefield botao = (ButtonMinefield) e.getSource();
            int x = botao.getCol();
            int y = botao.getLine();
            try {
                if (e.getKeyCode() == KeyEvent.VK_UP && y > 0) {
                     buttons[x - 1][y].requestFocus();                         
                 } else if (e.getKeyCode() == KeyEvent.VK_LEFT && x > 0) {
                     buttons[x][y - 1].requestFocus();
                 } else if (e.getKeyCode() == KeyEvent.VK_DOWN && y
                         < minefield.getHeight() - 1) {
                     buttons[x + 1][y].requestFocus();
                 } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && x
                         < minefield.getWidth() - 1) {
                     buttons[x][y + 1].requestFocus();                                      
                 }
                 else if (e.getKeyCode() == KeyEvent.VK_M) {
                     if (minefield.getGridState(x, y) == minefield.COVERED) {
                         minefield.setMineMarked(x, y);
                         if(mineCount <=0) {
                              JOptionPane.showMessageDialog(null, "지뢰가 부족합니다",
                                        "Lost!", JOptionPane.INFORMATION_MESSAGE);                          
                              minecount.setText(Integer.toString(mineCount+=1));
                              minefield.setMineCovered(x, y);
                              updateButtonsStates();
                               }
                            minecount.setText(Integer.toString(mineCount-=1));
                     } else if (minefield.getGridState(x,
                             y) == minefield.MARKED) {
                         minefield.setMineQuestion(x, y);
                         minecount.setText(Integer.toString(mineCount+=1));
                     } else if (minefield.getGridState(x,
                             y) == minefield.QUESTION) {
                         minefield.setMineCovered(x, y);
                         minecount.setText(Integer.toString(mineCount+=1));
                     }
                     updateButtonsStates();
                 }
             }catch(ArrayIndexOutOfBoundsException e1){
                buttons[0][0].requestFocus();
             }
                            
        }

        @Override
        public void keyTyped(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    };
       

    protected void updateButtonsStates() {
        for (int x = 0; x < minefield.getWidth(); x++) {
            for (int y = 0; y < minefield.getHeight(); y++) {
                buttons[x][y].setEstado(minefield.getGridState(x, y));
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {             
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Game");        
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());                      
        getContentPane().setLayout(layout);                                               
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1094, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 553, Short.MAX_VALUE)
        );
        pack();
    }
    /**
     * @param args the command line arguments
     */      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}