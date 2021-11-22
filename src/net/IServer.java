package net;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
    ITask getTask() throws RemoteException;
    void sendResult(ITask t) throws RemoteException;
    int getSize() throws RemoteException;
    void affichage() throws RemoteException;
}
