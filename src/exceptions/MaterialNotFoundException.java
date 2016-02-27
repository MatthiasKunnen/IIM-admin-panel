package exceptions;

import domain.Material;

public class MaterialNotFoundException extends MaterialException{

    public MaterialNotFoundException(Material m) {
        super(m);
    }

    public MaterialNotFoundException(String s) {
        super(s);
    }

    public MaterialNotFoundException(Material m, String s) {
        super(m, s);
    }

}
