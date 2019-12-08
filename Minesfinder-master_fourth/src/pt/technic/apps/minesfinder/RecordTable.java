package pt.technic.apps.minesfinder;

import java.awt.event.ActionEvent;

import java.io.Serializable;
import java.util.ArrayList;
/**
 *
 * @author Gabriel Massadas
 */
public class RecordTable implements Serializable {
	private static final long serialVersionUID = -3524696884433645622L;

    private transient final int MAX_CHAR = 10;
    private int ranking = 3;
    private String[] name = new String[ranking];
    private long[] score = new long[ranking];
        
    private String[] chName = new String[ranking];
    private int[] chScore = new int[ranking];
   

    private transient ArrayList<RecordTableListener> listeners;
    private Minefield chCount;

    public RecordTable() {
    	for(int i = 0; i<ranking; i++) {
    		name[i] = "Anonymous";
            score[i] = 9999999;
            chName[i] = "Anonymous";
            chScore[i] = 0;
    	}
                
        listeners = new ArrayList<>();
    }

    public String getName(int ranking) {
        return name[ranking].substring(0, Math.min(MAX_CHAR, name[ranking].length()));
    }    

    public long getScore(int ranking) {
        return score[ranking];
    }
    
    public String getChName(int ranking) {
        return chName[ranking].substring(0, Math.min(MAX_CHAR, chName[ranking].length()));
    }    
    
    public long getChScore(int ranking) {
        return chScore[ranking];
    }
    
    

    public void setRecord(String name, long score) {
    	 long nextRankScore;
         String nextRankName;
         for(int i=0; i<ranking; i++) {
            if(score < this.score[i]) {
               nextRankScore = this.score[i];
               nextRankName = this.name[i];
               
               this.name[i] = name;
               this.score[i] = score;
               
               name = nextRankName;
               score = nextRankScore;
               
               notifyRecordTableUpdated();
            }
         }
    }

    public void setChallengeRecord(String chName, int chScore) {
    	 int nextRankChScore;
         String nextRankChName;
         for(int i=0; i<ranking; i++) {
            if(chScore >= this.chScore[i]) {
               nextRankChScore = this.chScore[i];
               nextRankChName = this.chName[i];               
               this.chName[i] = chName;
               this.chScore[i] = chScore;               
               chName = nextRankChName;
               chScore = nextRankChScore;               
               notifyRecordTableUpdated();
            }
         }
    }


    public void addRecordTableListener(RecordTableListener list) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(list);
    }

    public void removeRecordTableListener(RecordTableListener list) {
        if (listeners != null) {
            listeners.remove(list);
        }
    }

    private void notifyRecordTableUpdated() {
        if (listeners != null) {
            for (RecordTableListener list : listeners) {
                list.recordUpdated(this);
            }
        }
    }
}
