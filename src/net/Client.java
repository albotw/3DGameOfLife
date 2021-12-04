package net;

import core.IGOLProcess;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private static IServer srv = null;

    public static void init() throws Exception{
        Client.srv = (IServer) Naming.lookup(("GOL_SERVER"));
    }

    public static void run() throws Exception{
        IGOLProcess task = Client.srv.getTask();
        task.run();
        Client.srv.sendResult(task);
    }
}
