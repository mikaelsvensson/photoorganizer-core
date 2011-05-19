package info.photoorganizer.util.command;


import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractButton;

public class CommandManager
{
    private static volatile CommandManager cmdMngr = null;
    
    public static CommandManager getInstance()
    {
        if (cmdMngr == null)
        {
            synchronized (CommandManager.class)
            {
                if (cmdMngr == null)
                {
                    cmdMngr = new CommandManager();
                }
            }
        }
        return cmdMngr;
    }

    private LinkedList<Command> history = new LinkedList<Command>();
    
    private int lastExecutedCommandIndex = -1;
    
    protected CommandManager()
    {
        
    }
    
    synchronized public void redoAction()
    {
        if (lastExecutedCommandIndex >= 0 && lastExecutedCommandIndex < history.size())
        {
            lastExecutedCommandIndex++;
            history.get(lastExecutedCommandIndex).redoAction();
        }
    }
    
    synchronized public void undoAction()
    {
        if (lastExecutedCommandIndex >= 0 && lastExecutedCommandIndex < history.size())
        {
            history.get(lastExecutedCommandIndex).undoAction();
            lastExecutedCommandIndex--;
        }
    }
    
    synchronized public void doAction(Command cmd)
    {
        cmd.doAction();
        
        if (-1 != lastExecutedCommandIndex)
        {
            if (lastExecutedCommandIndex < history.size() - 1)
            {
                ListIterator<Command> iterator = history.listIterator(lastExecutedCommandIndex);
                while (iterator.hasNext())
                {
                    iterator.remove();
                }
            }
        }
        history.add(cmd);
        lastExecutedCommandIndex = history.size() - 1;
    }
}
