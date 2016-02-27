package exceptions;

import domain.Material;

public abstract class MaterialException extends IllegalArgumentException {
    private Material material;
    public MaterialException() {
    }

    public MaterialException(String s) {
        super(s);
    }

    public MaterialException(Material m) {
        this.material = m;
    }

    public MaterialException(Material m, String s){
        super(s);
        this.material = m;
    }

    public Material getMaterial() {
        return material;
    }
}
