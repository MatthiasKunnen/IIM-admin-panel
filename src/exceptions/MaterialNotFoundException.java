package exceptions;

import domain.Material;

public class MaterialNotFoundException extends MaterialException{

    public MaterialNotFoundException(String s) {
        super(s);
    }

    public MaterialNotFoundException(Material m, String s) {
        super(m, s);
    }

    public MaterialNotFoundException(String s, Material m) {
        super(s, m);
    }

    public MaterialNotFoundException(Material m) {
        super(m);
    }
}
