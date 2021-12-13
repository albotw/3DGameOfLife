package network;

import core.IGOLProcess;
import core.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
    IGOLProcess getTask() throws RemoteException;

    Status getStatus() throws RemoteException;

    void sendResult(IGOLProcess t) throws RemoteException;
}
