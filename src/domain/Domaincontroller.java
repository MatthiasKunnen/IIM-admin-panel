package domain;

import java.util.Set;

public class Domaincontroller {

	private MaterialRepository materials;

	public Domaincontroller() {
		this.materials = new MaterialRepository();
	}

	public Set<Material> getMaterials() {
		return materials.getMaterials();
	}

	public Set<MaterialIdentifier> getMaterialIdentifiers() {
		return materials.getMaterialIdentifiers();
	}
}