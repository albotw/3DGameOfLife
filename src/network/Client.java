package network;

import core.IGOLProcess;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static CONFIG.CONFIG.SERVER_NAME;

public class Client extends Thread {
    public static void main(String[] args) {
        Client c = new Client();
        c.start();
    }

    private IServer srv = null;

    public Client() {
        try {
            this.srv = (IServer) Naming.lookup(SERVER_NAME);
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
                if (this.srv.getStatus() == Status.CONTINUE) {
                    IGOLProcess task = this.srv.getTask();
                    if (task != null) {
                        task.run();
                    }
                    this.srv.sendResult(task);
                } else {
                    System.out.println("Awaiting server");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
