package domain;

import java.util.*;

public class MaterialRepository {

    private Set<Material> materials;
    private Set<MaterialIdentifier> materialIdentifiers;

    public MaterialRepository() {
        this.materials = new HashSet<>();
        this.materialIdentifiers = new HashSet<>();
    }

    public Set<Material> getMaterials() {
        return Collections.unmodifiableSet(materials);
    }

    public Set<MaterialIdentifier> getMaterialIdentifiers() {
        return Collections.unmodifiableSet(materialIdentifiers);
    }

}