package info.photoorganizer.database;

public class DatabaseStorageException extends Exception
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public DatabaseStorageException()
    {
        super();
    }

    public DatabaseStorageException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DatabaseStorageException(String message)
    {
        super(message);
    }

    public DatabaseStorageException(Throwable cause)
    {
        super(cause);
    }

}
