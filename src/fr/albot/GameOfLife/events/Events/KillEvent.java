package fr.albot.GameOfLife.events.Events;

import fr.albot.GameOfLife.events.Event;
import fr.albot.GameOfLife.events.ThreadID;

public class KillEvent extends Event {
    public KillEvent(ThreadID sender) {
        super(sender);
    }
}
