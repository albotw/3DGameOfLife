package net;

import core.IGOLProcess;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
    IGOLProcess getTask() throws RemoteException;
    void sendResult(IGOLProcess t) throws RemoteException;
    int getSize() throws RemoteException;
    void affichage() throws RemoteException;
}
