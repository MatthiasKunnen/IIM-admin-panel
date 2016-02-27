package exceptions;

import domain.IEntity;

public class RepositoryItemAlreadyExistsException extends RepositoryException{
    public RepositoryItemAlreadyExistsException() {
    }

    public RepositoryItemAlreadyExistsException(String s) {
        super(s);
    }

    public RepositoryItemAlreadyExistsException(IEntity item) {
        super(item);
    }

    public RepositoryItemAlreadyExistsException(String s, IEntity item) {
        super(s, item);
    }
}
