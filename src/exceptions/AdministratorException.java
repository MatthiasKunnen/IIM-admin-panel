package exceptions;

import domain.Administrator;

/**
 * Created by matthias on 2016-02-24.
 */
public abstract class AdministratorException extends Exception {
    private Administrator administrator;
    public AdministratorException() {
    }

    public AdministratorException(String s) {
        super(s);
    }

    public AdministratorException(Administrator m) {
        this.administrator = m;
    }

    public AdministratorException(Administrator m, String s){
        this(s, m);
    }

    public AdministratorException(String s, Administrator m){
        super(s);
        this.administrator = m;
    }

    public Administrator getAdministrator() {
        return administrator;
    }
}
