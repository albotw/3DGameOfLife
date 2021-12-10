package events.Events;

import events.Event;
import events.ThreadID;

public class KillEvent extends Event {
    public KillEvent(ThreadID sender) {
        super(sender);
    }
}
