package events;

public class StopAppEvent extends Event{
    public StopAppEvent(ThreadID sender) {
        super(sender);
    }
}
