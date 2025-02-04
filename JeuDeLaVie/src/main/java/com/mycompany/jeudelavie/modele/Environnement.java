package com.mycompany.jeudelavie.modele;

import java.util.Observable;
import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

public class Environnement extends Observable implements Runnable {
    private Case[][] tab;
    
    private HashMap<Case, Point> map;
    private HashMap<Case, Boolean> nextStateMap;
    
    private boolean init = true;
    private boolean start = false;
    private boolean pause = true;
    private double mutation = 0;
    private boolean random = false;
    private boolean reset = false;

    private double speedFactor =1.0;
    private Map<String, boolean[][]> predefinedGrids;
    private boolean[][] highlightedGrid;
    
    // Getter et setter pour init
    public boolean getInit() {
        return init;
    }
    
    public void setInit(boolean init) {
        this.init = init;
    }

    // Getter et setter pour speedFactor
    public void setSpeedFactor(double factor) {
        if (factor > 0) {
            speedFactor = factor;
        }
    }

    public double getSpeedFactor() {
        return speedFactor;
    }

    // Getter et setter pour start
    public boolean getStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    // Getter et setter pour pause
    public boolean getPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    // Getter et setter pour mutation
    public double getMutation() {
        return mutation;
    }

    public void setMutation(double mutation) {
        this.mutation = mutation;
    }

