import core.Environment;
import events.*;
import graphics.Mesh;
import graphics.Renderer;
import graphics.Sprite;

public class App extends Thread{
    public static Environment env;
    public static Renderer renderer;

    private EventQueue eventQueue;

    public static void main(String[] args)
    {
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
            if (!this.eventQueue.isEmpty()){
                System.out.println("got event");
                Event e = this.eventQueue.get();
                if (e instanceof RenderInitDone) {
                    Sprite s = new Sprite(Mesh.Cube(false));
                    App.renderer.renderSprite(s);
                    System.out.println("add sprite");
                }
            }
        }
    }
}
