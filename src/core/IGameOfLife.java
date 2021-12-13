package core;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameOfLife extends Remote {
    IGOLProcess getNext() throws RemoteException;

    void sendResult(IGOLProcess t) throws RemoteException;

    Status getStatus() throws RemoteException;
}
