package exceptions;

import domain.Material;

public class MaterialAlreadyExistsException extends MaterialException {
    public MaterialAlreadyExistsException(String s) {
        super(s);
    }

    public MaterialAlreadyExistsException(Material m) {
        super(m);
    }

    public MaterialAlreadyExistsException(Material m, String s) {
        super(m, s);
    }
}
