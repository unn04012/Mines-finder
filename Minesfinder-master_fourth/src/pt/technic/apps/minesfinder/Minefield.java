package pt.technic.apps.minesfinder;

import java.util.Random;

/**
 *
 * @author Gabriel Massadas
 */
public class Minefield {

    public static final int EMPTY = 0;
    // from 1 to 8 is the number of mines around
    public static final int COVERED = 9;
    public static final int QUESTION = 10;
    public static final int MARKED = 11;
    public static final int BUSTED = 12;

    private boolean[][] mines;
    private int[][] states;
    private static int width;
    private static int height;
    private static int numMines;
    private Random random;
    private int numHint;
    private int numShield;

    private boolean firstPlay;
    private static boolean playerDefeated;
    private boolean gameFinished;
    private static int clearMap = 0;
    
    private long timeGameStarted;
    private long timeGameDuration;    
    
    
    public Minefield(int width, int height, int numMines) {

        if(numMines<=0){
            throw new IllegalArgumentException("Mines nuumber must be bigger than 0");
        }                    
        this.width = width;
        this.height = height;
        this.numMines = numMines;
        mines = new boolean[width][height];
        states = new int[width][height];
        numHint = 3;

        random = new Random();

        firstPlay = true;
        playerDefeated = false;
        gameFinished = false;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                states[x][y] = COVERED;
            }
        }
    }
    public void revealGridItem(int x, int y) {
        if (states[x][y] == COVERED && !gameFinished) {
            if (firstPlay) {
                firstPlay = false;
                placeMines(x, y);
                timeGameStarted=System.currentTimeMillis();
            }

            if(mines[x][y]) { 
                if(numShield > 0) { 
                   numShield -=1;                   
                   states[x][y] = COVERED;                         
                   return;    
                }else {
                   states[x][y] = BUSTED;
                   playerDefeated = true;
                     gameFinished = true;                                         
                     timeGameDuration = System.currentTimeMillis()-timeGameStarted;
                     return;
                }                                                                                  
             }                                                                            
        
            int minesAround = countMinesAround(x, y);
            states[x][y] = minesAround;

            if (minesAround == 0) {
                revealGridNeighbors(x, y);
            }
            
            if(checkVictory()) {
                gameFinished=true;
                playerDefeated=false;
                timeGameDuration=System.currentTimeMillis()-timeGameStarted;
                return;
            }
        }
    }
    
    public void revealGrid(int x, int y) {
        if (states[x][y] == COVERED && !gameFinished) {
            if (firstPlay) {
                firstPlay = false;
                placeMines(x, y);
                timeGameStarted=System.currentTimeMillis();
            }

            if(mines[x][y]) { 
                if(numShield > 0) {                              
                   states[x][y] = COVERED;                         
                   return;    
                }else {
                   states[x][y] = BUSTED;
                   playerDefeated = true;
                     gameFinished = true;                                         
                     timeGameDuration = System.currentTimeMillis()-timeGameStarted;
                     return;
                }                                                                                  
             }        
            int minesAround = countMinesAround(x, y);
            states[x][y] = minesAround;

            if (minesAround == 0) {
                revealGridNeighbors(x, y);
            }
            
            if(checkVictory()) {
                gameFinished=true;
                playerDefeated=false;
                timeGameDuration=System.currentTimeMillis()-timeGameStarted;
                return;
            }
        }
    }
    
    public long getGameDuration(){
        if(firstPlay){
            return 0;
        }
        if(!gameFinished){
            return System.currentTimeMillis()-timeGameStarted; 
        }
        return timeGameDuration;
    }
    
    public int getClearMap(){
        if(firstPlay){
            return 0;
        }
        if(!gameFinished){
            return clearMap; 
        }
        return clearMap;
    }

    public void revealGridNeighbors(int x, int y) {
        for (int col = Math.max(0, x - 1); col < Math.min(width, x + 2); col++) {
            for (int line = Math.max(0, y - 1); line < Math.min(height, y + 2); line++) {
                revealGrid(col, line);
            }
        }
    }

    public void setMineMarked(int x, int y) {
        if (states[x][y] == COVERED || states[x][y] == QUESTION) {
            states[x][y] = MARKED;
        }
    }

    public void setMineQuestion(int x, int y) {
        if (states[x][y] == COVERED || states[x][y] == MARKED) {
            states[x][y] = QUESTION;
        }
    }

    public void setMineCovered(int x, int y) {
        if (states[x][y] == MARKED || states[x][y] == QUESTION) {
            states[x][y] = COVERED;
        }
    }

    private boolean checkVictory() {
        boolean victory = true;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!mines[x][y]) {
                    victory = victory && states[x][y] >= 0 && states[x][y] < 9;
                }
            }
        }
        return victory;
    }

    private int countMinesAround(int x, int y) {
        int result = 0;
        for (int col = Math.max(0, x - 1); col < Math.min(width, x + 2); col++) {
            for (int line = Math.max(0, y - 1); line < Math.min(height, y + 2); line++) {
                if (mines[col][line]) {
                    result++;
                }
            }
        }
        return result - (mines[x][y] ? 1 : 0);
    }
    
    public int countMARKEDAround(int x, int y) { // !로 마크된 수 찾기
        int result = 0;
        for (int col = Math.max(0, x - 1); col < Math.min(width, x + 2); col++) {
            for (int line = Math.max(0, y - 1); line < Math.min(height, y + 2); line++) {
                if (states[col][line] == MARKED || states[col][line] == BUSTED) {
                    result++;
                }
            }
        }
        return result;
    }

    public boolean isPlayerDefeated() {
        return playerDefeated;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    private void placeMines(int plX, int plY) {
        // the plX and plY is the player's first play
        for (int i = 0; i < numMines; i++) {
            int x = 0;
            int y = 0;
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
            } while (mines[x][y] || (x == plX && y == plY));
            mines[x][y] = true;
        }
    }
       
    public int getGridState(int x, int y) {
        return states[x][y];
    }

    public boolean hasMine(int x, int y) {
        return mines[x][y];
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getNumMines() {
        return numMines;
    }
    public static void setWidth(int Width) {
        width = Width;
    }

    public static void setHeight(int Height) {
        height = Height;
    }

    public static void setNumMines(int NumMines) {
        numMines = NumMines;
    }
    
    public void revealMines() {
        for(int i=0; i<width; i++) {
             for(int j=0; j<height; j++) {
                 if(mines[i][j])     
                    if(states[i][j] == MARKED) {
                       continue;
                    }else 
                     states[i][j] = BUSTED;
             }
        }
    }
 
    public void backMines() {
        for(int i=0; i<width; i++) {
             for(int j=0; j<height; j++) {
                 if(mines[i][j])
                    if(states[i][j] == MARKED) {
                       continue;
                    }else
                     states[i][j] = COVERED;
             }
        }
    }
    
    public static int getMapCnt() {
    	return clearMap;
    }
    
    public static void resetMapCnt() {
    	clearMap = 0;
    }
    
    public static void incMapCnt() {
    	clearMap++;
    }

    public void resetNumShield() {
    	numShield = 3;
    }
    
    public void useShield() {
    	numShield--;
    }
    
    public int getNumShield() {
    	return numShield;
    }
     public int getNumHint() {
        return numHint;
     }

     public long returnTimeGameDuration() {
         return timeGameDuration;
     }
     public long returnTimeGameStarted() {
         return timeGameStarted;
     }
     
}
