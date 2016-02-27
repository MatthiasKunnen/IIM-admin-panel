package exceptions;


import domain.IEntity;

public class RepositoryException extends IllegalArgumentException {
    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private IEntity item;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public RepositoryException() {
    }

    public RepositoryException(String s) {
        super(s);
    }

    public RepositoryException(IEntity item) {
        this.item = item;
    }

    public RepositoryException(String s, IEntity item) {
        super(s);
        this.item = item;
    }

    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">

    public IEntity getItem() {
        return item;
    }
    //</editor-fold>
}
