package core;

import network.Status;

public interface IGameOfLife {
    IGOLProcess getNext();

    void sendResult(IGOLProcess t);

    Status getStatus();
}
