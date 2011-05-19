package info.photoorganizer.util.command;

public interface Command
{
    void doAction();
    void undoAction();
    void redoAction();
}
