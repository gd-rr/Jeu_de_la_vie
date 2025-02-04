package com.mycompany.jeudelavie.vue_controleur;


import com.mycompany.jeudelavie.modele.Environnement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Color;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller {
    private Environnement env;
    private FenetrePrincipale view;

    public Controller(Environnement env, FenetrePrincipale view) {
        this.env = env;
        this.view = view;
    }
    
    public void initController(){
        view.addSpeedSliderListener(new SpeedSliderListener());
        view.addPauseButtonListener(new PauseButtonListener());
        view.addStartButtonListener(new StartButtonListener());
        view.addResetButtonListener(new ResetButtonListener());
        view.addRandomButtonListener(new RandomButtonListener());
        setupGridCellListeners();
        setupGridCellHoverListeners();
        setupMenuListeners();
    }
    
    public void setFenetre(FenetrePrincipale fenetre) {
        this.view = fenetre;
    }

    private class PauseButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isPaused=!env.getPause();
            env.setPause(isPaused);
            view.updatePauseButtonText(isPaused);
        }
    }

    private class StartButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
                view.removeBorders();
                env.setStart(true);
                env.setReset(false);

            }
    }

    private class SpeedSliderListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent e) {
            double speedFactor = ((JSlider) e.getSource()).getValue() / 100.0; 
            env.setSpeedFactor(speedFactor); 

        }
    }

    private class ResetButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            env.setReset(true);
            env.setInit(true);
            env.setStart(false);
            if(env.getPause() == false){
                env.setPause(true);
            }
            env.setMutation(0);
            env.resetState();
            view.resetSlider();
            view.setResetBorders(true);
        }
    }

    private class RandomButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            env.setRandom(true);

        }
    }

    private void setupMenuListeners() {
        Map<String, boolean[][]> predefinedGrids = env.getPredefinedGrids();
    
        MouseAdapter hoverListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JMenuItem item = (JMenuItem) e.getSource();
                String gridName = item.getText();
                boolean[][] pattern = predefinedGrids.get(gridName);
                if (pattern != null) {
                    env.highlightPattern(0, 0, pattern); 
                }
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                env.clearHighlights(); 
            }
        };
    
        ActionListener clickListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem item = (JMenuItem) e.getSource();
                String gridName = item.getText();
                if (predefinedGrids.containsKey(gridName)) {
                    env.loadGrid(predefinedGrids.get(gridName)); 
                    env.clearHighlights(); 
                    view.updateView(env.getHighlightedGrid(),env.getMutation());
                }
            }
        };
    
        view.GridListenersPredefined(predefinedGrids, hoverListener, clickListener);
    }
    
    private void setupGridCellListeners() {
        view.addGridCellClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < env.getSizeX(); i++) {
                    for (int j = 0; j < env.getSizeY(); j++) {
                        if (view.getGridCell(i,j) == e.getSource()) { 
                            boolean currentState = env.getState(i, j);
                            env.setState(i, j, !currentState); 
                            view.updateView(env.getHighlightedGrid(),env.getMutation()); 
                        }
                    }
                }
            }
        });
    }
    

    private void setupGridCellHoverListeners() {
        view.addGridCellMouseListeners(
            new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    for (int i = 0; i < env.getSizeX(); i++) {
                        for (int j = 0; j < env.getSizeY(); j++) {
                            if (view.getGridCell(i, j) == e.getSource()) {
                                if (env.getState(i, j)) {
                                    view.getGridCell(i, j).setBackground(Color.RED); // Cell is alive
                                } else {
                                    view.getGridCell(i, j).setBackground(Color.BLUE); // Cell is dead
                                }
                                return; 
                            }
                        }
                    }
                }
            },
            new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    for (int i = 0; i < env.getSizeX(); i++) {
                        for (int j = 0; j < env.getSizeY(); j++) {
                            if (view.getGridCell(i, j) == e.getSource()) {
                                if (env.getState(i, j)) {
                                    view.getGridCell(i, j).setBackground(Color.BLACK); // Cell is alive
                                } else {
                                    view.getGridCell(i, j).setBackground(Color.WHITE); // Cell is dead
                                }
                                return;
                            }
                        }
                    }
                }
            }
        );
    }

    public boolean[][] getTabState(){
        int sizeX = env.getSizeX();
        int sizeY = env.getSizeY();
        boolean[][] tabState = new boolean[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
               tabState[i][j] = env.getState(i, j);
            }
        }

        return tabState;
    }
    
    public double getMutations() {
        return env.getMutation();
    }
    
    public boolean[][] getHighlightedGrid() {
        return env.getHighlightedGrid();
    }
    
    public int getSizeX(){
        return env.getSizeX();
    }
    
    public int getSizeY(){
        return env.getSizeY();
    }
    
}
