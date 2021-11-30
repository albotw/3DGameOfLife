package events;

import java.util.HashMap;

public class EventDispatcher {
    private HashMap<ThreadID, EventQueue> channel;
    public static EventDispatcher instance;

    public static EventDispatcher createEventDispatcher() {
        if (instance == null) {
            instance = new EventDispatcher();
        }

        return instance;
    }

    private EventDispatcher() {
        this.channel = new HashMap<ThreadID, EventQueue>();
    }

    public void subscribe(ThreadID id, EventQueue handle) {
        this.channel.put(id, handle);
    }

    public void publish(Event e, ThreadID target) {
        try{
            this.channel.get(target).grab(e);
        }catch(Exception ex) {
            System.err.println("Erreur lors de l'envoi du message " + e.toString());
        }
    }

    public void publishToAll(Event e) {
        for (EventQueue handle : this.channel.values()) {
            handle.grab(e);
        }
    }
}
