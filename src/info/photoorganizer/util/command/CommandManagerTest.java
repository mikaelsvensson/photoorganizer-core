package info.photoorganizer.util.command;


import org.junit.Assert;
import org.junit.Test;

public class CommandManagerTest
{
    private class WriteCommand implements Command
    {
        private StringBuilder sb = null;
        private String str = null;

        public WriteCommand(StringBuilder sb, String str)
        {
            this.sb = sb;
            this.str = str;
        }

        @Override
        public void doAction()
        {
            sb.append(str);
        }

        @Override
        public void redoAction()
        {
            doAction();
        }

        @Override
        public void undoAction()
        {
            sb.delete(sb.length() - str.length(), sb.length());
        }
        
    }
    
    @Test
    public void undoAndRedo()
    {
        CommandManager manager = CommandManager.getInstance();
        StringBuilder sb = addAB(manager);
        manager.undoAction();
        manager.redoAction();
        Assert.assertEquals("ab", sb.toString());
    }
    
    @Test
    public void undo()
    {
        CommandManager manager = CommandManager.getInstance();
        StringBuilder sb = addAB(manager);
        manager.undoAction();
        Assert.assertEquals("a", sb.toString());
    }
    
    private StringBuilder addAB(CommandManager manager)
    {
        StringBuilder sb = new StringBuilder();
        manager.doAction(new WriteCommand(sb, "a"));
        manager.doAction(new WriteCommand(sb, "b"));
        return sb;
    }
    
}
