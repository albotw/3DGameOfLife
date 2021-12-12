package network;

import core.v2.Cell;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
    Cell getTask() throws RemoteException;

    Status getStatus() throws RemoteException;

    void sendResult(Cell c) throws RemoteException;

    boolean isAlive(Cell c) throws RemoteException;
}
