package com.mycompany.jeudelavie.vue_controleur;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;


import java.awt.event.*;
import java.util.Map;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import javax.swing.border.Border;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * @author frederic
 */
public class FenetrePrincipale extends JFrame implements Observer {

    private JPanel[][] tab;
    private JTextField textFieldMutation;
    private JSlider speedSlider;
    
    private JToggleButton jbPause;
    private JButton jbStart, jbReset, jbRandom;
    private JMenuItem gridMenuItem;

    private Controller contr;
    
    private boolean resetBorders = false;
    
    // Getter et setter pour resetBorder
    public boolean getResetBorders() {
        return resetBorders;
    }
    
    public void setResetBorders(boolean resetBorders) {
        this.resetBorders = resetBorders;
    }

    public FenetrePrincipale(Controller _contr) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contr = _contr;
        build();
    }

    public void build() {
        
        setTitle("Jeu de la Vie");
        setSize(1100, 1000);
        
        // Panneau principal
        JPanel pan = new JPanel(new BorderLayout());
        
        // Panneau central
        JComponent pan1 = new JPanel (new GridLayout(contr.getSizeX(),contr.getSizeY()));
        tab = new JPanel[contr.getSizeX()][contr.getSizeY()];


        Border blackline = BorderFactory.createLineBorder(Color.black,1);
        pan1.setBorder(blackline);
        for(int i = 0; i<contr.getSizeX();i++){
            for (int j = 0; j < contr.getSizeY(); j++) {
                tab[i][j] = new JPanel();
                tab[i][j].setBorder(blackline);   
                
                pan1.add(tab[i][j]);
            }

        }
        
        // Panneau pour les boutons
        JPanel pan2 = new JPanel(new GridLayout(5, 1));  // GridLayout avec 5 lignes et 1 colonne
        
        jbPause = new JToggleButton("Pause");
        pan2.add(jbPause);
        
        textFieldMutation = new JTextField("Mutation : ");
        textFieldMutation.setEditable(false);  // Le champ de texte est en lecture seule
        textFieldMutation.setPreferredSize(new Dimension(100, 30));
        pan2.add(textFieldMutation);
        
        speedSlider = new JSlider(JSlider.HORIZONTAL, 25, 400, 100); 
        speedSlider.setMajorTickSpacing(75); 
        speedSlider.setMinorTickSpacing(25);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        pan2.add(new JLabel("Vitesse (en %):")); 
        pan2.add(speedSlider);

        
        pan.add(pan1, BorderLayout.CENTER);
        pan.add(pan2, BorderLayout.EAST);
        
        setContentPane(pan);
        

        
        // Ajout Menu
        JMenuBar jm = new JMenuBar();
        setJMenuBar(jm);
        jbStart = new JButton("Start");
        jm.add(jbStart);
        jbReset = new JButton("Reset");
        jm.add(jbReset);
        jbRandom = new JButton("Randomized");
        jm.add(jbRandom);
    }

        //Evenement Slider
        public void addSpeedSliderListener(ChangeListener listener) {
            speedSlider.addChangeListener(listener);
        }
        
        //Evenements des boutons
        public void addPauseButtonListener(ActionListener listener) {
                
            jbPause.addActionListener(listener);
        }

        public void addStartButtonListener(ActionListener listener) {
            jbStart.addActionListener(listener);
        }
        
        public void addResetButtonListener(ActionListener listener) {
            jbReset.addActionListener(listener);
        }
        
        public void addRandomButtonListener(ActionListener listener) {
            jbRandom.addActionListener(listener);
        }


        //Evenements de la souris

        public void addGridCellClickListener(MouseAdapter listener) {
            for (int i = 0; i < tab.length; i++) {
                for (int j = 0; j < tab[i].length; j++) {
                    tab[i][j].addMouseListener(listener);
                }
            }
        }
        
        public void addGridCellMouseListeners(MouseMotionAdapter hoverListener, MouseAdapter exitListener) {
            for (int i = 0; i < tab.length; i++) {
                for (int j = 0; j < tab[i].length; j++) {
                    tab[i][j].addMouseMotionListener(hoverListener);
                    tab[i][j].addMouseListener(exitListener);
                }
            }
        }
           
        public JPanel getGridCell(int x, int y) {
            if (x >= 0 && x < tab.length && y >= 0 && y < tab[0].length) {
                return tab[x][y];
            }
            throw new IndexOutOfBoundsException("Invalid grid cell coordinates");
        }
         
        public void GridListenersPredefined(Map<String, boolean[][]> predefinedGrids, MouseAdapter hoverListener, ActionListener clickListener) {
            JMenu gridMenu = new JMenu("Preloaded Grids");
            JMenuBar menuBar = getJMenuBar();
            menuBar.add(gridMenu);
        
            for (String gridName : predefinedGrids.keySet()) {
                JMenuItem item = new JMenuItem(gridName);
        
                item.addMouseListener(hoverListener);
                item.addActionListener(clickListener);
        
                gridMenu.add(item);
            }
        }


    // Méthode pour retirer les bordures des cellules
        public void removeBorders() {
            for (int i = 0; i < contr.getSizeX(); i++) {
                for (int j = 0; j < contr.getSizeY(); j++) {
                    tab[i][j].setBorder(null); // Retire la bordure
                }
            }
        }

    // Méthode pour rétablir les bordures des cellules
        public void resetBorders() {
            Border blackline = BorderFactory.createLineBorder(Color.black, 1);
            for (int i = 0; i < contr.getSizeX(); i++) {
                for (int j = 0; j < contr.getSizeY(); j++) {
                    tab[i][j].setBorder(blackline); // Rétablit la bordure
                }
            }
        }

        // Methode pour changer l'affichage du 
        public void updatePauseButtonText(boolean isPaused) {
            jbPause.setSelected(isPaused); 
            jbPause.setText(isPaused ? "Pause" : "Resume");
        }
        
        public void resetSlider() {
            speedSlider.setValue(100);
        }
        

        //Methodes update

        public void updateView(boolean[][] highlightedGrid, double mutations) {
            for (int i = 0; i < highlightedGrid.length; i++) {
                for (int j = 0; j < highlightedGrid[i].length; j++) {
                    if (highlightedGrid[i][j]) {
                        tab[i][j].setBackground(Color.LIGHT_GRAY);
                    } else {
                        tab[i][j].setBackground(Color.WHITE);
                    }
                }
            }
            textFieldMutation.setText("Mutation : " + mutations);
        }




        @Override
        public void update(Observable o, Object arg) {
            boolean[][] highlightedGrid = contr.getHighlightedGrid();
            
            boolean[][] tabState = contr.getTabState();
            double mutations = contr.getMutations();
            // raffraîchissement de la vue
            for (int i = 0; i < contr.getSizeX(); i++) {
                for (int j = 0; j < contr.getSizeY(); j++) {
                    if (highlightedGrid != null && highlightedGrid[i][j]) {
                        tab[i][j].setBackground(Color.LIGHT_GRAY); 
                    } else if (tabState[i][j]) {
                        tab[i][j].setBackground(Color.BLACK); 
                    } else {
                        tab[i][j].setBackground(Color.WHITE); 
                    }
                }
            }
            textFieldMutation.setText("Mutation : " + mutations);
            if(resetBorders){
                resetBorders();
                resetBorders = false;
            }
        
        }
}

