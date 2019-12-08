package pt.technic.apps.minesfinder;
import javax.swing.JLabel;

class TimeRunnable implements Runnable{    
    private JLabel timeLabel;    
    private int result;    
    private int pauseTime;
    private int timeStart;
    private GameWindowNormal normal;
    private boolean restart;
      
    
    
    public TimeRunnable(JLabel timerLabel) {
       this.timeLabel = timerLabel;       
    }
    
    public void run(){       
       timeStart  = (int)System.currentTimeMillis();
      while(true){         
         result =  ((int)(System.currentTimeMillis())-timeStart);
         if(result==999) {
            break;
         }                 	         
         timeLabel.setText(Integer.toString(((pauseTime+result)/1000)));          
         try{                       
              Thread.sleep(1000);
            }catch(InterruptedException e){               
               pauseTime = result;                        
               return;
           }
         }     
    }
    
    public int returnPauseTime() {
       return pauseTime;       
    }
    public int returnTimeStart() {
       return timeStart;
    }
    public int returnResult() {
       return result;
    }
}
    