package exceptions;

import domain.Administrator;

public class UnauthorizedException extends AdministratorException {

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private Administrator.Permission permission;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public UnauthorizedException() {
    }

    public UnauthorizedException(String s) {
        super(s);
    }

    public UnauthorizedException(Administrator m) {
        super(m);
    }

    public UnauthorizedException(Administrator m, String s) {
        super(m, s);
    }

    public UnauthorizedException(String s, Administrator m) {
        super(s, m);
    }

    public UnauthorizedException(String s, Administrator m, Administrator.Permission permission) {
        super(s, m);
        this.permission = permission;
    }

    public UnauthorizedException(Administrator.Permission permission) {
        this.permission = permission;
    }

    public UnauthorizedException(String s, Administrator.Permission permission) {
        super(s);
        this.permission = permission;
    }

    public UnauthorizedException(Administrator m, Administrator.Permission permission) {
        super(m);
        this.permission = permission;
    }

    public UnauthorizedException(Administrator m, String s, Administrator.Permission permission) {
        super(m, s);
        this.permission = permission;
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    public Administrator.Permission getPermission() {
        return permission;
    }
    //</editor-fold>
}
