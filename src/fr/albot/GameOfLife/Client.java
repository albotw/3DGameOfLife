package fr.albot.GameOfLife;

import fr.albot.GameOfLife.core.IGOLProcess;
import fr.albot.GameOfLife.core.IGameOfLife;
import fr.albot.GameOfLife.core.Status;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static fr.albot.GameOfLife.CONFIG.CONFIG.SERVER_NAME;
import static fr.albot.GameOfLife.CONFIG.CONFIG.WAIT_DELAY;

public class Client extends Thread {
    public static void main(String[] args) {
        Client c = new Client();
        c.start();
    }

    private IGameOfLife gameOfLife = null;

    public Client() {
        try {
            this.gameOfLife = (IGameOfLife) Naming.lookup(SERVER_NAME);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                if (this.gameOfLife.getStatus() == Status.CONTINUE) {
                    IGOLProcess task = this.gameOfLife.getNext();
                    if (task != null) {
                        task.run();
                    } else {
                        System.out.println("task is null !");
                    }
                    this.gameOfLife.sendResult(task);
                } else {
                    System.out.println("Awaiting server");
                    sleep(WAIT_DELAY);
                }
            } catch (RemoteException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
