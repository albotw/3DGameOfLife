package net;

public interface IBagOfTask {
    ITask getNext();
    void sendResult(ITask t);
    int getSize();
    boolean[] getTab();
}
