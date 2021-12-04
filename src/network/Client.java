package network;

import core.IGOLProcess;

import java.rmi.Naming;

public class Client {
    private static IServer srv = null;

    public static void init() throws Exception {
        Client.srv = (IServer) Naming.lookup(("GOL_SERVER"));
    }

    public static void run() throws Exception {
        IGOLProcess task = Client.srv.getTask();
        task.run();
        Client.srv.sendResult(task);
    }
}
