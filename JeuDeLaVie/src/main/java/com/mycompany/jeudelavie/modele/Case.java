package com.mycompany.jeudelavie.modele;

import java.util.Random;

public class Case{
    private static final Random rnd = new Random();

    public boolean getState() {

        return state;
    }
    
    public void setState(boolean state){
        this.state = state;
    }

    private boolean state;
    
    private Environnement env;
    
    public Case(Environnement env){
        this.env = env;
    }
    

    public void rndState() {
        state = rnd.nextBoolean();
    }
    
    public void initStateTestRulesTrue(){
        state = true;
    }
    
    public void initStateTestRulesFalse(){
        state = false;
    }

    public boolean nextState() {
        int cpt = 0; //compte le nombre de voisine vivante
        
        Case ch = env.getCase(this, Direction.h);
        Case chd = env.getCase(this, Direction.hd);
        Case cd = env.getCase(this, Direction.d);
        Case cdb = env.getCase(this, Direction.db);
        Case cb = env.getCase(this, Direction.b);
        Case cbg = env.getCase(this, Direction.bg);
        Case cg = env.getCase(this, Direction.g);
        Case cgh = env.getCase(this, Direction.gh);
        
        
        // Compter les voisins vivants
        if(ch != null && ch.state) cpt++;
        if(chd != null && chd.state) cpt++;
        if(cd != null && cd.state) cpt++;
        if(cdb != null && cdb.state) cpt++;
        if(cb != null && cb.state) cpt++;
        if(cbg != null && cbg.state) cpt++;
        if(cg != null && cg.state) cpt++;
        if(cgh != null && cgh.state) cpt++;
    
        // Règles de transition des états
        if(state) {
            // La cellule est vivante
            if (cpt < 2) {
                return false;// Meurt par isolement
            } else if (cpt > 3) {
                return false;// Meurt par surpopulation
            } // Si cpt == 2 ou 3, la cellule survit, donc aucune action nécessaire
        } else {
            // La cellule est morte
            if (cpt == 3) {
                return true;// La cellule devient vivante (naissance)
            }
        }
        return state;
    }


}
    