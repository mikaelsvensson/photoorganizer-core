package info.photoorganizer.metadata;

import info.photoorganizer.database.DatabaseStorageStrategy;
import info.photoorganizer.util.UUIDUtilities;

import java.util.UUID;

public class DatabaseObject
{
    private DatabaseStorageStrategy _storageStrategy = null;
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
//        if (getClass() != obj.getClass())
//            return false;
        DatabaseObject other = (DatabaseObject) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    private UUID id = null;

    public DatabaseObject(UUID id, DatabaseStorageStrategy storageStrategy)
    {
        super();
        this.id = (id != null ? id : UUIDUtilities.generateUuid());
        this._storageStrategy = storageStrategy;
    }

    protected DatabaseStorageStrategy getStorageStrategy()
    {
        return _storageStrategy;
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }


}
