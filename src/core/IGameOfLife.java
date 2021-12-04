package core;

public interface IGameOfLife {
    IGOLProcess getNext();

    void sendResult(IGOLProcess t);
}
