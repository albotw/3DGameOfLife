package network;

import core.IGOLProcess;
import core.IGameOfLife;
import core.Status;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static CONFIG.CONFIG.SERVER_NAME;
import static CONFIG.CONFIG.WAIT_DELAY;

public class Client extends Thread {
    public static void main(String[] args) {
        Client c = new Client();
        c.start();
    }

    private IGameOfLife gameOfLife = null;

    public Client() {
        try {
            this.gameOfLife = (IGameOfLife) Naming.lookup(SERVER_NAME);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
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
                    this.sleep(WAIT_DELAY);
                }
            } catch (RemoteException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
