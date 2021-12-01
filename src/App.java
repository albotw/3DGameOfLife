import core.Environment;
import events.*;
import graphics.Renderer;

public class App extends Thread {
    public static Environment env;
    public static Renderer renderer;

    private EventQueue eventQueue;

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    public App() {
        EventDispatcher.createEventDispatcher();
        this.eventQueue = new EventQueue(ThreadID.App);
        App.renderer = new Renderer();
        App.renderer.start();
    }

    public void run() {
        boolean running = true;
        while (running) {
            if (!this.eventQueue.isEmpty()) {
                Event e = this.eventQueue.get();
                if (e instanceof RenderInitDone) {
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
