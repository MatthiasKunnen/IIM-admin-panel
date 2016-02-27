package exceptions;

import domain.IEntity;

public class RepositoryItemNotFoundException extends RepositoryException {
    public RepositoryItemNotFoundException() {
    }

    public RepositoryItemNotFoundException(String s) {
        super(s);
    }

    public RepositoryItemNotFoundException(IEntity item) {
        super(item);
    }

    public RepositoryItemNotFoundException(String s, IEntity item) {
        super(s, item);
    }
}
