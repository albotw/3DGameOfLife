package net;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private IServer srv = null;

    public Client() {
        try {
            this.srv = (IServer) Naming.lookup("GOL_SERVER");

            //TODO éxécution réaliste
            ITask t = srv.getTask();
            t.run();
            srv.sendResult(t);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
