package events;

public class Event {
    private ThreadID sender;

    public Event(){sender = null;}
    public Event(ThreadID sender)
    {
        this.sender = sender;
    }

    public ThreadID getSender(){return this.sender;}
    public void setSender(ThreadID sender){this.sender = sender;}
}
