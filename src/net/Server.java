package net;

import core.IGOLProcess;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements IServer {
    //TODO: voir structure de l'application.

    protected Server() throws RemoteException {
        super();

        try {
            Naming.rebind("GOL_SERVER", this);
            System.out.println("--- server registered ---");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized IGOLProcess getTask() throws RemoteException {
        System.out.println("[SERVER] dispatched task");
        //TODO: répartition des taches.
        return null;
    }

    @Override
    public synchronized void sendResult(IGOLProcess t) throws RemoteException {
        System.out.println("[SERVER] got result");
        //TODO: enregistrement résultat
    }

    @Override
    public int getSize() throws RemoteException {
        //TODO: voire utilité
        return 0;
    }

    @Override
    public void affichage() throws RemoteException {
        //TODO voire utilité.
    }
}