    // Getter et setter pour random
    public boolean getRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }
    
    // Getter pour sizeX
    public int getSizeX() {
        return sizeX;
    }
    
    // Getter pour sizeY
    public int getSizeY() {
        return sizeY;
    }
    
    // Getter et setter pour init
    public boolean getReset() {
        return reset;
    }
    
    public void setReset(boolean reset) {
        this.reset = reset;
    }
    
    public void resetState(){
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].setState(false);
            }
        }
    }

    public void notifyObserv(){
        notifyObservers();
    }

    private int sizeX, sizeY;

    public boolean getState(int x, int y) {
        return tab[x][y].getState();
    }
    
    public void setState(int x, int y, boolean b){
        tab[x][y].setState(b);
    }

    public Case getCase(Case source, Direction d) {
        Point p = map.get(source);
        if(p == null){
            //System.out.println("p null");
            return null;
        }
        
        int i = (int)p.getX();
        int j = (int)p.getY();
        
        //si la taille de la grille change, avec ces variables il ny aura pas de pb 
        int maxI = tab.length;  // Taille de la grille (lignes)
        int maxJ = tab.length; // Taille de la grille (colonnes)
    
        switch(d){
            case h -> {
                if(j-1 >= 0){
                    return tab[i][j-1];
                }
            }
            case hd -> { 
                if((i+1 < maxI) && (j-1 >= 0)){
                    return tab[i+1][j-1];
                }
            }
            case d -> {
                if((i+1 < maxI)){
                    return tab[i+1][j];
                }
            }
            case db -> { 
                if((i+1 < maxI) && (j+1 < maxJ)){
                    return tab[i+1][j+1];
                }
            }
            case b -> {
                if((j+1 < maxJ)){
                    return tab[i][j+1];
                }
            }
            case bg -> { 
                if((i-1 >= 0) && (j+1 < maxJ)){
                    return tab[i-1][j+1];
                }
            }
            case g -> { 
                if((i-1 >= 0)){
                    return tab[i-1][j];
                }
            }
            case gh -> {
                if((i-1 >= 0) && (j-1 >= 0)){
                    return tab[i-1][j-1];
                }
            }
        }
        return null;
    }

    public Environnement(int _sizeX, int _sizeY) {

        sizeX = _sizeX;
        sizeY = _sizeY;
        
        map = new HashMap();
        nextStateMap = new HashMap();

        tab = new Case[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j] = new Case(this);
                map.put(tab[i][j], new Point(i, j));

            }

        }

        initPredefinedGrids();

    }

    private void initPredefinedGrids() {
        predefinedGrids = new HashMap<>();
    
        // Pulsar
        boolean[][] pulsar = new boolean[20][20];
        int[][] pulsarCoordinates = {
            {4, 6}, {4, 7}, {4, 8}, {4, 12}, {4, 13}, {4, 14},
            {6, 4}, {6, 9}, {6, 11}, {6, 16},
            {7, 4}, {7, 9}, {7, 11}, {7, 16},
            {8, 4}, {8, 9}, {8, 11}, {8, 16},
            {9, 6}, {9, 7}, {9, 8}, {9, 12}, {9, 13}, {9, 14},
            {11, 6}, {11, 7}, {11, 8}, {11, 12}, {11, 13}, {11, 14},
            {12, 4}, {12, 9}, {12, 11}, {12, 16},
            {13, 4}, {13, 9}, {13, 11}, {13, 16},
            {14, 4}, {14, 9}, {14, 11}, {14, 16},
            {16, 6}, {16, 7}, {16, 8}, {16, 12}, {16, 13}, {16, 14}
        };
        for (int[] coord : pulsarCoordinates) {
            pulsar[coord[0]][coord[1]] = true;
        }
        predefinedGrids.put("Pulsar", pulsar);
    
        // Pentadecathlon
        boolean[][] pentadecathlon = new boolean[20][20];
        int[][] pentadecathlonCoordinates = {
            {9, 6}, {9, 7}, {9, 8}, {9, 9}, {9, 10}, {9, 11}, {9, 12}, {9, 13},
            {8, 7}, {8, 12},
            {10, 7}, {10, 12}
        };
        for (int[] coord : pentadecathlonCoordinates) {
            pentadecathlon[coord[0]][coord[1]] = true;
        }
        predefinedGrids.put("Pentadecathlon", pentadecathlon);
    
        // Lightweight Spaceship (LWSS)
        boolean[][] lwss = new boolean[20][20];
        int[][] lwssCoordinates = {
            {2, 4}, {2, 5}, {2, 6}, {2, 7},
            {3, 3}, {3, 7},
            {4, 7}, {4, 3},
            {5, 5}, {5, 6}
        };
        for (int[] coord : lwssCoordinates) {
            lwss[coord[0]][coord[1]] = true;
        }
        predefinedGrids.put("LWSS", lwss);
    }
    
    

    public Map<String, boolean[][]> getPredefinedGrids() {
        return predefinedGrids;
    }

    public void loadGrid(boolean[][] grid) {
        this.setStart(false);
        this.setMutation(0);
        for (int i = 0; i < Math.min(grid.length, sizeX); i++) {
            for (int j = 0; j < Math.min(grid[i].length, sizeY); j++) {
                tab[i][j].setState(grid[i][j]);
            }
        }
        setChanged();
        notifyObservers();
    }

    public void highlightPattern(int startX, int startY, boolean[][] pattern) {
        highlightedGrid = new boolean[sizeX][sizeY]; 
    
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                int x = startX + i;
                int y = startY + j;
    
                if (x < sizeX && y < sizeY) {
                    highlightedGrid[x][y] = pattern[i][j]; 
                }
            }
        }
        setChanged();
        notifyObservers(); 
    }
    
    public void clearHighlights() {
        highlightedGrid = null;
        setChanged();
        notifyObservers(); 
    }
    
    public boolean[][] getHighlightedGrid() {
        return highlightedGrid;
    }

    public boolean rndState() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].rndState();

            }
        }
        return false;
    }
    
    public boolean initEmpty(){
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].setState(false);
            }
        }
        return false;
    }
    
    public void nextState(){
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                nextStateMap.put(tab[i][j], tab[i][j].nextState());
            }
        }
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].setState(nextStateMap.get(tab[i][j]));
            }
        }
    }


    
    
    @Override
    public void run() {
        if(init){
            init = initEmpty();
            setChanged();
            notifyObservers();
        }
        
        if(random){
            random = rndState();
            setChanged();
            notifyObservers();
        }
            
        if(start){
            while(pause){
                nextState();
                mutation++;
                setChanged();
                notifyObservers();
                try {
                    Thread.sleep((long) (500 / speedFactor));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(reset) break;
            }
        }
    }
}
