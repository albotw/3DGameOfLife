package network;

import core.v2.Cell;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static CONFIG.CONFIG.*;

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
                // V2
                long before = System.currentTimeMillis();
                if (this.srv.getStatus() == Status.CONTINUE){
                    Cell cell = this.srv.getTask();
                    if (cell != null) {
                        System.out.println("x: " + cell.x + " y: " + cell.y + " z: " + cell.z);
                        int counter = 0;
                        for (Cell c : cell.neighbours()) {
                            if (this.srv.isAlive(c)) counter++;
                        }
                        System.out.println(counter + " alive neighbors");

                        if (counter == ALIVE_THRESOLD) { //naissance
                            System.out.println("new cell");
                            this.srv.sendResult(cell);
                        } else if (counter == CURRENT_THRESHOLD) { //état courant
                            System.out.println("current cell");
                            if (this.srv.isAlive(cell)) this.srv.sendResult(cell);
                        }
                    }
                    else {
                        System.out.println("got null cell !");
                    }

                }
                else {
                    System.out.println("awaiting server");
                    try{
                        sleep(100);
                    }catch(InterruptedException ex) {ex.printStackTrace();}
                }

                long after = System.currentTimeMillis();
                System.out.println("### Cycle done in " + (after - before) + " ms ###");
                System.out.println("------");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
