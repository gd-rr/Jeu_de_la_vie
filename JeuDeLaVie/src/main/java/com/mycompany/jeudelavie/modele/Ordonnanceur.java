package com.mycompany.jeudelavie.modele;

import static java.lang.Thread.*;

public class Ordonnanceur extends Thread {

    private long sleepTime;
    private Runnable runnable;
    public Ordonnanceur(long _sleepTime, Runnable _runnable) {
        sleepTime = _sleepTime;
        runnable = _runnable;

    }

    public void run() {
        while (true) {
            runnable.run();
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
