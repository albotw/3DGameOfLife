import core.IGOLProcess;
import events.*;
import graphics.engine.Renderer;
import network.Client;
import network.IServer;
import network.Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;

public class App extends Thread {

    public static Client client;
    public static IServer server;
    public static Renderer renderer;

    private EventQueue eventQueue;

    public boolean isServer;

    public static void main(String[] args) {
        System.out.println(args[0]);
        if (Objects.equals(args[0], "-server")) {
            System.out.println("network.Server mode");
            App app = new App(true);
            app.start();
        } else if (Objects.equals(args[0], "-client")) {
            System.out.println("network.Client mode");
            App app = new App(false);
            app.start();
        } else if (Objects.equals(args[0], "-nonetwork")) {
            System.out.println("Renderer only mode");
            EventDispatcher.createEventDispatcher();
            Renderer r = new Renderer();
            r.start();
        } else {
            System.out.println("Erreur, mode d'éxécution manquant");
            System.exit(-1);
        }
    }

    public App(boolean isServer) {
        this.isServer = isServer;
        EventDispatcher.createEventDispatcher();
        this.eventQueue = new EventQueue(ThreadID.App);
        if (this.isServer) {
            //App.renderer = new Renderer();
            //App.renderer.start();
            try {
                App.server = new Server();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                App.server = (IServer) Naming.lookup("GOL_SERVER");
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            //traitement évènements
            if (!this.eventQueue.isEmpty()) {
                Event e = this.eventQueue.get();
                if (e instanceof StopAppEvent) {
                    running = false;
                }
            }

            //GOL
            if (this.isServer) {

            } else {
                try {
                    IGOLProcess task = App.server.getTask();
                    task.run();
                    App.server.sendResult(task);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            try {
                sleep(16);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
