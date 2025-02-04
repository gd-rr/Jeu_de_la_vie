package com.mycompany.jeudelavie;

import com.mycompany.jeudelavie.modele.Environnement;
import com.mycompany.jeudelavie.modele.Ordonnanceur;
import com.mycompany.jeudelavie.vue_controleur.FenetrePrincipale;
import com.mycompany.jeudelavie.vue_controleur.Controller;

import javax.swing.SwingUtilities;

/**
 *
 * @author frederic
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable(){
			public void run(){

				Environnement e = new Environnement(20, 20);
                                
                                Controller controleur= new Controller(e, null);

				FenetrePrincipale fenetre = new FenetrePrincipale(controleur);
				fenetre.setVisible(true);

                                controleur.setFenetre(fenetre);
                                controleur.initController();
				
				e.addObserver(fenetre);

				Ordonnanceur o = new Ordonnanceur(200, e);
				o.start();

			}
		});

    }

}
